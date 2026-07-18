package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.SGenGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.*;
import net.wavedk.extrautilitiesreutilized.network.SGenGUIButtonMessage;
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

public class SGenGUIScreen extends AbstractContainerScreen<SGenGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_blank_x20;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/s_gen_gui.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/arrow-empty.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("euru:textures/screens/arrow-1.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("euru:textures/screens/arrow-2.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("euru:textures/screens/arrow-3.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("euru:textures/screens/arrow-4.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("euru:textures/screens/arrow-5.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("euru:textures/screens/arrow-6.png");
	private static final ResourceLocation IMAGE_7 = ResourceLocation.parse("euru:textures/screens/arrow-7.png");
	private static final ResourceLocation IMAGE_8 = ResourceLocation.parse("euru:textures/screens/arrow-8.png");
	private static final ResourceLocation IMAGE_9 = ResourceLocation.parse("euru:textures/screens/arrow-9.png");
	private static final ResourceLocation IMAGE_10 = ResourceLocation.parse("euru:textures/screens/arrow-10.png");
	private static final ResourceLocation IMAGE_11 = ResourceLocation.parse("euru:textures/screens/arrow-11.png");
	private static final ResourceLocation IMAGE_12 = ResourceLocation.parse("euru:textures/screens/arrow-12.png");
	private static final ResourceLocation IMAGE_13 = ResourceLocation.parse("euru:textures/screens/arrow-13.png");
	private static final ResourceLocation IMAGE_14 = ResourceLocation.parse("euru:textures/screens/arrow-14.png");
	private static final ResourceLocation IMAGE_15 = ResourceLocation.parse("euru:textures/screens/arrow-15.png");
	private static final ResourceLocation IMAGE_16 = ResourceLocation.parse("euru:textures/screens/arrow-16.png");
	private static final ResourceLocation IMAGE_17 = ResourceLocation.parse("euru:textures/screens/arrow-17.png");
	private static final ResourceLocation IMAGE_18 = ResourceLocation.parse("euru:textures/screens/arrow-18.png");
	private static final ResourceLocation IMAGE_19 = ResourceLocation.parse("euru:textures/screens/arrow-19.png");
	private static final ResourceLocation IMAGE_20 = ResourceLocation.parse("euru:textures/screens/arrow-20.png");
	private static final ResourceLocation IMAGE_21 = ResourceLocation.parse("euru:textures/screens/arrow-21.png");
	private static final ResourceLocation IMAGE_22 = ResourceLocation.parse("euru:textures/screens/arrow-full.png");
	private static final ResourceLocation IMAGE_23 = ResourceLocation.parse("euru:textures/screens/subackdrop.png");
	private static final ResourceLocation IMAGE_24 = ResourceLocation.parse("euru:textures/screens/symbol_cross.png");
	private static final ResourceLocation IMAGE_25 = ResourceLocation.parse("euru:textures/screens/button-gun.png");
	private static final ResourceLocation IMAGE_26 = ResourceLocation.parse("euru:textures/screens/button-always-off.png");
	private static final ResourceLocation IMAGE_27 = ResourceLocation.parse("euru:textures/screens/button-red-off.png");
	private static final ResourceLocation IMAGE_28 = ResourceLocation.parse("euru:textures/screens/button-red.png");
	private static final ResourceLocation IMAGE_29 = ResourceLocation.parse("euru:textures/screens/button.png");
	private static final ResourceLocation IMAGE_30 = ResourceLocation.parse("euru:textures/screens/sgenbackdrop2-off.png");
	private static final ResourceLocation IMAGE_31 = ResourceLocation.parse("euru:textures/screens/sgenbackdrop2-on.png");

	public SGenGUIScreen(SGenGUIMenu container, Inventory inventory, Component text) {
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
		if (TooMuchGPProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 89 && mouseX < leftPos + 113 && mouseY > topPos + 31 && mouseY < topPos + 55) {
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.s_gen_gui.tooltip_not_enough_grid_power_available"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (SUTooltipGenProcedure.execute(world, x, y, z))
			if (mouseX > leftPos + 2 && mouseX < leftPos + 26 && mouseY > topPos + 38 && mouseY < topPos + 62) {
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.s_gen_gui.tooltip_speed_upgrade"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (mouseX > leftPos + 145 && mouseX < leftPos + 169 && mouseY > topPos + 31 && mouseY < topPos + 55) {
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
		if (ArrowEmptyProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_0, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow1Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_1, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow2Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_2, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow3Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_3, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow4Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_4, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow5Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_5, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow6Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_6, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow7Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_7, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow8Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_8, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow9Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_9, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow10Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_10, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow11Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_11, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow12Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_12, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow13Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_13, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow14Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_14, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow15Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_15, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow16Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_16, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow17Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_17, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow18Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_18, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow19Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_19, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow20Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_20, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow21Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_21, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (ArrowFullProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_22, this.leftPos + 90, this.topPos + 33, 0, 0, 22, 22, 22, 22);
		}
		if (SUTooltipGenProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_23, this.leftPos + 6, this.topPos + 41, 0, 0, 16, 16, 16, 16);
		}
		if (TooMuchGPProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_24, this.leftPos + 92, this.topPos + 34, 0, 0, 16, 16, 16, 16);
		}
		if (AlwaysOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_25, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (AlwaysOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_26, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_27, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_28, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		guiGraphics.blit(IMAGE_29, this.leftPos + 147, this.topPos + 33, 0, 0, 20, 20, 20, 20);
		if (SGenIsOffProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_30, this.leftPos + 56, this.topPos + 9, 0, 0, 64, 64, 64, 64);
		}
		if (SGenIsOnProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_31, this.leftPos + 56, this.topPos + 9, 0, 0, 64, 64, 64, 64);
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
		imagebutton_blank_x20 = new ImageButton(this.leftPos + 4, this.topPos + 16, 20, 20, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blank_x20.png"), ResourceLocation.parse("euru:textures/screens/blank_whiteborder_x20.png")),
				e -> {
					int x = SGenGUIScreen.this.x;
					int y = SGenGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new SGenGUIButtonMessage(0, x, y, z));
						SGenGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
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