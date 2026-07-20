package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.entity.Entity;

public class ToggleGridPowerOverlayProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).GPOverlay) {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.GPOverlay = false;
				_vars.markSyncDirty();
			}
		} else {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.GPOverlay = true;
				_vars.markSyncDirty();
			}
		}
	}
}