package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import java.util.List;

import java.io.File;

public class EnchanterRecipeTypeValueProcedure {
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
		String feneeeded = "";
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
			catobj = EuruModVariables.unified_config.get("recipes").getAsJsonObject();
			bobj = catobj.get(returnString).getAsJsonObject();
			itemobj = bobj.get(itemstring).getAsJsonObject();
			returnString = "" + itemobj.get("gp_required").getAsDouble();
			feneeeded = "" + itemobj.get("fe_required").getAsDouble();
		} else {
			return "";
		}
		return "Requires " + returnString + (" Grid Power & " + feneeeded + "FE.");
	}
}