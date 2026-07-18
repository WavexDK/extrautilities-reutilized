package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;

public class NetherstarGeneratorLuminanceProcedure {
	public static double execute(BlockState blockstate) {
		if (getBooleanFromBlockState(blockstate, "on")) {
			return 10;
		}
		return 0;
	}

	private static boolean getBooleanFromBlockState(BlockState blockState, String property) {
		Property<?> prop = blockState.getBlock().getStateDefinition().getProperty(property);
		return prop instanceof BooleanProperty bp && blockState.getValue(bp);
	}
}