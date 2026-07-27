package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

@EventBusSubscriber
public class EURUUnifiedConfigManagerProcedure {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		execute();
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
		File configFile = new File("");
		File file = new File("");
		double cVer = 0;
		com.google.gson.JsonArray rlArray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray eArray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray gLassoArray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray erlArray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray dropsfrom_evil = new com.google.gson.JsonArray();
		com.google.gson.JsonArray crusherrl = new com.google.gson.JsonArray();
		com.google.gson.JsonObject configJsonObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configSubJsonObjest = new com.google.gson.JsonObject();
		com.google.gson.JsonObject solarpanelObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject lunarobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject clOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject clt = new com.google.gson.JsonObject();
		com.google.gson.JsonObject generalOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gpGenerators = new com.google.gson.JsonObject();
		com.google.gson.JsonObject recipesOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject itemobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject lobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject eobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject arobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cwobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject srobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject gcobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject doeobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ef = new com.google.gson.JsonObject();
		com.google.gson.JsonObject machinesobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject crushobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject newJson = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wc = new com.google.gson.JsonObject();
		String cItem = "";
		String coutput = "";
		configFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_unified_config.json");
		EuruModVariables.cVer = 2.2;
		cVer = EuruModVariables.cVer;
		if (!configFile.exists()) {
			try {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			configJsonObject.addProperty("reload_blocks", false);
			configJsonObject.addProperty("info", "Changing the above value will make all the blocks in your world pull from the configs again. This can cause severe lag, use with caution.");
			configJsonObject.addProperty("range-configUpdate-min", 3000);
			configJsonObject.addProperty("range-configUpdate-max", 3600);
			configJsonObject.addProperty("info_2", "Changing the above values will change how often all blocks update from config, measured in ticks.");
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("efficiency", 0.95);
			itemobj.addProperty("efficiency_cutoff", 80);
			gobj.add("solarpanels", itemobj);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("efficiency", 0.75);
			itemobj.addProperty("efficiency_cutoff", 16);
			gobj.add("mills", itemobj);
			configJsonObject.add("group_man", gobj);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_required", 16);
			itemobj.addProperty("fe_required", 4000);
			itemobj.addProperty("lapis_required", 1);
			itemobj.addProperty("wait_time", 1600);
			itemobj.addProperty("lapis_input", (BuiltInRegistries.ITEM.getKey(Items.NETHER_STAR).toString()));
			itemobj.addProperty("output", (BuiltInRegistries.ITEM.getKey(EuruModBlocks.BLOCK_OF_EVIL_INFUSED_INGOT.get().asItem()).toString()));
			eobj.add((BuiltInRegistries.ITEM.getKey(Blocks.IRON_BLOCK.asItem()).toString()), itemobj);
			erlArray.add((BuiltInRegistries.ITEM.getKey(Blocks.IRON_BLOCK.asItem()).toString()));
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_required", 4);
			itemobj.addProperty("fe_required", 1000);
			itemobj.addProperty("lapis_required", 1);
			itemobj.addProperty("wait_time", 200);
			itemobj.addProperty("lapis_input", (BuiltInRegistries.ITEM.getKey(Items.LAPIS_LAZULI).toString()));
			itemobj.addProperty("output", (BuiltInRegistries.ITEM.getKey(EuruModItems.ENCHANTED_APPLE.get()).toString()));
			eobj.add((BuiltInRegistries.ITEM.getKey(Items.APPLE).toString()), itemobj);
			erlArray.add((BuiltInRegistries.ITEM.getKey(Items.APPLE).toString()));
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_required", 4);
			itemobj.addProperty("fe_required", 1000);
			itemobj.addProperty("lapis_required", 1);
			itemobj.addProperty("wait_time", 200);
			itemobj.addProperty("lapis_input", (BuiltInRegistries.ITEM.getKey(Items.LAPIS_LAZULI).toString()));
			itemobj.addProperty("output", (BuiltInRegistries.ITEM.getKey(EuruModItems.MAGICAL_NUGGET.get()).toString()));
			eobj.add((BuiltInRegistries.ITEM.getKey(EuruModItems.NUGGETO_EXPERIENCE.get()).toString()), itemobj);
			erlArray.add((BuiltInRegistries.ITEM.getKey(EuruModItems.NUGGETO_EXPERIENCE.get()).toString()));
			eobj.add("recipeList", erlArray);
			recipesOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.ENCHANTER.get().asItem()).toString()), eobj);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_required", 8);
			itemobj.addProperty("wait_time", 400);
			itemobj.addProperty("output", (BuiltInRegistries.ITEM.getKey(EuruModItems.LUNAR_REACTIVE_DUST.get()).toString()));
			bobj.add((BuiltInRegistries.ITEM.getKey(Items.LAPIS_LAZULI).toString()), itemobj);
			rlArray.add((BuiltInRegistries.ITEM.getKey(Items.LAPIS_LAZULI).toString()));
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_required", 8);
			itemobj.addProperty("wait_time", 200);
			itemobj.addProperty("output", (BuiltInRegistries.ITEM.getKey(EuruModBlocks.STONE_BURNT.get().asItem()).toString()));
			bobj.add((BuiltInRegistries.ITEM.getKey(Blocks.SMOOTH_STONE.asItem()).toString()), itemobj);
			rlArray.add((BuiltInRegistries.ITEM.getKey(Blocks.SMOOTH_STONE.asItem()).toString()));
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_required", 8);
			itemobj.addProperty("wait_time", 200);
			itemobj.addProperty("output", (BuiltInRegistries.ITEM.getKey(EuruModItems.UPGRADE_BASE.get()).toString()));
			bobj.add((BuiltInRegistries.ITEM.getKey(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.asItem()).toString()), itemobj);
			rlArray.add((BuiltInRegistries.ITEM.getKey(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.asItem()).toString()));
			bobj.add("recipeList", rlArray);
			recipesOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.RESONATOR.get().asItem()).toString()), bobj);
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.GRAVEL.asItem()).toString();
			coutput = BuiltInRegistries.ITEM.getKey(Blocks.SAND.asItem()).toString();
			newJson = new com.google.gson.JsonObject();
			newJson.addProperty("wait_time", 200);
			newJson.addProperty("fe_required", 1000);
			newJson.addProperty("gp_required", 0);
			newJson.addProperty("result", coutput);
			crushobj.add(cItem, newJson);
			crusherrl.add(cItem);
			crushobj.add("recipeList", crusherrl);
			recipesOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.CRUSHER.get().asItem()).toString()), crushobj);
			configJsonObject.add("recipes", recipesOBJ);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_generated", 9999);
			itemobj.addProperty("needs_sky", false);
			itemobj.addProperty("needs_day", false);
			itemobj.addProperty("needs_night", false);
			itemobj.addProperty("needs_water", 0);
			itemobj.addProperty("needs_lava", 0);
			itemobj.addProperty("needs_fire", 0);
			gpGenerators.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.CREATIVE_MILL.get().asItem()).toString()), itemobj);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_generated", 6);
			itemobj.addProperty("needs_sky", false);
			itemobj.addProperty("needs_day", false);
			itemobj.addProperty("needs_night", false);
			itemobj.addProperty("needs_water", 0);
			itemobj.addProperty("needs_lava", 0);
			itemobj.addProperty("needs_fire", 1);
			gpGenerators.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.FIRE_MILL.get().asItem()).toString()), itemobj);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_generated", 4);
			itemobj.addProperty("needs_sky", false);
			itemobj.addProperty("needs_day", false);
			itemobj.addProperty("needs_night", false);
			itemobj.addProperty("needs_water", 0);
			itemobj.addProperty("needs_lava", 1);
			itemobj.addProperty("needs_fire", 0);
			gpGenerators.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.LAVA_MILL.get().asItem()).toString()), itemobj);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_generated", 4);
			itemobj.addProperty("needs_sky", false);
			itemobj.addProperty("needs_day", false);
			itemobj.addProperty("needs_night", false);
			itemobj.addProperty("needs_water", 1);
			itemobj.addProperty("needs_lava", 0);
			itemobj.addProperty("needs_fire", 0);
			gpGenerators.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.WATER_MILL.get().asItem()).toString()), itemobj);
			itemobj = new com.google.gson.JsonObject();
			itemobj.addProperty("gp_generated", 16);
			itemobj.addProperty("needs_sky", false);
			itemobj.addProperty("needs_day", false);
			itemobj.addProperty("needs_night", false);
			itemobj.addProperty("needs_water", 0);
			itemobj.addProperty("needs_lava", 0);
			itemobj.addProperty("needs_fire", 0);
			gpGenerators.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.MANUAL_MILL.get().asItem()).toString()), itemobj);
			solarpanelObject.addProperty("gp_generated", 1);
			solarpanelObject.addProperty("needs_sky", true);
			solarpanelObject.addProperty("needs_day", true);
			solarpanelObject.addProperty("needs_night", false);
			solarpanelObject.addProperty("needs_water", 0);
			solarpanelObject.addProperty("needs_lava", 0);
			solarpanelObject.addProperty("needs_fire", 0);
			gpGenerators.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.SOLAR_PANEL.get().asItem()).toString()), solarpanelObject);
			lunarobj.addProperty("gp_generated", 0.7);
			lunarobj.addProperty("needs_sky", true);
			lunarobj.addProperty("needs_day", false);
			lunarobj.addProperty("needs_night", true);
			lunarobj.addProperty("needs_water", 0);
			lunarobj.addProperty("needs_lava", 0);
			lunarobj.addProperty("needs_fire", 0);
			gpGenerators.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.LUNAR_PANEL.get().asItem()).toString()), lunarobj);
			configJsonObject.add("gp_generation", gpGenerators);
			ef.addProperty("wait_time", 160);
			ef.addProperty("gp_needed", 8);
			ef.addProperty("fe_needed", 4000);
			machinesobj.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.ELECTRIC_FURNACE.get().asItem()).toString()), ef);
			configJsonObject.add("machines", machinesobj);
			dropsfrom_evil.add("minecraft:wither_skeleton");
			doeobj.add("drops_from", dropsfrom_evil);
			doeobj.addProperty("dropchance", 3);
			doeobj.addProperty("dropchance_multiplywithlooting", true);
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModItems.DROP_OF_EVIL.get()).toString()), doeobj);
			wc.addProperty("chance_for_growtick", 40);
			wc.addProperty("chance_for_plant_growth", 30);
			wc.addProperty("number_of_plants_grown", 2);
			wc.addProperty("info", "(!) NEVER Set the above values to 0, or your TPS will drop dramatically when using the Watering Can, and the item will not work (!)");
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModItems.WATERING_CAN.get()).toString()), wc);
			gcobj.addProperty("breaks_glass", true);
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModItems.GLASS_CUTTER.get()).toString()), gcobj);
			cwobj.addProperty("gp_needed", 4);
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModItems.CHICKEN_RING.get()).toString()), cwobj);
			srobj.addProperty("gp_needed", 16);
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModItems.SQUID_RING.get()).toString()), srobj);
			arobj.addProperty("gp_needed", 32);
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModItems.ANGEL_RING.get()).toString()), arobj);
			clOBJ.addProperty("gp_needed", 8);
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.CHUNK_LOADING_WARD.get().asItem()).toString()), clOBJ);
			clt.addProperty("enabled", true);
			generalOBJ.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.CHUNK_LOADER_TESTER.get().asItem()).toString()), clt);
			configJsonObject.add("general", generalOBJ);
			gLassoArray.add("minecraft:allay");
			gLassoArray.add("minecraft:bat");
			gLassoArray.add("minecraft:armadillo");
			gLassoArray.add("minecraft:axolotl");
			gLassoArray.add("minecraft:bee");
			gLassoArray.add("minecraft:camel");
			gLassoArray.add("minecraft:cat");
			gLassoArray.add("minecraft:chicken");
			gLassoArray.add("minecraft:cod");
			gLassoArray.add("minecraft:copper_golem");
			gLassoArray.add("minecraft:cow");
			gLassoArray.add("minecraft:dolphin");
			gLassoArray.add("minecraft:donkey");
			gLassoArray.add("minecraft:fox");
			gLassoArray.add("minecraft:glow_squid");
			gLassoArray.add("minecraft:goat");
			gLassoArray.add("minecraft:happy_ghast");
			gLassoArray.add("minecraft:horse");
			gLassoArray.add("minecraft:llama");
			gLassoArray.add("minecraft:mooshroom");
			gLassoArray.add("minecraft:mule");
			gLassoArray.add("minecraft:ocelot");
			gLassoArray.add("minecraft:sheep");
			gLassoArray.add("minecraft:tadpole");
			gLassoArray.add("minecraft:slime");
			gLassoArray.add("minecraft:villager");
			gLassoArray.add("minecraft:wandering_trader");
			gLassoArray.add("minecraft:wolf");
			gLassoArray.add("minecraft:turtle");
			gLassoArray.add("minecraft:sniffer");
			gLassoArray.add("minecraft:squid");
			gLassoArray.add("minecraft:tadpole");
			gLassoArray.add("minecraft:trader_llama");
			gLassoArray.add("minecraft:snow_golem");
			gLassoArray.add("minecraft:ocelot");
			gLassoArray.add("minecraft:panda");
			gLassoArray.add("minecraft:parrot");
			gLassoArray.add("minecraft:pig");
			gLassoArray.add("minecraft:pufferfish");
			gLassoArray.add("minecraft:polar_bear");
			gLassoArray.add("minecraft:rabbit");
			gLassoArray.add("minecraft:salmon");
			eArray.add("minecraft:frog");
			eArray.add("minecraft:creaking");
			eArray.add("minecraft:breeze");
			eArray.add("minecraft:blaze");
			eArray.add("minecraft:bogged");
			eArray.add("minecraft:cave_spider");
			eArray.add("minecraft:creeper");
			eArray.add("minecraft:elder_guardian");
			eArray.add("minecraft:enderman");
			eArray.add("minecraft:endermite");
			eArray.add("minecraft:wither_skeleton");
			eArray.add("minecraft:zoglin");
			eArray.add("minecraft:witch");
			eArray.add("minecraft:pillager");
			eArray.add("minecraft:illusioner");
			eArray.add("minecraft:evoker");
			eArray.add("minecraft:ghast");
			eArray.add("minecraft:hoglin");
			eArray.add("minecraft:guardian");
			eArray.add("minecraft:husk");
			eArray.add("minecraft:magma_cube");
			eArray.add("minecraft:piglin_brute");
			eArray.add("minecraft:phantom");
			eArray.add("minecraft:piglin");
			eArray.add("minecraft:skeleton");
			eArray.add("minecraft:silverfish");
			eArray.add("minecraft:shulker");
			eArray.add("minecraft:skeleton_horse");
			eArray.add("minecraft:spider");
			eArray.add("minecraft:vex");
			eArray.add("minecraft:stray");
			eArray.add("minecraft:vindicator");
			eArray.add("minecraft:zombie");
			eArray.add("minecraft:zombie_horse");
			eArray.add("minecraft:zombie_villager");
			lobj.add("golden", gLassoArray);
			lobj.add("cursed", eArray);
			configJsonObject.add("lasso_entities", lobj);
			configJsonObject.addProperty("config_version", cVer);
			configJsonObject.addProperty("info_3", "(!) Dont change \"config_version\"! Doing so will make all your configs regenerate. (!)");
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(configFile);
					fileWriter.write(mainGSONBuilderVariable.toJson(configJsonObject));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		} else {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					configJsonObject = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					if (configJsonObject.get("config_version").isJsonPrimitive() ? configJsonObject.get("config_version").getAsJsonPrimitive().isNumber() : false) {
						if (configJsonObject.get("config_version").getAsDouble() != cVer) {
							configFile.delete();
							EURUUnifiedConfigManagerProcedure.execute();
						}
					} else {
						configFile.delete();
						EURUUnifiedConfigManagerProcedure.execute();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		EURUGeneratorsManagerProcedure.execute();
	}
}