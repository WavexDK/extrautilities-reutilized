package net.wavedk.extrautilitiesreutilized.client.gui;

import net.wavedk.extrautilitiesreutilized.world.inventory.CreativeGenGUIMenu;
import net.wavedk.extrautilitiesreutilized.init.EuruModScreens;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.systems.RenderSystem;

public class CreativeGenGUIScreen extends AbstractContainerScreen<CreativeGenGUIMenu> implements EuruModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox energySend;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("euru:textures/screens/creative_gen_gui.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("euru:textures/screens/cgentext.png");

	public CreativeGenGUIScreen(CreativeGenGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 85;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 0 && elementState instanceof String stringState) {
			if (name.equals("energySend"))
				energySend.setValue(stringState);
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		energySend.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(IMAGE_0, this.leftPos + 2, this.topPos + 40, 0, 0, 280, 128, 280, 128);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (energySend.isFocused())
			return energySend.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String energySendValue = energySend.getValue();
		super.resize(minecraft, width, height);
		energySend.setValue(energySendValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.euru.creative_gen_gui.label_creative_generator"), 5, 5, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		energySend = new EditBox(this.font, this.leftPos + 5, this.topPos + 19, 120, 20, Component.translatable("gui.euru.creative_gen_gui.energySend"));
		energySend.setMaxLength(8192);
		energySend.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "energySend", content, false);
		});
		energySend.setHint(Component.translatable("gui.euru.creative_gen_gui.energySend"));
		this.addWidget(this.energySend);
	}
}