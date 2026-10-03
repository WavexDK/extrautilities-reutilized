package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;

public class HalitosisGenCFGManagerProcedure {
	public static com.google.gson.JsonObject execute() {
		com.google.gson.JsonObject cItemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configJsonObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenobj = new com.google.gson.JsonObject();
		String cItem = "";
		com.google.gson.JsonArray oarray = new com.google.gson.JsonArray();
		cItem = BuiltInRegistries.ITEM.getKey(Items.DRAGON_BREATH).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 200000);
		cItemOBJ.addProperty("feSpeed", 60);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		ogenobj.add("listFuel", oarray);
		ogenobj.add("fuelProperties", ogenfp);
		return ogenobj;
	}
}