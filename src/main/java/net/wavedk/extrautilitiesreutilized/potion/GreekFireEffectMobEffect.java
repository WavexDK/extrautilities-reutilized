package net.wavedk.extrautilitiesreutilized.potion;

import net.wavedk.extrautilitiesreutilized.procedures.GreekFireEffectOnEffectActiveTickProcedure;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class GreekFireEffectMobEffect extends MobEffect {
	public GreekFireEffectMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -8974061);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		GreekFireEffectOnEffectActiveTickProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
		return super.applyEffectTick(entity, amplifier);
	}
}