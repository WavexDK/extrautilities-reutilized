package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class TooMuchGPProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if (getBlockNBTLogic(world, BlockPos.containing(x, y, z), "tmGP") || getBlockNBTLogic(world, BlockPos.containing(x, y, z), "tmI") || getBlockNBTLogic(world, BlockPos.containing(x, y, z), "nsI")) {
			return true;
		}
		return false;
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}
}