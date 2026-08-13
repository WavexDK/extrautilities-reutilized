package net.wavedk.extrautilitiesreutilized.entity;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.BlockPos;

@EventBusSubscriber
public class CustomEyeOfEnderEntity extends EyeOfEnder {
	public static final ResourceLocation ENTITY_ID = ResourceLocation.fromNamespaceAndPath("euru", "custom_eye_of_ender");
	public static final ResourceKey<EntityType<?>> ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ENTITY_ID);
	public static EntityType<CustomEyeOfEnderEntity> TYPE;

	@SubscribeEvent
	public static void registerEntity(RegisterEvent event) {
		event.register(Registries.ENTITY_TYPE, ENTITY_ID, () -> {
			TYPE = EntityType.Builder.<CustomEyeOfEnderEntity>of(CustomEyeOfEnderEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(ENTITY_ID.toString());
			return TYPE;
		});
	}

	private ParticleOptions ambientParticle = ParticleTypes.PORTAL;
	private ParticleOptions breakParticle = ParticleTypes.CRIT;
	private double breakChance = 0.2D;
	private boolean customSurvive = true;
	private int customLife = 0;
	private double targetX;
	private double targetY;
	private double targetZ;

	public CustomEyeOfEnderEntity(EntityType<? extends CustomEyeOfEnderEntity> type, Level level) {
		super(type, level);
	}

	public CustomEyeOfEnderEntity(Level level, double x, double y, double z) {
		this(TYPE, level);
		this.setPos(x, y, z);
	}

	public void setAmbientParticle(ParticleOptions particle) {
		if (particle != null)
			this.ambientParticle = particle;
	}

	public void setBreakParticle(ParticleOptions particle) {
		if (particle != null)
			this.breakParticle = particle;
	}

	public void setBreakChance(double chance) {
		this.breakChance = Math.max(0.0D, Math.min(1.0D, chance));
		this.customSurvive = this.random.nextDouble() >= this.breakChance;
	}

	public void signalToPos(BlockPos targetPos) {
		double dx = (double) targetPos.getX() - this.getX();
		double dz = (double) targetPos.getZ() - this.getZ();
		double distance = Math.sqrt(dx * dx + dz * dz);
		if (distance > 12.0D) {
			this.targetX = this.getX() + dx / distance * 12.0D;
			this.targetZ = this.getZ() + dz / distance * 12.0D;
			this.targetY = this.getY() + 8.0D;
		} else {
			this.targetX = targetPos.getX();
			this.targetY = targetPos.getY();
			this.targetZ = targetPos.getZ();
		}
		this.customLife = 0;
	}

	@Override
	public void tick() {
		this.baseTick();
		Vec3 motion = this.getDeltaMovement();
		double currentX = this.getX() + motion.x;
		double currentY = this.getY() + motion.y;
		double currentZ = this.getZ() + motion.z;
		double horizDist = motion.horizontalDistance();
		this.setXRot((float) (Mth.atan2(motion.y, horizDist) * (180.0D / Math.PI)));
		this.setYRot((float) (Mth.atan2(motion.x, motion.z) * (180.0D / Math.PI)));
		if (!this.level().isClientSide()) {
			double diffX = this.targetX - currentX;
			double diffZ = this.targetZ - currentZ;
			float distToTarget = (float) Math.sqrt(diffX * diffX + diffZ * diffZ);
			float angle = (float) Mth.atan2(diffZ, diffX);
			double speed = Mth.lerp(0.0025D, horizDist, (double) distToTarget);
			double motionY = motion.y;
			if (distToTarget < 1.0F) {
				speed *= 0.8D;
				motionY *= 0.8D;
			}
			int verticalDir = this.getY() < this.targetY ? 1 : -1;
			motion = new Vec3(Math.cos((double) angle) * speed, motionY + ((double) verticalDir - motionY) * 0.015D, Math.sin((double) angle) * speed);
			this.setDeltaMovement(motion);
		}
		this.setPos(currentX, currentY, currentZ);
		if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel && this.ambientParticle != null) {
			serverLevel.sendParticles(this.ambientParticle, this.getX(), this.getY() + 0.1D, this.getZ(), 2, 0.1D, 0.1D, 0.1D, 0.01D);
		}
		if (!this.level().isClientSide()) {
			this.customLife++;
			if (this.customLife > 80) {
				this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);

				if (this.customSurvive) {
					this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItem()));
				} else {
					spawnBreakParticles();
				}

				this.discard();
			}
		}
	}

	private void spawnBreakParticles() {
		if (this.level() instanceof ServerLevel serverLevel && this.breakParticle != null) {
			int particleCount = 35;
			for (int i = 0; i < particleCount; i++) {
				double u = this.random.nextDouble();
				double v = this.random.nextDouble();
				double theta = u * 2.0 * Math.PI;
				double phi = Math.acos(2.0 * v - 1.0);

				double speed = 0.2D + (this.random.nextDouble() * 0.15D);

				double dx = speed * Math.sin(phi) * Math.cos(theta);
				double dy = speed * Math.sin(phi) * Math.sin(theta);
				double dz = speed * Math.cos(phi);
				serverLevel.sendParticles(this.breakParticle, this.getX(), this.getY() + 0.1D, this.getZ(), 0, dx, dy, dz, 1.0D);
			}
		}
	}
}