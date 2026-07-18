package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class SGenIsOffProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		return !getBooleanFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))), "on");
	}

	private static boolean getBooleanFromBlockState(BlockState blockState, String property) {
		Property<?> prop = blockState.getBlock().getStateDefinition().getProperty(property);
		return prop instanceof BooleanProperty bp && blockState.getValue(bp);
	}
}