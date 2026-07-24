package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * A block in movable form: an entity that renders a BlockState and is fully
 * script-controlled. By default it is KINEMATIC (no gravity, passes through the
 * world, moves exactly by the velocity / position / rotation you set). Turning
 * gravity on makes it fall and collide with terrain like a falling block. When
 * solid it is also a hard collider AND hittable, so it works as a floating
 * platform. Optional despawn timer; otherwise it persists (saved + reloaded).
 */
public class CneMovableBlockEntity extends Entity {
	private static final EntityDataAccessor<BlockState> DATA_BLOCK = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.BLOCK_STATE);
	private static final EntityDataAccessor<Float> DATA_ROT_X = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ROT_Y = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ROT_Z = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> DATA_SOLID = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.BOOLEAN);
	// Angular velocity in degrees per TICK on each axis; synced so the renderer can extrapolate
	// a smooth spin between ticks (it would otherwise step at the 20Hz rotation update).
	private static final EntityDataAccessor<Float> DATA_AV_X = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_AV_Y = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_AV_Z = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.FLOAT);
	// When breakable, hitting it destroys it and drops the block. When placeable, placing a block
	// against it grows it into a movable group (handled in CneExtrasRuntime).
	private static final EntityDataAccessor<Boolean> DATA_BREAKABLE = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_PLACEABLE = SynchedEntityData.defineId(CneMovableBlockEntity.class, EntityDataSerializers.BOOLEAN);

	// Server-only behaviour (saved to NBT; the client just renders the synced state/pose).
	private boolean gravityEnabled;
	private int despawnTicks; // 0 = never
	// Break-time mining state (server-only, one miner at a time, transient).
	private UUID miningAttacker;
	private long miningStart;
	private long lastAttackTick;

	public CneMovableBlockEntity(EntityType<? extends CneMovableBlockEntity> type, Level level) {
		super(type, level);
		this.noPhysics = true; // kinematic by default
	}

	public void configure(BlockState state, BlockPos pos) {
		this.setBlockState(state == null ? Blocks.AIR.defaultBlockState() : state);
		// Entity origin sits at the block's centre-bottom so the 1x1x1 box and the render
		// line up exactly with the block cell the coordinates point at.
		this.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(DATA_BLOCK, Blocks.AIR.defaultBlockState());
		builder.define(DATA_ROT_X, 0.0F);
		builder.define(DATA_ROT_Y, 0.0F);
		builder.define(DATA_ROT_Z, 0.0F);
		builder.define(DATA_SOLID, true);
		builder.define(DATA_AV_X, 0.0F);
		builder.define(DATA_AV_Y, 0.0F);
		builder.define(DATA_AV_Z, 0.0F);
		builder.define(DATA_BREAKABLE, false);
		builder.define(DATA_PLACEABLE, false);
	}

	public BlockState getBlockState() {
		return this.entityData.get(DATA_BLOCK);
	}

	public void setBlockState(BlockState state) {
		this.entityData.set(DATA_BLOCK, state == null ? Blocks.AIR.defaultBlockState() : state);
	}

	public float getRotX() {
		return this.entityData.get(DATA_ROT_X);
	}

	public float getRotY() {
		return this.entityData.get(DATA_ROT_Y);
	}

	public float getRotZ() {
		return this.entityData.get(DATA_ROT_Z);
	}

	public void setRotation(float x, float y, float z) {
		this.entityData.set(DATA_ROT_X, Float.isFinite(x) ? x : 0.0F);
		this.entityData.set(DATA_ROT_Y, Float.isFinite(y) ? y : 0.0F);
		this.entityData.set(DATA_ROT_Z, Float.isFinite(z) ? z : 0.0F);
	}

	public float getAvX() {
		return this.entityData.get(DATA_AV_X);
	}

	public float getAvY() {
		return this.entityData.get(DATA_AV_Y);
	}

	public float getAvZ() {
		return this.entityData.get(DATA_AV_Z);
	}

	/** Spin speed in degrees per SECOND on each axis; stored as degrees per tick. */
	public void setAngularVelocity(float degPerSecX, float degPerSecY, float degPerSecZ) {
		this.entityData.set(DATA_AV_X, Float.isFinite(degPerSecX) ? degPerSecX / 20.0F : 0.0F);
		this.entityData.set(DATA_AV_Y, Float.isFinite(degPerSecY) ? degPerSecY / 20.0F : 0.0F);
		this.entityData.set(DATA_AV_Z, Float.isFinite(degPerSecZ) ? degPerSecZ / 20.0F : 0.0F);
	}

	public boolean isSolid() {
		return this.entityData.get(DATA_SOLID);
	}

	public void setSolid(boolean solid) {
		this.entityData.set(DATA_SOLID, solid);
	}

	public void setGravityEnabled(boolean on) {
		this.gravityEnabled = on;
		this.noPhysics = !on; // kinematic passes through terrain; falling collides with it
	}

	public boolean isGravityEnabled() {
		return this.gravityEnabled;
	}

	public boolean isBreakable() {
		return this.entityData.get(DATA_BREAKABLE);
	}

	public void setBreakable(boolean breakable) {
		this.entityData.set(DATA_BREAKABLE, breakable);
	}

	public boolean isPlaceable() {
		return this.entityData.get(DATA_PLACEABLE);
	}

	public void setPlaceable(boolean placeable) {
		this.entityData.set(DATA_PLACEABLE, placeable);
	}

	public void setDespawnTicks(int ticks) {
		this.despawnTicks = Math.max(0, ticks);
	}

	public String getBlockId() {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
		return id == null ? "minecraft:air" : id.toString();
	}

	/** Stamps the block back into the world where the entity currently sits, then removes the entity. */
	public void placeIntoWorld() {
		if (!(this.level() instanceof ServerLevel level)) return;
		BlockState state = getBlockState();
		// Stamp into the cell the block VISUALLY fills. The origin is centre-bottom, so X/Z
		// floor correctly but Y is bottom-based; bias it by +0.5 so a place-back mid-fall
		// (Y a hair under an integer) doesn't land the block one cell too low.
		if (!state.isAir()) level.setBlock(BlockPos.containing(this.getX(), this.getY() + 0.5D, this.getZ()), state, Block.UPDATE_ALL);
		this.discard();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide) return;
		if (this.despawnTicks > 0 && --this.despawnTicks <= 0) {
			this.discard();
			return;
		}
		float avx = getAvX(), avy = getAvY(), avz = getAvZ();
		if (avx != 0.0F || avy != 0.0F || avz != 0.0F) setRotation(getRotX() + avx, getRotY() + avy, getRotZ() + avz);
		if (this.gravityEnabled) {
			this.applyGravity();
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
			if (this.onGround()) this.setDeltaMovement(Vec3.ZERO);
		} else {
			// Kinematic: integrate velocity directly (noPhysics, passes through the world).
			Vec3 dm = this.getDeltaMovement();
			if (dm.lengthSqr() > 1.0E-9D) this.setPos(this.getX() + dm.x, this.getY() + dm.y, this.getZ() + dm.z);
		}
	}

	@Override
	protected double getDefaultGravity() {
		return 0.04D; // matches FallingBlockEntity
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.fixed(1.0F, 1.0F);
	}

	@Override
	public boolean isPickable() {
		return isSolid() || isBreakable() || isPlaceable(); // hittable/buildable even when not solid
	}

	@Override
	public boolean canBeCollidedWith() {
		return isSolid();
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false; // water/lava currents must not drift a placed movable block on their own
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.level().isClientSide || !isBreakable()) return false;
		Entity attacker = source.getEntity();
		if (attacker == null) return false;
		BlockState st = getBlockState();
		boolean creative = attacker instanceof Player p && p.getAbilities().instabuild;
		if (!creative) {
			long now = this.level().getGameTime();
			// One miner at a time: restart progress on a new attacker or if mining lapsed (no hit for ~2s).
			// Window must exceed the gap between held/spam attacks (a slow weapon swings ~every 22 ticks),
			// since entities aren't continuously mined like blocks - you re-attack, you don't hold.
			if (!attacker.getUUID().equals(this.miningAttacker) || now - this.lastAttackTick > 40L) {
				this.miningAttacker = attacker.getUUID();
				this.miningStart = now;
			}
			this.lastAttackTick = now;
			int breakTicks = CneExtrasRuntime.blockBreakTimeTicks(attacker, this.level(), st, BlockPos.containing(this.getX(), this.getY(), this.getZ()));
			if (now - this.miningStart < breakTicks) return true; // still mining; not broken yet
			if (!st.isAir()) Block.popResource(this.level(), BlockPos.containing(this.getX(), this.getY() + 0.5D, this.getZ()), new ItemStack(st.getBlock()));
		}
		this.discard();
		return true;
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 hitVec, InteractionHand hand) {
		if (!isPlaceable()) return InteractionResult.PASS;
		ItemStack stack = player.getItemInHand(hand);
		if (!(stack.getItem() instanceof BlockItem blockItem)) return InteractionResult.PASS;
		if (this.level().isClientSide) return InteractionResult.SUCCESS;
		// Find the clicked face in the block's LOCAL (unrotated) frame, then grow into a group.
		Vec3 eye = player.getEyePosition();
		Vec3 end = eye.add(player.getViewVector(1.0F).scale(6.0D));
		double px = this.getX(), py = this.getY() + 0.5D, pz = this.getZ(); // cell centre = rotation pivot
		Vec3 le = inverseRotateLocal(eye.x - px, eye.y - py, eye.z - pz);
		Vec3 ee = inverseRotateLocal(end.x - px, end.y - py, end.z - pz);
		java.util.Optional<Vec3> hit = new net.minecraft.world.phys.AABB(-0.5D, -0.5D, -0.5D, 0.5D, 0.5D, 0.5D).clip(le, ee);
		if (hit.isEmpty()) return InteractionResult.PASS;
		Vec3 h = hit.get();
		double eps = 1.0E-3;
		int nx = 0, ny = 0, nz = 0;
		if (Math.abs(h.x + 0.5D) < eps) nx = -1;
		else if (Math.abs(h.x - 0.5D) < eps) nx = 1;
		else if (Math.abs(h.y + 0.5D) < eps) ny = -1;
		else if (Math.abs(h.y - 0.5D) < eps) ny = 1;
		else if (Math.abs(h.z + 0.5D) < eps) nz = -1;
		else if (Math.abs(h.z - 0.5D) < eps) nz = 1;
		if (nx == 0 && ny == 0 && nz == 0) return InteractionResult.PASS;
		boolean grew = CneExtrasRuntime.growMovableBlockIntoGroup(this, nx, ny, nz, blockItem.getBlock().defaultBlockState());
		if (grew && !player.getAbilities().instabuild) stack.shrink(1);
		return grew ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	/** Inverse of the renderer's XP*YP*ZP rotation about the cell centre - maps a world-relative
	 *  point into the block's unrotated frame (used for the place-on-it face pick). */
	private Vec3 inverseRotateLocal(double x, double y, double z) {
		double rx = Math.toRadians(getRotX()), ry = Math.toRadians(getRotY()), rz = Math.toRadians(getRotZ());
		double cx = Math.cos(rx), sx = Math.sin(rx);
		double cy = Math.cos(ry), sy = Math.sin(ry);
		double cz = Math.cos(rz), sz = Math.sin(rz);
		double y1 = y * cx + z * sx, z1 = -y * sx + z * cx, x1 = x;
		double x2 = x1 * cy - z1 * sy, z2 = x1 * sy + z1 * cy, y2 = y1;
		return new Vec3(x2 * cz + y2 * sz, -x2 * sz + y2 * cz, z2);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("BlockState")) {
			this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("BlockState")));
		}
		this.setRotation(tag.getFloat("RotX"), tag.getFloat("RotY"), tag.getFloat("RotZ"));
		this.setSolid(!tag.contains("Solid") || tag.getBoolean("Solid"));
		this.despawnTicks = Math.max(0, tag.getInt("Despawn"));
		setGravityEnabled(tag.getBoolean("Gravity"));
		this.entityData.set(DATA_AV_X, tag.getFloat("AvX"));
		this.entityData.set(DATA_AV_Y, tag.getFloat("AvY"));
		this.entityData.set(DATA_AV_Z, tag.getFloat("AvZ"));
		this.entityData.set(DATA_BREAKABLE, tag.getBoolean("Breakable"));
		this.entityData.set(DATA_PLACEABLE, tag.getBoolean("Placeable"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.put("BlockState", NbtUtils.writeBlockState(getBlockState()));
		tag.putFloat("RotX", getRotX());
		tag.putFloat("RotY", getRotY());
		tag.putFloat("RotZ", getRotZ());
		tag.putBoolean("Solid", isSolid());
		tag.putInt("Despawn", this.despawnTicks);
		tag.putBoolean("Gravity", this.gravityEnabled);
		tag.putFloat("AvX", getAvX());
		tag.putFloat("AvY", getAvY());
		tag.putFloat("AvZ", getAvZ());
		tag.putBoolean("Breakable", isBreakable());
		tag.putBoolean("Placeable", isPlaceable());
	}
}
