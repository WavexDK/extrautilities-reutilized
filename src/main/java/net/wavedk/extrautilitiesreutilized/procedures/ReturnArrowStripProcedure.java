package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ReturnArrowStripProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		double currentStep = 0;
		double stepNum = 0;
		double pStep = 0;
		pStep = 0;
		currentStep = 23;
		for (int index18 = 0; index18 < 23; index18++) {
			stepNum = Math.floor(getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") / getBlockNBTNumber(world, BlockPos.containing(x, y, z), "steps"));
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") >= stepNum * pStep && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") <= stepNum * (pStep + 1)) {
				currentStep = pStep;
				break;
			}
			pStep = pStep + 1;
		}
		return currentStep;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}