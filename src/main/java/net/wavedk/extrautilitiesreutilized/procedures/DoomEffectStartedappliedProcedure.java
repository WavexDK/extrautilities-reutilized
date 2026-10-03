package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModMobEffects;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

public class DoomEffectStartedappliedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 1200)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(EuruModMobEffects.DOOM);
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(EuruModMobEffects.DOOM, 1200, 1));
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("reapplied to 60s"), false);
			}
		}
	}
}