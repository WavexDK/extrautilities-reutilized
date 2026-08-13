package net.wavedk.extrautilitiesreutilized.ai;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.BlockPos;

import java.util.EnumSet;

public class MoveToBlockGoal extends Goal {
	private final Mob mob;
	private final Block targetBlock;
	private final String mode;
	private final boolean getInsideTarget;
	private final double speed;
	private BlockPos targetPos = null;
	private boolean finished = false;
	private int searchTimer = 0;

	public MoveToBlockGoal(Mob mob, Block targetBlock, String mode, boolean getInsideTarget, double speed) {
		this.mob = mob;
		this.targetBlock = targetBlock;
		this.mode = mode;
		this.getInsideTarget = getInsideTarget;
		this.speed = speed;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (finished || !mob.isAlive()) {
			return false;
		}
		if (mob.getPersistentData().getBoolean("abandonBlockTask")) {
			return false;
		}
		if ("RESET_ON_HIT".equals(mode)) {
			if (mob.getLastHurtByMob() != null && mob.getLastHurtByMob().isAlive()) {
				this.finished = true;
				return false;
			}
		}
		findClosestBlock();
		if ("STAND_WAIT".equals(mode)) {
			return true;
		}
		if (targetPos == null) {
			this.finished = true;
			return false;
		}
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		if (finished || !mob.isAlive()) {
			return false;
		}
		if (mob.getPersistentData().getBoolean("abandonBlockTask")) {
			return false;
		}
		if ("RESET_ON_HIT".equals(mode)) {
			if (mob.getLastHurtByMob() != null && mob.getLastHurtByMob().isAlive()) {
				this.finished = true;
				return false;
			}
		}
		if ("STAND_WAIT".equals(mode)) {
			return true;
		}
		if (targetPos == null) {
			this.finished = true;
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		searchTimer = 0;
		findClosestBlock();
		if (targetPos == null && !"STAND_WAIT".equals(mode)) {
			this.finished = true;
		}
	}

	@Override
	public void tick() {
		if (searchTimer++ % 10 == 0) {
			findClosestBlock();
		}
		if (targetPos == null) {
			if ("STAND_WAIT".equals(mode)) {
				mob.getNavigation().stop();
			} else {
				this.finished = true;
			}
			return;
		}
		if ("PRIORITY_LOCK".equals(mode) || "STAND_WAIT".equals(mode) || "RESET_ON_HIT".equals(mode)) {
			mob.setTarget(null);
		}
		double targetX = targetPos.getX() + 0.5D;
		double targetY = targetPos.getY() + (getInsideTarget ? 0.0D : 0.5D);
		double targetZ = targetPos.getZ() + 0.5D;
		mob.getLookControl().setLookAt(targetX, targetY, targetZ, 180.0F, 180.0F);
		double dx = targetX - mob.getX();
		double dz = targetZ - mob.getZ();
		float yaw = (float) (Math.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
		mob.setYRot(yaw);
		mob.setYHeadRot(yaw);
		mob.setYBodyRot(yaw);
		double distSq = mob.distanceToSqr(targetX, mob.getY(), targetZ);
		double stopDistanceSq = getInsideTarget ? 0.15D : 2.25D;
		if (distSq <= stopDistanceSq) {
			mob.getNavigation().stop();
			if ("ONCE".equals(mode)) {
				this.finished = true;
			}
		} else {
			if (mob.getNavigation().isDone() || mob.tickCount % 5 == 0) {
				mob.getNavigation().moveTo(targetX, targetPos.getY(), targetZ, speed);
			}
		}
	}

	private void findClosestBlock() {
		BlockPos center = mob.blockPosition();
		if (targetPos != null) {
			if (!mob.level().getBlockState(targetPos).is(targetBlock)) {
				targetPos = null;
			}
		}
		BlockPos found = null;
		double closestDistSq = Double.MAX_VALUE;
		for (BlockPos p : BlockPos.betweenClosed(center.offset(-15, -6, -15), center.offset(15, 6, 15))) {
			if (mob.level().getBlockState(p).is(targetBlock)) {
				double dist = p.distSqr(center);
				if (dist < closestDistSq) {
					closestDistSq = dist;
					found = p.immutable();
				}
			}
		}
		if (found != null) {
			this.targetPos = found;
		}
	}

	@Override
	public void stop() {
		mob.getNavigation().stop();
		this.targetPos = null;
	}
}