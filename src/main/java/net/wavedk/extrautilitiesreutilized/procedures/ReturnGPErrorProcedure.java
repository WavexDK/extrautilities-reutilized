package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ReturnGPErrorProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "tmGP")) {
			return "\u00A7cNot enough Grid Power available!";
		} else if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "tmI")) {
			return "\u00A7cThe output slot is filled!";
		} else if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "nsI")) {
			return "\u00A7cThe output slot is filled!";
		}
		return "\u00A79Click to view recipes";
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}
}