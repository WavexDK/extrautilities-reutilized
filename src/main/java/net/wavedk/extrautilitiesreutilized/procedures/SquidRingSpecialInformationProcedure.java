package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.core.registries.BuiltInRegistries;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class SquidRingSpecialInformationProcedure {
	public static String execute() {
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		String gpr = "";
		cfile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru"), File.separator + "euru_unified_config.json");
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(cfile));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				catobj = obj.get("general").getAsJsonObject();
				iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.SQUID_RING.get()).toString())).getAsJsonObject();
				gpr = "" + iobj.get("gp_needed").getAsDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "\u00A77Grants you temporary jetpack-like flying" + "\n" + ("\u00A77Requires " + gpr + "GP");
	}
}