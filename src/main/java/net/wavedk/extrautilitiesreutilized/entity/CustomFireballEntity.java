package net.wavedk.extrautilitiesreutilized.entity;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.BlockPos;

@EventBusSubscriber
public class CustomFireballEntity extends Fireball {
	public static final ResourceLocation ENTITY_ID = ResourceLocation.fromNamespaceAndPath("euru", "custom_fireball");
	public static final ResourceKey<EntityType<?>> ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ENTITY_ID);
	public static EntityType<CustomFireballEntity> TYPE;

	@SubscribeEvent
	public static void registerEntity(RegisterEvent event) {
		event.register(Registries.ENTITY_TYPE, ENTITY_ID, () -> {
			TYPE = EntityType.Builder.<CustomFireballEntity>of(CustomFireballEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build(ENTITY_ID.toString());
			return TYPE;
		});
	}

	private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(CustomFireballEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> DATA_ENABLE_FIRE = SynchedEntityData.defineId(CustomFireballEntity.class, EntityDataSerializers.BOOLEAN);
	private float explosionRadius = 3.0F;
	private int fireRadius = 0;
	private ParticleOptions trailParticle = ParticleTypes.SMOKE;
	private boolean enableGravity = false;

	public CustomFireballEntity(EntityType<? extends CustomFireballEntity> type, Level level) {
		super(type, level);
	}

	public CustomFireballEntity(EntityType<? extends CustomFireballEntity> type, double x, double y, double z, Vec3 direction, Level level) {
		super(type, x, y, z, direction, level);
	}

	public CustomFireballEntity(EntityType<? extends CustomFireballEntity> type, LivingEntity shooter, Vec3 acceleration, Level level) {
		super(type, shooter, acceleration, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(DATA_SIZE, 1.0F);
		entityData.define(DATA_ENABLE_FIRE, true);
	}

	public void setCustomTexture(ItemStack item) {
		this.setItem(item);
	}

	public void setCustomSize(float size) {
		float clampedSize = Math.max(0.5F, Math.min(15.0F, size));
		this.getEntityData().set(DATA_SIZE, clampedSize);
	}

	public float getCustomSize() {
		return this.getEntityData().get(DATA_SIZE);
	}

	public void setGravityEnabled(boolean gravity) {
		this.enableGravity = gravity;
	}

	@Override
	public boolean isNoGravity() {
		return !this.enableGravity;
	}

	public void setTrailParticle(ParticleOptions particle) {
		this.trailParticle = particle;
	}

	public void setExplosionParams(float radius, int fire, boolean canVisualFire) {
		this.explosionRadius = radius;
		this.fireRadius = fire;
		this.getEntityData().set(DATA_ENABLE_FIRE, canVisualFire);
	}

	public boolean isVisualFireEnabled() {
		return this.getEntityData().get(DATA_ENABLE_FIRE);
	}

	@Override
	public boolean isOnFire() {
		return isVisualFireEnabled();
	}

	@Override
	public boolean displayFireAnimation() {
		return isVisualFireEnabled();
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
			float size = getCustomSize();
			double spread = 0.08D * (double) size;
			int count = (int) (8 + (size * 10));
			if (this.trailParticle != null) {
				serverLevel.sendParticles(this.trailParticle, this.getX(), this.getY() + (0.2D * size), this.getZ(), count, spread, spread, spread, 0.01D);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		explodeAndDiscard();
	}

	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		explodeAndDiscard();
	}

	private void explodeAndDiscard() {
		if (!this.level().isClientSide()) {
			boolean createFireOnGround = this.fireRadius > 0;
			if (this.explosionRadius > 0.0F || createFireOnGround) {
				this.level().explode(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius, createFireOnGround, this.explosionRadius > 0 ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
				if (createFireOnGround) {
					BlockPos center = this.blockPosition();
					RandomSource random = this.getRandom();
					for (int x = -this.fireRadius; x <= this.fireRadius; x++) {
						for (int y = -this.fireRadius; y <= this.fireRadius; y++) {
							for (int z = -this.fireRadius; z <= this.fireRadius; z++) {
								BlockPos pos = center.offset(x, y, z);
								if (this.level().isEmptyBlock(pos) && random.nextFloat() < 0.3F) {
									this.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
								}
							}
						}
					}
				}
			}
			this.discard();
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("CustomSize", this.getCustomSize());
		tag.putFloat("ExplosionRadius", this.explosionRadius);
		tag.putInt("FireRadius", this.fireRadius);
		tag.putBoolean("EnableFire", this.isVisualFireEnabled());
		tag.putBoolean("EnableGravity", this.enableGravity);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("CustomSize")) {
			this.setCustomSize(tag.getFloat("CustomSize"));
		}
		if (tag.contains("ExplosionRadius")) {
			this.explosionRadius = tag.getFloat("ExplosionRadius");
		}
		if (tag.contains("FireRadius")) {
			this.fireRadius = tag.getInt("FireRadius");
		}
		if (tag.contains("EnableFire")) {
			this.getEntityData().set(DATA_ENABLE_FIRE, tag.getBoolean("EnableFire"));
		}
		if (tag.contains("EnableGravity")) {
			this.enableGravity = tag.getBoolean("EnableGravity");
		}
	}
}