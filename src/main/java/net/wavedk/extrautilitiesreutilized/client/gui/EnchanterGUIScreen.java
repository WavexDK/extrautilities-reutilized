package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.EnchanterGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.*;
import net.wavedk.extrautilitiesreutilized.network.EnchanterGUIButtonMessage;
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

public class EnchanterGUIScreen extends AbstractContainerScreen<EnchanterGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_blank_x20;
	private ImageButton imagebutton_blank_22x22;
	private ImageButton imagebutton_blankx24;
	private ImageButton imagebutton_blankx241;
	private ImageButton imagebutton_blankx242;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/enchanter_gui.png");
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("euru:textures/screens/strip.png");
	private static final ResourceLocation SPRITE_1 = ResourceLocation.parse("euru:textures/screens/enchanter_strip.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/book5.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("euru:textures/screens/crsvwv.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("euru:textures/screens/button-gun.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("euru:textures/screens/button-red.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("euru:textures/screens/button-red-off.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("euru:textures/screens/button-always-off.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("euru:textures/screens/subackdrop.png");

	public EnchanterGUIScreen(EnchanterGUIMenu container, Inventory inventory, Component text) {
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
		if (mouseX > leftPos + 96 && mouseX < leftPos + 116 && mouseY > topPos + 35 && mouseY < topPos + 55) {
			String hoverText = ResonatorRedstoneModeProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (SUTooltipSlotThreeProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 97 && mouseX < leftPos + 115 && mouseY > topPos + 7 && mouseY < topPos + 25) {
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.enchanter_gui.tooltip_speed_upgrade"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (mouseX > leftPos + 72 && mouseX < leftPos + 96 && mouseY > topPos + 34 && mouseY < topPos + 80) {
			String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 116 && mouseX < leftPos + 140 && mouseY > topPos + 34 && mouseY < topPos + 80) {
			String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 143 && mouseX < leftPos + 165 && mouseY > topPos + 15 && mouseY < topPos + 75) {
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
		guiGraphics.blit(SPRITE_0, this.leftPos + 142, this.topPos + 11, 0, Mth.clamp((int) BatteryLevelReturnProcedure.execute(world, x, y, z) * 64, 0, 3008), 24, 64, 24, 3072);
		guiGraphics.blit(SPRITE_1, this.leftPos + 18, this.topPos + 4, 0, Mth.clamp((int) ReturnArrowStripProcedure.execute(world, x, y, z) * 83, 0, 1660), 176, 83, 176, 1743);
		guiGraphics.blit(IMAGE_0, this.leftPos + 2, this.topPos + 21, 0, 0, 70, 60, 70, 60);
		if (ErrorShowProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_1, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (AlwaysOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_2, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_3, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_4, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (AlwaysOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_5, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (SUTooltipSlotThreeProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_6, this.leftPos + 98, this.topPos + 8, 0, 0, 16, 16, 16, 16);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.enchanter_gui.label_enchanter"), 5, 5, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_blank_x20 = new ImageButton(this.leftPos + 96, this.topPos + 35, 20, 20, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blank_x20.png"), ResourceLocation.parse("euru:textures/screens/blank_whiteborder_x20.png")),
				e -> {
					int x = EnchanterGUIScreen.this.x;
					int y = EnchanterGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new EnchanterGUIButtonMessage(0, x, y, z));
						EnchanterGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blank_x20);
		imagebutton_blank_22x22 = new ImageButton(this.leftPos + 69, this.topPos + 34, 24, 24, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blankx24.png"), ResourceLocation.parse("euru:textures/screens/blankx24.png")), e -> {
			int x = EnchanterGUIScreen.this.x;
			int y = EnchanterGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new EnchanterGUIButtonMessage(1, x, y, z));
				EnchanterGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blank_22x22);
		imagebutton_blankx24 = new ImageButton(this.leftPos + 73, this.topPos + 58, 24, 24, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blankx24.png"), ResourceLocation.parse("euru:textures/screens/blankx24.png")), e -> {
			int x = EnchanterGUIScreen.this.x;
			int y = EnchanterGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new EnchanterGUIButtonMessage(2, x, y, z));
				EnchanterGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blankx24);
		imagebutton_blankx241 = new ImageButton(this.leftPos + 115, this.topPos + 58, 24, 24, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blankx24.png"), ResourceLocation.parse("euru:textures/screens/blankx24.png")), e -> {
			int x = EnchanterGUIScreen.this.x;
			int y = EnchanterGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new EnchanterGUIButtonMessage(3, x, y, z));
				EnchanterGUIButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blankx241);
		imagebutton_blankx242 = new ImageButton(this.leftPos + 119, this.topPos + 34, 24, 24, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blankx24.png"), ResourceLocation.parse("euru:textures/screens/blankx24.png")), e -> {
			int x = EnchanterGUIScreen.this.x;
			int y = EnchanterGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new EnchanterGUIButtonMessage(4, x, y, z));
				EnchanterGUIButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_blankx242);
	}
}