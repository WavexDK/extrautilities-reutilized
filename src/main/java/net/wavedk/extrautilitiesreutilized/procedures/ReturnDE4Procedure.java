package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class ReturnDE4Procedure {
	public static String execute(List<String> strings) {
		if (strings == null)
			return "";
		String cString = "";
		String cMaxString = "";
		double cMax = 0;
		double cLevel = 0;
		double cWeight = 0;
		double cFE = 0;
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fpobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject mobj = new com.google.gson.JsonObject();
		cLevel = 4;
		cfile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_fe_config.json");
		if (strings != null) {
			for (String stringiterator : strings) {
				if (!stringiterator.contains("NATURAL MAX")) {
					cString = stringiterator;
				}
			}
		}
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
				bobj = obj.get((BuiltInRegistries.BLOCK.getKey(EuruModBlocks.DISENCHANTMENT_GENERATOR.get()).toString())).getAsJsonObject();
				fpobj = bobj.get("fuelProperties").getAsJsonObject();
				mobj = fpobj.get("math_-Dont_touch_this_if_you_dont_know_what_youre_doing").getAsJsonObject();
				cWeight = mobj.get("currentWeight").getAsDouble();
				cFE = mobj.get("totalFEGenerated").getAsDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cMax = new Object() {
			double convert(String s) {
				try {
					return Double.parseDouble(s.trim());
				} catch (Exception e) {
				}
				return 0;
			}
		}.convert(cString);
		return "Book Level: " + cLevel + "\n" + ((Math.round(cFE * Math.pow(cLevel / cMax, cWeight)) - Math.round(cFE * Math.pow((cLevel - 1) / cMax, cWeight))) + "FE");
	}
}