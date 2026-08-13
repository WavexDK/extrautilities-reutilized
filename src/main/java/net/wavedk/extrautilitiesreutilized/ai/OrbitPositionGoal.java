package net.wavedk.extrautilitiesreutilized.ai;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.BlockPos;

import java.util.EnumSet;

public class OrbitPositionGoal extends Goal {
	private final Mob mob;
	private final double centerX;
	private final double centerY;
	private final double centerZ;
	private final double speed;
	private final double radius;
	private final double dynamicRotSpeed;
	private final int stopCondition;
	private double currentAngle = 0.0;
	private double currentTiltRad;
	private float startHealth;
	private BlockState initialBlockState;

	public OrbitPositionGoal(Mob mob, double cx, double cy, double cz, double speed, double radius, double tiltAngleDeg, double dynamicRotSpeed, int stopCondition) {
		this.mob = mob;
		this.centerX = cx;
		this.centerY = cy;
		this.centerZ = cz;
		this.speed = speed;
		this.radius = Math.max(0.5, radius);
		this.currentTiltRad = Math.toRadians(tiltAngleDeg);
		this.dynamicRotSpeed = dynamicRotSpeed;
		this.stopCondition = stopCondition;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (mob.getPersistentData().getBoolean("abandonBlockTask")) {
			return false;
		}
		return !mob.isDeadOrDying();
	}

	@Override
	public void start() {
		this.startHealth = mob.getHealth();
		BlockPos pos = BlockPos.containing(centerX, centerY, centerZ);
		if (mob.level().isLoaded(pos)) {
			this.initialBlockState = mob.level().getBlockState(pos);
		}
	}

	@Override
	public boolean canContinueToUse() {
		if (!canUse())
			return false;
		if (stopCondition == 1 && mob.getHealth() < startHealth) {
			return false;
		}
		if (stopCondition == 2) {
			BlockPos pos = BlockPos.containing(centerX, centerY, centerZ);
			if (mob.level().isLoaded(pos)) {
				BlockState currentState = mob.level().getBlockState(pos);
				if (initialBlockState != null && !currentState.equals(initialBlockState)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void tick() {
		this.currentAngle += (0.05 * speed);
		if (this.currentAngle > Math.PI * 2) {
			this.currentAngle -= Math.PI * 2;
		}
		if (dynamicRotSpeed != 0.0D) {
			this.currentTiltRad += (0.01745329251D * dynamicRotSpeed);
			if (this.currentTiltRad > Math.PI * 2) {
				this.currentTiltRad -= Math.PI * 2;
			}
		}
		double rawX = Math.cos(currentAngle) * radius;
		double rawY = Math.sin(currentAngle) * radius * Math.sin(currentTiltRad);
		double rawZ = Math.sin(currentAngle) * radius * Math.cos(currentTiltRad);
		double targetX = centerX + rawX;
		double targetY = centerY + rawY;
		double targetZ = centerZ + rawZ;
		mob.setPos(targetX, targetY, targetZ);
		mob.setDeltaMovement(Vec3.ZERO);
		mob.hurtMarked = true;
	}

	@Override
	public void stop() {
		mob.getNavigation().stop();
	}
}