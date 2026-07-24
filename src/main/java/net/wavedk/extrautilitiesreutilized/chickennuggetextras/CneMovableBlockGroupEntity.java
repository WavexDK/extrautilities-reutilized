package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A whole rig of blocks in movable form: ONE entity that holds many (offset, BlockState)
 * cells and moves/rotates them together about its origin (the rig centre = the pivot).
 * Built from an area selection. Same control surface as a single movable block - the
 * set-velocity/rotation/solid/gravity/despawn/teleport/place blocks dispatch to it too.
 *
 * The cell list is synced as one compact string ("ox,oy,oz,stateId;..."); block palette
 * ids are registry-synced so they're stable client<->server. Collision is the single AABB
 * ENCLOSING the rotated rig (like any entity) - exact for an unrotated solid platform, an
 * over-approximation once rotated or for a hollow shape. The visual AND place-back rotate
 * each cell about the pivot, so they always agree.
 */
public class CneMovableBlockGroupEntity extends Entity {
	private static final EntityDataAccessor<String> DATA_BLOCKS = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Float> DATA_ROT_X = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ROT_Y = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ROT_Z = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> DATA_SOLID = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.BOOLEAN);
	// Angular velocity in degrees per TICK on each axis; synced so the renderer can extrapolate
	// a smooth spin between ticks (it would otherwise step at the 20Hz rotation update).
	private static final EntityDataAccessor<Float> DATA_AV_X = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_AV_Y = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_AV_Z = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.FLOAT);
	// When breakable, hitting a block mines it off (drops it). When placeable, a block placed
	// against the rig joins it. Synced so the client knows the interaction state too.
	private static final EntityDataAccessor<Boolean> DATA_BREAKABLE = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_PLACEABLE = SynchedEntityData.defineId(CneMovableBlockGroupEntity.class, EntityDataSerializers.BOOLEAN);

	private static final int MAX_CELLS = 1024; // cap so the synced string + entity stay sane

	// Parsed rig, valid on both sides (server builds it, client parses DATA_BLOCKS). Offsets
	// are 0-based from the rig's min corner; dimX/Y/Z span it so the centre is dim/2.
	private final List<BlockPos> offsets = new ArrayList<>();
	private final List<BlockState> states = new ArrayList<>();
	private int dimX = 1, dimY = 1, dimZ = 1;
	private AABB localBounds = new AABB(-0.5D, -0.5D, -0.5D, 0.5D, 0.5D, 0.5D);
	// Per-block collision shape relative to the origin (so only real blocks are solid, not the
	// whole bounding box). Rebuilt only when the cells change; moved by position on demand.
	private VoxelShape localCollisionShape = Shapes.empty();

	// Server-only behaviour.
	private boolean gravityEnabled;
	private int despawnTicks; // 0 = never
	// Break-time mining state (server-only, one miner + one cell at a time, transient).
	private UUID miningAttacker;
	private int miningCell = -1;
	private long miningStart;
	private long lastAttackTick;
	// Per-cell container inventories (chests/barrels), keyed by cell offset. Server-only; saved to NBT.
	private final java.util.Map<BlockPos, net.minecraft.world.SimpleContainer> cellContainers = new java.util.HashMap<>();

	public CneMovableBlockGroupEntity(EntityType<? extends CneMovableBlockGroupEntity> type, Level level) {
		super(type, level);
		this.noPhysics = true; // kinematic by default
		// Mark invisible so vanilla's F3+B overlay skips the whole-rig bounding box (which looked
		// like a "giant hitbox"). The custom renderer still draws the blocks, and now also draws a
		// per-block outline under F3+B. Collision is unaffected (it's per-block via the mixin).
		this.setInvisible(true);
	}

	/** Build the rig from collected blocks. Offsets are 0-based from minCorner; the entity
	 *  origin is placed at the rig centre (minCorner + dim/2) so it rotates about it. */
	public void configure(BlockPos minCorner, List<BlockPos> cellOffsets, List<BlockState> cellStates) {
		this.offsets.clear();
		this.states.clear();
		int n = Math.min(cellOffsets.size(), cellStates.size());
		for (int i = 0; i < n && this.offsets.size() < MAX_CELLS; i++) {
			BlockState st = cellStates.get(i);
			if (st == null || st.isAir()) continue;
			this.offsets.add(cellOffsets.get(i));
			this.states.add(st);
		}
		recomputeDims();
		this.entityData.set(DATA_BLOCKS, encode());
		this.setPos(minCorner.getX() + this.dimX / 2.0D, minCorner.getY() + this.dimY / 2.0D, minCorner.getZ() + this.dimZ / 2.0D);
	}

	private void recomputeDims() {
		int mx = 1, my = 1, mz = 1;
		for (BlockPos o : this.offsets) {
			mx = Math.max(mx, o.getX() + 1);
			my = Math.max(my, o.getY() + 1);
			mz = Math.max(mz, o.getZ() + 1);
		}
		this.dimX = mx;
		this.dimY = my;
		this.dimZ = mz;
		recomputeBounds();
		recomputeShape();
	}

	private void recomputeShape() {
		if (this.offsets.isEmpty()) {
			this.localCollisionShape = Shapes.empty();
			return;
		}
		double hx = this.dimX / 2.0D, hy = this.dimY / 2.0D, hz = this.dimZ / 2.0D;
		VoxelShape shape = Shapes.empty();
		int n = Math.min(this.offsets.size(), this.states.size());
		for (int i = 0; i < n; i++) {
			if (this.states.get(i).isAir()) continue; // mined-out cells leave no collision
			BlockPos o = this.offsets.get(i);
			double bx = o.getX() - hx, by = o.getY() - hy, bz = o.getZ() - hz;
			shape = Shapes.or(shape, Shapes.box(bx, by, bz, bx + 1.0D, by + 1.0D, bz + 1.0D));
		}
		this.localCollisionShape = shape;
	}

	private String encode() {
		StringBuilder sb = new StringBuilder();
		// dims prefix (3 fields) - synced so add/remove keep the centre fixed across client<->server.
		sb.append(this.dimX).append(',').append(this.dimY).append(',').append(this.dimZ);
		for (int i = 0; i < this.offsets.size(); i++) {
			BlockPos o = this.offsets.get(i);
			sb.append(';').append(o.getX()).append(',').append(o.getY()).append(',').append(o.getZ()).append(',').append(Block.getId(this.states.get(i)));
		}
		return sb.toString();
	}

	private void decode(String data) {
		this.offsets.clear();
		this.states.clear();
		int pdx = -1, pdy = -1, pdz = -1;
		if (data != null && !data.isEmpty()) {
			for (String part : data.split(";")) {
				String[] f = part.split(",");
				if (f.length == 3) { // dims prefix
					try {
						pdx = Integer.parseInt(f[0]);
						pdy = Integer.parseInt(f[1]);
						pdz = Integer.parseInt(f[2]);
					} catch (NumberFormatException ignored) {
					}
					continue;
				}
				if (f.length != 4) continue;
				try {
					BlockState st = Block.stateById(Integer.parseInt(f[3]));
					if (st == null) continue; // keep AIR cells: mined-out slots that hold the rig's shape/centre fixed
					this.offsets.add(new BlockPos(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2])));
					this.states.add(st);
					if (this.offsets.size() >= MAX_CELLS) break;
				} catch (NumberFormatException ignored) {
				}
			}
		}
		if (pdx > 0 && pdy > 0 && pdz > 0) {
			// dims came from the string: trust them so add/remove don't recompute (and re-centre).
			this.dimX = pdx;
			this.dimY = pdy;
			this.dimZ = pdz;
			recomputeBounds();
			recomputeShape();
		} else {
			recomputeDims();
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (DATA_BLOCKS.equals(key)) decode(this.entityData.get(DATA_BLOCKS));
		else if (DATA_ROT_X.equals(key) || DATA_ROT_Y.equals(key) || DATA_ROT_Z.equals(key)) recomputeBounds();
	}

	@Override
	public void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> values) {
		// Spawn data arrives in ONE batch through this overload; decode the rig from it.
		super.onSyncedDataUpdated(values);
		decode(this.entityData.get(DATA_BLOCKS));
	}

	public List<BlockPos> cellOffsets() {
		return this.offsets;
	}

	public List<BlockState> cellStates() {
		return this.states;
	}

	public int dimX() {
		return this.dimX;
	}

	public int dimY() {
		return this.dimY;
	}

	public int dimZ() {
		return this.dimZ;
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
		recomputeBounds();
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
		this.noPhysics = !on;
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
		for (BlockState s : this.states) {
			if (!s.isAir()) {
				ResourceLocation id = BuiltInRegistries.BLOCK.getKey(s.getBlock());
				return id == null ? "minecraft:air" : id.toString();
			}
		}
		return "minecraft:air";
	}

	/** True if this block keeps an inventory (chest, barrel, ...) - it has a Container block-entity. */
	private static boolean isContainerState(BlockState state) {
		if (state == null || !state.hasBlockEntity() || !(state.getBlock() instanceof net.minecraft.world.level.block.EntityBlock eb)) return false;
		net.minecraft.world.level.block.entity.BlockEntity be = eb.newBlockEntity(BlockPos.ZERO, state);
		return be instanceof net.minecraft.world.Container;
	}

	/** Copy a world container's contents into this cell's inventory (used when converting existing
	 *  chests into the rig so the items move in instead of dropping). Keyed by the cell offset. */
	public void captureCellContainer(BlockPos offset, net.minecraft.world.Container source) {
		if (source == null || source.isEmpty()) return;
		net.minecraft.world.SimpleContainer cont = new net.minecraft.world.SimpleContainer(27);
		int slots = Math.min(source.getContainerSize(), 27);
		for (int i = 0; i < slots; i++) cont.setItem(i, source.getItem(i).copy());
		this.cellContainers.put(offset, cont);
	}

	/** Mine a cell out (server): turn it to air so it stops rendering/colliding but the rig's
	 *  centre stays fixed, then discard the whole rig once nothing solid is left. */
	private void removeCellAt(int index) {
		if (index < 0 || index >= this.states.size()) return;
		// Drop any stored inventory for this cell (chest contents), then forget it.
		net.minecraft.world.SimpleContainer cont = this.cellContainers.remove(this.offsets.get(index));
		if (cont != null && !cont.isEmpty()) {
			double chx = this.dimX / 2.0D, chy = this.dimY / 2.0D, chz = this.dimZ / 2.0D;
			BlockPos co = this.offsets.get(index);
			Vec3 cr = rotateLocal(co.getX() - chx + 0.5D, co.getY() - chy + 0.5D, co.getZ() - chz + 0.5D);
			Vec3 cp = this.position();
			net.minecraft.world.Containers.dropContents(this.level(), BlockPos.containing(cp.x + cr.x, cp.y + cr.y, cp.z + cr.z), cont);
		}
		this.states.set(index, Blocks.AIR.defaultBlockState());
		this.entityData.set(DATA_BLOCKS, encode());
		recomputeShape();
		for (BlockState s : this.states) {
			if (!s.isAir()) return;
		}
		this.discard();
	}

	/** Add a cell at a local offset (or fill a mined-out air slot). Keeps the dim fixed so the
	 *  rig doesn't re-centre; the bounds grow to include the new cell. */
	private void addCellAt(BlockPos offset, BlockState state) {
		if (state == null || state.isAir()) return;
		for (int i = 0; i < this.offsets.size(); i++) {
			if (this.offsets.get(i).equals(offset)) {
				if (!this.states.get(i).isAir()) return; // a real block is already there
				this.states.set(i, state);
				this.entityData.set(DATA_BLOCKS, encode());
				recomputeShape();
				return;
			}
		}
		if (this.offsets.size() >= MAX_CELLS) return;
		this.offsets.add(offset);
		this.states.add(state);
		this.entityData.set(DATA_BLOCKS, encode());
		recomputeShape();
		recomputeBounds();
	}

	/** Which face of the local box [bx..]..[+1] the entry point sits on, as a unit normal. */
	private static int[] faceNormal(Vec3 hit, double bx, double by, double bz) {
		double eps = 1.0E-3;
		if (Math.abs(hit.x - bx) < eps) return new int[]{-1, 0, 0};
		if (Math.abs(hit.x - (bx + 1.0D)) < eps) return new int[]{1, 0, 0};
		if (Math.abs(hit.y - by) < eps) return new int[]{0, -1, 0};
		if (Math.abs(hit.y - (by + 1.0D)) < eps) return new int[]{0, 1, 0};
		if (Math.abs(hit.z - bz) < eps) return new int[]{0, 0, -1};
		if (Math.abs(hit.z - (bz + 1.0D)) < eps) return new int[]{0, 0, 1};
		return null;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.level().isClientSide || !isBreakable()) return false;
		Entity attacker = source.getEntity();
		if (attacker == null) return false;
		boolean creative = attacker instanceof Player p && p.getAbilities().instabuild;
		// Ray-pick the cell the attacker is looking at and mine just that one (drops the block).
		// The ray is transformed into the rig's LOCAL (unrotated) frame so it works when rotated.
		Vec3 eye = attacker.getEyePosition();
		Vec3 end = eye.add(attacker.getViewVector(1.0F).scale(6.0D));
		Vec3 origin = this.position();
		Vec3 localEye = inverseRotateLocal(eye.x - origin.x, eye.y - origin.y, eye.z - origin.z);
		Vec3 localEnd = inverseRotateLocal(end.x - origin.x, end.y - origin.y, end.z - origin.z);
		double hx = this.dimX / 2.0D, hy = this.dimY / 2.0D, hz = this.dimZ / 2.0D;
		int best = -1;
		double bestDist = Double.MAX_VALUE;
		int n = Math.min(this.offsets.size(), this.states.size());
		for (int i = 0; i < n; i++) {
			if (this.states.get(i).isAir()) continue;
			BlockPos o = this.offsets.get(i);
			double bx = o.getX() - hx, by = o.getY() - hy, bz = o.getZ() - hz;
			Optional<Vec3> hit = new AABB(bx, by, bz, bx + 1.0D, by + 1.0D, bz + 1.0D).clip(localEye, localEnd);
			if (hit.isPresent()) {
				double d = hit.get().distanceToSqr(localEye);
				if (d < bestDist) {
					bestDist = d;
					best = i;
				}
			}
		}
		if (best < 0) return false;
		BlockState st = this.states.get(best);
		if (!creative) {
			long now = this.level().getGameTime();
			// One miner + one cell at a time: restart on a new attacker/cell or if mining lapsed (no hit
			// for ~2s). Window must exceed the gap between held/spam attacks (a slow weapon swings ~every
			// 22 ticks), since entities aren't continuously mined like blocks - you re-attack, not hold.
			if (!attacker.getUUID().equals(this.miningAttacker) || best != this.miningCell || now - this.lastAttackTick > 40L) {
				this.miningAttacker = attacker.getUUID();
				this.miningCell = best;
				this.miningStart = now;
			}
			this.lastAttackTick = now;
			BlockPos o = this.offsets.get(best);
			Vec3 r = rotateLocal(o.getX() - hx + 0.5D, o.getY() - hy + 0.5D, o.getZ() - hz + 0.5D);
			BlockPos worldPos = BlockPos.containing(origin.x + r.x, origin.y + r.y, origin.z + r.z);
			int breakTicks = CneExtrasRuntime.blockBreakTimeTicks(attacker, this.level(), st, worldPos);
			if (now - this.miningStart < breakTicks) return true; // still mining this cell
			Block.popResource(this.level(), worldPos, new ItemStack(st.getBlock()));
		}
		removeCellAt(best);
		// Reset so the shifted cell indices don't let the next cell inherit this one's progress.
		this.miningAttacker = null;
		this.miningCell = -1;
		return true;
	}

	/** Inverse of rotateLocal: maps a world-relative point back into the rig's unrotated frame. */
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
	public InteractionResult interactAt(Player player, Vec3 hitVec, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockItem blockItem = stack.getItem() instanceof BlockItem bi ? bi : null;
		boolean placing = isPlaceable() && blockItem != null;
		// Ray-pick the cell + face the player is aiming at (local frame). Cells are synced so both sides
		// pick; the client just acknowledges and the server does the real work (place or open).
		Vec3 eye = player.getEyePosition();
		Vec3 end = eye.add(player.getViewVector(1.0F).scale(6.0D));
		Vec3 origin = this.position();
		Vec3 localEye = inverseRotateLocal(eye.x - origin.x, eye.y - origin.y, eye.z - origin.z);
		Vec3 localEnd = inverseRotateLocal(end.x - origin.x, end.y - origin.y, end.z - origin.z);
		double hx = this.dimX / 2.0D, hy = this.dimY / 2.0D, hz = this.dimZ / 2.0D;
		int best = -1;
		double bestDist = Double.MAX_VALUE;
		Vec3 bestHit = null;
		int n = Math.min(this.offsets.size(), this.states.size());
		for (int i = 0; i < n; i++) {
			if (this.states.get(i).isAir()) continue;
			BlockPos o = this.offsets.get(i);
			double bx = o.getX() - hx, by = o.getY() - hy, bz = o.getZ() - hz;
			Optional<Vec3> hit = new AABB(bx, by, bz, bx + 1.0D, by + 1.0D, bz + 1.0D).clip(localEye, localEnd);
			if (hit.isPresent()) {
				double d = hit.get().distanceToSqr(localEye);
				if (d < bestDist) {
					bestDist = d;
					best = i;
					bestHit = hit.get();
				}
			}
		}
		if (best < 0) return InteractionResult.PASS;
		BlockState targetState = this.states.get(best);
		BlockPos o = this.offsets.get(best);
		int[] nrm = faceNormal(bestHit, o.getX() - hx, o.getY() - hy, o.getZ() - hz);
		boolean willOpen = !placing && isContainerState(targetState);
		boolean willPlace = placing && nrm != null;
		if (!willOpen && !willPlace) return InteractionResult.PASS;
		if (this.level().isClientSide) return InteractionResult.SUCCESS; // client acknowledges; server does the work
		if (willOpen) {
			// Open the container cell's inventory in a normal chest GUI (server-driven, no block-entity).
			net.minecraft.world.SimpleContainer cont = this.cellContainers.computeIfAbsent(o, k -> new net.minecraft.world.SimpleContainer(27));
			player.openMenu(new net.minecraft.world.SimpleMenuProvider((id, inv, p) -> net.minecraft.world.inventory.ChestMenu.threeRows(id, inv, cont), targetState.getBlock().getName()));
			return InteractionResult.SUCCESS;
		}
		BlockPos target = o.offset(nrm[0], nrm[1], nrm[2]);
		BlockState placed = blockItem.getBlock().defaultBlockState();
		addCellAt(target, placed);
		// Two-tall blocks (doors, tall plants) are a LOWER + an UPPER half - add the upper cell above it
		// so the whole block shows, instead of only the bottom.
		if (placed.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF)) {
			addCellAt(target.above(), placed.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER));
		}
		if (!player.getAbilities().instabuild) stack.shrink(1);
		return InteractionResult.SUCCESS;
	}

	/** Rotate a point about the origin by the rig's X,Y,Z angles, matching the renderer's
	 *  mulPose(XP)*mulPose(YP)*mulPose(ZP) order so collision + place-back match the visual. */
	private Vec3 rotateLocal(double x, double y, double z) {
		double rx = Math.toRadians(getRotX()), ry = Math.toRadians(getRotY()), rz = Math.toRadians(getRotZ());
		double cz = Math.cos(rz), sz = Math.sin(rz);
		double x1 = x * cz - y * sz, y1 = x * sz + y * cz, z1 = z;
		double cy = Math.cos(ry), sy = Math.sin(ry);
		double x2 = x1 * cy + z1 * sy, y2 = y1, z2 = -x1 * sy + z1 * cy;
		double cx = Math.cos(rx), sx = Math.sin(rx);
		return new Vec3(x2, y2 * cx - z2 * sx, y2 * sx + z2 * cx);
	}

	private void recomputeBounds() {
		if (this.offsets.isEmpty()) {
			this.localBounds = new AABB(-0.5D, -0.5D, -0.5D, 0.5D, 0.5D, 0.5D);
			this.setBoundingBox(makeBoundingBox());
			return;
		}
		double hx = this.dimX / 2.0D, hy = this.dimY / 2.0D, hz = this.dimZ / 2.0D;
		double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
		for (BlockPos o : this.offsets) {
			double bx = o.getX() - hx, by = o.getY() - hy, bz = o.getZ() - hz;
			for (int i = 0; i <= 1; i++) {
				for (int j = 0; j <= 1; j++) {
					for (int k = 0; k <= 1; k++) {
						Vec3 r = rotateLocal(bx + i, by + j, bz + k);
						minX = Math.min(minX, r.x);
						maxX = Math.max(maxX, r.x);
						minY = Math.min(minY, r.y);
						maxY = Math.max(maxY, r.y);
						minZ = Math.min(minZ, r.z);
						maxZ = Math.max(maxZ, r.z);
					}
				}
			}
		}
		this.localBounds = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
		this.setBoundingBox(makeBoundingBox()); // refresh the cached AABB (broadphase / cull / collision query)
	}

	/**
	 * Per-block collision shape in WORLD space, used by the collision mixin so only the real
	 * blocks are solid (not the whole bounding box). Unrotated: exact union of the cells.
	 * Rotated: falls back to the enclosing box, since axis-aligned voxel shapes can't rotate.
	 */
	public VoxelShape collisionShape() {
		if (!isSolid() || this.offsets.isEmpty()) return Shapes.empty();
		if (getRotX() != 0.0F || getRotY() != 0.0F || getRotZ() != 0.0F) return Shapes.create(getBoundingBox());
		VoxelShape ls = this.localCollisionShape;
		if (ls == null || ls.isEmpty()) return Shapes.empty(); // all cells mined out -> nothing solid
		return ls.move(this.getX(), this.getY(), this.getZ());
	}

	/** Stamps every cell back into the world at its current (rotated) position, then removes the entity. */
	public void placeIntoWorld() {
		if (!(this.level() instanceof ServerLevel level)) return;
		double hx = this.dimX / 2.0D, hy = this.dimY / 2.0D, hz = this.dimZ / 2.0D;
		for (int i = 0; i < this.offsets.size(); i++) {
			BlockState st = this.states.get(i);
			if (st.isAir()) continue;
			BlockPos o = this.offsets.get(i);
			Vec3 r = rotateLocal(o.getX() - hx + 0.5D, o.getY() - hy + 0.5D, o.getZ() - hz + 0.5D);
			level.setBlock(BlockPos.containing(this.getX() + r.x, this.getY() + r.y, this.getZ() + r.z), st, Block.UPDATE_ALL);
		}
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
		Vec3 prevPos = this.position();
		float prevRotY = getRotY();
		float avx = getAvX(), avy = getAvY(), avz = getAvZ();
		if (avx != 0.0F || avy != 0.0F || avz != 0.0F) setRotation(getRotX() + avx, getRotY() + avy, getRotZ() + avz);
		if (this.gravityEnabled) {
			this.applyGravity();
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
			if (this.onGround()) this.setDeltaMovement(Vec3.ZERO);
		} else {
			Vec3 dm = this.getDeltaMovement();
			if (dm.lengthSqr() > 1.0E-9D) this.setPos(this.getX() + dm.x, this.getY() + dm.y, this.getZ() + dm.z);
		}
		// Carry anyone standing on the rig as it moves/spins (vanilla doesn't move platform riders).
		CneExtrasRuntime.carryRiders(this, prevPos, getRotY() - prevRotY, null);
	}

	@Override
	protected double getDefaultGravity() {
		return 0.04D;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.fixed(1.0F, 1.0F); // placeholder; the real box is makeBoundingBox below
	}

	@Override
	protected AABB makeBoundingBox() {
		AABB lb = this.localBounds;
		if (lb == null) return super.makeBoundingBox();
		return lb.move(this.getX(), this.getY(), this.getZ());
	}

	@Override
	public boolean isPickable() {
		// Pickable when solid, breakable, OR placeable, so a player can hit/build-on it even if not solid.
		return isSolid() || isBreakable() || isPlaceable();
	}

	@Override
	public boolean canBeCollidedWith() {
		// NOT collided as a whole-box entity. Collision is contributed PER BLOCK through the
		// collectColliders mixin (added to the world-collision list), so the player walks and
		// jumps on the real blocks like ground and the gaps/air stay passable.
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false; // water/lava currents must not drift a placed group on their own
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(DATA_BLOCKS, "");
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

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		String data = tag.getString("Blocks");
		this.entityData.set(DATA_BLOCKS, data);
		decode(data);
		this.setRotation(tag.getFloat("RotX"), tag.getFloat("RotY"), tag.getFloat("RotZ"));
		this.setSolid(!tag.contains("Solid") || tag.getBoolean("Solid"));
		this.despawnTicks = Math.max(0, tag.getInt("Despawn"));
		setGravityEnabled(tag.getBoolean("Gravity"));
		this.entityData.set(DATA_AV_X, tag.getFloat("AvX"));
		this.entityData.set(DATA_AV_Y, tag.getFloat("AvY"));
		this.entityData.set(DATA_AV_Z, tag.getFloat("AvZ"));
		this.entityData.set(DATA_BREAKABLE, tag.getBoolean("Breakable"));
		this.entityData.set(DATA_PLACEABLE, tag.getBoolean("Placeable"));
		// Per-cell container inventories (chest contents).
		this.cellContainers.clear();
		net.minecraft.nbt.ListTag cellInvs = tag.getList("CellInvs", net.minecraft.nbt.Tag.TAG_COMPOUND);
		for (int i = 0; i < cellInvs.size(); i++) {
			net.minecraft.nbt.CompoundTag c = cellInvs.getCompound(i);
			net.minecraft.world.SimpleContainer cont = new net.minecraft.world.SimpleContainer(27);
			cont.fromTag(c.getList("Items", net.minecraft.nbt.Tag.TAG_COMPOUND), this.registryAccess());
			this.cellContainers.put(new BlockPos(c.getInt("x"), c.getInt("y"), c.getInt("z")), cont);
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putString("Blocks", encode());
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
		// Per-cell container inventories (chest contents).
		net.minecraft.nbt.ListTag cellInvs = new net.minecraft.nbt.ListTag();
		for (java.util.Map.Entry<BlockPos, net.minecraft.world.SimpleContainer> e : this.cellContainers.entrySet()) {
			if (e.getValue().isEmpty()) continue;
			net.minecraft.nbt.CompoundTag c = new net.minecraft.nbt.CompoundTag();
			c.putInt("x", e.getKey().getX());
			c.putInt("y", e.getKey().getY());
			c.putInt("z", e.getKey().getZ());
			c.put("Items", e.getValue().createTag(this.registryAccess()));
			cellInvs.add(c);
		}
		if (!cellInvs.isEmpty()) tag.put("CellInvs", cellInvs);
	}
}
