package net.wavedk.extrautilitiesreutilized.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import com.mojang.blaze3d.vertex.PoseStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;

@Mixin(PlayerRenderer.class)
public abstract class CneFirstPersonSkinMixin {
	@ModifyExpressionValue(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/PlayerSkin;texture()Lnet/minecraft/resources/ResourceLocation;"), require = 0)
	private ResourceLocation cne$overrideFirstPersonSkin(ResourceLocation original, PoseStack poseStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, ModelPart arm, ModelPart sleeve) {
		ResourceLocation override = net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneEntityTextureRuntime.getOverrideTexture(player);
		return override != null ? override : original;
	}
}
