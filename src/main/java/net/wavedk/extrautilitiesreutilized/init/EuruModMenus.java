/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.world.inventory.*;
import net.wavedk.extrautilitiesreutilized.network.MenuStateUpdateMessage;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class EuruModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, EuruMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<ResonatorGUIMenu>> RESONATOR_GUI = REGISTRY.register("resonator_gui", () -> IMenuTypeExtension.create(ResonatorGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<BagOfHoldingGUIMenu>> BAG_OF_HOLDING_GUI = REGISTRY.register("bag_of_holding_gui", () -> IMenuTypeExtension.create(BagOfHoldingGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<EnchanterGUIMenu>> ENCHANTER_GUI = REGISTRY.register("enchanter_gui", () -> IMenuTypeExtension.create(EnchanterGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<MiniChestGUIMenu>> MINI_CHEST_GUI = REGISTRY.register("mini_chest_gui", () -> IMenuTypeExtension.create(MiniChestGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SlightlyLargerChestGUIMenu>> SLIGHTLY_LARGER_CHEST_GUI = REGISTRY.register("slightly_larger_chest_gui", () -> IMenuTypeExtension.create(SlightlyLargerChestGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<CrusherMainGUIMenu>> CRUSHER_MAIN_GUI = REGISTRY.register("crusher_main_gui", () -> IMenuTypeExtension.create(CrusherMainGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<EFurnaceGUIMenu>> E_FURNACE_GUI = REGISTRY.register("e_furnace_gui", () -> IMenuTypeExtension.create(EFurnaceGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<CreativeGenGUIMenu>> CREATIVE_GEN_GUI = REGISTRY.register("creative_gen_gui", () -> IMenuTypeExtension.create(CreativeGenGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<GeneratorGUIMenu>> GENERATOR_GUI = REGISTRY.register("generator_gui", () -> IMenuTypeExtension.create(GeneratorGUIMenu::new));

	public interface MenuAccessor {
		Map<String, Object> getMenuState();

		Map<Integer, Slot> getSlots();

		default void sendMenuStateUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
			getMenuState().put(elementType + ":" + name, elementState);
			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new MenuStateUpdateMessage(elementType, name, elementState));
			} else if (player.level().isClientSide) {
				if (Minecraft.getInstance().screen instanceof EuruModScreens.ScreenAccessor accessor && needClientUpdate)
					accessor.updateMenuState(elementType, name, elementState);
				PacketDistributor.sendToServer(new MenuStateUpdateMessage(elementType, name, elementState));
			}
		}

		default <T> T getMenuState(int elementType, String name, T defaultValue) {
			try {
				return (T) getMenuState().getOrDefault(elementType + ":" + name, defaultValue);
			} catch (ClassCastException e) {
				return defaultValue;
			}
		}
	}
}