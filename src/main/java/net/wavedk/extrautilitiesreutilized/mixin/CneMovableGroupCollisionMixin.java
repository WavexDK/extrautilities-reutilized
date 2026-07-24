package net.wavedk.extrautilitiesreutilized.mixin;

import java.util.ArrayList;
import java.util.List;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Makes movable block GROUPS collide PER BLOCK and behave like real ground (walk/jump),
 * by appending each nearby group's per-block shape to collectColliders' result - the
 * very list the movement code resolves against, alongside the world's block shapes. The
 * group is canBeCollidedWith()=false so it never contributes its whole bounding box.
 */
@Mixin(Entity.class)
public abstract class CneMovableGroupCollisionMixin {
	@ModifyReturnValue(method = "collectColliders", at = @At("RETURN"), require = 0)
	private static List<VoxelShape> cne$addGroupColliders(List<VoxelShape> original, @Local(argsOnly = true) Entity collider, @Local(argsOnly = true) Level level, @Local(argsOnly = true) AABB area) {
		if (level == null || area == null) return original;
		List<VoxelShape> result = null;
		for (net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneMovableBlockGroupEntity group : level.getEntitiesOfClass(net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneMovableBlockGroupEntity.class, area)) {
			if (group == collider) continue;
			VoxelShape shape = group.collisionShape();
			if (shape != null && !shape.isEmpty()) {
				if (result == null) result = new ArrayList<>(original);
				result.add(shape);
			}
		}
		// Stand-on-able (physics) hit-zones contribute their box to the world collision too,
		// excluding the host AND anything sharing its riding stack (so a mount/rider isn't
		// shoved by its own/its rider's zones, which canBeCollidedWith=false leaves unguarded).
		// collider can be null here (e.g. a block-break particle's collision check), so guard
		// before touching its riding stack - a null collider has no host/stack to exclude.
		Entity colliderRoot = collider == null ? null : collider.getRootVehicle();
		for (net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneHitZoneEntity zone : level.getEntitiesOfClass(net.wavedk.extrautilitiesreutilized.chickennuggetextras.CneHitZoneEntity.class, area)) {
			if (!zone.isPhysics()) continue;
			if (collider != null && (zone.isHostEntity(collider) || zone.isHostEntity(colliderRoot))) continue;
			if (colliderRoot != null) {
				boolean shared = false;
				for (Entity pass : colliderRoot.getIndirectPassengers()) {
					if (zone.isHostEntity(pass)) { shared = true; break; }
				}
				if (shared) continue;
			}
			if (result == null) result = new ArrayList<>(original);
			result.add(net.minecraft.world.phys.shapes.Shapes.create(zone.getBoundingBox()));
		}
		return result == null ? original : result;
	}
}
