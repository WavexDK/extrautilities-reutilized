package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.entity.Entity;

import java.io.File;

public class UpdategroupmillProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double solarPanelCutOff = 0;
		File file = new File("");
		File f2 = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj2 = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fobj = new com.google.gson.JsonObject();
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills > 0) {
			if (entity.getData(EuruModVariables.PLAYER_VARIABLES).group_efficiency_mills == 0 || entity.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills == 0) {
				fobj = EuruModVariables.unified_config.get("group_man").getAsJsonObject();
				obj2 = fobj.get("mills").getAsJsonObject();
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.group_efficiency_mills = obj2.get("efficiency").getAsDouble();
					_vars.group_cutoff_mills = obj2.get("efficiency_cutoff").getAsDouble();
					_vars.markSyncDirty();
				}
			}
			if (entity.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills > entity.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills) {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.group_update_mills = Math.floor(
							(entity.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills + Math.pow(entity.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills - entity.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills,
									entity.getData(EuruModVariables.PLAYER_VARIABLES).group_efficiency_mills)) * 100)
							/ 100;
					_vars.playerGPUpdateTotal = Math.round(
							(entity.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills + Math.pow(entity.getData(EuruModVariables.PLAYER_VARIABLES).group_count_mills - entity.getData(EuruModVariables.PLAYER_VARIABLES).group_cutoff_mills,
									entity.getData(EuruModVariables.PLAYER_VARIABLES).group_efficiency_mills) + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPUpdateTotal) * 10)
							/ 10d;
					_vars.markSyncDirty();
				}
			} else {
				{
					EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
					_vars.group_update_mills = Math.floor(entity.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_mills * 100) / 100;
					_vars.playerGPUpdateTotal = Math.round((entity.getData(EuruModVariables.PLAYER_VARIABLES).group_raw_mills + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPUpdateTotal) * 10) / 10d;
					_vars.markSyncDirty();
				}
			}
		}
	}
}