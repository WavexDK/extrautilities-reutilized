package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class WirelessRFHeatingCoilSpecialInformationProcedure {
	public static String execute(ItemStack itemstack) {
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("required_fe_per_tick") == 0) {
			cfile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(cfile));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					gobj = obj.get("general").getAsJsonObject();
					iobj = gobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.WIRELESS_RF_HEATING_COIL.get()).toString())).getAsJsonObject();
					{
						final String _tagName = "required_fe_per_tick";
						final double _tagValue = iobj.get("required_fe_per_tick").getAsDouble();
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if ((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock")).isEmpty()) {
			return "\u00A77Not synced with any Wireless Battery.";
		}
		return ("\u00A77Synced with: " + (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("syncedBlock")).replace(",", ", ")) + "\n"
				+ ("\u00A77Requires " + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("required_fe_per_tick") + "FE/t in the Wireless Battery.");
	}
}