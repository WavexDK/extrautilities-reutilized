package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.LevelAccessor;

public class NEGPAProcedureProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		if (TooMuchGPProcedure.execute(world, x, y, z)) {
			return "\u00A7cNot enough grid power available!";
		}
		return "\u00A79Click to view recipes";
	}
}