package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

@Mixin(Camera.class)
public abstract class CneCameraMixin {
	@Inject(method = "setup", at = @At("TAIL"))
	private void cne$applyCameraOffset(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
		net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneCameraRuntime.applyCameraPosition((Camera) (Object) this, partialTick);
	}
}
