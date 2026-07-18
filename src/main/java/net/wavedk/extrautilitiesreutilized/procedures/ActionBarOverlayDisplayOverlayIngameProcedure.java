package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.entity.Entity;

public class ActionBarOverlayDisplayOverlayIngameProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (!(entity.getData(EuruModVariables.PLAYER_VARIABLES).playerAB1).equals("") || (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerAB1).equals("")) {
			return true;
		}
		return false;
	}
}