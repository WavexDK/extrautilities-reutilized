package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class SGenBackdropProcedure {
	public static double execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == EuruModBlocks.SURVIVAL_GENERATOR.get()) {
			if (getPropertyByName((world.getBlockState(BlockPos.containing(x, y, z))), "on") instanceof BooleanProperty _getbp3 && (world.getBlockState(BlockPos.containing(x, y, z))).getValue(_getbp3)) {
				return 1;
			}
			return 0;
		}
		return 2;
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