package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import java.util.List;

import java.io.File;

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
			catobj = EuruModVariables.unified_config.get(returnString).getAsJsonObject();
			bobj = catobj.get("fuelProperties").getAsJsonObject();
			itemobj = bobj.get(itemstring).getAsJsonObject();
			returnString = "" + itemobj.get("feGenerated").getAsDouble();
			speed = "" + itemobj.get("feSpeed").getAsDouble();
		} else {
			return "";
		}
		return "Generates " + returnString + "FE @ " + speed + "FE/t";
	}
}