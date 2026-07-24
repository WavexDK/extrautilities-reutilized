package net.wavedk.extrautilitiesreutilized.mixin;

import java.util.function.Predicate;

import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;

@Mixin(ProjectileUtil.class)
public abstract class CneHitZonePickMixin {
	@ModifyVariable(method = "getEntityHitResult", at = @At("HEAD"), argsOnly = true, require = 0)
	private static Predicate<Entity> cne$skipOwnHitZones(Predicate<Entity> filter, @Local(argsOnly = true, ordinal = 0) Entity source, @Local(argsOnly = true, ordinal = 0) net.minecraft.world.phys.AABB area) {
		if (filter == null || source == null) return filter;
		// Hosts whose main hitbox is disabled: collect them so the body is skipped (only the zones
		// remain hittable). Scan the pick's OWN candidate box (covers the aim ray at any range),
		// not just a box around the viewer, so distant targets and projectiles work too.
		net.minecraft.world.phys.AABB scan = area != null ? area.inflate(2.0D) : source.getBoundingBox().inflate(8.0D);
		java.util.Set<java.util.UUID> mainOff = null;
		for (net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneHitZoneEntity zone : source.level().getEntitiesOfClass(net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneHitZoneEntity.class, scan)) {
			if (zone.isMainDisabled()) {
				java.util.UUID h = zone.hostUuid();
				if (h != null) { if (mainOff == null) mainOff = new java.util.HashSet<>(); mainOff.add(h); }
			}
		}
		final java.util.Set<java.util.UUID> off = mainOff;
		return filter.and(e -> !(e instanceof net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneHitZoneEntity zone && zone.isHostEntity(source)) && (off == null || !off.contains(e.getUUID())));
	}
}
