package net.wavedk.extrautilitiesreutilized.client;

import java.util.Random;

public class CameraShakeManager {
	private static final CameraShakeManager INSTANCE = new CameraShakeManager();
	private static final Random RANDOM = new Random();

	private float intensity;
	private int remainingTicks;
	private float decayRate;
	private float shakeYaw;
	private float shakePitch;

	public static CameraShakeManager getInstance() {
		return INSTANCE;
	}

	public void shake(float intensity, int durationTicks) {
		if (intensity > this.intensity || this.remainingTicks <= 0) {
			this.intensity = intensity;
			this.remainingTicks = durationTicks;
			this.decayRate = intensity / (float) durationTicks;
		}
	}

	public void tick() {
		if (this.remainingTicks > 0) {
			--this.remainingTicks;
			this.intensity = Math.max(0.0F, this.intensity - this.decayRate);
			this.shakeYaw = (RANDOM.nextFloat() * 2.0F - 1.0F) * this.intensity;
			this.shakePitch = (RANDOM.nextFloat() * 2.0F - 1.0F) * this.intensity;
		} else {
			this.intensity = 0.0F;
			this.shakeYaw = 0.0F;
			this.shakePitch = 0.0F;
		}
	}

	public float getShakeYaw(float tickDelta) {
		return this.remainingTicks <= 0 ? 0.0F : this.shakeYaw * (1.0F - tickDelta) + (RANDOM.nextFloat() * 2.0F - 1.0F) * this.intensity * tickDelta;
	}

	public float getShakePitch(float tickDelta) {
		return this.remainingTicks <= 0 ? 0.0F : this.shakePitch * (1.0F - tickDelta) + (RANDOM.nextFloat() * 2.0F - 1.0F) * this.intensity * tickDelta;
	}

	public boolean isShaking() {
		return this.remainingTicks > 0 && this.intensity > 0.0F;
	}
}