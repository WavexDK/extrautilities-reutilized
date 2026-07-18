package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.EnchanterGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.*;
import net.wavedk.extrautilitiesreutilized.network.EnchanterGUIButtonMessage;
import net.wavedk.extrautilitiesreutilized.init.EuruModScreens;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
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
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/enchanter_gui.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/book5.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("euru:textures/screens/0.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("euru:textures/screens/2.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("euru:textures/screens/3.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("euru:textures/screens/4.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("euru:textures/screens/5.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("euru:textures/screens/6.png");
	private static final ResourceLocation IMAGE_7 = ResourceLocation.parse("euru:textures/screens/7.png");
	private static final ResourceLocation IMAGE_8 = ResourceLocation.parse("euru:textures/screens/8_.png");
	private static final ResourceLocation IMAGE_9 = ResourceLocation.parse("euru:textures/screens/9.png");
	private static final ResourceLocation IMAGE_10 = ResourceLocation.parse("euru:textures/screens/10.png");
	private static final ResourceLocation IMAGE_11 = ResourceLocation.parse("euru:textures/screens/11.png");
	private static final ResourceLocation IMAGE_12 = ResourceLocation.parse("euru:textures/screens/12.png");
	private static final ResourceLocation IMAGE_13 = ResourceLocation.parse("euru:textures/screens/13.png");
	private static final ResourceLocation IMAGE_14 = ResourceLocation.parse("euru:textures/screens/14.png");
	private static final ResourceLocation IMAGE_15 = ResourceLocation.parse("euru:textures/screens/15.png");
	private static final ResourceLocation IMAGE_16 = ResourceLocation.parse("euru:textures/screens/16.png");
	private static final ResourceLocation IMAGE_17 = ResourceLocation.parse("euru:textures/screens/17.png");
	private static final ResourceLocation IMAGE_18 = ResourceLocation.parse("euru:textures/screens/crsvwv.png");
	private static final ResourceLocation IMAGE_19 = ResourceLocation.parse("euru:textures/screens/18.png");
	private static final ResourceLocation IMAGE_20 = ResourceLocation.parse("euru:textures/screens/19.png");
	private static final ResourceLocation IMAGE_21 = ResourceLocation.parse("euru:textures/screens/20.png");
	private static final ResourceLocation IMAGE_22 = ResourceLocation.parse("euru:textures/screens/button-gun.png");
	private static final ResourceLocation IMAGE_23 = ResourceLocation.parse("euru:textures/screens/button-red.png");
	private static final ResourceLocation IMAGE_24 = ResourceLocation.parse("euru:textures/screens/button-red-off.png");
	private static final ResourceLocation IMAGE_25 = ResourceLocation.parse("euru:textures/screens/button-always-off.png");
	private static final ResourceLocation IMAGE_26 = ResourceLocation.parse("euru:textures/screens/subackdrop.png");
	private static final ResourceLocation IMAGE_27 = ResourceLocation.parse("euru:textures/screens/button.png");

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
		if (mouseX > leftPos + 94 && mouseX < leftPos + 118 && mouseY > topPos + 33 && mouseY < topPos + 57) {
			String hoverText = ResonatorRedstoneModeProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (SUTooltipSlotThreeProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 148 && mouseX < leftPos + 172 && mouseY > topPos + 59 && mouseY < topPos + 83) {
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.enchanter_gui.tooltip_speed_upgrade"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (mouseX > leftPos + 148 && mouseX < leftPos + 172 && mouseY > topPos + 23 && mouseY < topPos + 47) {
			String hoverText = ReturnFEStorageProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (ErrorShowProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 73 && mouseX < leftPos + 97 && mouseY > topPos + 58 && mouseY < topPos + 82) {
				String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
				if (hoverText != null) {
					guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
				}
				customTooltipShown = true;
			}
		if (ErrorShowProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 69 && mouseX < leftPos + 93 && mouseY > topPos + 34 && mouseY < topPos + 58) {
				String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
				if (hoverText != null) {
					guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
				}
				customTooltipShown = true;
			}
		if (ErrorShowProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 115 && mouseX < leftPos + 139 && mouseY > topPos + 58 && mouseY < topPos + 82) {
				String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
				if (hoverText != null) {
					guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
				}
				customTooltipShown = true;
			}
		if (ErrorShowProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 119 && mouseX < leftPos + 143 && mouseY > topPos + 34 && mouseY < topPos + 58) {
				String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
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
		guiGraphics.blit(IMAGE_0, this.leftPos + 2, this.topPos + 21, 0, 0, 70, 60, 70, 60);
		if (ArrowEmptyProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_1, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow1Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_2, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow2Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_3, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow3Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_4, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow4Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_5, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow5Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_6, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow6Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_7, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow7Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_8, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow8Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_9, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow9Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_10, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow10Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_11, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow11Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_12, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow12Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_13, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow13Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_14, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow14Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_15, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow15Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_16, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow16Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_17, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (ErrorShowProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_18, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow17Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_19, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow18Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_20, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (Arrow19Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_21, this.leftPos + 18, this.topPos + 4, 0, 0, 176, 83, 176, 83);
		}
		if (AlwaysOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_22, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_23, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_24, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (AlwaysOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_25, this.leftPos + 96, this.topPos + 35, 0, 0, 20, 20, 20, 20);
		}
		if (SUTooltipSlotThreeProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_26, this.leftPos + 152, this.topPos + 63, 0, 0, 16, 16, 16, 16);
		}
		guiGraphics.blit(IMAGE_27, this.leftPos + 150, this.topPos + 25, 0, 0, 20, 20, 20, 20);
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
	}
}