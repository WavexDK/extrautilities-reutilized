package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.entity.Entity;

public class ResonatingRedstoneCrystalSpecialInformationProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		return "\u00A77Grid Power: " + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_SI + "\u00A77/" + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total_SI;
	}
}