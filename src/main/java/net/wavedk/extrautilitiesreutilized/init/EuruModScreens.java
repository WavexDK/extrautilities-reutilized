/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.client.gui.*;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(Dist.CLIENT)
public class EuruModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(EuruModMenus.RESONATOR_GUI.get(), ResonatorGUIScreen::new);
		event.register(EuruModMenus.S_GEN_GUI.get(), SGenGUIScreen::new);
		event.register(EuruModMenus.BAG_OF_HOLDING_GUI.get(), BagOfHoldingGUIScreen::new);
		event.register(EuruModMenus.ENCHANTER_GUI.get(), EnchanterGUIScreen::new);
		event.register(EuruModMenus.F_GEN_GUI.get(), FGenGUIScreen::new);
		event.register(EuruModMenus.MINI_CHEST_GUI.get(), MiniChestGUIScreen::new);
		event.register(EuruModMenus.SLIGHTLY_LARGER_CHEST_GUI.get(), SlightlyLargerChestGUIScreen::new);
		event.register(EuruModMenus.ELECTRIC_FURNACE_GUI.get(), ElectricFurnaceGUIScreen::new);
		event.register(EuruModMenus.E_GEN_GUI.get(), EGenGUIScreen::new);
		event.register(EuruModMenus.CRUSHER_MAIN_GUI.get(), CrusherMainGUIScreen::new);
		event.register(EuruModMenus.E_FURNACE_GUI.get(), EFurnaceGUIScreen::new);
		event.register(EuruModMenus.CREATIVE_GEN_GUI.get(), CreativeGenGUIScreen::new);
		event.register(EuruModMenus.O_GEN_GUI.get(), OGenGUIScreen::new);
		event.register(EuruModMenus.NS_GEN_GUI.get(), NSGenGUIScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}