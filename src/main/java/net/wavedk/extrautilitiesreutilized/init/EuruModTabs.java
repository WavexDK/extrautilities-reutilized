/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class EuruModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EuruMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EURU = REGISTRY.register("euru",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.euru.euru")).icon(() -> new ItemStack(EuruModBlocks.ANGEL_BLOCK.get())).displayItems((parameters, tabData) -> {
				tabData.accept(EuruModBlocks.CREATIVE_ENERGY_SOURCE.get().asItem());
				tabData.accept(EuruModBlocks.NETHERSTAR_GENERATOR.get().asItem());
				tabData.accept(EuruModBlocks.ENDER_GENERATOR.get().asItem());
				tabData.accept(EuruModBlocks.OVERCLOCKED_GENERATOR.get().asItem());
				tabData.accept(EuruModBlocks.FURNACE_GENERATOR.get().asItem());
				tabData.accept(EuruModBlocks.SURVIVAL_GENERATOR.get().asItem());
				tabData.accept(EuruModBlocks.RESONATOR.get().asItem());
				tabData.accept(EuruModBlocks.ELECTRIC_FURNACE.get().asItem());
				tabData.accept(EuruModBlocks.CRUSHER.get().asItem());
				tabData.accept(EuruModBlocks.CHUNK_LOADER_TESTER.get().asItem());
				tabData.accept(EuruModBlocks.MACHINE_BLOCK.get().asItem());
				tabData.accept(EuruModBlocks.ANGEL_BLOCK.get().asItem());
				tabData.accept(EuruModBlocks.GILDED_OBSIDIAN.get().asItem());
				tabData.accept(EuruModBlocks.STONE_BURNT.get().asItem());
				tabData.accept(EuruModBlocks.CREATIVE_MILL.get().asItem());
				tabData.accept(EuruModBlocks.WATER_MILL.get().asItem());
				tabData.accept(EuruModBlocks.LAVA_MILL.get().asItem());
				tabData.accept(EuruModBlocks.MANUAL_MILL.get().asItem());
				tabData.accept(EuruModBlocks.FIRE_MILL.get().asItem());
				tabData.accept(EuruModBlocks.ENCHANTER.get().asItem());
				tabData.accept(EuruModBlocks.BLOCK_OF_EVIL_INFUSED_INGOT.get().asItem());
				tabData.accept(EuruModBlocks.SLIGHTLY_LARGER_CHEST.get().asItem());
				tabData.accept(EuruModBlocks.CHUNK_LOADING_WARD.get().asItem());
				tabData.accept(EuruModBlocks.SOLAR_PANEL.get().asItem());
				tabData.accept(EuruModBlocks.LUNAR_PANEL.get().asItem());
				tabData.accept(EuruModBlocks.MINI_CHEST.get().asItem());
				tabData.accept(EuruModItems.ULTIMATE_SPEED_UPGRADE.get());
				tabData.accept(EuruModItems.MAGICAL_SPEED_UPGRADE.get());
				tabData.accept(EuruModItems.SPEED_UPGRADE.get());
				tabData.accept(EuruModItems.UPGRADE_BASE.get());
				tabData.accept(EuruModItems.GP_SCANNER.get());
				tabData.accept(EuruModItems.BAG_OF_HOLDING.get());
				tabData.accept(EuruModItems.LUNAR_REACTIVE_DUST.get());
				tabData.accept(EuruModItems.REDSTONE_GEAR.get());
				tabData.accept(EuruModItems.EVIL_INFUSED_INGOT.get());
				tabData.accept(EuruModItems.ENCHANTED_APPLE.get());
				tabData.accept(EuruModItems.ENCHANTED_INGOT.get());
				tabData.accept(EuruModItems.MAGICAL_NUGGET.get());
				tabData.accept(EuruModItems.GOLDEN_LASSO.get());
				tabData.accept(EuruModItems.CURSED_LASSO.get());
				tabData.accept(EuruModItems.ANGEL_RING.get());
				tabData.accept(EuruModItems.SQUID_RING.get());
				tabData.accept(EuruModItems.CHICKEN_RING.get());
				tabData.accept(EuruModBlocks.ENDER_LILLY.get().asItem());
				tabData.accept(EuruModItems.DROP_OF_EVIL.get());
				tabData.accept(EuruModItems.NUGGETO_EXPERIENCE.get());
				tabData.accept(EuruModItems.RESONATING_REDSTONE_CRYSTAL.get());
				tabData.accept(EuruModItems.GLASS_CUTTER.get());
				tabData.accept(EuruModItems.ENDER_SHARD.get());
				tabData.accept(EuruModItems.IRON_SICKLE.get());
				tabData.accept(EuruModItems.NETHERITE_SICKLE.get());
				tabData.accept(EuruModItems.DIAMOND_SICKLE.get());
				tabData.accept(EuruModItems.STONE_SICKLE.get());
				tabData.accept(EuruModItems.WOODEN_SICKLE.get());
				tabData.accept(EuruModItems.GOLDEN_SICKLE.get());
				tabData.accept(EuruModBlocks.CULINARY_GENERATOR.get().asItem());
			}).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EURU_COMPRESSED = REGISTRY.register("euru_compressed",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.euru.euru_compressed")).icon(() -> new ItemStack(EuruModBlocks.DOUBLE_COMPRESSED_COBBLESTONE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(EuruModBlocks.COMPRESSED_COBBLESTONE.get().asItem());
				tabData.accept(EuruModBlocks.DOUBLE_COMPRESSED_COBBLESTONE.get().asItem());
				tabData.accept(EuruModBlocks.TRIPLE_COMPRESSED_COBBLESTONE.get().asItem());
				tabData.accept(EuruModBlocks.QUADRUPLE_COMPRESSED_COBBLESTONE.get().asItem());
				tabData.accept(EuruModBlocks.QUINTUPLE_COMPRESSED_COBBLESTONE.get().asItem());
				tabData.accept(EuruModBlocks.SEXTUPLE_COMPRESSED_COBBLESTONE.get().asItem());
				tabData.accept(EuruModBlocks.SEPTUPLE_COMPRESSED_COBBLESTONE.get().asItem());
				tabData.accept(EuruModBlocks.OCTUPLE_COMPRESSED_COBBLESTONE.get().asItem());
			}).withTabsBefore(EURU.getId()).build());
}