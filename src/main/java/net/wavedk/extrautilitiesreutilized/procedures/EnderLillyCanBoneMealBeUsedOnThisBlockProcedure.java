package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class EnderLillyCanBoneMealBeUsedOnThisBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		setIntegerBlockState(world, x, y, z, "stage", 0);
	}

	private static void setIntegerBlockState(LevelAccessor world, double x, double y, double z, String property, int value) {
		BlockPos pos = BlockPos.containing(x, y, z);
		BlockState state = world.getBlockState(pos);
		if (state.getBlock().getStateDefinition().getProperty(property) instanceof IntegerProperty integerProperty && integerProperty.getPossibleValues().contains(value)) {
			world.setBlock(pos, state.setValue(integerProperty, value), 3);
		}
	}
}