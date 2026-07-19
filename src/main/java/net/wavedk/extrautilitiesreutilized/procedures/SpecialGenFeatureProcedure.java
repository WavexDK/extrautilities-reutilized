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
		if ((gen).equals(BuiltInRegistries.ITEM.getKey(EuruModBlocks.NETHERSTAR_GENERATOR.get().asItem()).toString())) {
			radius = 2;
			xOffset = 0 - radius;
			yOffset = 0 - radius;
			zOffset = 0 - radius;
			while (xOffset <= radius) {
				nX = x + xOffset;
				while (yOffset <= radius) {
					nY = y + yOffset;
					while (zOffset <= radius) {
						nZ = z + zOffset;
						if ((world.getBlockState(BlockPos.containing(nX, nY, nZ))).getBlock() == EuruModBlocks.NETHERSTAR_GENERATOR.get()) {
							if (nX != x && nY != y && nZ != z) {
								foundBlock = getPropertyByName((world.getBlockState(BlockPos.containing(nX, nY, nZ))), "on") instanceof BooleanProperty _getbp4 && (world.getBlockState(BlockPos.containing(nX, nY, nZ))).getValue(_getbp4);
							}
						}
						zOffset = zOffset + 1;
						if (foundBlock) {
							break;
						}
					}
					yOffset = yOffset + 1;
					if (foundBlock) {
						break;
					}
				}
				xOffset = xOffset + 1;
				if (foundBlock) {
					break;
				}
			}
			if (!foundBlock) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 10, 2, 2, 2, 0.05);
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