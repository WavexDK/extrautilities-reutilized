package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Registers the movable-block-group entity type. Built INSIDE the RegisterEvent (the entity
 * registry is frozen by the time this @EventBusSubscriber loads, so a static build throws
 * "Registry is already frozen"). Bigger client tracking range than the single block so large
 * platforms don't pop out when their centre leaves the default range.
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneMovableBlockGroupRegistration {
	public static volatile EntityType<CneMovableBlockGroupEntity> CNE_MOVABLE_BLOCK_GROUP;

	private CneMovableBlockGroupRegistration() {
	}

	@SubscribeEvent
	public static void registerEntityTypes(RegisterEvent event) {
		event.register(Registries.ENTITY_TYPE, helper -> {
			CNE_MOVABLE_BLOCK_GROUP = EntityType.Builder
				.<CneMovableBlockGroupEntity>of(CneMovableBlockGroupEntity::new, MobCategory.MISC)
				.sized(1.0F, 1.0F)
				.clientTrackingRange(16)
				.updateInterval(1)
				.noSummon()
				.build("cne_movable_block_group");
			helper.register(ResourceLocation.fromNamespaceAndPath("euru", "cne_movable_block_group"), CNE_MOVABLE_BLOCK_GROUP);
		});
	}
}
