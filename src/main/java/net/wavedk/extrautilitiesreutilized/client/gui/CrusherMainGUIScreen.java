package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.CrusherMainGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.*;
import net.wavedk.extrautilitiesreutilized.network.CrusherMainGUIButtonMessage;
import net.wavedk.extrautilitiesreutilized.init.EuruModScreens;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.stream.Collectors;
import java.util.Arrays;

import com.mojang.blaze3d.systems.RenderSystem;

public class CrusherMainGUIScreen extends AbstractContainerScreen<CrusherMainGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_blank_x20;
	private ImageButton imagebutton_blankx24;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/crusher_main_gui.png");
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("euru:textures/screens/onbganim.png");
	private static final ResourceLocation SPRITE_1 = ResourceLocation.parse("euru:textures/screens/strip.png");
	private static final ResourceLocation SPRITE_2 = ResourceLocation.parse("euru:textures/screens/arrowstrip.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/onbg.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("euru:textures/screens/subackdrop.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("euru:textures/screens/symbol_cross.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("euru:textures/screens/button-gun.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("euru:textures/screens/button-always-off.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("euru:textures/screens/button-red-off.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("euru:textures/screens/button-red.png");

	public CrusherMainGUIScreen(CrusherMainGUIMenu container, Inventory inventory, Component text) {
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
		if (mouseX > leftPos + 2 && mouseX < leftPos + 26 && mouseY > topPos + 14 && mouseY < topPos + 38) {
			String hoverText = ResonatorRedstoneModeProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (SUTooltipProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 2 && mouseX < leftPos + 26 && mouseY > topPos + 38 && mouseY < topPos + 62) {
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.crusher_main_gui.tooltip_speed_upgrades"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (TooMuchGPProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 76 && mouseX < leftPos + 100 && mouseY > topPos + 31 && mouseY < topPos + 55) {
				String hoverText = ReturnGPErrorProcedure.execute(world, x, y, z);
				if (hoverText != null) {
					guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
				}
				customTooltipShown = true;
			}
		if (mouseX > leftPos + 141 && mouseX < leftPos + 163 && mouseY > topPos + 11 && mouseY < topPos + 71) {
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
		if (SGenIsOnProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(SPRITE_0, this.leftPos + 56, this.topPos + 9, 0, Mth.clamp((int) LoopSprites32Procedure.execute(world, x, y, z) * 64, 0, 1984), 64, 64, 64, 2048);
		}
		guiGraphics.blit(SPRITE_1, this.leftPos + 140, this.topPos + 7, 0, Mth.clamp((int) BatteryLevelReturnProcedure.execute(world, x, y, z) * 64, 0, 3008), 24, 64, 24, 3072);
		guiGraphics.blit(SPRITE_2, this.leftPos + 77, this.topPos + 32, 0, Mth.clamp((int) ReturnArrowStripProcedure.execute(world, x, y, z) * 22, 0, 484), 22, 22, 22, 506);
		if (BEIsNotOnProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_0, this.leftPos + 56, this.topPos + 9, 0, 0, 64, 64, 64, 64);
		}
		if (SUTooltipProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_1, this.leftPos + 6, this.topPos + 41, 0, 0, 16, 16, 16, 16);
		}
		if (TooMuchGPProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_2, this.leftPos + 80, this.topPos + 35, 0, 0, 16, 16, 16, 16);
		}
		if (AlwaysOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_3, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (AlwaysOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_4, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_5, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_6, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
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
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.crusher_main_gui.label_resonator"), 4, 4, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_blank_x20 = new ImageButton(this.leftPos + 4, this.topPos + 16, 20, 20, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blank_x20.png"), ResourceLocation.parse("euru:textures/screens/blank_whiteborder_x20.png")),
				e -> {
					int x = CrusherMainGUIScreen.this.x;
					int y = CrusherMainGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new CrusherMainGUIButtonMessage(0, x, y, z));
						CrusherMainGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blank_x20);
		imagebutton_blankx24 = new ImageButton(this.leftPos + 76, this.topPos + 31, 24, 24, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blankx24.png"), ResourceLocation.parse("euru:textures/screens/blankx24.png")), e -> {
			int x = CrusherMainGUIScreen.this.x;
			int y = CrusherMainGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new CrusherMainGUIButtonMessage(1, x, y, z));
				CrusherMainGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blankx24);
	}
}