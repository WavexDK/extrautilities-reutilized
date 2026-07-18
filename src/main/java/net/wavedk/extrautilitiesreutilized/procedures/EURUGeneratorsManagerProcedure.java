package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class EURUGeneratorsManagerProcedure {
	public static void execute() {
		File configFile = new File("");
		File file = new File("");
		double cVer = 0;
		String cItem = "";
		com.google.gson.JsonObject configSubJsonObjest = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configJsonObject = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fuelPropertiesOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject coalOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject saplingsobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject logsobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject planksobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject stickobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wool = new com.google.gson.JsonObject();
		com.google.gson.JsonObject carpet = new com.google.gson.JsonObject();
		com.google.gson.JsonObject day = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wsword = new com.google.gson.JsonObject();
		com.google.gson.JsonObject waxe = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wpick = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wsho = new com.google.gson.JsonObject();
		com.google.gson.JsonObject whoe = new com.google.gson.JsonObject();
		com.google.gson.JsonObject shield = new com.google.gson.JsonObject();
		com.google.gson.JsonObject stickcarrot = new com.google.gson.JsonObject();
		com.google.gson.JsonObject warpstick = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bookshelf = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cbookshelf = new com.google.gson.JsonObject();
		com.google.gson.JsonObject button = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wooden_doors = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wooden_fences = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wooden_pressure_plates = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wooden_slabs = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wooden_stairs = new com.google.gson.JsonObject();
		com.google.gson.JsonObject wooden_trapdoors = new com.google.gson.JsonObject();
		com.google.gson.JsonObject coalblock = new com.google.gson.JsonObject();
		com.google.gson.JsonObject craft = new com.google.gson.JsonObject();
		com.google.gson.JsonObject chest = new com.google.gson.JsonObject();
		com.google.gson.JsonObject tchest = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ladder = new com.google.gson.JsonObject();
		com.google.gson.JsonObject juke = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bowl = new com.google.gson.JsonObject();
		com.google.gson.JsonObject signs = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bow = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cbow = new com.google.gson.JsonObject();
		com.google.gson.JsonObject boats = new com.google.gson.JsonObject();
		com.google.gson.JsonObject beds = new com.google.gson.JsonObject();
		com.google.gson.JsonObject beehive = new com.google.gson.JsonObject();
		com.google.gson.JsonObject torch = new com.google.gson.JsonObject();
		com.google.gson.JsonObject torchred = new com.google.gson.JsonObject();
		com.google.gson.JsonObject torchsoul = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cart = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fletch = new com.google.gson.JsonObject();
		com.google.gson.JsonObject smith = new com.google.gson.JsonObject();
		com.google.gson.JsonObject loom = new com.google.gson.JsonObject();
		com.google.gson.JsonObject camp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject soulcampo = new com.google.gson.JsonObject();
		com.google.gson.JsonObject scaff = new com.google.gson.JsonObject();
		com.google.gson.JsonObject compostor = new com.google.gson.JsonObject();
		com.google.gson.JsonObject astand = new com.google.gson.JsonObject();
		com.google.gson.JsonObject glowframe = new com.google.gson.JsonObject();
		com.google.gson.JsonObject frame = new com.google.gson.JsonObject();
		com.google.gson.JsonObject paint = new com.google.gson.JsonObject();
		com.google.gson.JsonObject lect = new com.google.gson.JsonObject();
		com.google.gson.JsonObject flowers = new com.google.gson.JsonObject();
		com.google.gson.JsonObject leaves = new com.google.gson.JsonObject();
		com.google.gson.JsonObject blaze = new com.google.gson.JsonObject();
		com.google.gson.JsonObject cItemOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject furnaceGenOBJ = new com.google.gson.JsonObject();
		com.google.gson.JsonObject fgenFP = new com.google.gson.JsonObject();
		com.google.gson.JsonObject hangingsignsobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject egenobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject egenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject lfegen = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject ogenobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject nsgenobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject nsgenfp = new com.google.gson.JsonObject();
		com.google.gson.JsonArray sArray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray cItemArray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray fgenarray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray earray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray oarray = new com.google.gson.JsonArray();
		com.google.gson.JsonArray nsarray = new com.google.gson.JsonArray();
		configFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/euru/"), File.separator + "euru_fe_config.json");
		cVer = EuruModVariables.cVer;
		if (!configFile.exists()) {
			try {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			} // furnaceGen
				// ladder
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.LADDER.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 180);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// glowitemframe
			cItem = BuiltInRegistries.ITEM.getKey(Items.GLOW_ITEM_FRAME).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 180);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// itemframe
			cItem = BuiltInRegistries.ITEM.getKey(Items.ITEM_FRAME).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 180);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// paint
			cItem = BuiltInRegistries.ITEM.getKey(Items.PAINTING).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 180);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// armorstand
			cItem = BuiltInRegistries.ITEM.getKey(Items.ARMOR_STAND).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 180);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// fences
			cItem = "minecraft:wooden_fences";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 250);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// fence_gates
			cItem = "minecraft:fence_gates";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 250);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// wooden_trapdoors
			cItem = "minecraft:wooden_trapdoors";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 250);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// wooden_pressure_plates
			cItem = "minecraft:wooden_pressure_plates";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 180);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// hangsigns
			cItem = "minecraft:hanging_signs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// signs
			cItem = "minecraft:signs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// door
			cItem = "minecraft:wooden_doors";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 800);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// bow
			cItem = BuiltInRegistries.ITEM.getKey(Items.CROSSBOW).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// bow
			cItem = BuiltInRegistries.ITEM.getKey(Items.BOW).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// hoe
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_HOE).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 500);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// axe
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_AXE).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 500);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// shovel
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_SHOVEL).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 500);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// pick
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_PICKAXE).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 500);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// sword
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_SWORD).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 500);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// banners
			cItem = "minecraft:banners";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 250);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// slabs
			cItem = "minecraft:wooden_slabs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 250);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// flowers
			cItem = "minecraft:leaves";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 120);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// flowers
			cItem = "minecraft:flowers";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 80);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// beds
			cItem = "minecraft:beds";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 400);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// chiseledbook
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.SCAFFOLDING.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// chiseledbook
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CHISELED_BOOKSHELF.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// book
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BOOKSHELF.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// cart
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CARTOGRAPHY_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// fletch
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.FLETCHING_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// smith
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.SMITHING_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// loom
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.LOOM.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// craft
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CRAFTING_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// logs
			cItem = "minecraft:logs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// wooden_stairs
			cItem = "minecraft:wooden_stairs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// boats
			cItem = "minecraft:boats";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 600);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// coalblock
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.COAL_BLOCK.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 14000);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// barrel
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BARREL.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// lectern
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.LECTERN.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// compost
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.COMPOSTER.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// tchest
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.TRAPPED_CHEST.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// chest
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CHEST.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// planks
			cItem = "minecraft:planks";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// wooden_buttons
			cItem = "minecraft:wooden_buttons";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 150);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// bowl
			cItem = BuiltInRegistries.ITEM.getKey(Items.BOWL).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 150);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// carrotrod
			cItem = BuiltInRegistries.ITEM.getKey(Items.CARROT_ON_A_STICK).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// warprod
			cItem = BuiltInRegistries.ITEM.getKey(Items.WARPED_FUNGUS_ON_A_STICK).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// fishrod
			cItem = BuiltInRegistries.ITEM.getKey(Items.FISHING_ROD).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// bamboo
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BAMBOO.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// stick
			cItem = BuiltInRegistries.ITEM.getKey(Items.STICK).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// saplings
			cItem = "minecraft:saplings";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// wool
			cItem = "minecraft:wool";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 260);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// wool_carpets
			cItem = "minecraft:wool_carpets";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 150);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// coals
			cItem = "minecraft:coals";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 1500);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.BLAZE_ROD).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 16000);
			cItemOBJ.addProperty("feSpeed", 20);
			fgenFP.add(cItem, cItemOBJ);
			fgenarray.add(cItem);
			furnaceGenOBJ.add("listFuel", fgenarray);
			furnaceGenOBJ.add("fuelProperties", fgenFP);
			configJsonObject.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.FURNACE_GENERATOR.get().asItem()).toString()), furnaceGenOBJ);// surGen
			// barrel
			cItemOBJ = new com.google.gson.JsonObject();
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BARREL.asItem()).toString();
			cItemOBJ.addProperty("feGenerated", 600);
			cItemOBJ.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add(cItem, cItemOBJ);
			sArray.add(cItem);// blaze
			cItemOBJ = new com.google.gson.JsonObject();
			cItem = "minecraft:fence_gates";
			cItemOBJ.addProperty("feGenerated", 450);
			cItemOBJ.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add(cItem, cItemOBJ);
			sArray.add(cItem);// blaze
			cItemOBJ = new com.google.gson.JsonObject();
			cItem = "minecraft:banners";
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add(cItem, cItemOBJ);
			sArray.add(cItem);// blaze
			cItemOBJ = new com.google.gson.JsonObject();
			cItem = BuiltInRegistries.ITEM.getKey(Items.FISHING_ROD).toString();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add(cItem, cItemOBJ);
			sArray.add(cItem);// blaze
			cItemOBJ = new com.google.gson.JsonObject();
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BAMBOO.asItem()).toString();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add(cItem, cItemOBJ);
			sArray.add(cItem);// blaze
			cItemOBJ = new com.google.gson.JsonObject();
			cItem = BuiltInRegistries.ITEM.getKey(Items.BLAZE_ROD).toString();
			cItemOBJ.addProperty("feGenerated", 20000);
			cItemOBJ.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add(cItem, cItemOBJ);
			sArray.add(cItem);// listFuel
			sArray.add("minecraft:coals");
			sArray.add("minecraft:saplings");
			sArray.add("minecraft:logs");
			sArray.add("minecraft:planks");
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.STICK).toString()));
			sArray.add("minecraft:wool");
			sArray.add("minecraft:hanging_signs");
			sArray.add("minecraft:wool_carpets");
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.DAYLIGHT_DETECTOR.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_AXE).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_SWORD).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_PICKAXE).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_SHOVEL).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_HOE).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.SHIELD).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.CARROT_ON_A_STICK).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.WARPED_FUNGUS_ON_A_STICK).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.BOOKSHELF.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.CHISELED_BOOKSHELF.asItem()).toString()));
			sArray.add("minecraft:wooden_buttons");
			sArray.add("minecraft:wooden_doors");
			sArray.add("minecraft:wooden_fences");
			sArray.add("minecraft:wooden_pressure_plates");
			sArray.add("minecraft:wooden_slabs");
			sArray.add("minecraft:wooden_stairs");
			sArray.add("minecraft:wooden_trapdoors");
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.COAL_BLOCK.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.CRAFTING_TABLE.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.CHEST.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.TRAPPED_CHEST.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.LADDER.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.JUKEBOX.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.BEEHIVE.asItem()).toString()));
			sArray.add("minecraft:beds");
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.BOWL).toString()));
			sArray.add("minecraft:signs");
			sArray.add("minecraft:boats");
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.BOW).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.CROSSBOW).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.TORCH).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.REDSTONE_TORCH).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.SOUL_TORCH).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.CARTOGRAPHY_TABLE.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.FLETCHING_TABLE.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.SMITHING_TABLE.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.LOOM.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.CAMPFIRE.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.SOUL_CAMPFIRE.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.SCAFFOLDING.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.COMPOSTER.asItem()).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.ARMOR_STAND).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.GLOW_ITEM_FRAME).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.ITEM_FRAME).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Items.PAINTING).toString()));
			sArray.add((BuiltInRegistries.ITEM.getKey(Blocks.LECTERN.asItem()).toString()));
			sArray.add("minecraft:flowers");
			sArray.add("minecraft:leaves");// fuelProperties
			// leaves
			leaves.addProperty("feGenerated", 80);
			leaves.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:leaves", leaves);// flowers
			hangingsignsobj.addProperty("feGenerated", 350);
			hangingsignsobj.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:hanging_signs", hangingsignsobj);// flowers
			flowers.addProperty("feGenerated", 80);
			flowers.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:flowers", flowers);// lectern
			lect.addProperty("feGenerated", 350);
			lect.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.LECTERN.asItem()).toString()), lect);// painting
			paint.addProperty("feGenerated", 200);
			paint.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.PAINTING).toString()), paint);// frame
			frame.addProperty("feGenerated", 200);
			frame.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.ITEM_FRAME).toString()), frame);// glowframe
			glowframe.addProperty("feGenerated", 200);
			glowframe.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.GLOW_ITEM_FRAME).toString()), glowframe);// armorstand
			astand.addProperty("feGenerated", 200);
			astand.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.ARMOR_STAND).toString()), astand);// composter
			compostor.addProperty("feGenerated", 350);
			compostor.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.COMPOSTER.asItem()).toString()), compostor);// scaffolding
			scaff.addProperty("feGenerated", 350);
			scaff.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.SCAFFOLDING.asItem()).toString()), scaff);// soulcamp
			soulcampo.addProperty("feGenerated", 350);
			soulcampo.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.SOUL_CAMPFIRE.asItem()).toString()), soulcampo);// campfire
			camp.addProperty("feGenerated", 350);
			camp.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.CAMPFIRE.asItem()).toString()), camp);// smith
			loom.addProperty("feGenerated", 350);
			loom.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.LOOM.asItem()).toString()), loom);// smith
			smith.addProperty("feGenerated", 350);
			smith.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.SMITHING_TABLE.asItem()).toString()), smith);// fletch
			fletch.addProperty("feGenerated", 350);
			fletch.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.FLETCHING_TABLE.asItem()).toString()), fletch);// cart
			cart.addProperty("feGenerated", 350);
			cart.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.CARTOGRAPHY_TABLE.asItem()).toString()), cart);// torch
			torch.addProperty("feGenerated", 100);
			torch.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.TORCH).toString()), torch);// redtorch
			torchred.addProperty("feGenerated", 100);
			torchred.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.REDSTONE_TORCH).toString()), torchred);// soultorch
			torchsoul.addProperty("feGenerated", 100);
			torchsoul.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.SOUL_TORCH).toString()), torchsoul);// beehive
			beehive.addProperty("feGenerated", 400);
			beehive.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.BEEHIVE.asItem()).toString()), beehive);// minecraft:beds
			beds.addProperty("feGenerated", 600);
			beds.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:beds", beds);// cbow
			cbow.addProperty("feGenerated", 200);
			cbow.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.CROSSBOW).toString()), cbow);// bow
			bow.addProperty("feGenerated", 200);
			bow.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.BOW).toString()), bow);// boats
			boats.addProperty("feGenerated", 600);
			boats.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:boats", boats);// minecraft:signs
			signs.addProperty("feGenerated", 350);
			signs.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:signs", signs);// bowl
			bowl.addProperty("feGenerated", 200);
			bowl.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.BOWL).toString()), bowl);// juke
			juke.addProperty("feGenerated", 350);
			juke.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.JUKEBOX.asItem()).toString()), juke);// ladder
			ladder.addProperty("feGenerated", 350);
			ladder.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.LADDER.asItem()).toString()), ladder);// tchest
			tchest.addProperty("feGenerated", 700);
			tchest.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.TRAPPED_CHEST.asItem()).toString()), tchest);// chest
			chest.addProperty("feGenerated", 700);
			chest.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.CHEST.asItem()).toString()), chest);// craft
			craft.addProperty("feGenerated", 350);
			craft.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.CRAFTING_TABLE.asItem()).toString()), craft);// coalblock
			coalblock.addProperty("feGenerated", 23000);
			coalblock.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.COAL_BLOCK.asItem()).toString()), coalblock);// minecraft:wooden_trapdoors
			wooden_trapdoors.addProperty("feGenerated", 350);
			wooden_trapdoors.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wooden_trapdoors", wooden_trapdoors);// minecraft:wooden_stairs
			wooden_stairs.addProperty("feGenerated", 350);
			wooden_stairs.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wooden_stairs", wooden_stairs);// minecraft:wooden_slabs
			wooden_slabs.addProperty("feGenerated", 175);
			wooden_slabs.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wooden_slabs", wooden_slabs);// wooden_pressure_plates
			wooden_pressure_plates.addProperty("feGenerated", 200);
			wooden_pressure_plates.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wooden_pressure_plates", wooden_pressure_plates);// minecraft:wooden_fences
			wooden_fences.addProperty("feGenerated", 450);
			wooden_fences.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wooden_fences", wooden_fences);// minecraft:wooden_doors
			wooden_doors.addProperty("feGenerated", 800);
			wooden_doors.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wooden_doors", wooden_doors);// minecraft:wooden_buttons
			button.addProperty("feGenerated", 80);
			button.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wooden_buttons", button);// cbookshelf
			cbookshelf.addProperty("feGenerated", 200);
			cbookshelf.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.CHISELED_BOOKSHELF.asItem()).toString()), cbookshelf);// bookshelf
			bookshelf.addProperty("feGenerated", 200);
			bookshelf.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.BOOKSHELF.asItem()).toString()), bookshelf);// stickwarp
			warpstick.addProperty("feGenerated", 200);
			warpstick.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.WARPED_FUNGUS_ON_A_STICK).toString()), warpstick);// stickcarrot
			stickcarrot.addProperty("feGenerated", 200);
			stickcarrot.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.CARROT_ON_A_STICK).toString()), stickcarrot);// shield
			shield.addProperty("feGenerated", 1200);
			shield.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.SHIELD).toString()), shield);// whoe
			whoe.addProperty("feGenerated", 400);
			whoe.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_HOE).toString()), whoe);// wsho
			wsho.addProperty("feGenerated", 400);
			wsho.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_SHOVEL).toString()), wsho);// wpick
			wpick.addProperty("feGenerated", 400);
			wpick.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_PICKAXE).toString()), wpick);// waxe
			waxe.addProperty("feGenerated", 400);
			waxe.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_AXE).toString()), waxe);// wsword
			wsword.addProperty("feGenerated", 400);
			wsword.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.WOODEN_SWORD).toString()), wsword);// daylight
			day.addProperty("feGenerated", 400);
			day.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Blocks.DAYLIGHT_DETECTOR.asItem()).toString()), day);// minecraft:wool_carpets
			carpet.addProperty("feGenerated", 100);
			carpet.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wool_carpets", carpet);// minecraft:wool
			wool.addProperty("feGenerated", 200);
			wool.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:wool", wool);// stick
			stickobj.addProperty("feGenerated", 100);
			stickobj.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add((BuiltInRegistries.ITEM.getKey(Items.STICK).toString()), stickobj);// planks
			planksobj.addProperty("feGenerated", 350);
			planksobj.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:planks", planksobj);// Logs
			logsobj.addProperty("feGenerated", 350);
			logsobj.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:logs", logsobj);// Coal
			coalOBJ.addProperty("feGenerated", 2500);
			coalOBJ.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:coals", coalOBJ);// Saplings
			saplingsobj.addProperty("feGenerated", 175);
			saplingsobj.addProperty("feSpeed", 5);
			fuelPropertiesOBJ.add("minecraft:saplings", saplingsobj);
			configSubJsonObjest.add("listFuel", sArray);
			configSubJsonObjest.add("fuelProperties", fuelPropertiesOBJ);
			configJsonObject.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.SURVIVAL_GENERATOR.get().asItem()).toString()), configSubJsonObjest);// egen
			// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.NETHER_STAR).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 480000);
			cItemOBJ.addProperty("feSpeed", 1600);
			nsgenfp.add(cItem, cItemOBJ);
			nsarray.add(cItem);
			nsgenobj.add("listFuel", nsarray);
			nsgenobj.add("fuelProperties", nsgenfp);
			configJsonObject.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.NETHERSTAR_GENERATOR.get().asItem()).toString()), nsgenobj);// egen
			// blaze
			cItem = BuiltInRegistries.ITEM.getKey(EuruModItems.ENDER_SHARD.get()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 1000);
			cItemOBJ.addProperty("feSpeed", 80);
			egenfp.add(cItem, cItemOBJ);
			earray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.ENDER_EYE).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 12000);
			cItemOBJ.addProperty("feSpeed", 120);
			egenfp.add(cItem, cItemOBJ);
			earray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.ENDER_PEARL).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 8000);
			cItemOBJ.addProperty("feSpeed", 80);
			egenfp.add(cItem, cItemOBJ);
			earray.add(cItem);
			egenobj.add("listFuel", earray);
			egenobj.add("fuelProperties", egenfp);
			configJsonObject.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.ENDER_GENERATOR.get().asItem()).toString()), egenobj);// ogen
			// blaze
			cItem = "minecraft:wooden_trapdoors";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wooden_pressure_plates";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wooden_doors";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wooden_stairs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_SWORD).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_SHOVEL).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_PICKAXE).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_HOE).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.WOODEN_AXE).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.WARPED_FUNGUS_ON_A_STICK).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.SMITHING_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.SCAFFOLDING.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.PAINTING).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.NOTE_BLOCK.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.LOOM.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.LECTERN.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.LADDER.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.JUKEBOX.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:hanging_signs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:signs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 200);
			cItemOBJ.addProperty("feSpeed", 200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.ITEM_FRAME).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.GLOW_ITEM_FRAME).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:flowers";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 60);
			cItemOBJ.addProperty("feSpeed", 60);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.FLETCHING_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.FISHING_ROD).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.CROSSBOW).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CRAFTING_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.COMPOSTER.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CHISELED_BOOKSHELF.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CHEST.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:coals";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 1200);
			cItemOBJ.addProperty("feSpeed", 1200);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.CARTOGRAPHY_TABLE.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.CARROT_ON_A_STICK).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wooden_buttons";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.BOWL).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.BOW).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BOOKSHELF.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:boats";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 400);
			cItemOBJ.addProperty("feSpeed", 400);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.BLAZE_ROD).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 2000);
			cItemOBJ.addProperty("feSpeed", 2000);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:beds";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 400);
			cItemOBJ.addProperty("feSpeed", 400);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BARREL.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:banners";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.STICK).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 60);
			cItemOBJ.addProperty("feSpeed", 60);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.BAMBOO.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 60);
			cItemOBJ.addProperty("feSpeed", 60);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Items.ARMOR_STAND).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wool";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wooden_slabs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:planks";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:saplings";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 100);
			cItemOBJ.addProperty("feSpeed", 100);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:logs";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.TRAPPED_CHEST.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.DAYLIGHT_DETECTOR.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wool_carpets";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 60);
			cItemOBJ.addProperty("feSpeed", 60);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:fence_gates";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = "minecraft:wooden_fences";
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 300);
			cItemOBJ.addProperty("feSpeed", 300);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);// blaze
			cItem = BuiltInRegistries.ITEM.getKey(Blocks.COAL_BLOCK.asItem()).toString();
			cItemOBJ = new com.google.gson.JsonObject();
			cItemOBJ.addProperty("feGenerated", 8000);
			cItemOBJ.addProperty("feSpeed", 2000);
			ogenfp.add(cItem, cItemOBJ);
			oarray.add(cItem);
			ogenobj.add("listFuel", oarray);
			ogenobj.add("fuelProperties", ogenfp);
			configJsonObject.add((BuiltInRegistries.ITEM.getKey(EuruModBlocks.OVERCLOCKED_GENERATOR.get().asItem()).toString()), ogenobj);
			configJsonObject.addProperty("config_version", cVer);
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
							EURUGeneratorsManagerProcedure.execute();
						}
					} else {
						configFile.delete();
						EURUGeneratorsManagerProcedure.execute();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}