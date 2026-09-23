package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.minecraft.core.registries.BuiltInRegistries;

import java.io.File;

public class DragonMillSpecialInformationProcedure {
	public static String execute() {
		String gpr = "";
		File cfile = new File("");
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		catobj = EuruModVariables.unified_config.get("gp_generation").getAsJsonObject();
		iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModBlocks.DRAGON_EGG_MILL.get().asItem()).toString())).getAsJsonObject();
		gpr = "" + iobj.get("gp_generated").getAsDouble();
		return "\u00A77Generates " + gpr + "\u00A77GP while a dragon egg is on top of it.";
	}
}