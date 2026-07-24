package net.wavedk.extrautilitiesreutilized.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class CneEntityTextureMixin {
	@ModifyExpressionValue(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"), require = 0)
	private ResourceLocation cne$overrideTexture(ResourceLocation original, LivingEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
		ResourceLocation override = net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneEntityTextureRuntime.getOverrideTexture(entity);
		return override != null ? override : original;
	}
}
