package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlocks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;

import java.util.Comparator;

public class SpecialGenFeatureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, String gen) {
		if (gen == null)
			return;
		boolean alreadyBlock = false;
		if ((gen).equals(BuiltInRegistries.ITEM.getKey(EuruModBlocks.NETHERSTAR_GENERATOR.get().asItem()).toString())) {
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