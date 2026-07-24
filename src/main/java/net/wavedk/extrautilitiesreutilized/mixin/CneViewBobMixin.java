package net.wavedk.extrautilitiesreutilized.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public abstract class CneViewBobMixin {
	// Dampen the first-person view/hand bob by scaling the single Mth.lerp bob-magnitude in bobView.
	// Factor comes from CneFirstPersonRuntime (1 = vanilla bob, 0 = steady). require=0 -> safe no-op.
	@ModifyExpressionValue(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F"), require = 0)
	private float cne$dampenViewBob(float original) {
		return original * (float) net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneFirstPersonRuntime.bobScaleFactor();
	}
}
