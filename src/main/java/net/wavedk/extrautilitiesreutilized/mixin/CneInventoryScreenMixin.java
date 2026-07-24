package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

@Mixin(AbstractContainerScreen.class)
public abstract class CneInventoryScreenMixin {
	@Shadow protected int leftPos;
	@Shadow protected int topPos;
	@Shadow protected int imageWidth;
	@Shadow protected int imageHeight;

	private float[] cne$posScale(int surface) {
		if (surface == 0) return new float[]{1.0F, 1.0F};
		return new float[]{(float) this.imageWidth / 176.0F, (float) this.imageHeight / 166.0F};
	}

	private int[] cne$resolveSurface() {
		Object self = this;
		if (self instanceof InventoryScreen) return new int[]{0, this.leftPos, this.topPos};
		if (self instanceof CreativeModeInventoryScreen creativeScreen) {
			return new int[]{creativeScreen.isInventoryOpen() ? 1 : 2, this.leftPos, this.topPos};
		}
		return null;
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void cne$renderInventoryWidgets(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		int[] s = cne$resolveSurface();
		if (s == null) return;
		float[] ps = cne$posScale(s[0]);
		net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneInventoryRuntime.renderComponents(graphics, s[0], s[1], s[2], ps[0], ps[1], mouseX, mouseY);
	}

	@Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
	private void cne$inventoryButtonClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		int[] s = cne$resolveSurface();
		if (s == null) return;
		float[] ps = cne$posScale(s[0]);
		if (net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneInventoryRuntime.mouseClicked(mouseX, mouseY, button, s[0], s[1], s[2], ps[0], ps[1])) {
			cir.setReturnValue(true);
		}
	}
}
