package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Registers the invisible hit-zone entity type. The EntityType is built INSIDE the
 * RegisterEvent, not in a static initializer: building it creates a holder in the entity
 * registry, which is already frozen by the time the @EventBusSubscriber class is loaded,
 * so a static build throws "Registry is already frozen". Here the registry is open.
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneHitZoneRegistration {
	public static volatile EntityType<CneHitZoneEntity> CNE_HIT_ZONE;

	private CneHitZoneRegistration() {
	}

	@SubscribeEvent
	public static void registerEntityTypes(RegisterEvent event) {
		event.register(Registries.ENTITY_TYPE, helper -> {
			CNE_HIT_ZONE = EntityType.Builder
				.<CneHitZoneEntity>of(CneHitZoneEntity::new, MobCategory.MISC)
				.sized(1.0F, 1.0F)
				.clientTrackingRange(8)
				.updateInterval(2)
				.noSummon()
				.build("cne_hit_zone");
			helper.register(ResourceLocation.fromNamespaceAndPath("euru", "cne_hit_zone"), CNE_HIT_ZONE);
		});
	}
}
