package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;
import net.wavedk.extrautilitiesreutilized.init.EuruModMobEffects;
import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

import java.io.File;

public class SpecialGenFeatureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, String gen) {
		if (gen == null)
			return;
		boolean alreadyBlock = false;
		boolean foundBlock = false;
		boolean found = false;
		double xOffset = 0;
		double radius = 0;
		double yOffset = 0;
		double zOffset = 0;
		double nX = 0;
		double nY = 0;
		double nZ = 0;
		double foundBlocks = 0;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		File cfile = new File("");
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject catobj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject bobj = new com.google.gson.JsonObject();
		if ((gen).equals(BuiltInRegistries.ITEM.getKey(EuruModBlocks.NETHERSTAR_GENERATOR.get().asItem()).toString())) {
			if (!foundBlock) {
				foundBlocks = 1;
				{
					final int _radiusLoopCenterX5 = (int) Math.floor(x);
					final int _radiusLoopCenterY5 = (int) Math.floor(y);
					final int _radiusLoopCenterZ5 = (int) Math.floor(z);
					final int _radiusLoopRadius5 = Math.max(0, (int) Math.floor(1));
					final int _radiusLoopMinX5 = _radiusLoopCenterX5 - _radiusLoopRadius5;
					final int _radiusLoopMaxX5 = _radiusLoopCenterX5 + _radiusLoopRadius5;
					final int _radiusLoopMinY5 = _radiusLoopCenterY5 - _radiusLoopRadius5;
					final int _radiusLoopMaxY5 = _radiusLoopCenterY5 + _radiusLoopRadius5;
					final int _radiusLoopMinZ5 = _radiusLoopCenterZ5 - _radiusLoopRadius5;
					final int _radiusLoopMaxZ5 = _radiusLoopCenterZ5 + _radiusLoopRadius5;
					for (int _radiusLoopX5 = _radiusLoopMinX5; _radiusLoopX5 <= _radiusLoopMaxX5; _radiusLoopX5++) {
						for (int _radiusLoopY5 = _radiusLoopMinY5; _radiusLoopY5 <= _radiusLoopMaxY5; _radiusLoopY5++) {
							for (int _radiusLoopZ5 = _radiusLoopMinZ5; _radiusLoopZ5 <= _radiusLoopMaxZ5; _radiusLoopZ5++) {
								nX = _radiusLoopX5;
								nY = _radiusLoopY5;
								nZ = _radiusLoopZ5;
								if (!((nX + ",") + "" + (nY + ",") + nZ).equals((x + ",") + "" + (y + ",") + z)) {
									if ((getPropertyByName((world.getBlockState(BlockPos.containing(nX, nY, nZ))), "on") instanceof BooleanProperty _getbp2 && (world.getBlockState(BlockPos.containing(nX, nY, nZ))).getValue(_getbp2)) == true
											&& (world.getBlockState(BlockPos.containing(nX, nY, nZ))).getBlock() == EuruModBlocks.NETHERSTAR_GENERATOR.get()) {
										foundBlocks = foundBlocks + 1;
									}
								}
							}
						}
					}
				}
				if (foundBlocks == 1) {
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 10, 2, 2, 2, 0.05);
				} else {
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, (int) (10 / foundBlocks), 2, 2, 2, 0.05);
				}
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 1));
					}
				}
			}
		} else if ((gen).equals(BuiltInRegistries.ITEM.getKey(EuruModBlocks.EXPLOSIVE_GENERATOR.get().asItem()).toString())) {
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "explosion_chance") == 0) {
				bobj = EuruModVariables.fe_config.get((BuiltInRegistries.ITEM.getKey(EuruModBlocks.EXPLOSIVE_GENERATOR.get().asItem()).toString())).getAsJsonObject();
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("explosion_chance", bobj.get("explosion_chance").getAsDouble());
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (Mth.nextInt(RandomSource.create(), 1, 1000) <= getBlockNBTNumber(world, BlockPos.containing(x, y, z), "explosion_chance") * 10) {
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.EXPLOSION)), 18);
					}
				}
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.EXPLOSION, x, (y + 1), z, 2, 1, 1, 1, 3);
			}
		} else if ((gen).equals(BuiltInRegistries.ITEM.getKey(EuruModBlocks.DEATH_GENERATOR.get().asItem()).toString())) {
			foundBlocks = 1;
			{
				final int _radiusLoopCenterX27 = (int) Math.floor(x);
				final int _radiusLoopCenterY27 = (int) Math.floor(y);
				final int _radiusLoopCenterZ27 = (int) Math.floor(z);
				final int _radiusLoopRadius27 = Math.max(0, (int) Math.floor(1));
				final int _radiusLoopMinX27 = _radiusLoopCenterX27 - _radiusLoopRadius27;
				final int _radiusLoopMaxX27 = _radiusLoopCenterX27 + _radiusLoopRadius27;
				final int _radiusLoopMinY27 = _radiusLoopCenterY27 - _radiusLoopRadius27;
				final int _radiusLoopMaxY27 = _radiusLoopCenterY27 + _radiusLoopRadius27;
				final int _radiusLoopMinZ27 = _radiusLoopCenterZ27 - _radiusLoopRadius27;
				final int _radiusLoopMaxZ27 = _radiusLoopCenterZ27 + _radiusLoopRadius27;
				for (int _radiusLoopX27 = _radiusLoopMinX27; _radiusLoopX27 <= _radiusLoopMaxX27; _radiusLoopX27++) {
					for (int _radiusLoopY27 = _radiusLoopMinY27; _radiusLoopY27 <= _radiusLoopMaxY27; _radiusLoopY27++) {
						for (int _radiusLoopZ27 = _radiusLoopMinZ27; _radiusLoopZ27 <= _radiusLoopMaxZ27; _radiusLoopZ27++) {
							nX = _radiusLoopX27;
							nY = _radiusLoopY27;
							nZ = _radiusLoopZ27;
							if (!((nX + ",") + "" + (nY + ",") + nZ).equals((x + ",") + "" + (y + ",") + z)) {
								if ((getPropertyByName((world.getBlockState(BlockPos.containing(nX, nY, nZ))), "on") instanceof BooleanProperty _getbp24 && (world.getBlockState(BlockPos.containing(nX, nY, nZ))).getValue(_getbp24)) == true
										&& (world.getBlockState(BlockPos.containing(nX, nY, nZ))).getBlock() == EuruModBlocks.DEATH_GENERATOR.get()) {
									foundBlocks = foundBlocks + 1;
								}
							}
						}
					}
				}
			}
			if (foundBlocks == 1) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.RAID_OMEN, x, y, z, 10, 2, 2, 2, 0.05);
			} else {
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.RAID_OMEN, x, y, z, (int) (10 / foundBlocks), 2, 2, 2, 0.05);
			}
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(EuruModMobEffects.DOOM, 1200, 1));
				}
			}
		} else if ((gen).equals(BuiltInRegistries.ITEM.getKey(EuruModBlocks.FROSTY_GENERATOR.get().asItem()).toString())) {
			if (Mth.nextInt(RandomSource.create(), 1, 100) <= 5) {
				{
					final int _radiusLoopCenterX40 = (int) Math.floor(x);
					final int _radiusLoopCenterY40 = (int) Math.floor(y);
					final int _radiusLoopCenterZ40 = (int) Math.floor(z);
					final int _radiusLoopRadius40 = Math.max(0, (int) Math.floor((Mth.nextInt(RandomSource.create(), 1, 3))));
					final int _radiusLoopMinX40 = _radiusLoopCenterX40 - _radiusLoopRadius40;
					final int _radiusLoopMaxX40 = _radiusLoopCenterX40 + _radiusLoopRadius40;
					final int _radiusLoopMinY40 = _radiusLoopCenterY40 - _radiusLoopRadius40;
					final int _radiusLoopMaxY40 = _radiusLoopCenterY40 + _radiusLoopRadius40;
					final int _radiusLoopMinZ40 = _radiusLoopCenterZ40 - _radiusLoopRadius40;
					final int _radiusLoopMaxZ40 = _radiusLoopCenterZ40 + _radiusLoopRadius40;
					for (int _radiusLoopX40 = _radiusLoopMinX40; _radiusLoopX40 <= _radiusLoopMaxX40; _radiusLoopX40++) {
						for (int _radiusLoopY40 = _radiusLoopMinY40; _radiusLoopY40 <= _radiusLoopMaxY40; _radiusLoopY40++) {
							for (int _radiusLoopZ40 = _radiusLoopMinZ40; _radiusLoopZ40 <= _radiusLoopMaxZ40; _radiusLoopZ40++) {
								nX = _radiusLoopX40;
								nY = _radiusLoopY40;
								nZ = _radiusLoopZ40;
								if ((world.getBlockState(BlockPos.containing(nX, nY + 1, nZ))).getBlock() == Blocks.AIR && world.getBlockState(BlockPos.containing(nX, nY, nZ)).canOcclude()) {
									if (world.getBlockState(BlockPos.containing(nX, nY, nZ)).isCollisionShapeFullBlock(world, BlockPos.containing(nX, nY, nZ))) {
										if (Mth.nextInt(RandomSource.create(), 1, 100) <= 1) {
											world.setBlock(BlockPos.containing(nX, nY + 1, nZ), Blocks.SNOW.defaultBlockState(), 3);
										}
									}
								}
							}
						}
					}
				}
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 1, 2, 2, 2, 0.05);
			}
		}
	}

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}
}