package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ResonatorRedstoneModeProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		String mode = "";
		if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 0) {
			mode = "Always On";
		} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 1) {
			mode = "Redstone On";
		} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 2) {
			mode = "Redstone Off";
		} else {
			mode = "Always Off";
		}
		return mode;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}