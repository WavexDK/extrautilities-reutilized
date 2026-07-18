package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class EnderLillyOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		if (getIntFromBlockState(blockstate, "stage") <= 6) {
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "ageCounter") > 15) {
				setBlockNBTNumber(world, x, y, z, "ageCounter", 0);
				setIntegerBlockState(world, x, y, z, "stage", getIntFromBlockState(blockstate, "stage") + 1);
			} else {
				setBlockNBTNumber(world, x, y, z, "ageCounter", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "ageCounter") + 1));
			}
		}
	}

	private static int getIntFromBlockState(BlockState blockState, String property) {
		Property<?> prop = blockState.getBlock().getStateDefinition().getProperty(property);
		return prop instanceof IntegerProperty ip ? blockState.getValue(ip) : -1;
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

	private static void setIntegerBlockState(LevelAccessor world, double x, double y, double z, String property, int value) {
		BlockPos pos = BlockPos.containing(x, y, z);
		BlockState state = world.getBlockState(pos);
		if (state.getBlock().getStateDefinition().getProperty(property) instanceof IntegerProperty integerProperty && integerProperty.getPossibleValues().contains(value)) {
			world.setBlock(pos, state.setValue(integerProperty, value), 3);
		}
	}
}