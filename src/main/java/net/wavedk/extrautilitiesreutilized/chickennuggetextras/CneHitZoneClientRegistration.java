package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only: gives the hit-zone entity a (no-op) renderer. A registered entity
 * type must have a renderer or the client crashes when one comes into view, even
 * though the zone itself draws nothing.
 */
@EventBusSubscriber(modid = "euru", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class CneHitZoneClientRegistration {
	private CneHitZoneClientRegistration() {
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CneHitZoneRegistration.CNE_HIT_ZONE, CneHitZoneRenderer::new);
	}
}
