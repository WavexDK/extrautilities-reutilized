package net.wavedk.extrautilitiesreutilized.mixin;

import com.mojang.blaze3d.vertex.PoseStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemInHandRenderer.class)
public abstract class CneItemInHandTrailMixin {
	// Capture the REAL rendered held-item transform (third-person) so the weapon trail rides the
	// actual blade instead of a bounding-box guess. require=0 -> silently no-ops if the target shifts.
	@Inject(method = "renderItem", at = @At("HEAD"), require = 0)
	private void cne$captureWeaponTrail(LivingEntity entity, ItemStack stack, ItemDisplayContext ctx, boolean leftHand, PoseStack pose, MultiBufferSource buffer, int light, CallbackInfo ci) {
		if (entity == null || stack.isEmpty() || pose == null) return;
		net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneWeaponTrailClient.onItemRendered(entity, ctx, pose.last().pose());
	}
}
