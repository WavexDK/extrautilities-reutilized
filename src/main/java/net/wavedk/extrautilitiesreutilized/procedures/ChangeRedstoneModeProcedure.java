package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class ChangeRedstoneModeProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 0) {
			setBlockNBTNumber(world, x, y, z, "redstoneMode", 1);
		} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 1) {
			setBlockNBTNumber(world, x, y, z, "redstoneMode", 2);
		} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 2) {
			setBlockNBTNumber(world, x, y, z, "redstoneMode", 3);
		} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "redstoneMode") == 3) {
			setBlockNBTNumber(world, x, y, z, "redstoneMode", 0);
		} else {
			setBlockNBTNumber(world, x, y, z, "redstoneMode", 0);
		}
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}

	private static void setBlockNBTNumber(LevelAccessor world, double x, double y, double z, String tag, double value) {
		if (!world.isClientSide()) {
			BlockPos pos = BlockPos.containing(x, y, z);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockState blockState = world.getBlockState(pos);
			if (blockEntity != null) {
				blockEntity.getPersistentData().putDouble(tag, value);
			}
			if (world instanceof Level level) {
				level.sendBlockUpdated(pos, blockState, blockState, 3);
			}
		}
	}
}