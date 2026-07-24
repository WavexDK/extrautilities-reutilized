package net.wavedk.extrautilitiesreutilized.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(ItemRenderer.class)
public abstract class CneItemFrameMixin {
	@Inject(method = "getModel", at = @At("RETURN"), cancellable = true, require = 0)
	private void cne$applyItemFrame(ItemStack stack, Level level, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
		BakedModel original = cir.getReturnValue();
		BakedModel swapped = net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneItemFrameRuntime.Client.resolveFrameModel(stack, original);
		if (swapped != null && swapped != original) cir.setReturnValue(swapped);
	}
}
