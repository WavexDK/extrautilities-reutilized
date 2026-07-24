package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only: gives the movable-block-group entity its renderer. A registered entity must
 * have a renderer or the client crashes when one is in view.
 */
@EventBusSubscriber(modid = "euru", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class CneMovableBlockGroupClientRegistration {
	private CneMovableBlockGroupClientRegistration() {
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CneMovableBlockGroupRegistration.CNE_MOVABLE_BLOCK_GROUP, CneMovableBlockGroupRenderer::new);
	}
}
