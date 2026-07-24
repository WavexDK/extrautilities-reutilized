package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.item.FallingBlockEntity;

@Mixin(FallingBlockEntity.class)
public abstract class CneFlyingDebrisMixin {
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FallingBlock;isFree(Lnet/minecraft/world/level/block/state/BlockState;)Z"), cancellable = true, require = 0)
	private void cne$discardDebrisOnLand(CallbackInfo ci) {
		FallingBlockEntity self = (FallingBlockEntity) (Object) this;
		if (self.level().isClientSide) return;
		if (self.getPersistentData().getBoolean("CneNoPlace")) {
			self.discard();
			ci.cancel();
		}
	}
}
