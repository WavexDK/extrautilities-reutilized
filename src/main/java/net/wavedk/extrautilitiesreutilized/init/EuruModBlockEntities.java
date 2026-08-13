/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.block.entity.*;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.core.registries.BuiltInRegistries;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EuruModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EuruMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SOLAR_PANEL = register("solar_panel", EuruModBlocks.SOLAR_PANEL, SolarPanelBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> LUNAR_PANEL = register("lunar_panel", EuruModBlocks.LUNAR_PANEL, LunarPanelBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> RESONATOR = register("resonator", EuruModBlocks.RESONATOR, ResonatorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SURVIVAL_GENERATOR = register("survival_generator", EuruModBlocks.SURVIVAL_GENERATOR, SurvivalGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> MANUAL_MILL = register("manual_mill", EuruModBlocks.MANUAL_MILL, ManualMillBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> WATER_MILL = register("water_mill", EuruModBlocks.WATER_MILL, WaterMillBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> LAVA_MILL = register("lava_mill", EuruModBlocks.LAVA_MILL, LavaMillBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> FIRE_MILL = register("fire_mill", EuruModBlocks.FIRE_MILL, FireMillBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ENCHANTER = register("enchanter", EuruModBlocks.ENCHANTER, EnchanterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> FURNACE_GENERATOR = register("furnace_generator", EuruModBlocks.FURNACE_GENERATOR, FurnaceGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> MINI_CHEST = register("mini_chest", EuruModBlocks.MINI_CHEST, MiniChestBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SLIGHTLY_LARGER_CHEST = register("slightly_larger_chest", EuruModBlocks.SLIGHTLY_LARGER_CHEST, SlightlyLargerChestBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> CREATIVE_MILL = register("creative_mill", EuruModBlocks.CREATIVE_MILL, CreativeMillBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ELECTRIC_FURNACE = register("electric_furnace", EuruModBlocks.ELECTRIC_FURNACE, ElectricFurnaceBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ENDER_GENERATOR = register("ender_generator", EuruModBlocks.ENDER_GENERATOR, EnderGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> CREATIVE_ENERGY_SOURCE = register("creative_energy_source", EuruModBlocks.CREATIVE_ENERGY_SOURCE, CreativeEnergySourceBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> OVERCLOCKED_GENERATOR = register("overclocked_generator", EuruModBlocks.OVERCLOCKED_GENERATOR, OverclockedGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> CRUSHER = register("crusher", EuruModBlocks.CRUSHER, CrusherBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> CHUNK_LOADING_WARD = register("chunk_loading_ward", EuruModBlocks.CHUNK_LOADING_WARD, ChunkLoadingWardTileEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> NETHERSTAR_GENERATOR = register("netherstar_generator", EuruModBlocks.NETHERSTAR_GENERATOR, NetherstarGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ENDER_LILLY = register("ender_lilly", EuruModBlocks.ENDER_LILLY, EnderLillyBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ENDER_LILLY_PLANT = register("ender_lilly_plant", EuruModBlocks.ENDER_LILLY_PLANT, EnderLillyPlantBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> CULINARY_GENERATOR = register("culinary_generator", EuruModBlocks.CULINARY_GENERATOR, CulinaryGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> DISENCHANTMENT_GENERATOR = register("disenchantment_generator", EuruModBlocks.DISENCHANTMENT_GENERATOR, DisenchantmentGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> EXPLOSIVE_GENERATOR = register("explosive_generator", EuruModBlocks.EXPLOSIVE_GENERATOR, ExplosiveGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> WOODEN_SPIKE = register("wooden_spike", EuruModBlocks.WOODEN_SPIKE, WoodenSpikeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> STONE_SPIKE = register("stone_spike", EuruModBlocks.STONE_SPIKE, StoneSpikeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> IRON_SPIKE = register("iron_spike", EuruModBlocks.IRON_SPIKE, IronSpikeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> GOLD_SPIKE = register("gold_spike", EuruModBlocks.GOLD_SPIKE, GoldSpikeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> DIAMOND_SPIKE = register("diamond_spike", EuruModBlocks.DIAMOND_SPIKE, DiamondSpikeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> NETHERITE_SPIKE = register("netherite_spike", EuruModBlocks.NETHERITE_SPIKE, NetheriteSpikeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> TRANSFER_CABLE = register("transfer_cable", EuruModBlocks.TRANSFER_CABLE, TransferCableBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SOLAR_PANEL.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, LUNAR_PANEL.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RESONATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SURVIVAL_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SURVIVAL_GENERATOR.get(), (blockEntity, side) -> ((SurvivalGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MANUAL_MILL.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, WATER_MILL.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, LAVA_MILL.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FIRE_MILL.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ENCHANTER.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ENCHANTER.get(), (blockEntity, side) -> ((EnchanterBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FURNACE_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, FURNACE_GENERATOR.get(), (blockEntity, side) -> ((FurnaceGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MINI_CHEST.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SLIGHTLY_LARGER_CHEST.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CREATIVE_MILL.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ELECTRIC_FURNACE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ELECTRIC_FURNACE.get(), (blockEntity, side) -> ((ElectricFurnaceBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ENDER_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ENDER_GENERATOR.get(), (blockEntity, side) -> ((EnderGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CREATIVE_ENERGY_SOURCE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CREATIVE_ENERGY_SOURCE.get(), (blockEntity, side) -> ((CreativeEnergySourceBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, OVERCLOCKED_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, OVERCLOCKED_GENERATOR.get(), (blockEntity, side) -> ((OverclockedGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CRUSHER.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CRUSHER.get(), (blockEntity, side) -> ((CrusherBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, NETHERSTAR_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, NETHERSTAR_GENERATOR.get(), (blockEntity, side) -> ((NetherstarGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ENDER_LILLY.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CULINARY_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CULINARY_GENERATOR.get(), (blockEntity, side) -> ((CulinaryGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, DISENCHANTMENT_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, DISENCHANTMENT_GENERATOR.get(), (blockEntity, side) -> ((DisenchantmentGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EXPLOSIVE_GENERATOR.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, EXPLOSIVE_GENERATOR.get(), (blockEntity, side) -> ((ExplosiveGeneratorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, WOODEN_SPIKE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, STONE_SPIKE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRON_SPIKE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GOLD_SPIKE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, DIAMOND_SPIKE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, NETHERITE_SPIKE.get(), (blockEntity, side) -> new SidedInvWrapper((WorldlyContainer) blockEntity, side));
	}
}