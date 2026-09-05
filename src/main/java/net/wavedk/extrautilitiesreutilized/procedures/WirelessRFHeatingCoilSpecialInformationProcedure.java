package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import java.io.File;

public class WirelessRFHeatingCoilSpecialInformationProcedure {
	public static String execute(ItemStack itemstack) {
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		gobj = EuruModVariables.unified_config.get("general").getAsJsonObject();
		iobj = gobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.WIRELESS_RF_HEATING_COIL.get()).toString())).getAsJsonObject();
		{
			final String _tagName = "required_fe_per_tick";
			final double _tagValue = iobj.get("required_fe_per_tick").getAsDouble();
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
		if ((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock")).isEmpty()) {
			return "\u00A77Not synced with any Wireless Battery.";
		}
		return ("\u00A77Synced with: " + (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock")).replace(",", ", ")) + "\n"
				+ ("\u00A77Requires " + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("required_fe_per_tick") + "FE/t in the Wireless Battery.");
	}
}