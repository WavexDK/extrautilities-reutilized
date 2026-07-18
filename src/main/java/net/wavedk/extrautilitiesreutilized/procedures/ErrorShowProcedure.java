package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ErrorShowProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if ((getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("")) {
			return false;
		}
		if (!(getBlockNBTString(world, BlockPos.containing(x, y, z), "errorMessage")).equals("noInput")) {
			return true;
		}
		return false;
	}

	private static String getBlockNBTString(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getString(tag);
		return "";
	}
}