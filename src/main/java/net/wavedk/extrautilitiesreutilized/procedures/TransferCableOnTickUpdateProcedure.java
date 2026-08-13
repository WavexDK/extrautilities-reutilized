package net.wavedk.extrautilitiesreutilized.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class TransferCableOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Direction direction) {
		if (direction == null)
			return;
		double sentCoord = 0;
		sentCoord = receiveEnergySimulate(world, BlockPos.containing(x, y, z), 1000, direction);
		if (getEnergyStored(world, BlockPos.containing(x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ()), (direction.getOpposite())) >= sentCoord && sentCoord > 0) {
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ()), (direction.getOpposite()));
				if (_entityStorage != null)
					_entityStorage.extractEnergy((int) sentCoord, false);
			}
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), direction);
				if (_entityStorage != null)
					_entityStorage.receiveEnergy((int) sentCoord, false);
			}
		}
	}

	private static int receiveEnergySimulate(LevelAccessor level, BlockPos pos, int amount, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.receiveEnergy(amount, true);
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
}