package net.wavedk.extrautilitiesreutilized.procedures;

public class SlimeyGenCFGManagerProcedure {
	public static com.google.gson.JsonObject execute() {
		com.google.gson.JsonObject cItemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configJsonObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenobj = new com.google.gson.JsonObject();
		String cItem = "";
		com.google.gson.JsonArray oarray = new com.google.gson.JsonArray();
		cItem = "c:storage_blocks/slime";
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 28800);
		cItemOBJ.addProperty("feSpeed", 35);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = "c:slime_balls";
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 3200);
		cItemOBJ.addProperty("feSpeed", 30);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		ogenobj.add("listFuel", oarray);
		ogenobj.add("fuelProperties", ogenfp);
		return ogenobj;
	}
}