package net.wavedk.extrautilitiesreutilized.procedures;

import net.neoforged.fml.loading.FMLPaths;

import java.util.List;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class SGenRecipeTypeValueProcedure {
	public static String execute(List<String> strings) {
		if (strings == null)
			return "";
		File fil = new File("");
		boolean setreturn = false;
		com.google.gson.JsonObject jsono = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject itemobj = new com.google.gson.JsonObject();
		String returnString = "";
		String itemstring = "";
		String speed = "";
		returnString = "";
		if (strings != null) {
			for (String stringiterator : strings) {
				if (!setreturn) {
					returnString = stringiterator;
					setreturn = true;
				} else {
					itemstring = stringiterator;
				}
			}
		}
		if ((returnString).length() > 0) {
			fil = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_fe_config.json");
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(fil));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					jsono = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					catobj = jsono.get(returnString).getAsJsonObject();
					bobj = catobj.get("fuelProperties").getAsJsonObject();
					itemobj = bobj.get(itemstring).getAsJsonObject();
					returnString = "" + itemobj.get("feGenerated").getAsDouble();
					speed = "" + itemobj.get("feSpeed").getAsDouble();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			return "";
		}
		return "Generates " + returnString + "FE @ " + speed + "FE/t";
	}
}