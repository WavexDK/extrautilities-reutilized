package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.BuiltInRegistries;

import java.io.File;

public class AngelRingItemInInventoryTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		String gpr = "";
		File cfile = new File("");
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		catobj = EuruModVariables.unified_config.get("general").getAsJsonObject();
		iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.ANGEL_RING.get()).toString())).getAsJsonObject();
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGP_Used_Update = entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update + iobj.get("gp_needed").getAsDouble();
				_vars.markSyncDirty();
			}
		}
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used <= entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Total && !entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
			entity.getPersistentData().putDouble("canFlyCounterAngelRing", 5);
		}
	}
}