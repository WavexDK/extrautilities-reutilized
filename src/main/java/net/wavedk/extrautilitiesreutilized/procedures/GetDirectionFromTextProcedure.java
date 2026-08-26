package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;

public class GetDirectionFromTextProcedure {
	public static Direction execute(BlockState blockstate, String directionString) {
		if (directionString == null)
			return Direction.NORTH;
		if ((directionString).equals("up")) {
			return Direction.UP;
		} else if ((directionString).equals("down")) {
			return Direction.DOWN;
		} else if ((directionString).equals("west")) {
			return Direction.WEST;
		} else if ((directionString).equals("east")) {
			return Direction.EAST;
		} else if ((directionString).equals("north")) {
			return Direction.NORTH;
		} else if ((directionString).equals("south")) {
			return Direction.SOUTH;
		} else if ((directionString).equals("front")) {
			return getDirectionFromBlockState(blockstate);
		} else if ((directionString).equals("back")) {
			return (getDirectionFromBlockState(blockstate)).getOpposite();
		} else if ((directionString).equals("left")) {
			return (getDirectionFromBlockState(blockstate)).getClockWise(Direction.Axis.Y);
		} else if ((directionString).equals("right")) {
			return (getDirectionFromBlockState(blockstate)).getClockWise(Direction.Axis.Y);
		}
		return Direction.DOWN;
	}

	private static Direction getDirectionFromBlockState(BlockState blockState) {
		Property<?> prop = getPropertyByName(blockState, "facing");
		if (prop instanceof DirectionProperty dp)
			return blockState.getValue(dp);
		prop = getPropertyByName(blockState, "axis");
		return prop instanceof EnumProperty ep && ep.getPossibleValues().toArray()[0] instanceof Direction.Axis ? Direction.fromAxisAndDirection((Direction.Axis) blockState.getValue(ep), Direction.AxisDirection.POSITIVE) : Direction.NORTH;
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