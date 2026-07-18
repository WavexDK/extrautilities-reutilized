package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class Arrow8Procedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		double currentStep = 0;
		double stepNum = 0;
		double pStep = 0;
		pStep = 8;
		stepNum = Math.floor(getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") / getBlockNBTNumber(world, BlockPos.containing(x, y, z), "steps"));
		if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") >= stepNum * pStep && getBlockNBTNumber(world, BlockPos.containing(x, y, z), "cProgress") <= stepNum * (pStep + 1)) {
			currentStep = pStep;
		}
		if (!(getBlockNBTNumber(world, BlockPos.containing(x, y, z), "wait_time") == 0) && currentStep == pStep) {
			return true;
		}
		return false;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}