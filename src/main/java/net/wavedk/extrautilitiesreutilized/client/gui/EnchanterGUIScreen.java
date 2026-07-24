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
	private ImageButton imagebutton_blank_22x22;
	private ImageButton imagebutton_blankx24;
	private ImageButton imagebutton_blankx241;
	private ImageButton imagebutton_blankx242;
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
	private static final ResourceLocation IMAGE_27 = ResourceLocation.parse("euru:textures/screens/s1.png");
	private static final ResourceLocation IMAGE_28 = ResourceLocation.parse("euru:textures/screens/s2.png");
	private static final ResourceLocation IMAGE_29 = ResourceLocation.parse("euru:textures/screens/s3.png");
	private static final ResourceLocation IMAGE_30 = ResourceLocation.parse("euru:textures/screens/s4.png");
	private static final ResourceLocation IMAGE_31 = ResourceLocation.parse("euru:textures/screens/s5.png");
	private static final ResourceLocation IMAGE_32 = ResourceLocation.parse("euru:textures/screens/s6.png");
	private static final ResourceLocation IMAGE_33 = ResourceLocation.parse("euru:textures/screens/s7.png");
	private static final ResourceLocation IMAGE_34 = ResourceLocation.parse("euru:textures/screens/s8.png");
	private static final ResourceLocation IMAGE_35 = ResourceLocation.parse("euru:textures/screens/s9.png");
	private static final ResourceLocation IMAGE_36 = ResourceLocation.parse("euru:textures/screens/s10.png");
	private static final ResourceLocation IMAGE_37 = ResourceLocation.parse("euru:textures/screens/s11.png");
	private static final ResourceLocation IMAGE_38 = ResourceLocation.parse("euru:textures/screens/s12.png");
	private static final ResourceLocation IMAGE_39 = ResourceLocation.parse("euru:textures/screens/s13.png");
	private static final ResourceLocation IMAGE_40 = ResourceLocation.parse("euru:textures/screens/s14.png");
	private static final ResourceLocation IMAGE_41 = ResourceLocation.parse("euru:textures/screens/s15.png");
	private static final ResourceLocation IMAGE_42 = ResourceLocation.parse("euru:textures/screens/s16.png");
	private static final ResourceLocation IMAGE_43 = ResourceLocation.parse("euru:textures/screens/s17.png");
	private static final ResourceLocation IMAGE_44 = ResourceLocation.parse("euru:textures/screens/s18.png");
	private static final ResourceLocation IMAGE_45 = ResourceLocation.parse("euru:textures/screens/s19.png");
	private static final ResourceLocation IMAGE_46 = ResourceLocation.parse("euru:textures/screens/s20.png");
	private static final ResourceLocation IMAGE_47 = ResourceLocation.parse("euru:textures/screens/s21.png");
	private static final ResourceLocation IMAGE_48 = ResourceLocation.parse("euru:textures/screens/s22.png");
	private static final ResourceLocation IMAGE_49 = ResourceLocation.parse("euru:textures/screens/s23.png");
	private static final ResourceLocation IMAGE_50 = ResourceLocation.parse("euru:textures/screens/s24.png");
	private static final ResourceLocation IMAGE_51 = ResourceLocation.parse("euru:textures/screens/s25.png");
	private static final ResourceLocation IMAGE_52 = ResourceLocation.parse("euru:textures/screens/s26.png");
	private static final ResourceLocation IMAGE_53 = ResourceLocation.parse("euru:textures/screens/s27.png");
	private static final ResourceLocation IMAGE_54 = ResourceLocation.parse("euru:textures/screens/s28.png");
	private static final ResourceLocation IMAGE_55 = ResourceLocation.parse("euru:textures/screens/s29.png");
	private static final ResourceLocation IMAGE_56 = ResourceLocation.parse("euru:textures/screens/s30.png");
	private static final ResourceLocation IMAGE_57 = ResourceLocation.parse("euru:textures/screens/s31.png");
	private static final ResourceLocation IMAGE_58 = ResourceLocation.parse("euru:textures/screens/s32.png");
	private static final ResourceLocation IMAGE_59 = ResourceLocation.parse("euru:textures/screens/s33.png");
	private static final ResourceLocation IMAGE_60 = ResourceLocation.parse("euru:textures/screens/s34.png");
	private static final ResourceLocation IMAGE_61 = ResourceLocation.parse("euru:textures/screens/s35.png");
	private static final ResourceLocation IMAGE_62 = ResourceLocation.parse("euru:textures/screens/s36.png");
	private static final ResourceLocation IMAGE_63 = ResourceLocation.parse("euru:textures/screens/s37.png");
	private static final ResourceLocation IMAGE_64 = ResourceLocation.parse("euru:textures/screens/s38.png");
	private static final ResourceLocation IMAGE_65 = ResourceLocation.parse("euru:textures/screens/s39.png");
	private static final ResourceLocation IMAGE_66 = ResourceLocation.parse("euru:textures/screens/s40.png");
	private static final ResourceLocation IMAGE_67 = ResourceLocation.parse("euru:textures/screens/s41.png");
	private static final ResourceLocation IMAGE_68 = ResourceLocation.parse("euru:textures/screens/s42.png");
	private static final ResourceLocation IMAGE_69 = ResourceLocation.parse("euru:textures/screens/s43.png");
	private static final ResourceLocation IMAGE_70 = ResourceLocation.parse("euru:textures/screens/s44.png");
	private static final ResourceLocation IMAGE_71 = ResourceLocation.parse("euru:textures/screens/s45.png");
	private static final ResourceLocation IMAGE_72 = ResourceLocation.parse("euru:textures/screens/s46.png");
	private static final ResourceLocation IMAGE_73 = ResourceLocation.parse("euru:textures/screens/bbbattery.png");
	private static final ResourceLocation IMAGE_74 = ResourceLocation.parse("euru:textures/screens/bbbatteryoff.png");

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
			if (mouseX > leftPos + 97 && mouseX < leftPos + 115 && mouseY > topPos + 7 && mouseY < topPos + 25) {
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.enchanter_gui.tooltip_speed_upgrade"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (mouseX > leftPos + 146 && mouseX < leftPos + 168 && mouseY > topPos + 15 && mouseY < topPos + 75) {
			String hoverText = ReturnFEStorageProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 73 && mouseX < leftPos + 97 && mouseY > topPos + 58 && mouseY < topPos + 82) {
			String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 69 && mouseX < leftPos + 93 && mouseY > topPos + 34 && mouseY < topPos + 58) {
			String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 115 && mouseX < leftPos + 139 && mouseY > topPos + 58 && mouseY < topPos + 82) {
			String hoverText = ReturnEnchanterErrrorProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
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
			guiGraphics.blit(IMAGE_26, this.leftPos + 98, this.topPos + 8, 0, 0, 16, 16, 16, 16);
		}
		if (Bl2Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_27, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl3Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_28, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl4Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_29, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl5Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_30, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl6Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_31, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl7Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_32, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl8Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_33, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl9Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_34, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl10Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_35, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl11Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_36, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl12Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_37, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl13Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_38, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl14Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_39, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl15Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_40, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl16Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_41, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl17Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_42, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl18Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_43, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl19Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_44, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl20Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_45, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl21Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_46, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl22Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_47, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl23Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_48, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl24Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_49, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl25Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_50, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl26Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_51, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl27Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_52, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl28Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_53, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl29Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_54, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl30Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_55, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl31Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_56, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl32Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_57, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl33Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_58, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl34Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_59, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl35Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_60, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl36Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_61, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl37Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_62, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl38Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_63, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl39Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_64, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl40Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_65, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl41Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_66, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl42Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_67, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl43Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_68, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl44Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_69, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl45Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_70, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl46Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_71, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl47Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_72, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl48Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_73, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
		}
		if (Bl1Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_74, this.leftPos + 145, this.topPos + 11, 0, 0, 24, 64, 24, 64);
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