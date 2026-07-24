package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Registers the movable-block entity type. The EntityType is built INSIDE the RegisterEvent,
 * not in a static initializer: building it creates a holder in the entity registry, which is
 * already frozen by the time the @EventBusSubscriber class loads, so a static build throws
 * "Registry is already frozen". Here the registry is open.
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneMovableBlockRegistration {
	public static volatile EntityType<CneMovableBlockEntity> CNE_MOVABLE_BLOCK;

	private CneMovableBlockRegistration() {
	}

	@SubscribeEvent
	public static void registerEntityTypes(RegisterEvent event) {
		event.register(Registries.ENTITY_TYPE, helper -> {
			CNE_MOVABLE_BLOCK = EntityType.Builder
				.<CneMovableBlockEntity>of(CneMovableBlockEntity::new, MobCategory.MISC)
				.sized(1.0F, 1.0F)
				.clientTrackingRange(10)
				.updateInterval(1)
				.noSummon()
				.build("cne_movable_block");
			helper.register(ResourceLocation.fromNamespaceAndPath("euru", "cne_movable_block"), CNE_MOVABLE_BLOCK);
		});
	}
}
