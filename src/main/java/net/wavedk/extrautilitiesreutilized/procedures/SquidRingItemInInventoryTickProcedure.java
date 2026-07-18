package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class SquidRingItemInInventoryTickProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
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
				iobj = catobj.get((BuiltInRegistries.ITEM.getKey(EuruModItems.SQUID_RING.get()).toString())).getAsJsonObject();
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
		if (entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGPChecking) {
			{
				EuruModVariables.PlayerVariables _vars = entity.getData(EuruModVariables.PLAYER_VARIABLES);
				_vars.playerGP_Used_Update = new Object() {
					double convert(String s) {
						try {
							return Double.parseDouble(s.trim());
						} catch (Exception e) {
						}
						return 0;
					}
				}.convert(gpr) + entity.getData(EuruModVariables.PLAYER_VARIABLES).playerGP_Used_Update;
				_vars.markSyncDirty();
			}
		}
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal("\u00A7cThe Ring of the Flying Ring is still in development and does not yet work!"), true);
	}
}