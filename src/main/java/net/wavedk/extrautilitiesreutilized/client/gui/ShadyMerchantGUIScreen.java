package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.ShadyMerchantGUIMenu;
import net.wavedk.extrautilitiesreutilized.network.ShadyMerchantGUIButtonMessage;
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

import com.mojang.blaze3d.systems.RenderSystem;

public class ShadyMerchantGUIScreen extends AbstractContainerScreen<ShadyMerchantGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_fakensbutton;
	private ImageButton imagebutton_fakediabutton;
	private ImageButton imagebutton_fakenethbutton;
	private ImageButton imagebutton_fakenethpickbutton;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/shady_merchant_gui.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/villagerbg.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("euru:textures/screens/scrollergui.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("euru:textures/screens/xpgui.png");

	public ShadyMerchantGUIScreen(ShadyMerchantGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 274;
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
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(IMAGE_0, this.leftPos + -1, this.topPos + 0, 0, 0, 276, 166, 276, 166);
		guiGraphics.blit(IMAGE_1, this.leftPos + 93, this.topPos + 18, 0, 0, 6, 27, 6, 27);
		guiGraphics.blit(IMAGE_2, this.leftPos + 135, this.topPos + 16, 0, 0, 102, 5, 102, 5);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.shady_merchant_gui.label_trades"), 34, 6, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.shady_merchant_gui.label_legit_merchant"), 150, 6, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.shady_merchant_gui.label_inventory"), 106, 72, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_fakensbutton = new ImageButton(this.leftPos + 4, this.topPos + 18, 88, 20,
				new WidgetSprites(ResourceLocation.parse("euru:textures/screens/fakensbutton.png"), ResourceLocation.parse("euru:textures/screens/fakensbuttonhighlight.png")), e -> {
					int x = ShadyMerchantGUIScreen.this.x;
					int y = ShadyMerchantGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new ShadyMerchantGUIButtonMessage(0, x, y, z));
						ShadyMerchantGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_fakensbutton);
		imagebutton_fakediabutton = new ImageButton(this.leftPos + 4, this.topPos + 38, 88, 20,
				new WidgetSprites(ResourceLocation.parse("euru:textures/screens/fakediabutton.png"), ResourceLocation.parse("euru:textures/screens/fakediabuttonhighlight.png")), e -> {
					int x = ShadyMerchantGUIScreen.this.x;
					int y = ShadyMerchantGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new ShadyMerchantGUIButtonMessage(1, x, y, z));
						ShadyMerchantGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_fakediabutton);
		imagebutton_fakenethbutton = new ImageButton(this.leftPos + 4, this.topPos + 58, 88, 20,
				new WidgetSprites(ResourceLocation.parse("euru:textures/screens/fakenethbutton.png"), ResourceLocation.parse("euru:textures/screens/fakenethbuttonhighlight.png")), e -> {
					int x = ShadyMerchantGUIScreen.this.x;
					int y = ShadyMerchantGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new ShadyMerchantGUIButtonMessage(2, x, y, z));
						ShadyMerchantGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_fakenethbutton);
		imagebutton_fakenethpickbutton = new ImageButton(this.leftPos + 4, this.topPos + 78, 88, 20,
				new WidgetSprites(ResourceLocation.parse("euru:textures/screens/fakenethpickbutton.png"), ResourceLocation.parse("euru:textures/screens/fakenethpickbuttonhighlight.png")), e -> {
					int x = ShadyMerchantGUIScreen.this.x;
					int y = ShadyMerchantGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new ShadyMerchantGUIButtonMessage(3, x, y, z));
						ShadyMerchantGUIButtonMessage.handleButtonAction(entity, 3, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_fakenethpickbutton);
	}
}