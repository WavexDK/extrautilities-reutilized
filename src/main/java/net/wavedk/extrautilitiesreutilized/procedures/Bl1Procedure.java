package net.wavedk.extrautilitiesreutilized.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class Bl1Procedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		double cNum = 0;
		cNum = 1;
		if (world instanceof ServerLevel _level) {
			_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((getEnergyStored(world, BlockPos.containing(x, y, z), null) + "/" + getMaxEnergyStored(world, BlockPos.containing(x, y, z), null))), false);
		}
		if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < (getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) / 47d) * cNum) {
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("1=true"), false);
			}
		}
		if (getEnergyStored(world, BlockPos.containing(x, y, z), null) >= (getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) / 47d) * (cNum - 1)) {
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("2=true"), false);
			}
		}
		if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < (getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) / 47d) * cNum
				&& getEnergyStored(world, BlockPos.containing(x, y, z), null) >= (getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) / 47d) * (cNum - 1)) {
			return true;
		}
		return false;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
		}
		return 0;
	}

	public static int getMaxEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getMaxEnergyStored();
		}
		return 0;
	}
}