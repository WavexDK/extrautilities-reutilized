package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ReturnGPErrorProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "tmGP")) {
			return "Not enough Grid Power available!";
		} else if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "tmI")) {
			return "The output slot is filled!";
		} else if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "nsI")) {
			return "The output slot is filled!";
		}
		return "Not enough Grid Power available!";
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}
}