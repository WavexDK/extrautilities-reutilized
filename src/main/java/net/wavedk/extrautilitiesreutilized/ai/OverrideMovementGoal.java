package net.wavedk.extrautilitiesreutilized.ai;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;

import java.util.EnumSet;

public class OverrideMovementGoal extends Goal {
	private final Mob mob;
	private final Vec3 targetPos;
	private Vec3 startPos;
	private final double targetSpeed;
	private final boolean canFly;
	private final String easing;
	private boolean finished = false;
	private double totalDistance = 0.0D;

	public OverrideMovementGoal(Mob mob, double x, double y, double z, double speed, boolean canFly, String easing) {
		this.mob = mob;
		this.targetPos = new Vec3(Math.floor(x) + 0.5D, y, Math.floor(z) + 0.5D);
		this.targetSpeed = speed;
		this.canFly = canFly;
		this.easing = easing != null ? easing : "LINEAR";
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		if (finished || !mob.isAlive())
			return false;
		if (mob.getPersistentData().getBoolean("abandonBlockTask")) {
			return false;
		}
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return canUse();
	}

	@Override
	public void start() {
		this.startPos = mob.position();
		this.totalDistance = startPos.distanceTo(targetPos);
		if (canFly) {
			mob.setNoGravity(true);
		}
	}

	private double calculateSpeedMultiplier(double progress) {
		double t = Math.max(0.0D, Math.min(1.0D, progress));
		switch (easing) {
			case "SINE_IN" :
				return 1.0D - Math.cos((t * Math.PI) / 2.0D);
			case "SINE_OUT" :
				return Math.sin((t * Math.PI) / 2.0D);
			case "SINE_IN_OUT" :
				return -(Math.cos(Math.PI * t) - 1.0D) / 2.0D;
			case "LINEAR" :
			default :
				return 1.0D;
		}
	}

	@Override
	public void tick() {
		if (finished)
			return;
		if (mob.getPersistentData().getBoolean("abandonBlockTask")) {
			this.finished = true;
			return;
		}
		double distanceSq = mob.position().distanceToSqr(targetPos);
		if (distanceSq <= 0.8D) {
			this.finished = true;
			mob.getNavigation().stop();
			return;
		}
		double currentDist = mob.position().distanceTo(targetPos);
		double progress = totalDistance > 0 ? (1.0D - (currentDist / totalDistance)) : 1.0D;
		double currentSpeed = targetSpeed * calculateSpeedMultiplier(progress);
		currentSpeed = Math.max(0.05D, currentSpeed);
		mob.getLookControl().setLookAt(targetPos.x, targetPos.y + 0.5D, targetPos.z, 180.0F, 180.0F);
		if (canFly) {
			Vec3 dir = targetPos.subtract(mob.position()).normalize();
			Vec3 moveVec = dir.scale(currentSpeed * 0.25D);
			mob.setDeltaMovement(moveVec);
			float yaw = (float) (Math.atan2(dir.z, dir.x) * (180.0D / Math.PI)) - 90.0F;
			mob.setYRot(yaw);
			mob.setYHeadRot(yaw);
			mob.setYBodyRot(yaw);
		} else {
			if (mob.getNavigation().isDone() || mob.tickCount % 5 == 0) {
				mob.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, currentSpeed);
			}
		}
	}

	@Override
	public void stop() {
		mob.getNavigation().stop();
		if (canFly) {
			mob.setNoGravity(false);
		}
	}

	public boolean isFinished() {
		return finished;
	}
}