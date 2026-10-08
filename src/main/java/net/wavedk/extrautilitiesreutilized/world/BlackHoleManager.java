package net.wavedk.extrautilitiesreutilized.world;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

@EventBusSubscriber
public class BlackHoleManager {
	private static final Random RANDOM = new Random();
	private static final Map<UUID, String> CLUSTER_MAP = new ConcurrentHashMap<>();

	public record BlackHoleKey(ResourceKey<Level> dimension, BlockPos pos) {
	}

	public record BlackHoleData(BlackHoleKey key, int stackSize, double blockRadius, boolean pullEntities, double entityRadius, double power, boolean killEntities) {
	}

	public static void setBlackHole(LevelAccessor levelAccessor, double x, double y, double z, int stackSize, double blockRadius, boolean pullEntities, double entityRadius, double power, boolean killEntities, boolean active) {
		if (!(levelAccessor instanceof ServerLevel serverLevel))
			return;
		BlockPos pos = BlockPos.containing(x, y, z);
		BlackHoleKey key = new BlackHoleKey(serverLevel.dimension(), pos.immutable());
		BlackHoleSavedData saveData = BlackHoleSavedData.get(serverLevel);
		if (active) {
			saveData.getBlackHoles().put(key, new BlackHoleData(key, stackSize, blockRadius, pullEntities, entityRadius, power, killEntities));
			saveData.setDirty();
		} else {
			if (saveData.getBlackHoles().containsKey(key)) {
				saveData.getBlackHoles().remove(key);
				saveData.setDirty();
			}
		}
	}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		if (!(event.getLevel() instanceof ServerLevel serverLevel))
			return;
		ResourceKey<Level> currentDim = serverLevel.dimension();
		BlackHoleSavedData saveData = BlackHoleSavedData.get(serverLevel);
		ConcurrentHashMap<BlackHoleKey, BlackHoleData> activeBlackHoles = saveData.getBlackHoles();
		if (activeBlackHoles.isEmpty())
			return;
		for (BlackHoleData bh : activeBlackHoles.values()) {
			if (bh.key().dimension().equals(currentDim)) {
				tickBlackHole(serverLevel, bh);
			}
		}
	}

	private static void tickBlackHole(ServerLevel serverLevel, BlackHoleData bh) {
		BlockPos pos = bh.key().pos();
		double centerX = pos.getX() + 0.5;
		double centerY = pos.getY() + 0.5;
		double centerZ = pos.getZ() + 0.5;
		Vec3 center = new Vec3(centerX, centerY, centerZ);
		double maxRadius = Math.max(bh.entityRadius(), bh.blockRadius());
		if (maxRadius > 0) {
			AABB searchBox = new AABB(centerX - maxRadius, centerY - maxRadius, centerZ - maxRadius, centerX + maxRadius, centerY + maxRadius, centerZ + maxRadius);
			List<Entity> entities = serverLevel.getEntities((Entity) null, searchBox, e -> e.isAlive() && !e.isSpectator());
			Map<String, List<FallingBlockEntity>> clusters = new HashMap<>();
			List<FallingBlockEntity> standaloneBlocks = new ArrayList<>();
			for (Entity entity : entities) {
				if (entity instanceof Player player && player.getAbilities().flying) {
					continue;
				}
				if (entity instanceof FallingBlockEntity fallingBlock) {
					String clusterId = CLUSTER_MAP.get(fallingBlock.getUUID());
					if (clusterId != null) {
						clusters.computeIfAbsent(clusterId, k -> new ArrayList<>()).add(fallingBlock);
					} else {
						standaloneBlocks.add(fallingBlock);
					}
					continue;
				}
				Vec3 entityPos = entity.position();
				double dist = entityPos.distanceTo(center);
				if (bh.pullEntities() && dist <= bh.entityRadius() && dist > 0.1) {
					if (bh.killEntities() && dist <= 1.5) {
						entity.hurt(serverLevel.damageSources().generic(), 1000.0F);
					} else {
						double factor = (1.0 - (dist / bh.entityRadius()));
						double currentPower = bh.power() * Math.pow(factor, 2) * 0.35;

						Vec3 pullDir = center.subtract(entityPos).normalize().scale(currentPower);

						entity.setDeltaMovement(entity.getDeltaMovement().scale(0.8).add(pullDir));
						entity.hurtMarked = true;
					}
				}
			}
			for (List<FallingBlockEntity> clusterList : clusters.values()) {
				if (clusterList.isEmpty())
					continue;
				double totalX = 0, totalY = 0, totalZ = 0;
				for (FallingBlockEntity fbe : clusterList) {
					Vec3 p = fbe.position();
					totalX += p.x;
					totalY += p.y;
					totalZ += p.z;
				}
				Vec3 clusterCenter = new Vec3(totalX / clusterList.size(), totalY / clusterList.size(), totalZ / clusterList.size());
				double clusterDist = clusterCenter.distanceTo(center);
				if (clusterDist > 3.0) {
					Vec3 sharedPullDir = center.subtract(clusterCenter).normalize().scale(Math.max(0.35, bh.power() * 0.25));
					for (FallingBlockEntity fbe : clusterList) {
						fbe.setDeltaMovement(sharedPullDir);
						fbe.hurtMarked = true;
					}
				} else {
					for (FallingBlockEntity fbe : clusterList) {
						CLUSTER_MAP.remove(fbe.getUUID());
						Vec3 fbePos = fbe.position();
						double d = fbePos.distanceTo(center);
						if (d <= 2.0) {
							fbe.discard();
						} else {
							Vec3 pullDir = center.subtract(fbePos).scale(0.5);
							fbe.setDeltaMovement(pullDir);
							fbe.hurtMarked = true;
						}
					}
				}
			}
			for (FallingBlockEntity fallingBlock : standaloneBlocks) {
				Vec3 fbePos = fallingBlock.position();
				double d = fbePos.distanceTo(center);
				if (d <= 2.0) {
					fallingBlock.discard();
				} else if (d <= bh.blockRadius()) {
					Vec3 pullDir = center.subtract(fbePos).normalize().scale(Math.max(0.35, bh.power() * 0.25));
					fallingBlock.setDeltaMovement(pullDir);
					fallingBlock.hurtMarked = true;
				}
			}
		}
		if (bh.blockRadius() > 0 && bh.stackSize() > 0) {
			int attempts = Math.max(1, (int) (bh.power() * 2));
			for (int i = 0; i < attempts; i++) {
				int rx = (int) (centerX + (RANDOM.nextDouble() * 2 - 1) * bh.blockRadius());
				int ry = (int) (centerY + (RANDOM.nextDouble() * 2 - 1) * bh.blockRadius());
				int rz = (int) (centerZ + (RANDOM.nextDouble() * 2 - 1) * bh.blockRadius());
				BlockPos targetPos = new BlockPos(rx, ry, rz);
				if (targetPos.distSqr(pos) <= bh.blockRadius() * bh.blockRadius()) {
					if (isBreakableBlock(serverLevel, targetPos) && isExposed(serverLevel, targetPos)) {
						pull3DBlockCluster(serverLevel, targetPos, center, bh.stackSize(), bh.power());
					}
				}
			}
		}
	}

	private static boolean isExposed(Level level, BlockPos pos) {
		BlockPos[] neighbors = {pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west()};
		for (BlockPos n : neighbors) {
			if (!isSolidBlock(level, n)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSolidBlock(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.isAir() || state.is(Blocks.BEDROCK) || state.getDestroySpeed(level, pos) < 0) {
			return false;
		}
		return state.canOcclude();
	}

	private static boolean isBreakableBlock(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return !state.isAir() && !state.is(Blocks.BEDROCK) && state.getDestroySpeed(level, pos) >= 0;
	}

	private static void pull3DBlockCluster(ServerLevel level, BlockPos startPos, Vec3 center, int targetSize, double power) {
		List<BlockPos> cluster = new ArrayList<>();
		Set<BlockPos> visited = new HashSet<>();
		Queue<BlockPos> queue = new LinkedList<>();
		queue.add(startPos);
		visited.add(startPos);
		BlockPos[] directions = {new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, -1, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
		while (!queue.isEmpty() && cluster.size() < targetSize) {
			BlockPos current = queue.poll();
			if (isBreakableBlock(level, current)) {
				cluster.add(current);
				if (cluster.size() < targetSize) {
					for (BlockPos dir : directions) {
						BlockPos neighbor = current.offset(dir);
						if (!visited.contains(neighbor)) {
							visited.add(neighbor);
							queue.add(neighbor);
						}
					}
				}
			}
		}
		if (cluster.isEmpty())
			return;
		double avgX = 0, avgY = 0, avgZ = 0;
		for (BlockPos pos : cluster) {
			avgX += pos.getX() + 0.5;
			avgY += pos.getY() + 0.5;
			avgZ += pos.getZ() + 0.5;
		}
		Vec3 clusterCenter = new Vec3(avgX / cluster.size(), avgY / cluster.size(), avgZ / cluster.size());
		Vec3 sharedPullVelocity = center.subtract(clusterCenter).normalize().scale(Math.max(0.35, power * 0.25));
		String clusterId = UUID.randomUUID().toString();
		for (BlockPos pos : cluster) {
			BlockState state = level.getBlockState(pos);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, state);
			fallingBlock.dropItem = false;
			fallingBlock.setNoGravity(true);

			CLUSTER_MAP.put(fallingBlock.getUUID(), clusterId);

			fallingBlock.setDeltaMovement(sharedPullVelocity);
			fallingBlock.hurtMarked = true;
		}
	}
}