package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

public class SpecialGenFeatureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, String gen) {
		if (gen == null)
			return;
		boolean alreadyBlock = false;
		boolean foundBlock = false;
		double xOffset = 0;
		double radius = 0;
		double yOffset = 0;
		double zOffset = 0;
		double nX = 0;
		double nY = 0;
		double nZ = 0;
		double foundBlocks = 0;
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
}