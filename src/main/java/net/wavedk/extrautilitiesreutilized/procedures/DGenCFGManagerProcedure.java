package net.wavedk.extrautilitiesreutilized.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;

public class DGenCFGManagerProcedure {
	public static com.google.gson.JsonObject execute() {
		com.google.gson.JsonObject cItemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configJsonObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenobj = new com.google.gson.JsonObject();
		String cItem = "";
		com.google.gson.JsonArray oarray = new com.google.gson.JsonArray();
		cItem = BuiltInRegistries.ITEM.getKey(Items.BONE).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 400);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Blocks.BONE_BLOCK.asItem()).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 1200);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Blocks.WITHER_SKELETON_SKULL.asItem()).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 1200);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Items.FERMENTED_SPIDER_EYE).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 300);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Items.SPIDER_EYE).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 200);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		cItem = BuiltInRegistries.ITEM.getKey(Items.ROTTEN_FLESH).toString();
		cItemOBJ = new com.google.gson.JsonObject();
		cItemOBJ.addProperty("feGenerated", 400);
		cItemOBJ.addProperty("feSpeed", 20);
		ogenfp.add(cItem, cItemOBJ);
		oarray.add(cItem);
		ogenobj.add("listFuel", oarray);
		ogenobj.add("fuelProperties", ogenfp);
		return ogenobj;
	}
}