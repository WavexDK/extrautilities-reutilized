package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.minecraft.core.registries.BuiltInRegistries;

import java.io.File;

public class AngelRingSpecialInformationProcedure {
	public static String execute() {
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		String gpr = "";
		catobj = EuruModVariables.unified_config.get("general").getAsJsonObject();
		iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.ANGEL_RING.get()).toString())).getAsJsonObject();
		gpr = "" + iobj.get("gp_needed").getAsDouble();
		return "\u00A77Grants you permanent creative-like flying" + "\n" + ("\u00A77Requires " + gpr + "GP");
	}
}