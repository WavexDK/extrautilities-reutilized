package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModMobEffects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

public class DoomEffectStartedappliedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (!((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 1200)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(EuruModMobEffects.DOOM);
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(EuruModMobEffects.DOOM, 1200, 1));
		}
	}
}