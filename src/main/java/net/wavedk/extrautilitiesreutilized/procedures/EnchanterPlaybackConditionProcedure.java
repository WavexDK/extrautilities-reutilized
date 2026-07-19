package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;

public class EnchanterPlaybackConditionProcedure {
	public static boolean execute(BlockState blockstate) {
		return getPropertyByName(blockstate, "on") instanceof BooleanProperty _getbp1 && blockstate.getValue(_getbp1);
	}

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
}