package net.wavedk.extrautilitiesreutilized.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class TransferCableConnectProcedureProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z, Direction direction) {
		if (direction == null)
			return false;
		ListTag connectedList = new ListTag();
		if (canReceiveEnergy(world, BlockPos.containing(x, y, z), null) || canExtractEnergy(world, BlockPos.containing(x, y, z), null)) {
			connectedList = (Tag) (world.getBlockEntity(BlockPos.containing((int) (x + direction.getStepX()), (int) (y + direction.getStepY()), (int) (z + direction.getStepZ()))) instanceof BlockEntity _blockEnt5
					? _blockEnt5.getPersistentData()
					: new CompoundTag()).get("connectedList") instanceof ListTag _list7 ? _list7 : new ListTag();
			for (Tag tagiterator : connectedList.copy()) {
				if (world instanceof ServerLevel _level) {
					_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("" + ((Tag) tagiterator instanceof StringTag _stringTag9 ? _stringTag9.getAsString() : ""))), false);
				}
			}
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("" + connectedList.size())), false);
			}
			return true;
		}
		return false;
	}

	private static boolean canReceiveEnergy(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.canReceive();
		}
		return false;
	}

	private static boolean canExtractEnergy(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.canExtract();
		}
		return false;
	}
}