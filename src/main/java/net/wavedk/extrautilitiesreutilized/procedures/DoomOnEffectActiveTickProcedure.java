package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModMobEffects;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.Minecraft;

public class DoomOnEffectActiveTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 200) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (10s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 180) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (9s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 160) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (8s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 140) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (7s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 120) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (6s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 100) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (5s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 80) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (4s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 60) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (3s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 40) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (2s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 20) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("You feel impending doom.. (1s)"), true);
		} else if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(EuruModMobEffects.DOOM) ? _livEnt.getEffect(EuruModMobEffects.DOOM).getDuration() : 0) == 2) {
			if (getEntityGameType(entity) == GameType.CREATIVE) {
				if (entity instanceof ServerPlayer _player)
					_player.setGameMode(GameType.SURVIVAL);
				entity.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("euru:doom_damage_type")))), 999999);
				if (entity instanceof ServerPlayer _player)
					_player.setGameMode(GameType.CREATIVE);
			} else if (getEntityGameType(entity) == GameType.SPECTATOR) {
				if (entity instanceof ServerPlayer _player)
					_player.setGameMode(GameType.SURVIVAL);
				entity.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("euru:doom_damage_type")))), 999999);
				if (entity instanceof ServerPlayer _player)
					_player.setGameMode(GameType.SPECTATOR);
			} else {
				entity.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("euru:doom_damage_type")))), 999999);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(""), true);
		}
	}

	private static GameType getEntityGameType(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer();
		} else if (entity instanceof Player player && player.level().isClientSide()) {
			PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
			if (playerInfo != null)
				return playerInfo.getGameMode();
		}
		return null;
	}
}