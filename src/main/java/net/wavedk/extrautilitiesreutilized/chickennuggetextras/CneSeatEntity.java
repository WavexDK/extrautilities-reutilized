package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * A tiny invisible seat the rider sits on so they "sit" on a block. Self-removes the moment
 * it has no passenger (dismount). Where it sits is set by where it's spawned.
 */
public class CneSeatEntity extends Entity {
	public CneSeatEntity(EntityType<? extends CneSeatEntity> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.setNoGravity(true);
		this.setInvisible(true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	public void tick() {
		super.tick();
		// A couple of ticks of grace so the rider's mount registers, then vanish once empty.
		if (!this.level().isClientSide && this.tickCount > 2 && !this.isVehicle()) this.discard();
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dim, float partial) {
		return Vec3.ZERO; // the rider sits exactly where the seat sits
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.fixed(0.05F, 0.05F);
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
	}
}
