/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.block.*;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

public class EuruModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(EuruMod.MODID);
	public static final DeferredBlock<Block> SOLAR_PANEL;
	public static final DeferredBlock<Block> LUNAR_PANEL;
	public static final DeferredBlock<Block> RESONATOR;
	public static final DeferredBlock<Block> MACHINE_BLOCK;
	public static final DeferredBlock<Block> SURVIVAL_GENERATOR;
	public static final DeferredBlock<Block> CHUNK_LOADER_TESTER;
	public static final DeferredBlock<Block> GILDED_OBSIDIAN;
	public static final DeferredBlock<Block> ANGEL_BLOCK;
	public static final DeferredBlock<Block> COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> DOUBLE_COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> TRIPLE_COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> QUADRUPLE_COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> QUINTUPLE_COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> SEXTUPLE_COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> SEPTUPLE_COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> OCTUPLE_COMPRESSED_COBBLESTONE;
	public static final DeferredBlock<Block> MANUAL_MILL;
	public static final DeferredBlock<Block> WATER_MILL;
	public static final DeferredBlock<Block> LAVA_MILL;
	public static final DeferredBlock<Block> STONE_BURNT;
	public static final DeferredBlock<Block> FIRE_MILL;
	public static final DeferredBlock<Block> ENCHANTER;
	public static final DeferredBlock<Block> FURNACE_GENERATOR;
	public static final DeferredBlock<Block> BLOCK_OF_EVIL_INFUSED_INGOT;
	public static final DeferredBlock<Block> MINI_CHEST;
	public static final DeferredBlock<Block> SLIGHTLY_LARGER_CHEST;
	public static final DeferredBlock<Block> CREATIVE_MILL;
	public static final DeferredBlock<Block> ELECTRIC_FURNACE;
	public static final DeferredBlock<Block> ENDER_GENERATOR;
	public static final DeferredBlock<Block> CREATIVE_ENERGY_SOURCE;
	public static final DeferredBlock<Block> OVERCLOCKED_GENERATOR;
	public static final DeferredBlock<Block> CRUSHER;
	public static final DeferredBlock<Block> CHUNK_LOADING_WARD;
	public static final DeferredBlock<Block> NETHERSTAR_GENERATOR;
	public static final DeferredBlock<Block> ENDER_LILLY;
	public static final DeferredBlock<Block> ENDER_LILLY_PLANT;
	public static final DeferredBlock<Block> MUFFLER;
	public static final DeferredBlock<Block> CULINARY_GENERATOR;
	static {
		SOLAR_PANEL = REGISTRY.register("solar_panel", SolarPanelBlock::new);
		LUNAR_PANEL = REGISTRY.register("lunar_panel", LunarPanelBlock::new);
		RESONATOR = REGISTRY.register("resonator", ResonatorBlock::new);
		MACHINE_BLOCK = REGISTRY.register("machine_block", MachineBlockBlock::new);
		SURVIVAL_GENERATOR = REGISTRY.register("survival_generator", SurvivalGeneratorBlock::new);
		CHUNK_LOADER_TESTER = REGISTRY.register("chunk_loader_tester", ChunkLoaderTesterBlock::new);
		GILDED_OBSIDIAN = REGISTRY.register("gilded_obsidian", GildedObsidianBlock::new);
		ANGEL_BLOCK = REGISTRY.register("angel_block", AngelBlockBlock::new);
		COMPRESSED_COBBLESTONE = REGISTRY.register("compressed_cobblestone", CompressedCobblestoneBlock::new);
		DOUBLE_COMPRESSED_COBBLESTONE = REGISTRY.register("double_compressed_cobblestone", DoubleCompressedCobblestoneBlock::new);
		TRIPLE_COMPRESSED_COBBLESTONE = REGISTRY.register("triple_compressed_cobblestone", TripleCompressedCobblestoneBlock::new);
		QUADRUPLE_COMPRESSED_COBBLESTONE = REGISTRY.register("quadruple_compressed_cobblestone", QuadrupleCompressedCobblestoneBlock::new);
		QUINTUPLE_COMPRESSED_COBBLESTONE = REGISTRY.register("quintuple_compressed_cobblestone", QuintupleCompressedCobblestoneBlock::new);
		SEXTUPLE_COMPRESSED_COBBLESTONE = REGISTRY.register("sextuple_compressed_cobblestone", SextupleCompressedCobblestoneBlock::new);
		SEPTUPLE_COMPRESSED_COBBLESTONE = REGISTRY.register("septuple_compressed_cobblestone", SeptupleCompressedCobblestoneBlock::new);
		OCTUPLE_COMPRESSED_COBBLESTONE = REGISTRY.register("octuple_compressed_cobblestone", OctupleCompressedCobblestoneBlock::new);
		MANUAL_MILL = REGISTRY.register("manual_mill", ManualMillBlock::new);
		WATER_MILL = REGISTRY.register("water_mill", WaterMillBlock::new);
		LAVA_MILL = REGISTRY.register("lava_mill", LavaMillBlock::new);
		STONE_BURNT = REGISTRY.register("stone_burnt", StoneBurntBlock::new);
		FIRE_MILL = REGISTRY.register("fire_mill", FireMillBlock::new);
		ENCHANTER = REGISTRY.register("enchanter", EnchanterBlock::new);
		FURNACE_GENERATOR = REGISTRY.register("furnace_generator", FurnaceGeneratorBlock::new);
		BLOCK_OF_EVIL_INFUSED_INGOT = REGISTRY.register("block_of_evil_infused_ingot", BlockOfEvilInfusedIngotBlock::new);
		MINI_CHEST = REGISTRY.register("mini_chest", MiniChestBlock::new);
		SLIGHTLY_LARGER_CHEST = REGISTRY.register("slightly_larger_chest", SlightlyLargerChestBlock::new);
		CREATIVE_MILL = REGISTRY.register("creative_mill", CreativeMillBlock::new);
		ELECTRIC_FURNACE = REGISTRY.register("electric_furnace", ElectricFurnaceBlock::new);
		ENDER_GENERATOR = REGISTRY.register("ender_generator", EnderGeneratorBlock::new);
		CREATIVE_ENERGY_SOURCE = REGISTRY.register("creative_energy_source", CreativeEnergySourceBlock::new);
		OVERCLOCKED_GENERATOR = REGISTRY.register("overclocked_generator", OverclockedGeneratorBlock::new);
		CRUSHER = REGISTRY.register("crusher", CrusherBlock::new);
		CHUNK_LOADING_WARD = REGISTRY.register("chunk_loading_ward", ChunkLoadingWardBlock::new);
		NETHERSTAR_GENERATOR = REGISTRY.register("netherstar_generator", NetherstarGeneratorBlock::new);
		ENDER_LILLY = REGISTRY.register("ender_lilly", EnderLillyBlock::new);
		ENDER_LILLY_PLANT = REGISTRY.register("ender_lilly_plant", EnderLillyPlantBlock::new);
		MUFFLER = REGISTRY.register("muffler", MufflerBlock::new);
		CULINARY_GENERATOR = REGISTRY.register("culinary_generator", CulinaryGeneratorBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}