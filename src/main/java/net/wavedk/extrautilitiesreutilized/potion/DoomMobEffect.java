package net.wavedk.extrautilitiesreutilized.potion;

import net.wavedk.extrautilitiesreutilized.procedures.DoomOnEffectActiveTickProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.DoomEffectStartedappliedProcedure;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class DoomMobEffect extends MobEffect {
	public DoomMobEffect() {
		super(MobEffectCategory.HARMFUL, -10092544);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		DoomEffectStartedappliedProcedure.execute(entity.level(), entity);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		DoomOnEffectActiveTickProcedure.execute(entity.level(), entity);
		return super.applyEffectTick(entity, amplifier);
	}
}