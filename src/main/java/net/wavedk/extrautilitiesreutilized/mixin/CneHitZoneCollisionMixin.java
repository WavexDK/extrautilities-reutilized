package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public abstract class CneHitZoneCollisionMixin {
	@Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true, require = 0)
	private void cne$ignoreOwnHitZones(Entity other, CallbackInfoReturnable<Boolean> cir) {
		if (other instanceof net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneHitZoneEntity zone && zone.isHostEntity((Entity) (Object) this)) {
			cir.setReturnValue(false);
		}
	}
}
