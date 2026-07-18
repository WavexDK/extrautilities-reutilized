package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class UpdateGPProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking == false) {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGPChecking = true;
				_vars.markSyncDirty();
			}
			GPOverlayTickProcedure.execute(world, entity);
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.group_raw_solarpanels = 0;
				_vars.group_count_solarpanels = 0;
				_vars.group_raw_mills = 0;
				_vars.group_count_mills = 0;
				_vars.markSyncDirty();
			}
		} else {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGP_Used = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update;
				_vars.playerGP_Used_Update = 0;
				_vars.playerGPChecking = false;
				_vars.playerGPTickUpdateCounter = 1;
				_vars.playerGPUpdateTotal = 0;
				_vars.markSyncDirty();
			}
			UpdategroupmillProcedure.execute(entity);
			UpdategroupsolarpanelsProcedure.execute(entity);
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGP_Total = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPUpdateTotal;
				_vars.markSyncDirty();
			}
			GPOverlayTickProcedure.execute(world, entity);
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.updateMultipliers = true;
				_vars.markSyncDirty();
			}
		}
	}
}