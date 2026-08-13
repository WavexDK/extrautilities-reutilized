package net.wavedk.extrautilitiesreutilized.block.entity;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlockEntities;
import net.wavedk.extrautilitiesreutilized.block.TransferCableBlock;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.energy.IEnergyStorage;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.*;

public class TransferCableBlockEntity extends BlockEntity {
	private static final int CAPACITY = Math.max(0, 400000);
	private static final int MAX_RECEIVE = Math.max(0, 200);
	private static final int MAX_EXTRACT = Math.max(0, 200);
	private static final int FLUID_CAPACITY = Math.max(0, 8000);
	private static final Comparator<TransferCableBlockEntity> NETWORK_ORDER = Comparator.comparingInt((TransferCableBlockEntity be) -> be.getBlockPos().getX()).thenComparingInt(be -> be.getBlockPos().getY())
			.thenComparingInt(be -> be.getBlockPos().getZ());
	// Persistent pool data is consolidated onto the first cable in NETWORK_ORDER.
	private int localEnergy = Math.max(0, Math.min(CAPACITY, 0));
	private final List<FluidTank> localFluidTanks = new ArrayList<>();
	private boolean localEnergyPoolEnabled = true;
	private boolean localFluidPoolEnabled = true;
	// Every member receives the same transient member list. Topology changes invalidate it.
	private transient List<TransferCableBlockEntity> cachedNetwork;
	private final IEnergyStorage networkEnergyStorage = new IEnergyStorage() {
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			List<TransferCableBlockEntity> network = collectNetwork();
			if (!readEnergyPoolEnabled(network) || maxReceive <= 0 || MAX_RECEIVE <= 0)
				return 0;
			int stored = readNetworkEnergy(network);
			int received = Math.min(Math.min(maxReceive, MAX_RECEIVE), CAPACITY - stored);
			if (!simulate && received > 0)
				writeNetworkEnergy(network, stored + received);
			return received;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			List<TransferCableBlockEntity> network = collectNetwork();
			if (!readEnergyPoolEnabled(network) || maxExtract <= 0 || MAX_EXTRACT <= 0)
				return 0;
			int stored = readNetworkEnergy(network);
			int extracted = Math.min(Math.min(maxExtract, MAX_EXTRACT), stored);
			if (!simulate && extracted > 0)
				writeNetworkEnergy(network, stored - extracted);
			return extracted;
		}

		@Override
		public int getEnergyStored() {
			return readNetworkEnergy(collectNetwork());
		}

		@Override
		public int getMaxEnergyStored() {
			return CAPACITY;
		}

		@Override
		public boolean canExtract() {
			return MAX_EXTRACT > 0 && readEnergyPoolEnabled(collectNetwork());
		}

		@Override
		public boolean canReceive() {
			return MAX_RECEIVE > 0 && readEnergyPoolEnabled(collectNetwork());
		}
	};
	private final IFluidHandler networkFluidHandler = new IFluidHandler() {
		@Override
		public int getTanks() {
			return Math.max(1, readNetworkFluids(collectNetwork()).size());
		}

		@Override
		public FluidStack getFluidInTank(int tank) {
			List<FluidStack> fluids = readNetworkFluids(collectNetwork());
			return tank >= 0 && tank < fluids.size() ? fluids.get(tank).copy() : FluidStack.EMPTY;
		}

		@Override
		public int getTankCapacity(int tank) {
			return FLUID_CAPACITY;
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			return stack != null && !stack.isEmpty();
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			if (resource == null || resource.isEmpty() || FLUID_CAPACITY <= 0)
				return 0;
			List<TransferCableBlockEntity> network = collectNetwork();
			if (!readFluidPoolEnabled(network))
				return 0;
			List<FluidStack> fluids = readNetworkFluids(network);
			int room = Math.max(0, FLUID_CAPACITY - totalFluidAmount(fluids));
			int filled = Math.min(room, resource.getAmount());
			if (action.execute() && filled > 0) {
				mergeFluid(fluids, resource.copyWithAmount(filled));
				writeNetworkFluids(network, fluids);
			}
			return filled;
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (resource == null || resource.isEmpty())
				return FluidStack.EMPTY;
			List<TransferCableBlockEntity> network = collectNetwork();
			if (!readFluidPoolEnabled(network))
				return FluidStack.EMPTY;
			List<FluidStack> fluids = readNetworkFluids(network);
			for (int i = 0; i < fluids.size(); i++) {
				FluidStack stored = fluids.get(i);
				if (!FluidStack.isSameFluidSameComponents(stored, resource))
					continue;
				int amount = Math.min(resource.getAmount(), stored.getAmount());
				FluidStack drained = stored.copyWithAmount(amount);
				if (action.execute() && amount > 0) {
					stored.shrink(amount);
					fluids.removeIf(FluidStack::isEmpty);
					writeNetworkFluids(network, fluids);
				}
				return drained;
			}
			return FluidStack.EMPTY;
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			if (maxDrain <= 0)
				return FluidStack.EMPTY;
			List<TransferCableBlockEntity> network = collectNetwork();
			if (!readFluidPoolEnabled(network))
				return FluidStack.EMPTY;
			List<FluidStack> fluids = readNetworkFluids(network);
			if (fluids.isEmpty())
				return FluidStack.EMPTY;
			FluidStack stored = fluids.get(0);
			int amount = Math.min(maxDrain, stored.getAmount());
			FluidStack drained = stored.copyWithAmount(amount);
			if (action.execute() && amount > 0) {
				stored.shrink(amount);
				fluids.removeIf(FluidStack::isEmpty);
				writeNetworkFluids(network, fluids);
			}
			return drained;
		}
	};

	public TransferCableBlockEntity(BlockPos pos, BlockState state) {
		super(EuruModBlockEntities.TRANSFER_CABLE.get(), pos, state);
	}

	public IEnergyStorage getEnergyStorage() {
		return networkEnergyStorage;
	}

	public IFluidHandler getFluidHandler() {
		return networkFluidHandler;
	}

	/** Procedure API: changes the enabled state for this cable's whole connected pool. */
	public void setCableEnergyPoolEnabled(boolean enabled) {
		if (level == null || level.isClientSide)
			return;
		writeEnergyPoolEnabled(collectNetwork(), enabled);
	}

	/** Procedure API: changes the enabled state for this cable's whole connected pool. */
	public void setCableFluidPoolEnabled(boolean enabled) {
		if (level == null || level.isClientSide)
			return;
		writeFluidPoolEnabled(collectNetwork(), enabled);
	}

	/** Procedure API used by the cable-network foreach block. */
	public List<BlockPos> getCableNetworkPositions() {
		List<BlockPos> positions = new ArrayList<>();
		for (TransferCableBlockEntity cable : collectNetwork())
			positions.add(cable.getBlockPos().immutable());
		return positions;
	}

	/** Called when a connection property or cable layout changes. */
	public void invalidateNetworkCache() {
		List<TransferCableBlockEntity> oldCache = cachedNetwork;
		cachedNetwork = null;
		if (oldCache != null)
			for (TransferCableBlockEntity cable : oldCache)
				cable.cachedNetwork = null;
	}

	/** Called for a freshly placed cable so its configured initial energy is not added again. */
	public void joinExistingNetwork() {
		if (level == null || level.isClientSide)
			return;
		invalidateNetworkCache();
		List<TransferCableBlockEntity> network = collectNetwork();
		if (network.size() <= 1)
			return;
		setLocalEnergy(0);
		writeNetworkEnergy(network, readNetworkEnergy(network));
		writeNetworkFluids(network, readNetworkFluids(network));
		writeEnergyPoolEnabled(network, readEnergyPoolEnabled(network));
		writeFluidPoolEnabled(network, readFluidPoolEnabled(network));
	}

	/**
	 * Preserves energy, fluid, and enabled states when removal splits a network.
	 * Stored amounts are divided between resulting components by cable count.
	 */
	public void prepareForRemoval(BlockState removedState) {
		if (level == null || level.isClientSide)
			return;
		invalidateNetworkCache();
		List<List<TransferCableBlockEntity>> components = new ArrayList<>();
		Set<BlockPos> assigned = new HashSet<>();
		for (Direction direction : Direction.values()) {
			if (!removedState.getValue(TransferCableBlock.property(direction)))
				continue;
			BlockPos start = worldPosition.relative(direction);
			if (assigned.contains(start) || !(level.getBlockEntity(start) instanceof TransferCableBlockEntity))
				continue;
			List<TransferCableBlockEntity> component = collectComponent(start, worldPosition);
			if (!component.isEmpty()) {
				components.add(component);
				for (TransferCableBlockEntity cable : component)
					assigned.add(cable.getBlockPos());
			}
		}
		long totalEnergyLong = Math.max(0, localEnergy);
		List<FluidStack> totalFluids = readLocalFluids();
		boolean energyEnabled = localEnergyPoolEnabled;
		boolean fluidEnabled = localFluidPoolEnabled;
		for (List<TransferCableBlockEntity> component : components) {
			totalEnergyLong += readNetworkEnergy(component);
			for (FluidStack stack : readNetworkFluids(component))
				mergeFluid(totalFluids, stack);
			energyEnabled &= readEnergyPoolEnabled(component);
			fluidEnabled &= readFluidPoolEnabled(component);
		}
		int totalEnergy = (int) Math.min(CAPACITY, totalEnergyLong);
		setLocalEnergy(0);
		clearLocalFluids();
		for (List<TransferCableBlockEntity> component : components) {
			for (TransferCableBlockEntity cable : component) {
				cable.setLocalEnergy(0);
				cable.clearLocalFluids();
			}
		}
		int cablesRemaining = components.stream().mapToInt(List::size).sum();
		int countRemaining = cablesRemaining;
		int energyRemaining = totalEnergy;
		List<FluidStack> fluidRemaining = copyFluids(totalFluids);
		for (List<TransferCableBlockEntity> component : components) {
			int componentSize = component.size();
			int energyShare = countRemaining == componentSize ? energyRemaining : (int) ((long) energyRemaining * componentSize / countRemaining);
			writeNetworkEnergy(component, Math.min(CAPACITY, energyShare));
			energyRemaining -= energyShare;
			List<FluidStack> fluidShare = new ArrayList<>();
			for (FluidStack remaining : fluidRemaining) {
				int share = countRemaining == componentSize ? remaining.getAmount() : (int) ((long) remaining.getAmount() * componentSize / countRemaining);
				if (share > 0)
					fluidShare.add(remaining.copyWithAmount(share));
				remaining.shrink(share);
			}
			writeNetworkFluids(component, fluidShare);
			writeEnergyPoolEnabled(component, energyEnabled);
			writeFluidPoolEnabled(component, fluidEnabled);
			installNetworkCache(component);
			countRemaining -= componentSize;
		}
	}

	private List<TransferCableBlockEntity> collectNetwork() {
		if (cachedNetwork != null)
			return cachedNetwork;
		if (level == null)
			return List.of(this);
		List<TransferCableBlockEntity> network = collectComponent(worldPosition, null);
		if (network.isEmpty())
			network = new ArrayList<>(List.of(this));
		installNetworkCache(network);
		return cachedNetwork;
	}

	private void installNetworkCache(List<TransferCableBlockEntity> network) {
		if (network.isEmpty())
			return;
		network.sort(NETWORK_ORDER);
		List<TransferCableBlockEntity> shared = List.copyOf(network);
		for (TransferCableBlockEntity cable : shared)
			cable.cachedNetwork = shared;
	}

	private List<TransferCableBlockEntity> collectComponent(BlockPos start, BlockPos excluded) {
		List<TransferCableBlockEntity> result = new ArrayList<>();
		if (level == null || (excluded != null && start.equals(excluded)) || !level.hasChunkAt(start))
			return result;
		ArrayDeque<BlockPos> open = new ArrayDeque<>();
		Set<BlockPos> visited = new HashSet<>();
		open.add(start.immutable());
		while (!open.isEmpty()) {
			BlockPos pos = open.removeFirst();
			if (!visited.add(pos) || (excluded != null && pos.equals(excluded)) || !level.hasChunkAt(pos))
				continue;
			if (!(level.getBlockEntity(pos) instanceof TransferCableBlockEntity cable))
				continue;
			result.add(cable);
			for (Direction direction : Direction.values()) {
				BlockPos next = pos.relative(direction);
				if ((excluded == null || !next.equals(excluded)) && !visited.contains(next) && TransferCableBlock.isCableConnection(level, pos, direction))
					open.addLast(next.immutable());
			}
		}
		result.sort(NETWORK_ORDER);
		return result;
	}

	private static int readNetworkEnergy(List<TransferCableBlockEntity> network) {
		long total = 0;
		for (TransferCableBlockEntity cable : network)
			total += Math.max(0, cable.localEnergy);
		return (int) Math.min(CAPACITY, total);
	}

	private static void writeNetworkEnergy(List<TransferCableBlockEntity> network, int energy) {
		if (network.isEmpty())
			return;
		int clamped = Math.max(0, Math.min(CAPACITY, energy));
		for (int i = 0; i < network.size(); i++)
			network.get(i).setLocalEnergy(i == 0 ? clamped : 0);
	}

	private static boolean readEnergyPoolEnabled(List<TransferCableBlockEntity> network) {
		for (TransferCableBlockEntity cable : network)
			if (!cable.localEnergyPoolEnabled)
				return false;
		return true;
	}

	private static void writeEnergyPoolEnabled(List<TransferCableBlockEntity> network, boolean enabled) {
		for (TransferCableBlockEntity cable : network)
			cable.setLocalEnergyPoolEnabled(enabled);
	}

	private static boolean readFluidPoolEnabled(List<TransferCableBlockEntity> network) {
		for (TransferCableBlockEntity cable : network)
			if (!cable.localFluidPoolEnabled)
				return false;
		return true;
	}

	private static void writeFluidPoolEnabled(List<TransferCableBlockEntity> network, boolean enabled) {
		for (TransferCableBlockEntity cable : network)
			cable.setLocalFluidPoolEnabled(enabled);
	}

	private static List<FluidStack> readNetworkFluids(List<TransferCableBlockEntity> network) {
		List<FluidStack> fluids = new ArrayList<>();
		for (TransferCableBlockEntity cable : network)
			for (FluidStack stack : cable.readLocalFluids())
				mergeFluid(fluids, stack);
		return fluids;
	}

	private static void writeNetworkFluids(List<TransferCableBlockEntity> network, List<FluidStack> fluids) {
		if (network.isEmpty())
			return;
		for (TransferCableBlockEntity cable : network)
			cable.clearLocalFluids();
		TransferCableBlockEntity root = network.get(0);
		for (FluidStack stack : fluids)
			if (!stack.isEmpty())
				root.addLocalFluid(stack.copy());
	}

	private static List<FluidStack> copyFluids(List<FluidStack> fluids) {
		List<FluidStack> copy = new ArrayList<>();
		for (FluidStack stack : fluids)
			if (!stack.isEmpty())
				copy.add(stack.copy());
		return copy;
	}

	private static void mergeFluid(List<FluidStack> fluids, FluidStack addition) {
		if (addition == null || addition.isEmpty())
			return;
		for (FluidStack stored : fluids) {
			if (FluidStack.isSameFluidSameComponents(stored, addition)) {
				long combined = (long) stored.getAmount() + addition.getAmount();
				stored.setAmount((int) Math.min(Integer.MAX_VALUE, combined));
				return;
			}
		}
		fluids.add(addition.copy());
	}

	private static int totalFluidAmount(List<FluidStack> fluids) {
		long total = 0;
		for (FluidStack stack : fluids)
			total += Math.max(0, stack.getAmount());
		return (int) Math.min(Integer.MAX_VALUE, total);
	}

	private List<FluidStack> readLocalFluids() {
		List<FluidStack> result = new ArrayList<>();
		for (FluidTank tank : localFluidTanks)
			if (!tank.getFluid().isEmpty())
				result.add(tank.getFluid().copy());
		return result;
	}

	private FluidTank makeLocalTank() {
		return new FluidTank(Math.max(1, FLUID_CAPACITY)) {
			@Override
			protected void onContentsChanged() {
				super.onContentsChanged();
				markPoolChanged();
			}
		};
	}

	private void addLocalFluid(FluidStack stack) {
		if (stack == null || stack.isEmpty())
			return;
		FluidTank tank = makeLocalTank();
		tank.setFluid(stack.copy());
		localFluidTanks.add(tank);
		markPoolChanged();
	}

	private void clearLocalFluids() {
		if (localFluidTanks.isEmpty())
			return;
		localFluidTanks.clear();
		markPoolChanged();
	}

	private void setLocalEnergy(int energy) {
		int clamped = Math.max(0, Math.min(CAPACITY, energy));
		if (localEnergy == clamped)
			return;
		localEnergy = clamped;
		markPoolChanged();
	}

	private void setLocalEnergyPoolEnabled(boolean enabled) {
		if (localEnergyPoolEnabled == enabled)
			return;
		localEnergyPoolEnabled = enabled;
		markPoolChanged();
	}

	private void setLocalFluidPoolEnabled(boolean enabled) {
		if (localFluidPoolEnabled == enabled)
			return;
		localFluidPoolEnabled = enabled;
		markPoolChanged();
	}

	private void markPoolChanged() {
		setChanged();
		if (level != null)
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
	}

	@Override
	public void setRemoved() {
		invalidateNetworkCache();
		super.setRemoved();
	}

	@Override
	public void clearRemoved() {
		super.clearRemoved();
		cachedNetwork = null;
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		super.loadAdditional(tag, lookupProvider);
		int savedEnergy = tag.contains("cableNetworkEnergy") ? tag.getInt("cableNetworkEnergy") : tag.getInt("energyStorage");
		localEnergy = Math.max(0, Math.min(CAPACITY, savedEnergy));
		localEnergyPoolEnabled = !tag.contains("cableEnergyPoolEnabled") || tag.getBoolean("cableEnergyPoolEnabled");
		localFluidPoolEnabled = !tag.contains("cableFluidPoolEnabled") || tag.getBoolean("cableFluidPoolEnabled");
		localFluidTanks.clear();
		if (tag.get("cableNetworkFluids") instanceof ListTag list) {
			for (Tag entry : list) {
				if (!(entry instanceof CompoundTag fluidTag))
					continue;
				FluidTank tank = makeLocalTank();
				tank.readFromNBT(lookupProvider, fluidTag);
				if (!tank.getFluid().isEmpty())
					localFluidTanks.add(tank);
			}
		}
		cachedNetwork = null;
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		super.saveAdditional(tag, lookupProvider);
		tag.putInt("cableNetworkEnergy", localEnergy);
		tag.putBoolean("cableEnergyPoolEnabled", localEnergyPoolEnabled);
		tag.putBoolean("cableFluidPoolEnabled", localFluidPoolEnabled);
		ListTag fluids = new ListTag();
		for (FluidTank tank : localFluidTanks)
			if (!tank.getFluid().isEmpty())
				fluids.add(tank.writeToNBT(lookupProvider, new CompoundTag()));
		tag.put("cableNetworkFluids", fluids);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return saveWithFullMetadata(lookupProvider);
	}
}