package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Invisible per-bone hit zone - a lightweight child entity riding a host mob's body
 * part (bone position computed each tick from the host's size/rotation/pose, so it
 * works on any model). Direct hits forward to the host x a multiplier; solid zones
 * also block movement. The host is SYNCED so the client can recognise its own zones
 * and skip them for the owner's collision and aim ray (otherwise a zone on the local
 * player smashes them down and blocks their crosshair). Explosions/AoE are ignored to
 * avoid double-counting. Zones reposition themselves and self-remove when the host is gone.
 * A zone can also act as a bone-following SEAT (see isSeat/rideTick): it rides the host and an
 * entity rides it, so seating flows through vanilla passenger mechanics end to end.
 */
public class CneHitZoneEntity extends Entity {
	private static final EntityDataAccessor<Float> WIDTH = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> SOLID = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.BOOLEAN);
	// PHYSICS = stand-on-able like a real block: the zone's box is added to the WORLD collision via
	// the collectColliders mixin (walk/jump on it), instead of the entity-collision "bump" SOLID gives.
	private static final EntityDataAccessor<Boolean> PHYSICS = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.BOOLEAN);
	// When set, the HOST's main body box is excluded from the pick ray (crosshair + projectiles),
	// so only the hit-zones are hittable. Synced so the client crosshair honours it too.
	private static final EntityDataAccessor<Boolean> MAIN_DISABLED = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> MULTIPLIER = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.FLOAT);
	// Synced so client-side movement/aim prediction can exclude the owner's own zones.
	private static final EntityDataAccessor<Optional<UUID>> HOST = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	// Synced only so the F3+B debug outline can colour each bone differently.
	private static final EntityDataAccessor<Integer> BONE_ID = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.INT);
	// When on, the zone copies the bone's orientation: the debug box tilts to match, and the
	// upright collision box expands to enclose that rotated extent (a real rotated hitbox is
	// impossible in MC - collision is always axis-aligned).
	private static final EntityDataAccessor<Boolean> COPY_ROT = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.BOOLEAN);
	// When on, arm/leg zones swing along the walk cycle (CneExtrasRuntime.limbSwingPitch),
	// affecting both the position arc and the box tilt - reproduced on both sides.
	private static final EntityDataAccessor<Boolean> LIMB_SWING = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.BOOLEAN);
	// Manual placement: instead of a named bone, the zone sits at a fixed local offset (right/up/forward,
	// in the host's body-yaw frame). For custom models whose bones the named presets can't match. Synced
	// so the client positions it identically.
	private static final EntityDataAccessor<Boolean> MANUAL = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> OFFSET_X = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> OFFSET_Y = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> OFFSET_Z = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.FLOAT);
	// Bone-tracking (experimental): follow the host's ACTUAL animated model bone, fed from the client via
	// CneBoneSyncRuntime. TRACK_BONE is the raw Blockbench bone/folder name; synced so the client knows
	// which bone to sample and which zone to position.
	private static final EntityDataAccessor<Boolean> BONE_TRACKED = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<String> TRACK_BONE = SynchedEntityData.defineId(CneHitZoneEntity.class, EntityDataSerializers.STRING);

	// Which bone to follow. The zone re-positions on BOTH sides so it tracks the host
	// smoothly - on the client the host is the predicted entity, with no server-sync lag.
	private String bone = "chest";
	private int missingHostTicks;
	// Server-only: how long a seat zone has been riding the host with nobody on it, so an
	// abandoned seat steps back out of the host's passenger list (transient, like missingHostTicks).
	private int emptySeatTicks;
	private Entity cachedHost;
	// Server-only: game time of the last real forwarded hit, for the "damaged" trigger (transient).
	private long lastHurtGameTime = Long.MIN_VALUE;
	// Server-only: the entity that dealt that last hit (attacker, or the projectile if unowned).
	private Entity lastAttacker;
	// Captured bone orientation for bone-tracked zones, rebuilt each tick from CneBoneSyncRuntime on
	// whichever side has the pose (client via putLocal, server via the payload) - NOT synced, both sides
	// reconstruct it from the same POSES channel the tracked position uses. Identity when not tracked/stale.
	private final org.joml.Quaternionf trackedRot = new org.joml.Quaternionf();
	private boolean hasTrackedRot;

	public String boneName() {
		return this.bone;
	}

	/** True if this zone forwarded a real hit within the last {@code ticks} game-ticks (server-side). */
	public boolean wasHurtSince(long ticks) {
		return this.lastHurtGameTime != Long.MIN_VALUE
			&& (this.level().getGameTime() - this.lastHurtGameTime) <= Math.max(0L, ticks);
	}

	/** The entity that last damaged this zone (the attacker, or the projectile if unowned), or null. */
	public Entity lastAttacker() {
		return this.lastAttacker;
	}

	public int boneColorId() {
		return this.entityData.get(BONE_ID);
	}

	public boolean isCopyRotation() {
		return this.entityData.get(COPY_ROT);
	}

	public boolean isLimbSwing() {
		return this.entityData.get(LIMB_SWING);
	}

	/** True when the box should render/collide rotated (copy-rotation or limb-swing). */
	public boolean hasRotation() {
		return isCopyRotation() || isLimbSwing();
	}

	/** Captured bone orientation for the renderer; null when not bone-tracked or no fresh pose. */
	public org.joml.Quaternionf trackedRotation() {
		return this.hasTrackedRot ? this.trackedRot : null;
	}

	private static int boneToId(String bone) {
		// Solid helper zones (make-entity-solid) reuse the real bone spots via a "_solid" suffix.
		if (bone.endsWith("_solid")) bone = bone.substring(0, bone.length() - 6);
		return switch (bone) {
			case "head" -> 0;
			case "chest", "body", "torso" -> 1;
			case "main_hand", "right_hand", "hand", "arm" -> 2;
			case "off_hand", "left_hand" -> 3;
			case "right_leg" -> 4;
			case "left_leg" -> 5;
			case "feet", "foot", "ground" -> 6;
			default -> 7;
		};
	}

	public CneHitZoneEntity(EntityType<? extends CneHitZoneEntity> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.setNoGravity(true);
		this.setInvisible(true);
	}

	public void configure(Entity hostEntity, String boneName, float width, float height, boolean solid, float multiplier, boolean copyRotation, boolean limbSwing, boolean physics) {
		this.entityData.set(HOST, hostEntity == null ? Optional.empty() : Optional.of(hostEntity.getUUID()));
		this.bone = boneName == null || boneName.isBlank() ? "chest" : boneName.trim().toLowerCase(Locale.ROOT);
		this.entityData.set(BONE_ID, boneToId(this.bone));
		this.entityData.set(WIDTH, clampSize(width));
		this.entityData.set(HEIGHT, clampSize(height));
		this.entityData.set(SOLID, solid);
		this.entityData.set(MULTIPLIER, clampMultiplier(multiplier));
		this.entityData.set(COPY_ROT, copyRotation);
		this.entityData.set(LIMB_SWING, limbSwing);
		this.entityData.set(PHYSICS, physics);
		this.refreshDimensions();
		if (hostEntity != null) {
			applyBoneRotation(hostEntity);
			reposition(hostEntity);
		}
	}

	/** Manual placement: a fixed local offset (right/up/forward, body-yaw-relative) instead of a named bone. */
	public void configureManual(Entity hostEntity, double dx, double dy, double dz, float width, float height, boolean solid, float multiplier, boolean physics) {
		this.entityData.set(HOST, hostEntity == null ? Optional.empty() : Optional.of(hostEntity.getUUID()));
		this.bone = "manual";
		this.entityData.set(BONE_ID, 7);
		this.entityData.set(MANUAL, true);
		this.entityData.set(OFFSET_X, (float) dx);
		this.entityData.set(OFFSET_Y, (float) dy);
		this.entityData.set(OFFSET_Z, (float) dz);
		this.entityData.set(WIDTH, clampSize(width));
		this.entityData.set(HEIGHT, clampSize(height));
		this.entityData.set(SOLID, solid);
		this.entityData.set(MULTIPLIER, clampMultiplier(multiplier));
		this.entityData.set(COPY_ROT, false);
		this.entityData.set(LIMB_SWING, false);
		this.entityData.set(PHYSICS, physics);
		this.refreshDimensions();
		if (hostEntity != null) {
			applyBoneRotation(hostEntity);
			reposition(hostEntity);
		}
	}

	/** True if this is a manual zone sitting at (about) the given offset - used to avoid piling duplicates. */
	public boolean isManualAt(double dx, double dy, double dz) {
		return this.entityData.get(MANUAL)
			&& Math.abs(this.entityData.get(OFFSET_X) - dx) < 0.01D
			&& Math.abs(this.entityData.get(OFFSET_Y) - dy) < 0.01D
			&& Math.abs(this.entityData.get(OFFSET_Z) - dz) < 0.01D;
	}

	/** Track the host's ACTUAL animated model bone (raw Blockbench name, case-sensitive), fed by the client. */
	public void configureTracked(Entity hostEntity, String boneName, float width, float height, boolean solid, float multiplier, boolean copyRotation, boolean physics) {
		this.entityData.set(HOST, hostEntity == null ? Optional.empty() : Optional.of(hostEntity.getUUID()));
		this.bone = boneName == null || boneName.isBlank() ? "head" : boneName.trim();
		this.entityData.set(BONE_TRACKED, true);
		this.entityData.set(TRACK_BONE, this.bone);
		this.entityData.set(MANUAL, false);
		this.entityData.set(BONE_ID, 7);
		this.entityData.set(WIDTH, clampSize(width));
		this.entityData.set(HEIGHT, clampSize(height));
		this.entityData.set(SOLID, solid);
		this.entityData.set(MULTIPLIER, clampMultiplier(multiplier));
		this.entityData.set(COPY_ROT, copyRotation);
		this.entityData.set(LIMB_SWING, false);
		this.entityData.set(PHYSICS, physics);
		this.refreshDimensions();
		if (hostEntity != null) {
			applyBoneRotation(hostEntity);
			reposition(hostEntity);
		}
	}

	public boolean isBoneTracked() {
		return this.entityData.get(BONE_TRACKED);
	}

	public String trackedBoneName() {
		return this.entityData.get(TRACK_BONE);
	}

	private void reposition(Entity hostEntity) {
		Vec3 pos = null;
		if (this.entityData.get(BONE_TRACKED)) {
			double[] p = CneBoneSyncRuntime.pose(hostEntity.getId(), this.entityData.get(TRACK_BONE), this.level().getGameTime());
			// p[0..2] is a LOCAL offset in the host body-yaw frame; resolve it through the SAME offsetPosition
			// transform so the zone stays attached to the host (translates AND rotates with him) even while
			// unobserved, and never freezes at an absolute world point in the air.
			if (p != null) pos = CneExtrasRuntime.offsetPosition(hostEntity, p[0], p[1], p[2]);
			// else: pose expired (host not rendered for a few ticks) -> host-relative fallback below.
		}
		if (pos == null) {
			if (this.entityData.get(MANUAL)) {
				pos = CneExtrasRuntime.offsetPosition(hostEntity, this.entityData.get(OFFSET_X), this.entityData.get(OFFSET_Y), this.entityData.get(OFFSET_Z));
			} else {
				// Use the SYNCED bone id - the bone string field is server-only, so on the client it
				// was always the default and every zone collapsed onto the host's centre.
				pos = CneExtrasRuntime.bonePosition(hostEntity, idToBone(this.entityData.get(BONE_ID)), isLimbSwing());
			}
		}
		this.setPos(pos.x, pos.y - this.entityData.get(HEIGHT) / 2.0D, pos.z);
	}

	private static String idToBone(int id) {
		return switch (id) {
			case 0 -> "head";
			case 1 -> "chest";
			case 2 -> "main_hand";
			case 3 -> "off_hand";
			case 4 -> "right_leg";
			case 5 -> "left_leg";
			case 6 -> "feet";
			default -> "fullbody"; // id 7 = whole-body / unknown -> entity centre
		};
	}

	private UUID hostId() {
		return this.entityData.get(HOST).orElse(null);
	}

	private static float clampSize(float value) {
		if (!Float.isFinite(value) || value <= 0.0F) return 1.0F;
		return Math.max(0.05F, Math.min(64.0F, value));
	}

	private static float clampMultiplier(float value) {
		return Float.isFinite(value) ? Math.max(0.0F, Math.min(100.0F, value)) : 1.0F;
	}

	/** True if the given entity is this zone's host - used (on both sides) to skip the owner's own zones for collision and aim. */
	public boolean isHostEntity(Entity entity) {
		UUID id = hostId();
		return entity != null && id != null && id.equals(entity.getUUID());
	}

	/**
	 * True while this zone acts as a bone-following SEAT: it rides its own host so a rider on the
	 * zone counts through vanilla passenger mechanics (host.getPassengers() contains the zone,
	 * host.getIndirectPassengers() contains the rider, rider.getRootVehicle() == host, and
	 * dismount/death ejection are all vanilla - discarding the zone ejects the rider too).
	 * Seat state is DERIVED from the riding relationship instead of a stored flag: riding the host
	 * is the only reason a zone ever rides anything, a derived flag can never go stale, and it
	 * persists for free - vanilla saves the passenger chain on the ROOT vehicle (the host), so a
	 * separate NBT flag on the zone would only duplicate what the chain already encodes.
	 */
	public boolean isSeat() {
		return this.isPassenger() && isHostEntity(this.getVehicle());
	}

	@Override
	public void tick() {
		// Carry the previous position so the renderer interpolates smoothly between ticks
		// (set both old-position pairs since different subsystems read different ones).
		this.xo = this.xOld = this.getX();
		this.yo = this.yOld = this.getY();
		this.zo = this.zOld = this.getZ();
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
		UUID id = hostId();
		if (id == null) {
			if (!this.level().isClientSide) this.discard();
			return;
		}
		Entity host = resolveHost(id);
		if (host == null || !host.isAlive()) {
			// Only the server removes the zone; the client just stops following until the
			// host is back (e.g. briefly out of tracking range).
			if (!this.level().isClientSide && ++this.missingHostTicks > 40) this.discard();
			return;
		}
		this.missingHostTicks = 0;
		applyBoneRotation(host);
		Vec3 prevPos = this.position();
		reposition(host);
		// Carry riders standing on a solid/stand-on zone as the host moves (skip the host + its mount).
		if (canBeCollidedWith() || isPhysics()) CneExtrasRuntime.carryRiders(this, prevPos, 0.0D, host);
	}

	@Override
	public void rideTick() {
		// While a zone is a passenger (seat mode), vanilla runs tick() and THEN snaps this entity to
		// the host's passenger attachment point, which would pin every seat at the vanilla seat spot
		// and undo the bone follow that tick() just did. Let vanilla run, then re-run the zone's own
		// reposition so the zone (and therefore the rider seated on it) ends every tick on the bone.
		// This runs on BOTH sides - passengers tick through rideTick instead of the normal entity
		// tick, so this IS the client-side follow while seated, not a second one fighting it.
		super.rideTick();
		Entity host = this.getVehicle();
		if (host == null || !isHostEntity(host)) return; // not our seat chain: leave vanilla's placement alone
		if (!this.level().isClientSide) {
			// Abandoned seat: give the mount packets a couple of ticks to land (mirrors CneSeatEntity's
			// grace), then step out of the host's passenger list so an empty seat does not clutter
			// host.getPassengers() forever. The zone itself stays and keeps following its bone.
			if (this.isVehicle()) this.emptySeatTicks = 0;
			else if (++this.emptySeatTicks > 5) {
				this.emptySeatTicks = 0;
				this.stopRiding();
				return;
			}
		}
		reposition(host);
	}

	@Override
	protected void positionRider(Entity passenger, Entity.MoveFunction move) {
		// Seat the rider ON TOP of the zone box - simple and vanilla-flavored (vanilla's default
		// attachment would bury the rider inside the box). Uses the nominal synced height rather
		// than the live bounding box, whose rotation-enclosing expansion would make the rider bob
		// as the bone tilts. Riding a zone only ever happens in seat mode, so no mode check needed.
		move.accept(passenger, this.getX(), this.getY() + (double) this.entityData.get(HEIGHT), this.getZ());
	}

	private void applyBoneRotation(Entity host) {
		// Bone-tracked zones rotate by the REAL captured bone orientation (the quaternion from POSES,
		// available on both sides). Euler yaw/pitch is zeroed so makeBoundingBox + the renderer use the
		// quaternion path instead. No fresh pose -> fall through to the Euler fallback, like position does.
		if (this.entityData.get(BONE_TRACKED) && isCopyRotation()) {
			double[] p = CneBoneSyncRuntime.pose(host.getId(), this.entityData.get(TRACK_BONE), this.level().getGameTime());
			if (p != null && p.length >= 8) {
				this.trackedRot.set((float) p[3], (float) p[4], (float) p[5], (float) p[6]);
				this.hasTrackedRot = true;
				this.setYRot(0.0F);
				this.setXRot(0.0F);
				return;
			}
		}
		this.hasTrackedRot = false;
		boolean copyRot = isCopyRotation();
		boolean limbSwing = isLimbSwing();
		if (!copyRot && !limbSwing) {
			this.setYRot(0.0F);
			this.setXRot(0.0F);
			return;
		}
		int bone = this.entityData.get(BONE_ID);
		float bodyYaw = host instanceof LivingEntity living ? living.yBodyRot : host.getYRot();
		float yaw;
		float pitch;
		if (copyRot && bone == 0) {
			// Head copies where it looks (yaw + pitch).
			yaw = host.getYHeadRot();
			pitch = host.getXRot();
		} else {
			// Everything else faces the body; no look-pitch on arms.
			yaw = bodyYaw;
			pitch = 0.0F;
		}
		if (limbSwing) {
			// Arm/leg zones tilt with the walk-cycle swing. Negated so the box leans the SAME
			// way as the position arc: Axis.XP +pitch leans the box TOP forward, but the arc
			// swings the bottom/hand forward - this keeps the F3+B outline coherent with the
			// hit area. (Collision AABB is sign-symmetric, so gameplay is unaffected either way.)
			pitch -= (float) Math.toDegrees(CneExtrasRuntime.limbSwingPitch(host, idToBone(bone)));
		}
		this.setYRot(yaw);
		this.setXRot(pitch);
	}

	private Entity resolveHost(UUID id) {
		Entity cached = this.cachedHost;
		if (cached != null && cached.isAlive() && id.equals(cached.getUUID())) return cached;
		this.cachedHost = null;
		// The zone rides the host's bone, so the host is always close by; find it once and
		// cache it (works on the client too, where the player is the predicted instance).
		for (Entity e : this.level().getEntities(this, this.getBoundingBox().inflate(12.0D), e -> id.equals(e.getUUID()))) {
			this.cachedHost = e;
			break;
		}
		return this.cachedHost;
	}

	@Override
	public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
		// Ignore the server's position stream on the client. The zone's position is computed
		// locally each tick from the host (see tick), so applying the lagged server position
		// on top of that is exactly what caused the jitter/teleport while the host moved.
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.scalable(this.entityData.get(WIDTH), this.entityData.get(HEIGHT));
	}

	@Override
	protected AABB makeBoundingBox() {
		AABB box = super.makeBoundingBox();
		if (!hasRotation()) return box;
		// Collision can't rotate, so expand the upright box to ENCLOSE the box rotated by the
		// bone's yaw/pitch about its centre - the hit area then covers the tilted extent.
		double hw = (box.maxX - box.minX) / 2.0D;
		double hh = (box.maxY - box.minY) / 2.0D;
		double hd = (box.maxZ - box.minZ) / 2.0D;
		double cx = (box.minX + box.maxX) / 2.0D;
		double cy = (box.minY + box.maxY) / 2.0D;
		double cz = (box.minZ + box.maxZ) / 2.0D;
		if (this.hasTrackedRot) {
			// Bone-tracked: enclose the local box rotated by the captured bone quaternion. Rotate all 8
			// corners, take the symmetric extent. MC collision is axis-aligned, so an enclosing AABB is
			// the only option (the box can't truly tilt for collision, only visually in the renderer).
			double ex = 0.0D, ey = 0.0D, ez = 0.0D;
			for (int i = -1; i <= 1; i += 2) {
				for (int j = -1; j <= 1; j += 2) {
					for (int k = -1; k <= 1; k += 2) {
						org.joml.Vector3f v = this.trackedRot.transform(new org.joml.Vector3f((float) (i * hw), (float) (j * hh), (float) (k * hd)));
						ex = Math.max(ex, Math.abs(v.x));
						ey = Math.max(ey, Math.abs(v.y));
						ez = Math.max(ez, Math.abs(v.z));
					}
				}
			}
			return new AABB(cx - ex, cy - ey, cz - ez, cx + ex, cy + ey, cz + ez);
		}
		double yaw = Math.toRadians(this.getYRot());
		double pitch = Math.toRadians(this.getXRot());
		double cosY = Math.cos(yaw), sinY = Math.sin(yaw);
		double cosP = Math.cos(pitch), sinP = Math.sin(pitch);
		double ex = 0.0D, ey = 0.0D, ez = 0.0D;
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				for (int k = -1; k <= 1; k += 2) {
					double lx = i * hw, ly = j * hh, lz = k * hd;
					double y1 = ly * cosP - lz * sinP;
					double z1 = ly * sinP + lz * cosP;
					double wx = lx * cosY + z1 * sinY;
					double wz = -lx * sinY + z1 * cosY;
					ex = Math.max(ex, Math.abs(wx));
					ey = Math.max(ey, Math.abs(y1));
					ez = Math.max(ez, Math.abs(wz));
				}
			}
		}
		return new AABB(cx - ex, cy - ey, cz - ez, cx + ex, cy + ey, cz + ez);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (WIDTH.equals(key) || HEIGHT.equals(key)) this.refreshDimensions();
	}

	@Override
	public void onSyncedDataUpdated(java.util.List<SynchedEntityData.DataValue<?>> values) {
		// The client applies the spawn data in ONE batch through this list overload (the
		// single-key overload above only fires for later changes), so the size set on the
		// server never reached the client box without refreshing here.
		super.onSyncedDataUpdated(values);
		this.refreshDimensions();
	}

	@Override
	public boolean isPickable() {
		return this.entityData.get(MULTIPLIER) > 0.0F;
	}

	public boolean isPhysics() {
		return this.entityData.get(PHYSICS);
	}

	public boolean isMainDisabled() {
		return this.entityData.get(MAIN_DISABLED);
	}

	public void setMainDisabled(boolean disabled) {
		this.entityData.set(MAIN_DISABLED, disabled);
	}

	public UUID hostUuid() {
		return this.entityData.get(HOST).orElse(null);
	}

	@Override
	public boolean canBeCollidedWith() {
		// Physics zones use WORLD collision (collectColliders mixin = stand-on-able), not the
		// entity-collision bump, so they must NOT report collidable here (avoids double collision).
		return this.entityData.get(SOLID) && !this.entityData.get(PHYSICS);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!(this.level() instanceof ServerLevel serverLevel)) return false;
		if (source.is(DamageTypeTags.IS_EXPLOSION) || source.getDirectEntity() == null) return false;
		UUID id = hostId();
		if (id == null) return false;
		Entity hostEntity = serverLevel.getEntity(id);
		if (hostEntity == null) return false;
		this.lastHurtGameTime = this.level().getGameTime();
		this.lastAttacker = source.getEntity() != null ? source.getEntity() : source.getDirectEntity();
		return hostEntity.hurt(source, amount * this.entityData.get(MULTIPLIER));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(WIDTH, 1.0F);
		builder.define(HEIGHT, 1.0F);
		builder.define(SOLID, false);
		builder.define(MULTIPLIER, 1.0F);
		builder.define(HOST, Optional.empty());
		builder.define(BONE_ID, 7);
		builder.define(COPY_ROT, false);
		builder.define(LIMB_SWING, false);
		builder.define(PHYSICS, false);
		builder.define(MAIN_DISABLED, false);
		builder.define(MANUAL, false);
		builder.define(OFFSET_X, 0.0F);
		builder.define(OFFSET_Y, 0.0F);
		builder.define(OFFSET_Z, 0.0F);
		builder.define(BONE_TRACKED, false);
		builder.define(TRACK_BONE, "");
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.entityData.set(WIDTH, clampSize(tag.getFloat("Width")));
		this.entityData.set(HEIGHT, clampSize(tag.getFloat("Height")));
		this.entityData.set(SOLID, tag.getBoolean("Solid"));
		this.entityData.set(MULTIPLIER, tag.contains("Mult") ? clampMultiplier(tag.getFloat("Mult")) : 1.0F);
		this.bone = tag.contains("Bone") ? tag.getString("Bone") : "chest";
		this.entityData.set(BONE_ID, boneToId(this.bone));
		this.entityData.set(COPY_ROT, tag.getBoolean("CopyRot"));
		this.entityData.set(LIMB_SWING, tag.getBoolean("LimbSwing"));
		this.entityData.set(PHYSICS, tag.getBoolean("Physics"));
		this.entityData.set(MAIN_DISABLED, tag.getBoolean("MainOff"));
		this.entityData.set(MANUAL, tag.getBoolean("Manual"));
		this.entityData.set(OFFSET_X, tag.getFloat("OX"));
		this.entityData.set(OFFSET_Y, tag.getFloat("OY"));
		this.entityData.set(OFFSET_Z, tag.getFloat("OZ"));
		this.entityData.set(BONE_TRACKED, tag.getBoolean("Tracked"));
		this.entityData.set(TRACK_BONE, tag.getString("TrackBone"));
		this.entityData.set(HOST, tag.hasUUID("Host") ? Optional.of(tag.getUUID("Host")) : Optional.empty());
		this.refreshDimensions();
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putFloat("Width", this.entityData.get(WIDTH));
		tag.putFloat("Height", this.entityData.get(HEIGHT));
		tag.putBoolean("Solid", this.entityData.get(SOLID));
		tag.putFloat("Mult", this.entityData.get(MULTIPLIER));
		tag.putString("Bone", this.bone);
		tag.putBoolean("CopyRot", this.entityData.get(COPY_ROT));
		tag.putBoolean("LimbSwing", this.entityData.get(LIMB_SWING));
		tag.putBoolean("Physics", this.entityData.get(PHYSICS));
		tag.putBoolean("MainOff", this.entityData.get(MAIN_DISABLED));
		tag.putBoolean("Manual", this.entityData.get(MANUAL));
		tag.putFloat("OX", this.entityData.get(OFFSET_X));
		tag.putFloat("OY", this.entityData.get(OFFSET_Y));
		tag.putFloat("OZ", this.entityData.get(OFFSET_Z));
		tag.putBoolean("Tracked", this.entityData.get(BONE_TRACKED));
		tag.putString("TrackBone", this.entityData.get(TRACK_BONE));
		this.entityData.get(HOST).ifPresent(uuid -> tag.putUUID("Host", uuid));
	}
}
