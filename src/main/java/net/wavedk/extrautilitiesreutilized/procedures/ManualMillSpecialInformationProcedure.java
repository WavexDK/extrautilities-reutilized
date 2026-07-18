package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.core.registries.BuiltInRegistries;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class ManualMillSpecialInformationProcedure {
	public static String execute() {
		String gpr = "";
		File cfile = new File("");
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
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
				catobj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				catobj = catobj.get("gp_generation").getAsJsonObject();
				iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModBlocks.MANUAL_MILL.get().asItem()).toString())).getAsJsonObject();
				gpr = "" + iobj.get("gp_generated").getAsDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "\u00A77Generates " + gpr + "\u00A77GP while a player is actively right-clicking it.";
	}
}