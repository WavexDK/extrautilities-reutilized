package net.wavedk.extrautilitiesreutilized.procedures;

import org.apache.commons.lang3.function.FailableFunction;

import net.wavedk.extrautilitiesreutilized.world.inventory.CreativeGenGUIMenu;
import net.wavedk.extrautilitiesreutilized.init.EuruModMenus;
import net.wavedk.extrautilitiesreutilized.block.entity.CreativeEnergySourceBlockEntity;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.UUID;

public class CreativeEnergySourceOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		String getDep = "";
		boolean done = false;
		double feSpeed = 0;
		double testSend = 0;
		Entity player = null;
		player = world instanceof ServerLevel _serverGetEntityUUID ? _serverGetEntityUUID.getEntity(tryOrDefault((getBlockNBTString(world, BlockPos.containing(x, y, z), "placedBy")), UUID::fromString, () -> new UUID(0, 0))) : null;
		if (player instanceof ServerPlayer || player instanceof Player) {
			if (player instanceof Player _plr5 && _plr5.containerMenu instanceof CreativeGenGUIMenu) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("energySend", new Object() {
							double convert(String s) {
								try {
									return Double.parseDouble(s.trim());
								} catch (Exception e) {
								}
								return 0;
							}
						}.convert((player instanceof Player _entity6 && _entity6.containerMenu instanceof EuruModMenus.MenuAccessor _menu6) ? _menu6.getMenuState(0, "energySend", "") : ""));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		}
		getDep = x + "" + y + z + (world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD));
		feSpeed = getMaxEnergyStored(world, BlockPos.containing(x, y, z), null);
		testSend = getBlockNBTNumber(world, BlockPos.containing(x, y, z), "energySend");
		if (getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) > getEnergyStored(world, BlockPos.containing(x, y, z), null)) {
			if (world.getBlockEntity(new BlockPos((int) x, (int) y, (int) z)) instanceof CreativeEnergySourceBlockEntity be) {
				be.addEnergy((int) feSpeed);
			}
		}
		if (world instanceof ILevelExtension _ext) {
			IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x + 1, y, z), Direction.WEST);
			if (_entityStorage != null)
				_entityStorage.receiveEnergy(receiveEnergySimulate(world, BlockPos.containing(x + 1, y, z), (int) testSend, Direction.WEST), false);
		}
		if (world instanceof ILevelExtension _ext) {
			IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x - 1, y, z), Direction.EAST);
			if (_entityStorage != null)
				_entityStorage.receiveEnergy(receiveEnergySimulate(world, BlockPos.containing(x - 1, y, z), (int) testSend, Direction.EAST), false);
		}
		if (world instanceof ILevelExtension _ext) {
			IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z - 1), Direction.SOUTH);
			if (_entityStorage != null)
				_entityStorage.receiveEnergy(receiveEnergySimulate(world, BlockPos.containing(x, y, z - 1), (int) testSend, Direction.SOUTH), false);
		}
		if (world instanceof ILevelExtension _ext) {
			IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z + 1), Direction.NORTH);
			if (_entityStorage != null)
				_entityStorage.receiveEnergy(receiveEnergySimulate(world, BlockPos.containing(x, y, z + 1), (int) testSend, Direction.NORTH), false);
		}
		if (world instanceof ILevelExtension _ext) {
			IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y + 1, z), Direction.DOWN);
			if (_entityStorage != null)
				_entityStorage.receiveEnergy(receiveEnergySimulate(world, BlockPos.containing(x, y + 1, z), (int) testSend, Direction.DOWN), false);
		}
		if (world instanceof ILevelExtension _ext) {
			IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y - 1, z), Direction.UP);
			if (_entityStorage != null)
				_entityStorage.receiveEnergy(receiveEnergySimulate(world, BlockPos.containing(x, y - 1, z), (int) testSend, Direction.UP), false);
		}
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}

	private static <A, B> A tryOrDefault(B funcArg, FailableFunction<B, A, Exception> func, Supplier<A> fallback) {
		try {
			return func.apply(funcArg);
		} catch (Exception e) {
			return fallback.get();
		}
	}

	public static int getMaxEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getMaxEnergyStored();
		}
		return 0;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
		}
		return 0;
	}

	private static int receiveEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.receiveEnergy(amount, true);
		}
		return 0;
	}
}