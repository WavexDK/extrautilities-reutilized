package net.wavedk.extrautilitiesreutilized.world;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@EventBusSubscriber
public class TeleportBlockHandler {
	public record PortalKey(ResourceKey<Level> dimension, BlockPos pos) {
	}

	public record TeleportData(PortalKey key, ResourceKey<Level> targetDimension, double targetX, double targetY, double targetZ, SoundEvent sound, ParticleOptions particle, int requiredTicks, int triggerOffsetY) {
	}

	private static final ConcurrentHashMap<String, Integer> PLAYER_CHARGING = new ConcurrentHashMap<>();

	public static void registerPortal(ServerLevel level, ResourceKey<Level> currentDim, BlockPos pos, ResourceKey<Level> targetDim, double tx, double ty, double tz, SoundEvent sound, ParticleOptions particle, int ticks, int offsetY) {
		if (pos != null && currentDim != null && level != null) {
			PortalKey key = new PortalKey(currentDim, pos.immutable());
			PortalSavedData saveData = PortalSavedData.get(level);

			saveData.getPortals().put(key, new TeleportData(key, targetDim, tx, ty, tz, sound, particle, ticks, offsetY));
			saveData.setDirty();
		}
	}

	public static void unregisterPortal(ServerLevel level, ResourceKey<Level> currentDim, BlockPos pos) {
		if (pos != null && currentDim != null && level != null) {
			PortalKey key = new PortalKey(currentDim, pos.immutable());
			PortalSavedData saveData = PortalSavedData.get(level);

			if (saveData.getPortals().containsKey(key)) {
				saveData.getPortals().remove(key);
				saveData.setDirty();
			}
		}
	}

	private static void playPrivateSound(ServerPlayer player, SoundEvent sound, SoundSource source, double x, double y, double z) {
		if (sound == null || player == null)
			return;
		player.connection.send(new ClientboundSoundPacket(Holder.direct(sound), source, x, y, z, 1.0f, 1.0f, player.getRandom().nextLong()));
	}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		if (!(event.getLevel() instanceof ServerLevel level))
			return;
		ResourceKey<Level> currentDimension = level.dimension();
		PortalSavedData saveData = PortalSavedData.get(level);
		ConcurrentHashMap<PortalKey, TeleportData> activePortals = saveData.getPortals();
		if (activePortals.isEmpty())
			return;
		List<PortalKey> toRemovePortals = new ArrayList<>();
		for (PortalKey pKey : activePortals.keySet()) {
			if (pKey.dimension().equals(currentDimension)) {
				BlockPos portalPos = pKey.pos();
				if (level.isLoaded(portalPos) && level.getBlockState(portalPos).isAir()) {
					toRemovePortals.add(pKey);
				}
			}
		}
		if (!toRemovePortals.isEmpty()) {
			for (PortalKey key : toRemovePortals) {
				activePortals.remove(key);
				String posSuffix = "_" + key.pos().asLong();
				PLAYER_CHARGING.keySet().removeIf(k -> k.endsWith(posSuffix));
			}
			saveData.setDirty();
		}
		List<TeleportData> levelPortals = new ArrayList<>();
		for (TeleportData data : activePortals.values()) {
			if (data.key().dimension().equals(currentDimension)) {
				levelPortals.add(data);
			}
		}
		if (levelPortals.isEmpty())
			return;
		List<ServerPlayer> playersInLevel = new ArrayList<>(level.players());
		for (ServerPlayer player : playersInLevel) {
			for (TeleportData portal : levelPortals) {
				BlockPos portalPos = portal.key().pos();
				double targetCenterX = portalPos.getX() + 0.5;
				double targetCenterY = portalPos.getY() + portal.triggerOffsetY();
				double targetCenterZ = portalPos.getZ() + 0.5;
				double dx = player.getX() - targetCenterX;
				double dy = player.getY() - targetCenterY;
				double dz = player.getZ() - targetCenterZ;
				boolean isStandingOnPortal = Math.abs(dx) <= 1.25 && Math.abs(dz) <= 1.25 && dy >= -0.8 && dy <= 2.0;
				String playerChargeKey = player.getUUID() + "_" + portalPos.asLong();
				if (isStandingOnPortal) {
					int currentTicks = PLAYER_CHARGING.getOrDefault(playerChargeKey, 0) + 1;
					PLAYER_CHARGING.put(playerChargeKey, currentTicks);
					float progress = Math.min(1.0f, (float) currentTicks / (float) portal.requiredTicks());
					ParticleOptions particle = portal.particle() != null ? portal.particle() : ParticleTypes.PORTAL;
					int particleCount = 10 + (int) (progress * 20);
					double radius = 1.2 * (1.0 - progress);
					for (int i = 0; i < particleCount; i++) {
						double angle = level.getRandom().nextDouble() * Math.PI * 2.0;
						double spawnX = player.getX() + Math.cos(angle) * radius;
						double spawnY = player.getY() + 0.2 + (level.getRandom().nextDouble() * 1.5);
						double spawnZ = player.getZ() + Math.sin(angle) * radius;
						double vx = (player.getX() - spawnX) * 0.3;
						double vy = ((player.getY() + 1.0) - spawnY) * 0.3;
						double vz = (player.getZ() - spawnZ) * 0.3;
						level.sendParticles(particle, spawnX, spawnY, spawnZ, 0, vx, vy, vz, 0.5);
					}
					SoundEvent sound = portal.sound() != null ? portal.sound() : SoundEvents.PORTAL_TRIGGER;
					if (currentTicks == 1) {
						playPrivateSound(player, sound, SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ());
					}
					if (currentTicks >= portal.requiredTicks()) {
						PLAYER_CHARGING.remove(playerChargeKey);
						ServerLevel targetLevel = level.getServer().getLevel(portal.targetDimension());
						if (targetLevel != null) {
							Set<RelativeMovement> relativeMovements = Collections.emptySet();
							if (level.dimension() != portal.targetDimension()) {
								player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
								player.teleportTo(targetLevel, portal.targetX(), portal.targetY(), portal.targetZ(), relativeMovements, player.getYRot(), player.getXRot());
								player.connection.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
								for (MobEffectInstance effectinstance : player.getActiveEffects()) {
									player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effectinstance, false));
								}
								player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
							} else {
								player.teleportTo(targetLevel, portal.targetX(), portal.targetY(), portal.targetZ(), relativeMovements, player.getYRot(), player.getXRot());
							}
							SoundEvent arrivalSound = portal.sound() != null ? portal.sound() : SoundEvents.ENDERMAN_TELEPORT;
							playPrivateSound(player, arrivalSound, SoundSource.PLAYERS, portal.targetX(), portal.targetY(), portal.targetZ());
						}
					}
				} else {
					PLAYER_CHARGING.remove(playerChargeKey);
				}
			}
		}
	}
}