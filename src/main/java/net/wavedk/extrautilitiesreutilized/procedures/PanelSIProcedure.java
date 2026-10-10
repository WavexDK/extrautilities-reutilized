package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

public class PanelSIProcedure {
	public static String execute(ItemStack itemstack) {
		com.google.gson.JsonObject coibj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject allblock = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gpgen = new com.google.gson.JsonObject();
		com.google.gson.JsonObject eobj = new com.google.gson.JsonObject();
		String cat = "";
		coibj = EuruModVariables.unified_config.get("gp_efficiency_manager").getAsJsonObject();
		allblock = coibj.get("group_allblocks").getAsJsonObject();
		cat = allblock.get((BuiltInRegistries.ITEM.getKey(itemstack.getItem()).toString())).getAsString();
		eobj = coibj.get(cat).getAsJsonObject();
		gpgen = EuruModVariables.unified_config.get("gp_generation").getAsJsonObject();
		iobj = gpgen.get((BuiltInRegistries.ITEM.getKey(itemstack.getItem()).toString())).getAsJsonObject();
		return "\u00A77Generates " + iobj.get("gp_generated").getAsDouble() + "\u00A77GP at night, if the block can see the sky" + "\n"
				+ ("\u00A77Efficiency reduced by " + eobj.get("efficiency").getAsDouble() + "/block @ " + eobj.get("efficiency_cutoff").getAsDouble() + " blocks.");
	}
}