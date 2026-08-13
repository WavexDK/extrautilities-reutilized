package net.wavedk.extrautilitiesreutilized.client.renderer;

import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.LivingEntity;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(value = Dist.CLIENT)
public class SemiAquaticRenderer {
	@SubscribeEvent
	public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
		LivingEntity entity = event.getEntity();
		if (entity != null && entity.isInWater() && entity.getXRot() < -5.0F) {
			PoseStack poseStack = event.getPoseStack();
			poseStack.pushPose();
			float centerY = entity.getBbHeight() / 2.0F;
			float targetRotationX = -10.0F - entity.getXRot();
			poseStack.translate(0.0F, centerY, 0.0F);
			poseStack.mulPose(Axis.XP.rotationDegrees(targetRotationX));
			poseStack.translate(0.0F, -centerY, 0.0F);
		}
	}

	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
		LivingEntity entity = event.getEntity();
		if (entity != null && entity.isInWater() && entity.getXRot() < -5.0F) {
			event.getPoseStack().popPose();
		}
	}
}