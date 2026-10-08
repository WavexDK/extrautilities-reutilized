package net.wavedk.extrautilitiesreutilized.client;

import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(modid = "euru", value = Dist.CLIENT)
public class CameraShakeClientEvents {
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		CameraShakeManager.getInstance().tick();
	}

	@SubscribeEvent
	public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
		CameraShakeManager shakeManager = CameraShakeManager.getInstance();
		if (shakeManager.isShaking()) {
			float tickDelta = (float) event.getPartialTick();
			float yawOffset = shakeManager.getShakeYaw(tickDelta);
			float pitchOffset = shakeManager.getShakePitch(tickDelta);

			event.setYaw(event.getYaw() + yawOffset * 0.3F);
			event.setPitch(event.getPitch() + pitchOffset * 0.3F);
		}
	}
}