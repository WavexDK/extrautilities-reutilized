package net.wavedk.extrautilitiesreutilized.procedures;

public class PGenCFGManagerProcedure {
	public static com.google.gson.JsonObject execute() {
		com.google.gson.JsonObject cItemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configJsonObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenobj = new com.google.gson.JsonObject();
		String cItem = "";
		com.google.gson.JsonArray oarray = new com.google.gson.JsonArray();
		cItem = "euru:pink_stuff";
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 300);
		cItemOBJ.addProperty("feSpeed", 30);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		ogenobj.add("listFuel", oarray);
		ogenobj.add("fuelProperties", ogenfp);
		return ogenobj;
	}
}