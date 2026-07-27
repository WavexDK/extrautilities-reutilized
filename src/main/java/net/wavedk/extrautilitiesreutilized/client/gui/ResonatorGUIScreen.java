package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.ResonatorGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.*;
import net.wavedk.extrautilitiesreutilized.network.ResonatorGUIButtonMessage;
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
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import java.util.stream.Collectors;
import java.util.Arrays;

import com.mojang.blaze3d.systems.RenderSystem;

public class ResonatorGUIScreen extends AbstractContainerScreen<ResonatorGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_empty;
	private Button button_empty1;
	private ImageButton imagebutton_blank_x20;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/resonator_gui.png");
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("euru:textures/screens/arrowstrip.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/resbackdrop.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("euru:textures/screens/subackdrop.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("euru:textures/screens/symbol_cross.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("euru:textures/screens/button-gun.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("euru:textures/screens/button-always-off.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("euru:textures/screens/button-red-off.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("euru:textures/screens/button-red.png");

	public ResonatorGUIScreen(ResonatorGUIMenu container, Inventory inventory, Component text) {
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
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.resonator_gui.tooltip_speed_upgrades"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (mouseX > leftPos + 76 && mouseX < leftPos + 100 && mouseY > topPos + 31 && mouseY < topPos + 55) {
			String hoverText = ReturnGPErrorProcedure.execute(world, x, y, z);
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
		guiGraphics.blit(SPRITE_0, this.leftPos + 77, this.topPos + 32, 0, Mth.clamp((int) ReturnArrowStripProcedure.execute(world, x, y, z) * 22, 0, 484), 22, 22, 22, 506);
		guiGraphics.blit(IMAGE_0, this.leftPos + 56, this.topPos + 9, 0, 0, 64, 64, 64, 64);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.resonator_gui.label_resonator"), 4, 4, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		button_empty = new PlainTextButton(this.leftPos + 76, this.topPos + 31, 24, 20, Component.translatable("gui.euru.resonator_gui.button_empty"), e -> {
			int x = ResonatorGUIScreen.this.x;
			int y = ResonatorGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new ResonatorGUIButtonMessage(0, x, y, z));
				ResonatorGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}, this.font);
		this.addRenderableWidget(button_empty);
		button_empty1 = new PlainTextButton(this.leftPos + 76, this.topPos + 35, 24, 20, Component.translatable("gui.euru.resonator_gui.button_empty1"), e -> {
			int x = ResonatorGUIScreen.this.x;
			int y = ResonatorGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new ResonatorGUIButtonMessage(1, x, y, z));
				ResonatorGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}, this.font);
		this.addRenderableWidget(button_empty1);
		imagebutton_blank_x20 = new ImageButton(this.leftPos + 4, this.topPos + 16, 20, 20, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blank_x20.png"), ResourceLocation.parse("euru:textures/screens/blank_whiteborder_x20.png")),
				e -> {
					int x = ResonatorGUIScreen.this.x;
					int y = ResonatorGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new ResonatorGUIButtonMessage(2, x, y, z));
						ResonatorGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blank_x20);
	}
}