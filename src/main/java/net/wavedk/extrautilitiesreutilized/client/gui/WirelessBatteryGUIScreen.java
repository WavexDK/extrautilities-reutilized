package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.WirelessBatteryGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.ReturnFEStorageProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.BatteryLevelReturnProcedure;
import net.wavedk.extrautilitiesreutilized.init.EuruModScreens;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.stream.Collectors;
import java.util.Arrays;

import com.mojang.blaze3d.systems.RenderSystem;

public class WirelessBatteryGUIScreen extends AbstractContainerScreen<WirelessBatteryGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/wireless_battery_gui.png");
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("euru:textures/screens/strip.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/arrow-empty.png");

	public WirelessBatteryGUIScreen(WirelessBatteryGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		boolean customTooltipShown = false;
		if (mouseX > leftPos + 143 && mouseX < leftPos + 165 && mouseY > topPos + 14 && mouseY < topPos + 74) {
			String hoverText = ReturnFEStorageProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (!customTooltipShown)
			this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(SPRITE_0, this.leftPos + 142, this.topPos + 10, 0, Mth.clamp((int) BatteryLevelReturnProcedure.execute(world, x, y, z) * 64, 0, 3008), 24, 64, 24, 3072);
		guiGraphics.blit(IMAGE_0, this.leftPos + 76, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.wireless_battery_gui.label_wireless_battery"), 7, 5, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.wireless_battery_gui.label_input_wireless_fe_heating_coil_t"), 7, 60, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
	}
}