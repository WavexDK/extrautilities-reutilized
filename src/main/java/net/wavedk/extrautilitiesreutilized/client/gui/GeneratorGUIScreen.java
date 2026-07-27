package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.GeneratorGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.*;
import net.wavedk.extrautilitiesreutilized.network.GeneratorGUIButtonMessage;
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

public class GeneratorGUIScreen extends AbstractContainerScreen<GeneratorGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_empty1;
	private Button button_empty;
	private ImageButton imagebutton_blank_x20;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/generator_gui.png");
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("euru:textures/screens/strip.png");
	private static final ResourceLocation SPRITE_1 = ResourceLocation.parse("euru:textures/screens/sgen_strip.png");
	private static final ResourceLocation SPRITE_2 = ResourceLocation.parse("euru:textures/screens/arrowstrip.png");
	private static final ResourceLocation SPRITE_3 = ResourceLocation.parse("euru:textures/screens/fgen_strip.png");
	private static final ResourceLocation SPRITE_4 = ResourceLocation.parse("euru:textures/screens/nsgen_strip.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/subackdrop.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("euru:textures/screens/button-gun.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("euru:textures/screens/button-always-off.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("euru:textures/screens/button-red-off.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("euru:textures/screens/button-red.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("euru:textures/screens/symbol_cross.png");

	public GeneratorGUIScreen(GeneratorGUIMenu container, Inventory inventory, Component text) {
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
		if (mouseX > leftPos + 89 && mouseX < leftPos + 113 && mouseY > topPos + 31 && mouseY < topPos + 55) {
			String hoverText = NEGPAProcedureProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (SUTooltipGenProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 2 && mouseX < leftPos + 26 && mouseY > topPos + 38 && mouseY < topPos + 62) {
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.generator_gui.tooltip_speed_upgrade"), mouseX, mouseY);
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
		guiGraphics.blit(SPRITE_0, this.leftPos + 140, this.topPos + 7, 0, Mth.clamp((int) BatteryLevelReturnProcedure.execute(world, x, y, z) * 64, 0, 3008), 24, 64, 24, 3072);
		guiGraphics.blit(SPRITE_1, this.leftPos + 56, this.topPos + 9, 0, Mth.clamp((int) SGenBackdropProcedure.execute(world, x, y, z) * 64, 0, 128), 64, 64, 64, 192);
		guiGraphics.blit(SPRITE_2, this.leftPos + 90, this.topPos + 32, 0, Mth.clamp((int) ReturnArrowStripProcedure.execute(world, x, y, z) * 22, 0, 484), 22, 22, 22, 506);
		guiGraphics.blit(SPRITE_3, this.leftPos + 56, this.topPos + 9, 0, Mth.clamp((int) FGenBackdropProcedure.execute(world, x, y, z) * 64, 0, 128), 64, 64, 64, 192);
		guiGraphics.blit(SPRITE_4, this.leftPos + 56, this.topPos + 9, 0, Mth.clamp((int) NSGenBackdropProcedure.execute(world, x, y, z) * 64, 0, 128), 64, 64, 64, 192);
		if (SUTooltipGenProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_0, this.leftPos + 6, this.topPos + 41, 0, 0, 16, 16, 16, 16);
		}
		if (AlwaysOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_1, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (AlwaysOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_2, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_3, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_4, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (TooMuchGPProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_5, this.leftPos + 93, this.topPos + 35, 0, 0, 16, 16, 16, 16);
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
		guiGraphics.drawString(this.font, ReturnNameOfMachineProcedure.execute(world, x, y, z), 4, 4, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		button_empty1 = new PlainTextButton(this.leftPos + 89, this.topPos + 35, 24, 20, Component.translatable("gui.euru.generator_gui.button_empty1"), e -> {
			int x = GeneratorGUIScreen.this.x;
			int y = GeneratorGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new GeneratorGUIButtonMessage(0, x, y, z));
				GeneratorGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}, this.font);
		this.addRenderableWidget(button_empty1);
		button_empty = new PlainTextButton(this.leftPos + 89, this.topPos + 31, 24, 20, Component.translatable("gui.euru.generator_gui.button_empty"), e -> {
			int x = GeneratorGUIScreen.this.x;
			int y = GeneratorGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new GeneratorGUIButtonMessage(1, x, y, z));
				GeneratorGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}, this.font);
		this.addRenderableWidget(button_empty);
		imagebutton_blank_x20 = new ImageButton(this.leftPos + 4, this.topPos + 16, 20, 20, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blank_x20.png"), ResourceLocation.parse("euru:textures/screens/blank_whiteborder_x20.png")),
				e -> {
					int x = GeneratorGUIScreen.this.x;
					int y = GeneratorGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new GeneratorGUIButtonMessage(2, x, y, z));
						GeneratorGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
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