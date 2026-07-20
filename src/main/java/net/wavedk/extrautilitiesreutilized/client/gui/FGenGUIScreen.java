package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.FGenGUIMenu;
import net.wavedk.extrautilitiesreutilized.procedures.*;
import net.wavedk.extrautilitiesreutilized.network.FGenGUIButtonMessage;
import net.wavedk.extrautilitiesreutilized.init.EuruModScreens;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
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

public class FGenGUIScreen extends AbstractContainerScreen<FGenGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_empty;
	private Button button_empty1;
	private ImageButton imagebutton_blank_x20;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/f_gen_gui.png");
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
	private static final ResourceLocation IMAGE_19 = ResourceLocation.parse("euru:textures/screens/fgenbackdrop2-off.png");
	private static final ResourceLocation IMAGE_20 = ResourceLocation.parse("euru:textures/screens/arrow-19.png");
	private static final ResourceLocation IMAGE_21 = ResourceLocation.parse("euru:textures/screens/arrow-20.png");
	private static final ResourceLocation IMAGE_22 = ResourceLocation.parse("euru:textures/screens/arrow-21.png");
	private static final ResourceLocation IMAGE_23 = ResourceLocation.parse("euru:textures/screens/arrow-full.png");
	private static final ResourceLocation IMAGE_24 = ResourceLocation.parse("euru:textures/screens/fgenbackdrop2-on.png");
	private static final ResourceLocation IMAGE_25 = ResourceLocation.parse("euru:textures/screens/subackdrop.png");
	private static final ResourceLocation IMAGE_26 = ResourceLocation.parse("euru:textures/screens/symbol_cross.png");
	private static final ResourceLocation IMAGE_27 = ResourceLocation.parse("euru:textures/screens/button-gun.png");
	private static final ResourceLocation IMAGE_28 = ResourceLocation.parse("euru:textures/screens/button-always-off.png");
	private static final ResourceLocation IMAGE_29 = ResourceLocation.parse("euru:textures/screens/button-red-off.png");
	private static final ResourceLocation IMAGE_30 = ResourceLocation.parse("euru:textures/screens/button-red.png");
	private static final ResourceLocation IMAGE_31 = ResourceLocation.parse("euru:textures/screens/s1.png");
	private static final ResourceLocation IMAGE_32 = ResourceLocation.parse("euru:textures/screens/s2.png");
	private static final ResourceLocation IMAGE_33 = ResourceLocation.parse("euru:textures/screens/s3.png");
	private static final ResourceLocation IMAGE_34 = ResourceLocation.parse("euru:textures/screens/s4.png");
	private static final ResourceLocation IMAGE_35 = ResourceLocation.parse("euru:textures/screens/s5.png");
	private static final ResourceLocation IMAGE_36 = ResourceLocation.parse("euru:textures/screens/s6.png");
	private static final ResourceLocation IMAGE_37 = ResourceLocation.parse("euru:textures/screens/s7.png");
	private static final ResourceLocation IMAGE_38 = ResourceLocation.parse("euru:textures/screens/s8.png");
	private static final ResourceLocation IMAGE_39 = ResourceLocation.parse("euru:textures/screens/s9.png");
	private static final ResourceLocation IMAGE_40 = ResourceLocation.parse("euru:textures/screens/s10.png");
	private static final ResourceLocation IMAGE_41 = ResourceLocation.parse("euru:textures/screens/s11.png");
	private static final ResourceLocation IMAGE_42 = ResourceLocation.parse("euru:textures/screens/s12.png");
	private static final ResourceLocation IMAGE_43 = ResourceLocation.parse("euru:textures/screens/s13.png");
	private static final ResourceLocation IMAGE_44 = ResourceLocation.parse("euru:textures/screens/s14.png");
	private static final ResourceLocation IMAGE_45 = ResourceLocation.parse("euru:textures/screens/s15.png");
	private static final ResourceLocation IMAGE_46 = ResourceLocation.parse("euru:textures/screens/s16.png");
	private static final ResourceLocation IMAGE_47 = ResourceLocation.parse("euru:textures/screens/s17.png");
	private static final ResourceLocation IMAGE_48 = ResourceLocation.parse("euru:textures/screens/s18.png");
	private static final ResourceLocation IMAGE_49 = ResourceLocation.parse("euru:textures/screens/s19.png");
	private static final ResourceLocation IMAGE_50 = ResourceLocation.parse("euru:textures/screens/s20.png");
	private static final ResourceLocation IMAGE_51 = ResourceLocation.parse("euru:textures/screens/s21.png");
	private static final ResourceLocation IMAGE_52 = ResourceLocation.parse("euru:textures/screens/s22.png");
	private static final ResourceLocation IMAGE_53 = ResourceLocation.parse("euru:textures/screens/s23.png");
	private static final ResourceLocation IMAGE_54 = ResourceLocation.parse("euru:textures/screens/s24.png");
	private static final ResourceLocation IMAGE_55 = ResourceLocation.parse("euru:textures/screens/s25.png");
	private static final ResourceLocation IMAGE_56 = ResourceLocation.parse("euru:textures/screens/s26.png");
	private static final ResourceLocation IMAGE_57 = ResourceLocation.parse("euru:textures/screens/s27.png");
	private static final ResourceLocation IMAGE_58 = ResourceLocation.parse("euru:textures/screens/s28.png");
	private static final ResourceLocation IMAGE_59 = ResourceLocation.parse("euru:textures/screens/s29.png");
	private static final ResourceLocation IMAGE_60 = ResourceLocation.parse("euru:textures/screens/s30.png");
	private static final ResourceLocation IMAGE_61 = ResourceLocation.parse("euru:textures/screens/s31.png");
	private static final ResourceLocation IMAGE_62 = ResourceLocation.parse("euru:textures/screens/s32.png");
	private static final ResourceLocation IMAGE_63 = ResourceLocation.parse("euru:textures/screens/s33.png");
	private static final ResourceLocation IMAGE_64 = ResourceLocation.parse("euru:textures/screens/s34.png");
	private static final ResourceLocation IMAGE_65 = ResourceLocation.parse("euru:textures/screens/s35.png");
	private static final ResourceLocation IMAGE_66 = ResourceLocation.parse("euru:textures/screens/s36.png");
	private static final ResourceLocation IMAGE_67 = ResourceLocation.parse("euru:textures/screens/s37.png");
	private static final ResourceLocation IMAGE_68 = ResourceLocation.parse("euru:textures/screens/s38.png");
	private static final ResourceLocation IMAGE_69 = ResourceLocation.parse("euru:textures/screens/s39.png");
	private static final ResourceLocation IMAGE_70 = ResourceLocation.parse("euru:textures/screens/s40.png");
	private static final ResourceLocation IMAGE_71 = ResourceLocation.parse("euru:textures/screens/s41.png");
	private static final ResourceLocation IMAGE_72 = ResourceLocation.parse("euru:textures/screens/s42.png");
	private static final ResourceLocation IMAGE_73 = ResourceLocation.parse("euru:textures/screens/s43.png");
	private static final ResourceLocation IMAGE_74 = ResourceLocation.parse("euru:textures/screens/s44.png");
	private static final ResourceLocation IMAGE_75 = ResourceLocation.parse("euru:textures/screens/s45.png");
	private static final ResourceLocation IMAGE_76 = ResourceLocation.parse("euru:textures/screens/s46.png");
	private static final ResourceLocation IMAGE_77 = ResourceLocation.parse("euru:textures/screens/bbbattery.png");
	private static final ResourceLocation IMAGE_78 = ResourceLocation.parse("euru:textures/screens/bbbatteryoff.png");

	public FGenGUIScreen(FGenGUIMenu container, Inventory inventory, Component text) {
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
				guiGraphics.renderTooltip(font, Component.translatable("gui.euru.f_gen_gui.tooltip_speed_upgrade"), mouseX, mouseY);
				customTooltipShown = true;
			}
		if (mouseX > leftPos + 140 && mouseX < leftPos + 164 && mouseY > topPos + 5 && mouseY < topPos + 29) {
			String hoverText = ReturnFEStorageProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 140 && mouseX < leftPos + 164 && mouseY > topPos + 28 && mouseY < topPos + 52) {
			String hoverText = ReturnFEStorageProcedure.execute(world, x, y, z);
			if (hoverText != null) {
				guiGraphics.renderComponentTooltip(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 140 && mouseX < leftPos + 164 && mouseY > topPos + 51 && mouseY < topPos + 75) {
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
		if (SGenIsOffProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_19, this.leftPos + 56, this.topPos + 9, 0, 0, 64, 64, 64, 64);
		}
		if (Arrow19Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_20, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow20Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_21, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (Arrow21Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_22, this.leftPos + 90, this.topPos + 32, 0, 0, 22, 22, 22, 22);
		}
		if (ArrowFullProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_23, this.leftPos + 90, this.topPos + 33, 0, 0, 22, 22, 22, 22);
		}
		if (SGenIsOnProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_24, this.leftPos + 56, this.topPos + 9, 0, 0, 64, 64, 64, 64);
		}
		if (SUTooltipGenProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_25, this.leftPos + 6, this.topPos + 41, 0, 0, 16, 16, 16, 16);
		}
		if (TooMuchGPProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_26, this.leftPos + 92, this.topPos + 34, 0, 0, 16, 16, 16, 16);
		}
		if (AlwaysOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_27, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (AlwaysOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_28, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOffProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_29, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (RedstoneOnProcedureProcedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_30, this.leftPos + 4, this.topPos + 16, 0, 0, 20, 20, 20, 20);
		}
		if (Bl2Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_31, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl3Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_32, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl4Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_33, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl5Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_34, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl6Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_35, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl7Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_36, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl8Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_37, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl9Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_38, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl10Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_39, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl11Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_40, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl12Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_41, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl13Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_42, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl14Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_43, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl15Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_44, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl16Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_45, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl17Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_46, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl18Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_47, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl19Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_48, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl20Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_49, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl21Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_50, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl22Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_51, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl23Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_52, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl24Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_53, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl25Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_54, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl26Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_55, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl27Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_56, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl28Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_57, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl29Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_58, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl30Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_59, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl31Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_60, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl32Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_61, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl33Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_62, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl34Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_63, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl35Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_64, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl36Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_65, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl37Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_66, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl38Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_67, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl39Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_68, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl40Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_69, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl41Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_70, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl42Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_71, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl43Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_72, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl44Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_73, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl45Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_74, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl46Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_75, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl47Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_76, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl48Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_77, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
		}
		if (Bl1Procedure.execute(world, x, y, z)) {
			guiGraphics.blit(IMAGE_78, this.leftPos + 140, this.topPos + 7, 0, 0, 24, 64, 24, 64);
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
		button_empty = new PlainTextButton(this.leftPos + 89, this.topPos + 31, 24, 20, Component.translatable("gui.euru.f_gen_gui.button_empty"), e -> {
			int x = FGenGUIScreen.this.x;
			int y = FGenGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new FGenGUIButtonMessage(0, x, y, z));
				FGenGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}, this.font);
		this.addRenderableWidget(button_empty);
		button_empty1 = new PlainTextButton(this.leftPos + 89, this.topPos + 35, 24, 20, Component.translatable("gui.euru.f_gen_gui.button_empty1"), e -> {
			int x = FGenGUIScreen.this.x;
			int y = FGenGUIScreen.this.y;
			if (true) {
				PacketDistributor.sendToServer(new FGenGUIButtonMessage(1, x, y, z));
				FGenGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}, this.font);
		this.addRenderableWidget(button_empty1);
		imagebutton_blank_x20 = new ImageButton(this.leftPos + 4, this.topPos + 16, 20, 20, new WidgetSprites(ResourceLocation.parse("euru:textures/screens/blank_x20.png"), ResourceLocation.parse("euru:textures/screens/blank_whiteborder_x20.png")),
				e -> {
					int x = FGenGUIScreen.this.x;
					int y = FGenGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new FGenGUIButtonMessage(2, x, y, z));
						FGenGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
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