package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;

public class FrostyGenCFGManagerProcedure {
	public static com.google.gson.JsonObject execute() {
		com.google.gson.JsonObject cItemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configJsonObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenobj = new com.google.gson.JsonObject();
		String cItem = "";
		com.google.gson.JsonArray oarray = new com.google.gson.JsonArray();
		cItem = BuiltInRegistries.ITEM.getKey(Blocks.BLUE_ICE.asItem()).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 1600);
		cItemOBJ.addProperty("feSpeed", 40);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Blocks.PACKED_ICE.asItem()).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 1000);
		cItemOBJ.addProperty("feSpeed", 40);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Blocks.SNOW.asItem()).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 200);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Blocks.SNOW_BLOCK.asItem()).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 400);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Items.SNOWBALL).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 200);
		cItemOBJ.addProperty("feSpeed", 10);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Blocks.ICE.asItem()).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 800);
		cItemOBJ.addProperty("feSpeed", 40);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		ogenobj.add("listFuel", oarray);
		ogenobj.add("fuelProperties", ogenfp);
		return ogenobj;
	}
}