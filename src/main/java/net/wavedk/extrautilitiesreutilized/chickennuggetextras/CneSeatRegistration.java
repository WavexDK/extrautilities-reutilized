package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

/** Registers the invisible seat entity (built inside RegisterEvent to avoid a frozen-registry crash). */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneSeatRegistration {
	public static volatile EntityType<CneSeatEntity> CNE_SEAT;

	private CneSeatRegistration() {
	}

	@SubscribeEvent
	public static void registerEntityTypes(RegisterEvent event) {
		event.register(Registries.ENTITY_TYPE, helper -> {
			CNE_SEAT = EntityType.Builder
				.<CneSeatEntity>of(CneSeatEntity::new, MobCategory.MISC)
				.sized(0.05F, 0.05F)
				.clientTrackingRange(8)
				.updateInterval(1)
				.noSummon()
				.build("cne_seat");
			helper.register(ResourceLocation.fromNamespaceAndPath("euru", "cne_seat"), CNE_SEAT);
		});
	}
}
