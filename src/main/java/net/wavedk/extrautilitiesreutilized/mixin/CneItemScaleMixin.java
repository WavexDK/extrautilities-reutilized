package net.wavedk.extrautilitiesreutilized.mixin;

import com.mojang.blaze3d.vertex.PoseStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class CneItemScaleMixin {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0, shift = At.Shift.AFTER), require = 0)
	private void cne$scaleItem(ItemStack stack, ItemDisplayContext ctx, boolean leftHand, PoseStack pose, MultiBufferSource buffer, int light, int overlay, BakedModel model, CallbackInfo ci) {
		float s = net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneItemScaleRuntime.scaleFor(stack, ctx == ItemDisplayContext.GUI);
		if (s != 1.0F && s > 0.0F) {
			pose.translate(0.5F, 0.5F, 0.5F);
			pose.scale(s, s, s);
			pose.translate(-0.5F, -0.5F, -0.5F);
		}
	}
}
