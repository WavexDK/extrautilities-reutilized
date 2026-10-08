package net.wavedk.extrautilitiesreutilized.potion;

import net.wavedk.extrautilitiesreutilized.procedures.GravityEffectOnEffectActiveTickProcedure;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class GravityEffectMobEffect extends MobEffect {
	public GravityEffectMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -164739);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		GravityEffectOnEffectActiveTickProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
		return super.applyEffectTick(entity, amplifier);
	}
}