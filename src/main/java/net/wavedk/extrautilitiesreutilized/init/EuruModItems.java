/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.procedures.GoldenLassoPropertyValueProviderProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.EnderShardPropertyValueProviderProcedure;
import net.wavedk.extrautilitiesreutilized.item.inventory.BagOfHoldingInventoryCapability;
import net.wavedk.extrautilitiesreutilized.item.*;
import net.wavedk.extrautilitiesreutilized.block.display.ChunkLoadingWardDisplayItem;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EuruModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(EuruMod.MODID);
	public static final DeferredItem<Item> SOLAR_PANEL = block(EuruModBlocks.SOLAR_PANEL);
	public static final DeferredItem<Item> GP_SCANNER = REGISTRY.register("gp_scanner", GPScannerItem::new);
	public static final DeferredItem<Item> LUNAR_PANEL = block(EuruModBlocks.LUNAR_PANEL);
	public static final DeferredItem<Item> RESONATOR = block(EuruModBlocks.RESONATOR);
	public static final DeferredItem<Item> UPGRADE_BASE = REGISTRY.register("upgrade_base", UpgradeBaseItem::new);
	public static final DeferredItem<Item> SPEED_UPGRADE = REGISTRY.register("speed_upgrade", SpeedUpgradeItem::new);
	public static final DeferredItem<Item> RESONATING_REDSTONE_CRYSTAL = REGISTRY.register("resonating_redstone_crystal", ResonatingRedstoneCrystalItem::new);
	public static final DeferredItem<Item> ENDER_SHARD = REGISTRY.register("ender_shard", EnderShardItem::new);
	public static final DeferredItem<Item> GLASS_CUTTER = REGISTRY.register("glass_cutter", GlassCutterItem::new);
	public static final DeferredItem<Item> MAGICAL_SPEED_UPGRADE = REGISTRY.register("magical_speed_upgrade", MagicalSpeedUpgradeItem::new);
	public static final DeferredItem<Item> MACHINE_BLOCK = block(EuruModBlocks.MACHINE_BLOCK);
	public static final DeferredItem<Item> LUNAR_REACTIVE_DUST = REGISTRY.register("lunar_reactive_dust", LunarReactiveDustItem::new);
	public static final DeferredItem<Item> SURVIVAL_GENERATOR = block(EuruModBlocks.SURVIVAL_GENERATOR);
	public static final DeferredItem<Item> CHUNK_LOADER_TESTER = block(EuruModBlocks.CHUNK_LOADER_TESTER);
	public static final DeferredItem<Item> GILDED_OBSIDIAN = block(EuruModBlocks.GILDED_OBSIDIAN);
	public static final DeferredItem<Item> NUGGETO_EXPERIENCE = REGISTRY.register("nuggeto_experience", NuggetoExperienceItem::new);
	public static final DeferredItem<Item> ANGEL_BLOCK = block(EuruModBlocks.ANGEL_BLOCK);
	public static final DeferredItem<Item> COMPRESSED_COBBLESTONE = block(EuruModBlocks.COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> DOUBLE_COMPRESSED_COBBLESTONE = block(EuruModBlocks.DOUBLE_COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> TRIPLE_COMPRESSED_COBBLESTONE = block(EuruModBlocks.TRIPLE_COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> QUADRUPLE_COMPRESSED_COBBLESTONE = block(EuruModBlocks.QUADRUPLE_COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> QUINTUPLE_COMPRESSED_COBBLESTONE = block(EuruModBlocks.QUINTUPLE_COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> SEXTUPLE_COMPRESSED_COBBLESTONE = block(EuruModBlocks.SEXTUPLE_COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> SEPTUPLE_COMPRESSED_COBBLESTONE = block(EuruModBlocks.SEPTUPLE_COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> OCTUPLE_COMPRESSED_COBBLESTONE = block(EuruModBlocks.OCTUPLE_COMPRESSED_COBBLESTONE);
	public static final DeferredItem<Item> MANUAL_MILL = block(EuruModBlocks.MANUAL_MILL);
	public static final DeferredItem<Item> WATER_MILL = block(EuruModBlocks.WATER_MILL);
	public static final DeferredItem<Item> REDSTONE_GEAR = REGISTRY.register("redstone_gear", RedstoneGearItem::new);
	public static final DeferredItem<Item> GOLDEN_LASSO = REGISTRY.register("golden_lasso", GoldenLassoItem::new);
	public static final DeferredItem<Item> LAVA_MILL = block(EuruModBlocks.LAVA_MILL);
	public static final DeferredItem<Item> STONE_BURNT = block(EuruModBlocks.STONE_BURNT);
	public static final DeferredItem<Item> CURSED_LASSO = REGISTRY.register("cursed_lasso", CursedLassoItem::new);
	public static final DeferredItem<Item> DROP_OF_EVIL = REGISTRY.register("drop_of_evil", DropOfEvilItem::new);
	public static final DeferredItem<Item> FIRE_MILL = block(EuruModBlocks.FIRE_MILL);
	public static final DeferredItem<Item> BAG_OF_HOLDING = REGISTRY.register("bag_of_holding", BagOfHoldingItem::new);
	public static final DeferredItem<Item> ENCHANTER = block(EuruModBlocks.ENCHANTER);
	public static final DeferredItem<Item> MAGICAL_NUGGET = REGISTRY.register("magical_nugget", MagicalNuggetItem::new);
	public static final DeferredItem<Item> ENCHANTED_INGOT = REGISTRY.register("enchanted_ingot", EnchantedIngotItem::new);
	public static final DeferredItem<Item> ENCHANTED_APPLE = REGISTRY.register("enchanted_apple", EnchantedAppleItem::new);
	public static final DeferredItem<Item> FURNACE_GENERATOR = block(EuruModBlocks.FURNACE_GENERATOR);
	public static final DeferredItem<Item> CHICKEN_RING = REGISTRY.register("chicken_ring", ChickenRingItem::new);
	public static final DeferredItem<Item> SQUID_RING = REGISTRY.register("squid_ring", SquidRingItem::new);
	public static final DeferredItem<Item> ANGEL_RING = REGISTRY.register("angel_ring", AngelRingItem::new);
	public static final DeferredItem<Item> GOLDEN_LASSO_CW = REGISTRY.register("golden_lasso_cw", GoldenLassoCWItem::new);
	public static final DeferredItem<Item> GOLDEN_LASSO_SW = REGISTRY.register("golden_lasso_sw", GoldenLassoSWItem::new);
	public static final DeferredItem<Item> GOLDEN_LASSO_AR = REGISTRY.register("golden_lasso_ar", GoldenLassoARItem::new);
	public static final DeferredItem<Item> CURSED_LASSO_AR = REGISTRY.register("cursed_lasso_ar", CursedLassoARItem::new);
	public static final DeferredItem<Item> EVIL_INFUSED_INGOT = REGISTRY.register("evil_infused_ingot", EvilInfusedIngotItem::new);
	public static final DeferredItem<Item> BLOCK_OF_EVIL_INFUSED_INGOT = block(EuruModBlocks.BLOCK_OF_EVIL_INFUSED_INGOT);
	public static final DeferredItem<Item> MINI_CHEST = block(EuruModBlocks.MINI_CHEST);
	public static final DeferredItem<Item> SLIGHTLY_LARGER_CHEST = block(EuruModBlocks.SLIGHTLY_LARGER_CHEST);
	public static final DeferredItem<Item> CREATIVE_MILL = block(EuruModBlocks.CREATIVE_MILL);
	public static final DeferredItem<Item> ULTIMATE_SPEED_UPGRADE = REGISTRY.register("ultimate_speed_upgrade", UltimateSpeedUpgradeItem::new);
	public static final DeferredItem<Item> ELECTRIC_FURNACE = block(EuruModBlocks.ELECTRIC_FURNACE);
	public static final DeferredItem<Item> ENDER_GENERATOR = block(EuruModBlocks.ENDER_GENERATOR);
	public static final DeferredItem<Item> CREATIVE_ENERGY_SOURCE = block(EuruModBlocks.CREATIVE_ENERGY_SOURCE);
	public static final DeferredItem<Item> OVERCLOCKED_GENERATOR = block(EuruModBlocks.OVERCLOCKED_GENERATOR);
	public static final DeferredItem<Item> CRUSHER = block(EuruModBlocks.CRUSHER);
	public static final DeferredItem<Item> CHUNK_LOADING_WARD = REGISTRY.register(EuruModBlocks.CHUNK_LOADING_WARD.getId().getPath(), () -> new ChunkLoadingWardDisplayItem(EuruModBlocks.CHUNK_LOADING_WARD.get(), new Item.Properties()));
	public static final DeferredItem<Item> NETHERSTAR_GENERATOR = block(EuruModBlocks.NETHERSTAR_GENERATOR);
	public static final DeferredItem<Item> ENDER_LILLY = block(EuruModBlocks.ENDER_LILLY);
	public static final DeferredItem<Item> DIAMOND_SICKLE = REGISTRY.register("diamond_sickle", DiamondSickleItem::new);
	public static final DeferredItem<Item> NETHERITE_SICKLE = REGISTRY.register("netherite_sickle", NetheriteSickleItem::new);
	public static final DeferredItem<Item> IRON_SICKLE = REGISTRY.register("iron_sickle", IronSickleItem::new);
	public static final DeferredItem<Item> STONE_SICKLE = REGISTRY.register("stone_sickle", StoneSickleItem::new);
	public static final DeferredItem<Item> WOODEN_SICKLE = REGISTRY.register("wooden_sickle", WoodenSickleItem::new);
	public static final DeferredItem<Item> GOLDEN_SICKLE = REGISTRY.register("golden_sickle", GoldenSickleItem::new);
	public static final DeferredItem<Item> CULINARY_GENERATOR = block(EuruModBlocks.CULINARY_GENERATOR);
	public static final DeferredItem<Item> DISENCHANTMENT_GENERATOR = block(EuruModBlocks.DISENCHANTMENT_GENERATOR);
	public static final DeferredItem<Item> WATERING_CAN = REGISTRY.register("watering_can", WateringCanItem::new);
	public static final DeferredItem<Item> EXPLOSIVE_GENERATOR = block(EuruModBlocks.EXPLOSIVE_GENERATOR);
	public static final DeferredItem<Item> GOLDEN_LASSO_CLW = REGISTRY.register("golden_lasso_clw", GoldenLassoCLWItem::new);
	public static final DeferredItem<Item> EYE_OF_REDSTONE = REGISTRY.register("eye_of_redstone", EyeOfRedstoneItem::new);
	public static final DeferredItem<Item> EVIL_INFUSED_NUGGET = REGISTRY.register("evil_infused_nugget", EvilInfusedNuggetItem::new);
	public static final DeferredItem<Item> WOODEN_SPIKE = block(EuruModBlocks.WOODEN_SPIKE);
	public static final DeferredItem<Item> STONE_SPIKE = block(EuruModBlocks.STONE_SPIKE);
	public static final DeferredItem<Item> IRON_SPIKE = block(EuruModBlocks.IRON_SPIKE);
	public static final DeferredItem<Item> GOLD_SPIKE = block(EuruModBlocks.GOLD_SPIKE);
	public static final DeferredItem<Item> DIAMOND_SPIKE = block(EuruModBlocks.DIAMOND_SPIKE);
	public static final DeferredItem<Item> NETHERITE_SPIKE = block(EuruModBlocks.NETHERITE_SPIKE);

	// Start of user code block custom items
	// End of user code block custom items
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> new BagOfHoldingInventoryCapability(stack), BAG_OF_HOLDING.get());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}

	@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ItemsClientSideHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void clientLoad(FMLClientSetupEvent event) {
			event.enqueueWork(() -> {
				ItemProperties.register(ENDER_SHARD.get(), ResourceLocation.parse("euru:ender_shard_count"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) EnderShardPropertyValueProviderProcedure.execute(itemStackToRender));
				ItemProperties.register(GOLDEN_LASSO.get(), ResourceLocation.parse("euru:golden_lasso_full"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) GoldenLassoPropertyValueProviderProcedure.execute(itemStackToRender));
				ItemProperties.register(CURSED_LASSO.get(), ResourceLocation.parse("euru:cursed_lasso_full"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) GoldenLassoPropertyValueProviderProcedure.execute(itemStackToRender));
				ItemProperties.register(CURSED_LASSO_AR.get(), ResourceLocation.parse("euru:cursed_lasso_ar_full"),
						(itemStackToRender, clientWorld, entity, itemEntityId) -> (float) GoldenLassoPropertyValueProviderProcedure.execute(itemStackToRender));
			});
		}
	}
}