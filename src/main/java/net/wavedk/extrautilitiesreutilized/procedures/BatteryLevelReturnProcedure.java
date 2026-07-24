package net.wavedk.extrautilitiesreutilized.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class BatteryLevelReturnProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double cNum = 0;
		for (int index94 = 0; index94 < 48; index94++) {
			if (getEnergyStored(world, BlockPos.containing(x, y, z), null) < (getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) / 47d) * cNum
					&& getEnergyStored(world, BlockPos.containing(x, y, z), null) >= (getMaxEnergyStored(world, BlockPos.containing(x, y, z), null) / 47d) * (cNum - 1)) {
				return cNum;
			}
			cNum = cNum + 1;
		}
		return 0;
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