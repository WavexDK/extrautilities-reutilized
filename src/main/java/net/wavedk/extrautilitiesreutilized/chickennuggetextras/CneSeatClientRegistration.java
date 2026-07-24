package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/** Client-only: a registered entity needs a renderer or the client crashes; the seat's draws nothing. */
@EventBusSubscriber(modid = "euru", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class CneSeatClientRegistration {
	private CneSeatClientRegistration() {
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CneSeatRegistration.CNE_SEAT, CneSeatRenderer::new);
	}
}
