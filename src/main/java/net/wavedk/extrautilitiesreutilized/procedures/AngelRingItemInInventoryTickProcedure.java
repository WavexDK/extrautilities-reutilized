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

public class AngelRingItemInInventoryTickProcedure {
	public static void execute(ItemStack itemstack) {
		String gpr = "";
		File cfile = new File("");
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject iobj = new com.google.gson.JsonObject();
		cfile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru"), File.separator + "euru_unified_config.json");
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(cfile));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				catobj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				catobj = catobj.get("general").getAsJsonObject();
				iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.ANGEL_RING.get()).toString())).getAsJsonObject();
				gpr = "" + iobj.get("gp_needed").getAsDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		{
			final String _tagName = "gp-using";
			final double _tagValue = new Object() {
				double convert(String s) {
					try {
						return Double.parseDouble(s.trim());
					} catch (Exception e) {
					}
					return 0;
				}
			}.convert(gpr);
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
	}
}