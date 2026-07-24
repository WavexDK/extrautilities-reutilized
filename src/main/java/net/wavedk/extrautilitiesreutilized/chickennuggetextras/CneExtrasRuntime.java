package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber
public final class CneExtrasRuntime {
	private static final String STOP_ON_DEATH_TAG = "CNEStopSoundsOnDeath";
	private static final String ATTACK_COOLDOWN_TAG = "CNEMultiAttackCooldown";
	private static final String ATTACK_DUE_TAG = "CNEMultiAttackDue";
	private static final String ATTACK_TARGET_TAG = "CNEMultiAttackTarget";
	private static final String LIGHT_ENABLED_TAG = "CNEDynamicLightEnabled";
	private static final String LIGHT_LEVEL_TAG = "CNEDynamicLightLevel";
	private static final String LIGHT_LAST_DIM_TAG = "CNEDynamicLightLastDimension";
	private static final String LIGHT_LAST_X_TAG = "CNEDynamicLightLastX";
	private static final String LIGHT_LAST_Y_TAG = "CNEDynamicLightLastY";
	private static final String LIGHT_LAST_Z_TAG = "CNEDynamicLightLastZ";
	private static final String PATH_TARGET_TAG = "CNEForcedPathTarget";
	private static final String PATH_SPEED_TAG = "CNEForcedPathSpeed";
	private static final String PATH_STOP_TAG = "CNEForcedPathStop";
	private static final String PATH_FALLBACK_TAG = "CNEForcedPathFallback";
	private static final String PATH_EXPIRY_TAG = "CNEForcedPathExpiry";
	private static final int PATH_TIMEOUT_TICKS = 1200;
	private static final double MIN_DISTANCE = 0.001D;
	private static final int MAX_SOUND_RECORDS = 256;
	// Key prefix every ENTITY PERSISTENT variable is stored under (see persistentEntityKey). These
	// vars must (a) survive a player death-respawn and (b) be visible client-side for GUIs/overlays.
	private static final String PERSISTENT_VAR_PREFIX = "CNEEntityVar_";
	// SERVER-side change set: UUIDs of entities whose CNEEntityVar_ data changed since the last server
	// tick. onServerTick drains this once per tick and pushes each entity's var compound to the players
	// tracking it, so client-side get-blocks read the synced value. Change-driven, so an entity whose
	// vars never move costs nothing. Set only ever touched with server-side entities (see markVarsDirty).
	private static final java.util.Set<java.util.UUID> DIRTY_VAR_ENTITIES = java.util.concurrent.ConcurrentHashMap.newKeySet();
	private static final List<SoundRecord> RECENT_SOUNDS = Collections.synchronizedList(new ArrayList<>());
	private static final ScheduledExecutorService REAL_TIME_SCHEDULER = Executors.newSingleThreadScheduledExecutor(runnable -> {
		Thread thread = new Thread(runnable, "CNE-RealTimeTimer");
		thread.setDaemon(true);
		return thread;
	});

	private CneExtrasRuntime() {
	}

	public static void setEntityLocationSafe(Entity entity, double x, double y, double z, boolean keepMotion) {
		if (entity == null || !Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) return;
		Vec3 previousMotion = entity.getDeltaMovement();
		entity.stopRiding();
		if (entity instanceof ServerPlayer player) {
			// A player's physics are simulated on the CLIENT, so setDeltaMovement on
			// the server is silently ignored - that was the "keep motion bugs out"
			// problem. Apply the velocity first, then teleport, then push the motion
			// to the client with an explicit packet so it actually keeps moving.
			Vec3 motion = keepMotion ? previousMotion : Vec3.ZERO;
			entity.setDeltaMovement(motion);
			player.hasImpulse = true;
			player.connection.teleport(x, y, z, player.getYRot(), player.getXRot());
			if (keepMotion && motion.lengthSqr() > 1.0E-6D) {
				player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(player));
			} else {
				player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(player.getId(), Vec3.ZERO));
			}
		} else {
			entity.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
			entity.setPos(x, y, z);
			entity.setDeltaMovement(keepMotion ? previousMotion : Vec3.ZERO);
			entity.hasImpulse = true;
		}
		entity.fallDistance = 0.0F;
	}

	public static double getEntityPersistentNumber(Entity entity, String name, double fallback) {
		if (entity == null) return fallback;
		CompoundTag data = entity.getPersistentData();
		String key = persistentEntityKey(name);
		return data.contains(key, net.minecraft.nbt.Tag.TAG_ANY_NUMERIC) ? data.getDouble(key) : fallback;
	}

	public static void setEntityPersistentNumber(Entity entity, String name, double value) {
		if (entity == null || !Double.isFinite(value)) return;
		CompoundTag data = entity.getPersistentData();
		String key = persistentEntityKey(name);
		// Change-detect: skip the write and the client resync when the value is unchanged. Callers
		// like per-tick resolvers rewrite the same value every tick; without this the entity would be
		// flagged dirty and its whole var compound resynced to trackers every tick for no reason.
		if (data.contains(key, net.minecraft.nbt.Tag.TAG_ANY_NUMERIC) && data.getDouble(key) == value) return;
		data.putDouble(key, value);
		markVarsDirty(entity);
	}

	public static boolean getEntityPersistentLogic(Entity entity, String name, boolean fallback) {
		if (entity == null) return fallback;
		CompoundTag data = entity.getPersistentData();
		String key = persistentEntityKey(name);
		return data.contains(key) ? data.getBoolean(key) : fallback;
	}

	public static void setEntityPersistentLogic(Entity entity, String name, boolean value) {
		if (entity == null) return;
		CompoundTag data = entity.getPersistentData();
		String key = persistentEntityKey(name);
		if (data.contains(key) && data.getBoolean(key) == value) return;
		data.putBoolean(key, value);
		markVarsDirty(entity);
	}

	public static String getEntityPersistentText(Entity entity, String name, String fallback) {
		if (entity == null) return fallback == null ? "" : fallback;
		CompoundTag data = entity.getPersistentData();
		String key = persistentEntityKey(name);
		return data.contains(key, net.minecraft.nbt.Tag.TAG_STRING) ? data.getString(key) : String.valueOf(fallback == null ? "" : fallback);
	}

	public static void setEntityPersistentText(Entity entity, String name, String value) {
		if (entity == null) return;
		CompoundTag data = entity.getPersistentData();
		String key = persistentEntityKey(name);
		String v = String.valueOf(value == null ? "" : value);
		if (data.contains(key, net.minecraft.nbt.Tag.TAG_STRING) && data.getString(key).equals(v)) return;
		data.putString(key, v);
		markVarsDirty(entity);
	}

	public static void removeEntityPersistentValue(Entity entity, String name) {
		if (entity == null) return;
		entity.getPersistentData().remove(persistentEntityKey(name));
		markVarsDirty(entity);
	}

	// Flag an entity for a client resync of its persistent vars on the next server tick. Only a
	// server-side entity is ever queued (a client-side write is a mirror of a server push and must
	// not echo back). Cheap: one UUID into a concurrent set; the flush is bounded by how many
	// distinct entities changed this tick. Lifetime vars are deliberately NOT synced this way -
	// they are server-only per-life state (see the lifetime setters, which do not call this).
	private static void markVarsDirty(Entity entity) {
		if (entity == null) return;
		try {
			if (entity.level() == null || entity.level().isClientSide()) return;
			DIRTY_VAR_ENTITIES.add(entity.getUUID());
		} catch (Exception ignored) {
		}
	}

	// Build a CompoundTag holding ONLY the entity's CNEEntityVar_ keys (copies), or null if it has
	// none. This is exactly what the sync payload carries and what the client writes back into the
	// mirrored entity's persistent data, so client-side get-blocks resolve the synced value.
	private static CompoundTag collectEntityVars(Entity entity) {
		if (entity == null) return null;
		CompoundTag data = entity.getPersistentData();
		CompoundTag out = null;
		for (String key : data.getAllKeys()) {
			if (!key.startsWith(PERSISTENT_VAR_PREFIX)) continue;
			net.minecraft.nbt.Tag tag = data.get(key);
			if (tag == null) continue;
			if (out == null) out = new CompoundTag();
			out.put(key, tag.copy());
		}
		return out;
	}

	// Send an entity's current persistent-var compound to every player tracking it (and itself, if a
	// player). An entity with no vars still sends an EMPTY compound so a client that had stale vars
	// (e.g. a var was removed) clears them. Server-side only; guarded soft.
	private static void syncEntityVars(Entity entity) {
		if (entity == null) return;
		try {
			if (entity.level() == null || entity.level().isClientSide()) return;
			CompoundTag vars = collectEntityVars(entity);
			if (vars == null) vars = new CompoundTag();
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity,
				new EntityVarSyncMessage(entity.getId(), vars));
		} catch (Exception ignored) {
		}
	}

	// Drain the change set once per server tick: resolve each dirty entity on the server and push its
	// vars to trackers. Resolution is O(levels) per dirty entity via ServerLevel.getEntity(uuid) (a
	// hash lookup per level), and the set only holds entities that CHANGED this tick, so the common
	// case is empty and the busy case is small. Cleared unconditionally so a vanished entity's stale
	// UUID cannot accumulate.
	private static void flushDirtyEntityVars(ServerTickEvent.Post event) {
		if (DIRTY_VAR_ENTITIES.isEmpty()) return;
		MinecraftServer server = event.getServer();
		java.util.List<java.util.UUID> ids = new ArrayList<>(DIRTY_VAR_ENTITIES);
		DIRTY_VAR_ENTITIES.clear();
		if (server == null) return;
		for (java.util.UUID id : ids) {
			if (id == null) continue;
			try {
				Entity resolved = null;
				for (ServerLevel level : server.getAllLevels()) {
					Entity candidate = level.getEntity(id);
					if (candidate != null) {
						resolved = candidate;
						break;
					}
				}
				if (resolved != null) syncEntityVars(resolved);
			} catch (Exception ignored) {
			}
		}
	}

	// ---- entity LIFETIME variables: stored like persistent vars but wiped when the entity
	// dies, so they only exist during the entity's current life (a respawned player or a new
	// mob starts with none). Separate "CNELifeVar_" key prefix so they never collide.
	private static String lifetimeEntityKey(String name) {
		String key = String.valueOf(name == null ? "" : name).trim().toLowerCase(Locale.ROOT);
		key = key.replace('\\', '/').replace(' ', '_');
		key = key.replaceAll("[^a-z0-9_./:-]", "_");
		key = key.replaceAll("_+", "_");
		while (key.startsWith("_")) key = key.substring(1);
		while (key.endsWith("_")) key = key.substring(0, key.length() - 1);
		return "CNELifeVar_" + (key.isBlank() ? "value" : key);
	}

	public static double getEntityLifetimeNumber(Entity entity, String name, double fallback) {
		if (entity == null) return fallback;
		CompoundTag data = entity.getPersistentData();
		String key = lifetimeEntityKey(name);
		return data.contains(key, net.minecraft.nbt.Tag.TAG_ANY_NUMERIC) ? data.getDouble(key) : fallback;
	}

	public static void setEntityLifetimeNumber(Entity entity, String name, double value) {
		if (entity == null || !Double.isFinite(value)) return;
		entity.getPersistentData().putDouble(lifetimeEntityKey(name), value);
	}

	public static boolean getEntityLifetimeLogic(Entity entity, String name, boolean fallback) {
		if (entity == null) return fallback;
		CompoundTag data = entity.getPersistentData();
		String key = lifetimeEntityKey(name);
		return data.contains(key) ? data.getBoolean(key) : fallback;
	}

	public static void setEntityLifetimeLogic(Entity entity, String name, boolean value) {
		if (entity == null) return;
		entity.getPersistentData().putBoolean(lifetimeEntityKey(name), value);
	}

	public static String getEntityLifetimeText(Entity entity, String name, String fallback) {
		if (entity == null) return fallback == null ? "" : fallback;
		CompoundTag data = entity.getPersistentData();
		String key = lifetimeEntityKey(name);
		return data.contains(key, net.minecraft.nbt.Tag.TAG_STRING) ? data.getString(key) : String.valueOf(fallback == null ? "" : fallback);
	}

	public static void setEntityLifetimeText(Entity entity, String name, String value) {
		if (entity == null) return;
		entity.getPersistentData().putString(lifetimeEntityKey(name), String.valueOf(value == null ? "" : value));
	}

	public static void removeEntityLifetimeValue(Entity entity, String name) {
		if (entity == null) return;
		entity.getPersistentData().remove(lifetimeEntityKey(name));
	}

	// Wipe all lifetime variables when the entity actually dies. Lowest priority so a death
	// that another handler cancels (the health-lock floor, a totem) keeps the variables.
	@SubscribeEvent(priority = net.neoforged.bus.api.EventPriority.LOWEST)
	public static void onClearLifetimeVarsOnDeath(LivingDeathEvent event) {
		if (event.isCanceled()) return;
		Entity entity = event.getEntity();
		if (entity == null) return;
		CompoundTag data = entity.getPersistentData();
		for (String key : new java.util.HashSet<>(data.getAllKeys())) {
			if (key.startsWith("CNELifeVar_")) data.remove(key);
		}
	}

	public static void grantAdvancementCriterion(Entity entity, String advancementId, String criterion) {
		if (!(entity instanceof ServerPlayer player) || criterion == null || criterion.trim().isEmpty()) return;
		AdvancementHolder advancement = resolveAdvancement(player, advancementId);
		if (advancement == null) return;
		player.getAdvancements().award(advancement, criterion.trim());
	}

	public static void revokeAdvancementCriterion(Entity entity, String advancementId, String criterion) {
		if (!(entity instanceof ServerPlayer player) || criterion == null || criterion.trim().isEmpty()) return;
		AdvancementHolder advancement = resolveAdvancement(player, advancementId);
		if (advancement == null) return;
		player.getAdvancements().revoke(advancement, criterion.trim());
	}

	public static boolean hasAdvancementCriterion(Entity entity, String advancementId, String criterion) {
		if (!(entity instanceof ServerPlayer player) || criterion == null || criterion.trim().isEmpty()) return false;
		AdvancementHolder advancement = resolveAdvancement(player, advancementId);
		if (advancement == null) return false;
		CriterionProgress progress = player.getAdvancements().getOrStartProgress(advancement).getCriterion(criterion.trim());
		return progress != null && progress.isDone();
	}

	public static double countAdvancementCriteria(Entity entity, String advancementId, boolean completedOnly) {
		if (!(entity instanceof ServerPlayer player)) return 0;
		AdvancementHolder advancement = resolveAdvancement(player, advancementId);
		if (advancement == null) return 0;
		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
		int completed = 0;
		for (String ignored : progress.getCompletedCriteria()) completed++;
		if (completedOnly) return completed;
		int remaining = 0;
		for (String ignored : progress.getRemainingCriteria()) remaining++;
		return completed + remaining;
	}

	// Accepts "namespace:path" but also a bare element name: "eee" first tries the
	// literal id (minecraft:eee), then this mod's own namespace (euru:eee), so
	// procedures can just use the element name without typing the full id.
	private static AdvancementHolder resolveAdvancement(ServerPlayer player, String advancementId) {
		if (advancementId == null) return null;
		String trimmed = advancementId.trim();
		if (trimmed.isEmpty()) return null;
		net.minecraft.server.ServerAdvancementManager manager = player.serverLevel().getServer().getAdvancements();
		ResourceLocation direct = ResourceLocation.tryParse(trimmed);
		AdvancementHolder holder = direct == null ? null : manager.get(direct);
		if (holder == null && !trimmed.contains(":")) {
			ResourceLocation owned = ResourceLocation.tryParse("euru:" + trimmed.toLowerCase(Locale.ROOT));
			if (owned != null) holder = manager.get(owned);
		}
		return holder;
	}

	/**
	 * Grants the next incomplete criterion of the advancement, in step order
	 * (step_1, step_2, ... numerically; other names alphabetically). Pairs with the
	 * Progress Advancement element's auto-numbered criteria so a procedure can just
	 * say "add 1 progress" without knowing any criterion names.
	 */
	public static void addAdvancementProgress(Entity entity, String advancementId) {
		if (!(entity instanceof ServerPlayer player)) return;
		AdvancementHolder advancement = resolveAdvancement(player, advancementId);
		if (advancement == null) return;
		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
		for (String name : orderedCriteria(progress)) {
			CriterionProgress criterion = progress.getCriterion(name);
			if (criterion != null && !criterion.isDone()) {
				player.getAdvancements().award(advancement, name);
				return;
			}
		}
	}

	/** Sets the completed-criteria count exactly: awards the first N steps in order and revokes the rest. */
	public static void setAdvancementProgress(Entity entity, String advancementId, double completedCount) {
		if (!(entity instanceof ServerPlayer player)) return;
		AdvancementHolder advancement = resolveAdvancement(player, advancementId);
		if (advancement == null) return;
		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
		List<String> names = orderedCriteria(progress);
		int target = (int) Math.max(0, Math.min(names.size(), completedCount));
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			CriterionProgress criterion = progress.getCriterion(name);
			boolean done = criterion != null && criterion.isDone();
			if (i < target && !done) {
				player.getAdvancements().award(advancement, name);
			} else if (i >= target && done) {
				player.getAdvancements().revoke(advancement, name);
			}
		}
	}

	private static List<String> orderedCriteria(AdvancementProgress progress) {
		List<String> names = new ArrayList<>();
		for (String name : progress.getCompletedCriteria()) names.add(name);
		for (String name : progress.getRemainingCriteria()) names.add(name);
		names.sort(CneExtrasRuntime::compareCriteriaNames);
		return names;
	}

	// step_2 must sort before step_10, so compare numeric suffixes when the prefixes match.
	private static int compareCriteriaNames(String a, String b) {
		int splitA = trailingNumberStart(a);
		int splitB = trailingNumberStart(b);
		if (splitA >= 0 && splitB >= 0 && a.substring(0, splitA).equals(b.substring(0, splitB))) {
			try {
				return Long.compare(Long.parseLong(a.substring(splitA)), Long.parseLong(b.substring(splitB)));
			} catch (NumberFormatException ignored) {
			}
		}
		return a.compareTo(b);
	}

	private static int trailingNumberStart(String value) {
		int index = value.length();
		while (index > 0 && Character.isDigit(value.charAt(index - 1))) {
			index--;
		}
		return index < value.length() && index >= 0 ? index : -1;
	}

	public static boolean configureLocalAudioInput(Entity entity, String deviceName, boolean enabled, boolean mono) {
		if (entity == null) return false;
		CompoundTag data = entity.getPersistentData();
		data.putBoolean("CNELocalAudioInputEnabled", enabled);
		data.putBoolean("CNELocalAudioInputMono", mono);
		data.putString("CNELocalAudioInputDevice", String.valueOf(deviceName == null ? "" : deviceName));
		if (FMLEnvironment.dist != Dist.CLIENT) return false;
		return ClientAudio.configureInput(deviceName, enabled, mono);
	}

	public static boolean configureLocalAudioOutput(Entity entity, String deviceName, boolean enabled, boolean mono) {
		if (entity == null) return false;
		CompoundTag data = entity.getPersistentData();
		data.putBoolean("CNELocalAudioOutputEnabled", enabled);
		data.putBoolean("CNELocalAudioOutputMono", mono);
		data.putString("CNELocalAudioOutputDevice", String.valueOf(deviceName == null ? "" : deviceName));
		if (FMLEnvironment.dist != Dist.CLIENT) return false;
		return ClientAudio.configureOutput(deviceName, enabled, mono);
	}

	public static boolean recordLocalMicrophone(Entity entity, String clipId, int ticks, boolean mono) {
		if (FMLEnvironment.dist != Dist.CLIENT) return false;
		return ClientAudio.record(clipId, Math.max(1, ticks) * 50L, mono);
	}

	public static boolean playLocalMicrophoneRecording(Entity entity, String clipId, boolean monoOutput) {
		if (FMLEnvironment.dist != Dist.CLIENT) return false;
		boolean played = ClientAudio.play(clipId, monoOutput);
		if (played) recordMicrophoneSoundForMobs(entity);
		return played;
	}

	public static boolean playLocalMicrophoneRecordingAt(Entity entity, String clipId, double rangeBlocks) {
		if (FMLEnvironment.dist != Dist.CLIENT || entity == null) return false;
		boolean played = ClientAudio.playPositional(clipId, entity, rangeBlocks);
		if (played) recordMicrophoneSoundForMobs(entity);
		return played;
	}

	public static void configureMicrophoneVoiceRange(double blocks) {
		if (FMLEnvironment.dist != Dist.CLIENT) return;
		ClientAudio.configureVoiceRange(blocks);
	}

	public static double microphoneVoiceRange() {
		if (FMLEnvironment.dist != Dist.CLIENT) return 16.0D;
		return ClientAudio.voiceRange();
	}

	public static void configureMicrophoneGain(double gain) {
		if (FMLEnvironment.dist != Dist.CLIENT) return;
		ClientAudio.configureGain(gain);
	}

	public static double microphoneGain() {
		if (FMLEnvironment.dist != Dist.CLIENT) return 1.0D;
		return ClientAudio.gain();
	}

	// Mic playback also registers a normal recent-sound record at the speaker, so the
	// "recent sound near entity" mob-listening blocks react to the player's voice. The
	// id to match on in those blocks is chickennuggetextras:microphone.
	private static void recordMicrophoneSoundForMobs(Entity entity) {
		if (entity == null || entity.level() == null || entity.level().isClientSide()) return;
		recordSound(entity.level(), "chickennuggetextras:microphone", entity.position());
	}

	/** Voice chat relay hook: live talking is mob-audible just like mic playback. */
	public static void recordVoiceForMobs(Entity entity) {
		recordMicrophoneSoundForMobs(entity);
	}

	public static void queueRealTimeWork(LevelAccessor world, double seconds, Runnable action) {
		if (action == null) return;
		long delayMs = Double.isFinite(seconds) ? (long) Math.max(0.0D, Math.min(86400.0D, seconds) * 1000.0D) : 0L;
		MinecraftServer knownServer = world instanceof Level level ? level.getServer() : null;
		final MinecraftServer captured = knownServer;
		REAL_TIME_SCHEDULER.schedule(() -> {
			MinecraftServer server = captured != null ? captured : ServerLifecycleHooks.getCurrentServer();
			if (server == null || server.isStopped()) return;
			server.execute(() -> {
				try {
					action.run();
				} catch (Exception ignored) {
				}
			});
		}, delayMs, TimeUnit.MILLISECONDS);
	}

	private static final class ClientAudio {
		static boolean configureInput(String deviceName, boolean enabled, boolean mono) {
			return CneAudioClientRuntime.configureInput(deviceName, enabled, mono);
		}

		static boolean configureOutput(String deviceName, boolean enabled, boolean mono) {
			return CneAudioClientRuntime.applyOutputDevice(deviceName, enabled);
		}

		static boolean record(String clipId, long durationMs, boolean mono) {
			return CneAudioClientRuntime.record(clipId, durationMs, mono);
		}

		static boolean play(String clipId, boolean monoOutput) {
			return CneAudioClientRuntime.play(clipId, monoOutput);
		}

		static boolean playPositional(String clipId, Entity speaker, double rangeBlocks) {
			return CneAudioClientRuntime.playPositional(clipId, speaker, rangeBlocks);
		}

		static void configureVoiceRange(double blocks) {
			CneAudioClientRuntime.configureVoiceRange(blocks);
		}

		static double voiceRange() {
			return CneAudioClientRuntime.voiceRange();
		}

		static void configureGain(double gain) {
			CneAudioClientRuntime.configureMicrophoneGain(gain);
		}

		static double gain() {
			return CneAudioClientRuntime.microphoneGain();
		}

		static double inputLevel() {
			return CneAudioClientRuntime.inputLevel();
		}
	}

	public static void setDynamicLightAt(Entity context, double x, double y, double z, double lightLevel) {
		if (context == null || context.level() == null || !Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) return;
		int level = Math.max(0, Math.min(15, (int) Math.round(lightLevel)));
		BlockPos anchor = BlockPos.containing(x, y, z);
		if (level <= 0) {
			BlockPos lit = findExistingLightNear(context.level(), anchor);
			if (lit != null) {
				removeLightBlock(context.level(), lit);
			}
			return;
		}
		BlockPos pos = findDynamicLightPos(context.level(), anchor);
		if (pos == null) return;
		placeLightBlock(context.level(), pos, level);
	}

	public static void removeDynamicLightAt(Entity context, double x, double y, double z) {
		setDynamicLightAt(context, x, y, z, 0.0D);
	}

	public static void setDynamicLightOnEntity(Entity entity, double lightLevel) {
		if (entity == null) return;
		int level = Math.max(0, Math.min(15, (int) Math.round(lightLevel)));
		if (level <= 0) {
			removeDynamicLightFromEntity(entity);
			return;
		}
		CompoundTag data = entity.getPersistentData();
		data.putBoolean(LIGHT_ENABLED_TAG, true);
		data.putInt(LIGHT_LEVEL_TAG, level);
		updateEntityDynamicLight(entity);
	}

	public static void removeDynamicLightFromEntity(Entity entity) {
		if (entity == null) return;
		removeTrackedEntityLight(entity);
		CompoundTag data = entity.getPersistentData();
		data.remove(LIGHT_ENABLED_TAG);
		data.remove(LIGHT_LEVEL_TAG);
		data.remove(LIGHT_LAST_DIM_TAG);
		data.remove(LIGHT_LAST_X_TAG);
		data.remove(LIGHT_LAST_Y_TAG);
		data.remove(LIGHT_LAST_Z_TAG);
		data.remove("CNEDynamicLightRed");
		data.remove("CNEDynamicLightGreen");
		data.remove("CNEDynamicLightBlue");
		data.remove("CNEDynamicLights");
	}

	private static void updateEntityDynamicLight(Entity entity) {
		if (entity == null || entity.level().isClientSide() || entity.isRemoved()) return;
		CompoundTag data = entity.getPersistentData();
		if (!data.getBoolean(LIGHT_ENABLED_TAG)) return;

		int level = Math.max(0, Math.min(15, data.getInt(LIGHT_LEVEL_TAG)));
		if (level <= 0) {
			removeDynamicLightFromEntity(entity);
			return;
		}

		// The light is a real minecraft:light block, which can only sit in air, in an
		// existing light block, or (waterlogged) in a water source. Demanding the exact
		// eye block made the light vanish in tall grass, crops, snow layers, on blocks
		// with odd bounding boxes (farmland, paths, soul sand) and while swimming - the
		// old position was already cleared and the new placement silently failed. Search
		// the closest hostable spot around the eye position instead.
		BlockPos anchor = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
		BlockPos nextPos = findDynamicLightPos(entity.level(), anchor);
		String dimension = entity.level().dimension().location().toString();
		String previousDimension = data.getString(LIGHT_LAST_DIM_TAG);
		BlockPos previousPos = data.contains(LIGHT_LAST_X_TAG, net.minecraft.nbt.Tag.TAG_ANY_NUMERIC)
			? BlockPos.containing(data.getInt(LIGHT_LAST_X_TAG), data.getInt(LIGHT_LAST_Y_TAG), data.getInt(LIGHT_LAST_Z_TAG))
			: null;

		if (nextPos == null) {
			// Nothing nearby can hold a light block (entity is fully enclosed in solid
			// blocks). Keep the previous light while it is still close instead of
			// flickering off; drop it once it is left behind or in another dimension.
			if (previousPos != null && (!dimension.equals(previousDimension) || !previousPos.closerThan(anchor, 8.0D))) {
				removeTrackedEntityLight(entity);
			}
			return;
		}

		if (previousPos == null || !dimension.equals(previousDimension) || !previousPos.equals(nextPos)) {
			removeTrackedEntityLight(entity);
		}

		if (placeLightBlock(entity.level(), nextPos, level)) {
			data.putString(LIGHT_LAST_DIM_TAG, dimension);
			data.putInt(LIGHT_LAST_X_TAG, nextPos.getX());
			data.putInt(LIGHT_LAST_Y_TAG, nextPos.getY());
			data.putInt(LIGHT_LAST_Z_TAG, nextPos.getZ());
		}
	}

	private static void removeTrackedEntityLight(Entity entity) {
		if (entity == null || entity.level().isClientSide()) return;
		CompoundTag data = entity.getPersistentData();
		if (!data.contains(LIGHT_LAST_X_TAG, net.minecraft.nbt.Tag.TAG_ANY_NUMERIC)) return;
		String dimension = entity.level().dimension().location().toString();
		String previousDimension = data.getString(LIGHT_LAST_DIM_TAG);
		BlockPos previousPos = BlockPos.containing(data.getInt(LIGHT_LAST_X_TAG), data.getInt(LIGHT_LAST_Y_TAG), data.getInt(LIGHT_LAST_Z_TAG));
		if (dimension.equals(previousDimension)) {
			removeLightBlock(entity.level(), previousPos);
		}
		data.remove(LIGHT_LAST_DIM_TAG);
		data.remove(LIGHT_LAST_X_TAG);
		data.remove(LIGHT_LAST_Y_TAG);
		data.remove(LIGHT_LAST_Z_TAG);
	}

	// Positions probed around the anchor when looking for a spot that can hold the
	// light block: anchor (eye), above, feet, two above, then the horizontal
	// neighbors at eye level and one above. Light only loses one level per block,
	// so a neighbor is visually identical to the exact position.
	private static final int[][] LIGHT_PLACEMENT_OFFSETS = {
		{0, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 2, 0},
		{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1},
		{1, 1, 0}, {-1, 1, 0}, {0, 1, 1}, {0, 1, -1}
	};

	private static BlockPos findDynamicLightPos(Level level, BlockPos anchor) {
		for (int[] offset : LIGHT_PLACEMENT_OFFSETS) {
			BlockPos pos = anchor.offset(offset[0], offset[1], offset[2]);
			if (canHostDynamicLight(level.getBlockState(pos))) return pos;
		}
		return null;
	}

	private static BlockPos findExistingLightNear(Level level, BlockPos anchor) {
		for (int[] offset : LIGHT_PLACEMENT_OFFSETS) {
			BlockPos pos = anchor.offset(offset[0], offset[1], offset[2]);
			if (level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.LIGHT)) return pos;
		}
		return null;
	}

	// Air, an existing light block, or a still water source (the light block can be
	// waterlogged, so swimming keeps the glow). Never replace plants/snow/etc. -
	// that would destroy them, so those positions are skipped instead.
	private static boolean canHostDynamicLight(net.minecraft.world.level.block.state.BlockState state) {
		if (state.isAir() || state.is(net.minecraft.world.level.block.Blocks.LIGHT)) return true;
		return state.is(net.minecraft.world.level.block.Blocks.WATER) && state.getFluidState().isSource();
	}

	private static boolean placeLightBlock(Level level, BlockPos pos, int lightLevel) {
		net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
		if (!canHostDynamicLight(state)) return false;
		boolean waterlogged = state.is(net.minecraft.world.level.block.Blocks.WATER)
			|| (state.is(net.minecraft.world.level.block.Blocks.LIGHT) && state.getValue(BlockStateProperties.WATERLOGGED));
		return level.setBlock(pos, net.minecraft.world.level.block.Blocks.LIGHT.defaultBlockState()
			.setValue(net.minecraft.world.level.block.LightBlock.LEVEL, lightLevel)
			.setValue(BlockStateProperties.WATERLOGGED, waterlogged), 3);
	}

	private static void removeLightBlock(Level level, BlockPos pos) {
		net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
		if (!state.is(net.minecraft.world.level.block.Blocks.LIGHT)) return;
		boolean waterlogged = state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED);
		level.setBlock(pos, waterlogged
			? net.minecraft.world.level.block.Blocks.WATER.defaultBlockState()
			: net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
	}

	// ---- Particle FX (Photon-style shapes built from vanilla particles) ----

	private static final Map<UUID, OrbitFx> ORBIT_FX = new ConcurrentHashMap<>();

	public static void fxParticleLine(Entity context, net.minecraft.core.particles.ParticleOptions particleId, double x1, double y1, double z1, double x2, double y2, double z2, double count) {
		net.minecraft.core.particles.ParticleOptions particle = particleId;
		if (particle == null || context == null || !(context.level() instanceof ServerLevel serverLevel)) return;
		int points = (int) Math.max(2, Math.min(2000, count));
		for (int i = 0; i < points; i++) {
			double t = i / (double) (points - 1);
			serverLevel.sendParticles(particle, x1 + (x2 - x1) * t, y1 + (y2 - y1) * t, z1 + (z2 - z1) * t, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	public static void fxParticleRing(Entity context, net.minecraft.core.particles.ParticleOptions particleId, double x, double y, double z, double radius, double count, boolean vertical) {
		net.minecraft.core.particles.ParticleOptions particle = particleId;
		if (particle == null || context == null || !(context.level() instanceof ServerLevel serverLevel)) return;
		int points = (int) Math.max(3, Math.min(2000, count));
		double r = Math.max(0.1D, Math.min(64.0D, radius));
		for (int i = 0; i < points; i++) {
			double angle = Math.PI * 2.0D * i / points;
			if (vertical) {
				serverLevel.sendParticles(particle, x + Math.cos(angle) * r, y + Math.sin(angle) * r, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			} else {
				serverLevel.sendParticles(particle, x + Math.cos(angle) * r, y, z + Math.sin(angle) * r, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public static void fxParticleSphere(Entity context, net.minecraft.core.particles.ParticleOptions particleId, double x, double y, double z, double radius, double count, boolean surfaceOnly) {
		net.minecraft.core.particles.ParticleOptions particle = particleId;
		if (particle == null || context == null || !(context.level() instanceof ServerLevel serverLevel)) return;
		int points = (int) Math.max(1, Math.min(2000, count));
		double r = Math.max(0.1D, Math.min(64.0D, radius));
		RandomSource random = serverLevel.random;
		for (int i = 0; i < points; i++) {
			double up = random.nextDouble() * 2.0D - 1.0D;
			double theta = random.nextDouble() * Math.PI * 2.0D;
			double ring = Math.sqrt(Math.max(0.0D, 1.0D - up * up));
			double dist = surfaceOnly ? r : r * Math.cbrt(random.nextDouble());
			serverLevel.sendParticles(particle, x + ring * Math.cos(theta) * dist, y + up * dist, z + ring * Math.sin(theta) * dist, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	public static void fxParticleHelix(Entity context, net.minecraft.core.particles.ParticleOptions particleId, double x, double y, double z, double radius, double height, double turns, double count) {
		net.minecraft.core.particles.ParticleOptions particle = particleId;
		if (particle == null || context == null || !(context.level() instanceof ServerLevel serverLevel)) return;
		int points = (int) Math.max(2, Math.min(2000, count));
		double r = Math.max(0.1D, Math.min(64.0D, radius));
		double h = Math.max(0.0D, Math.min(320.0D, height));
		double rotations = Math.max(0.25D, Math.min(64.0D, turns));
		for (int i = 0; i < points; i++) {
			double t = i / (double) (points - 1);
			double angle = t * rotations * Math.PI * 2.0D;
			serverLevel.sendParticles(particle, x + Math.cos(angle) * r, y + t * h, z + Math.sin(angle) * r, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	public static void fxParticleBurst(Entity context, net.minecraft.core.particles.ParticleOptions particleId, double x, double y, double z, double count, double speed) {
		net.minecraft.core.particles.ParticleOptions particle = particleId;
		if (particle == null || context == null || !(context.level() instanceof ServerLevel serverLevel)) return;
		int amount = (int) Math.max(1, Math.min(2000, count));
		double burstSpeed = Math.max(0.0D, Math.min(4.0D, speed));
		serverLevel.sendParticles(particle, x, y, z, amount, 0.25D, 0.25D, 0.25D, burstSpeed);
	}

	public static void fxParticleOrbit(Entity target, net.minecraft.core.particles.ParticleOptions particleId, double radius, double durationTicks, double perTick) {
		if (target == null) return;
		if (durationTicks <= 0) {
			ORBIT_FX.remove(target.getUUID());
			return;
		}
		OrbitFx fx = new OrbitFx();
		fx.particleId = particleId;
		fx.radius = Math.max(0.25D, Math.min(16.0D, radius));
		fx.perTick = (int) Math.max(1, Math.min(16, perTick));
		fx.ticksLeft = (int) Math.max(1, Math.min(72000, durationTicks));
		ORBIT_FX.put(target.getUUID(), fx);
	}

	private static void tickOrbitFx(Entity entity) {
		if (entity == null || ORBIT_FX.isEmpty()) return;
		OrbitFx fx = ORBIT_FX.get(entity.getUUID());
		if (fx == null || !(entity.level() instanceof ServerLevel serverLevel)) return;
		if (entity.isRemoved() || fx.ticksLeft-- <= 0) {
			ORBIT_FX.remove(entity.getUUID());
			return;
		}
		net.minecraft.core.particles.ParticleOptions particle = fx.particleId;
		if (particle == null) {
			ORBIT_FX.remove(entity.getUUID());
			return;
		}
		fx.angle += 0.25D;
		double centerY = entity.getY() + entity.getBbHeight() * 0.5D;
		for (int i = 0; i < fx.perTick; i++) {
			double angle = fx.angle + (Math.PI * 2.0D * i / fx.perTick);
			serverLevel.sendParticles(particle, entity.getX() + Math.cos(angle) * fx.radius, centerY, entity.getZ() + Math.sin(angle) * fx.radius, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	private static final class OrbitFx {
		net.minecraft.core.particles.ParticleOptions particleId;
		double radius;
		int perTick;
		int ticksLeft;
		double angle;
	}

	// ---- Animated particle emitters (motion over time, Photon-style) ----

	private static final Map<UUID, FxEmitter> POSITION_EMITTERS = new ConcurrentHashMap<>();
	private static final Map<UUID, FxEmitter> ENTITY_EMITTERS = new ConcurrentHashMap<>();

	public static void fxEmitterStart(Entity context, String shape, net.minecraft.core.particles.ParticleOptions particleId, double x, double y, double z, double radius, double intensity, double durationTicks) {
		if (context == null || !(context.level() instanceof ServerLevel serverLevel)) return;
		FxEmitter emitter = new FxEmitter();
		emitter.dimension = serverLevel.dimension().location().toString();
		emitter.shape = normalizeEmitterShape(shape);
		emitter.particleId = particleId;
		emitter.x = x;
		emitter.y = y;
		emitter.z = z;
		emitter.radius = Math.max(0.25D, Math.min(64.0D, radius));
		emitter.rate = (int) Math.max(1, Math.min(64, intensity));
		emitter.ticksLeft = (int) Math.max(1, Math.min(72000, durationTicks));
		POSITION_EMITTERS.put(UUID.randomUUID(), emitter);
	}

	public static void fxEmitterStopNear(Entity context, double x, double y, double z, double radius) {
		if (context == null || context.level() == null) return;
		String dimension = context.level().dimension().location().toString();
		double radiusSq = Math.max(0.25D, radius) * Math.max(0.25D, radius);
		POSITION_EMITTERS.entrySet().removeIf(entry -> {
			FxEmitter emitter = entry.getValue();
			if (!emitter.dimension.equals(dimension)) return false;
			double dx = emitter.x - x;
			double dy = emitter.y - y;
			double dz = emitter.z - z;
			return dx * dx + dy * dy + dz * dz <= radiusSq;
		});
	}

	public static void fxEmitterAttach(Entity target, String shape, net.minecraft.core.particles.ParticleOptions particleId, double radius, double intensity, double durationTicks) {
		if (target == null) return;
		if (durationTicks <= 0) {
			ENTITY_EMITTERS.remove(target.getUUID());
			return;
		}
		FxEmitter emitter = new FxEmitter();
		emitter.shape = normalizeEmitterShape(shape);
		emitter.particleId = particleId;
		emitter.radius = Math.max(0.25D, Math.min(64.0D, radius));
		emitter.rate = (int) Math.max(1, Math.min(64, intensity));
		emitter.ticksLeft = (int) Math.max(1, Math.min(72000, durationTicks));
		ENTITY_EMITTERS.put(target.getUUID(), emitter);
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event) {
		tickPositionEmitters(event);
		tickExplosionJobs(event);
		flushDirtyEntityVars(event);
	}

	private static void tickPositionEmitters(ServerTickEvent.Post event) {
		if (POSITION_EMITTERS.isEmpty()) return;
		Iterator<Map.Entry<UUID, FxEmitter>> iterator = POSITION_EMITTERS.entrySet().iterator();
		while (iterator.hasNext()) {
			FxEmitter emitter = iterator.next().getValue();
			ServerLevel level = resolveServerLevel(event.getServer(), emitter.dimension);
			if (level == null || --emitter.ticksLeft < 0) {
				iterator.remove();
				continue;
			}
			spawnEmitterShape(level, emitter, emitter.x, emitter.y, emitter.z);
		}
	}

	private static ServerLevel resolveServerLevel(MinecraftServer server, String dimension) {
		if (server == null) return null;
		for (ServerLevel candidate : server.getAllLevels()) {
			if (candidate.dimension().location().toString().equals(dimension)) return candidate;
		}
		return null;
	}

	// ---- Temporary explosion (sectioned destroy, timed regenerate) ----
	//
	// Destruction scans outward from the center in shells. To avoid a single-tick
	// lag spike on a big blast, only a budget of blocks is removed per tick, so a
	// large explosion visibly layers outward. Every removed block is snapshotted
	// (state + optional block-entity NBT) and put back after the regenerate delay,
	// in the chosen order. Inventory blocks are emptied before removal (no item
	// drops) but their NBT is kept so they regenerate with their full contents.

	private static final List<ExplosionJob> EXPLOSION_JOBS = Collections.synchronizedList(new ArrayList<>());
	private static final int EXPLOSION_BLOCKS_PER_TICK = 8000;
	// At or below this many blocks we build a compact CENTRE-OUT (by distance) target list up front, so
	// the blast destroys as an expanding sphere (temporary blasts, capped at ~2.1M, always take this
	// path). Above it (only enormous PERMANENT blasts) we stream positions in raster order with O(1)
	// memory - the cap keeps the one-time build from freezing the cast tick for too long.
	private static final int PACKED_MAX_BLOCKS = 6_000_000;
	// A temporary explosion must remember every block it destroys to heal it later, so its radius is
	// bounded by that snapshot's memory. A permanent explosion keeps nothing, so it can be far bigger -
	// the high cap is only a sanity backstop so a typo'd radius can't grind for hours.
	private static final int EXPLOSION_MAX_TEMP_RADIUS = 80;
	private static final int EXPLOSION_MAX_PERM_RADIUS = 256;
	private static final int EXPLOSION_MAX_JOBS = 12;
	// Give up regenerating after ~10 min of zero progress (the player left and the
	// chunks never reloaded), so a stuck job cannot pin memory forever.
	private static final int EXPLOSION_MAX_STALL_TICKS = 12000;
	private static final int EXPLOSION_FLYING_CAP_MAX = 256; // ceiling on "blocks flying" debris per explosion (scaled up with radius)

	public static void temporaryExplosion(Entity context, double x, double y, double z, double radius, double regenTicks, double regenSpeed,
			String regenDirection, boolean protectTileEntities, boolean saveBlockNbt, boolean breakFluids, boolean sendBlocksFlying) {
		if (context == null || !(context.level() instanceof ServerLevel level)) return;
		if (EXPLOSION_JOBS.size() >= EXPLOSION_MAX_JOBS) return; // backpressure: too many active explosions
		int r = (int) Math.max(1, Math.min(EXPLOSION_MAX_TEMP_RADIUS, Math.round(radius)));
		BlockPos center = BlockPos.containing(x, y, z);

		ExplosionJob job = new ExplosionJob();
		job.dimension = level.dimension().location().toString();
		configureExplosion(job, level, center, r);
		// "regen after N ticks" is measured from when the DESTRUCTION FINISHES (set in tick()), not from
		// the trigger - so on a big blast that takes many ticks to carve out, the delay you pick is the
		// gap after the crater forms, and regen never starts mid-explosion.
		job.regenDelayTicks = (int) Math.max(0L, Math.min(720000L, Math.round(regenTicks)));
		job.regenDirection = normalizeRegenDirection(regenDirection);
		job.protectTileEntities = protectTileEntities;
		job.saveBlockNbt = saveBlockNbt;
		job.regenSpeed = (int) Math.max(1L, Math.min(200000L, Math.round(regenSpeed)));
		job.breakFluids = breakFluids;
		job.sendBlocksFlying = sendBlocksFlying;
		EXPLOSION_JOBS.add(job);

		playExplosionEffects(level, center, r);
	}

	// A real destructive explosion: destroys a sphere of blocks and does NOT regenerate them. Same
	// sectioned, perf-safe destruction as the temporary explosion, with optional fluid breaking,
	// flying debris (which here lands and stays, since the blocks are thrown for good), and loot drops.
	public static void permanentExplosion(Entity context, double x, double y, double z, double radius,
			boolean breakFluids, boolean sendBlocksFlying, boolean dropItems) {
		if (context == null || !(context.level() instanceof ServerLevel level)) return;
		if (EXPLOSION_JOBS.size() >= EXPLOSION_MAX_JOBS) return;
		int r = (int) Math.max(1, Math.min(EXPLOSION_MAX_PERM_RADIUS, Math.round(radius)));
		BlockPos center = BlockPos.containing(x, y, z);

		ExplosionJob job = new ExplosionJob();
		job.dimension = level.dimension().location().toString();
		job.permanent = true; // destroyed for good - the job ends after destruction, no regen
		job.dropItems = dropItems;
		configureExplosion(job, level, center, r);
		job.breakFluids = breakFluids;
		job.sendBlocksFlying = sendBlocksFlying;
		EXPLOSION_JOBS.add(job);

		playExplosionEffects(level, center, r);
	}

	// Either build a center-out list of every target up front (small/medium blasts - the nicest
	// destruction order) OR, once that list would be too big to hold, switch to streaming the
	// positions in raster order with O(1) memory so the radius is effectively unbounded. Also sets
	// the per-blast radius (drives throw force) and the flying-debris cap (more debris for bigger blasts).
	private static void configureExplosion(ExplosionJob job, ServerLevel level, BlockPos center, int r) {
		job.radius = r;
		job.centerX = center.getX();
		job.centerY = center.getY();
		job.centerZ = center.getZ();
		job.flyingCap = Math.min(EXPLOSION_FLYING_CAP_MAX, 64 + r * 4);
		long estBlocks = (long) (4.18879D * (double) r * (double) r * (double) r); // ~(4/3)*pi*r^3
		// Throw the debris cap's worth of blocks spread EVENLY across the whole blast (not all from the
		// first blocks near the centre) so blocks keep flying as the destruction expands outward.
		job.flyingChance = (float) Math.min(1.0D, (double) job.flyingCap / Math.max(1.0D, (double) estBlocks));
		if (estBlocks <= PACKED_MAX_BLOCKS) {
			// Compact centre-out (by distance) target list: the blast destroys as an expanding SPHERE.
			job.packedTargets = buildPackedCenterOut(level, center, r);
			// Only a temporary blast snapshots blocks (to heal them); presize for it. A permanent blast
			// leaves removed as the empty default (a safe no-op for forceRegenAll, and no wasted memory).
			if (!job.permanent) {
				job.removed = new ArrayList<>(job.packedTargets.length);
			}
		} else {
			// Only reached by enormous PERMANENT blasts (temporary ones are capped well under the
			// threshold). Stream the bounding box in raster order, sphere-tested per cell, O(1) memory.
			job.streaming = true;
			job.sRadiusSq = (long) r * (long) r;
			job.sMinX = center.getX() - r;
			job.sMaxX = center.getX() + r;
			job.sMinZ = center.getZ() - r;
			job.sMaxZ = center.getZ() + r;
			job.sMinY = Math.max(level.getMinBuildHeight(), center.getY() - r);
			job.sMaxY = Math.min(level.getMaxBuildHeight() - 1, center.getY() + r);
		}
	}

	// The blast's sound + an opening particle burst sized to the radius. A lone vanilla EXPLOSION_EMITTER
	// is a single fixed-size puff, so a huge blast looked the same as a small one; we add more puffs
	// spread across the radius for a bigger one (spread capped near the particle view distance so they
	// stay visible). The ongoing destruction itself reveals the blast's true extent over the next ticks.
	private static void playExplosionEffects(ServerLevel level, BlockPos center, int r) {
		RandomSource rnd = level.random;
		double cx = center.getX() + 0.5D;
		double cy = center.getY() + 0.5D;
		double cz = center.getZ() + 0.5D;
		level.playSound(null, center, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 4.0F, 0.8F + rnd.nextFloat() * 0.2F);
		level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, cx, cy, cz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		if (r <= 3) return;
		double spread = Math.min(r, 30);
		double vSpread = Math.min(r, 20);
		int puffs = Math.min(64, 3 + r);
		for (int i = 0; i < puffs; i++) {
			double a = rnd.nextDouble() * Math.PI * 2.0D;
			double rad = rnd.nextDouble() * spread;
			double px = cx + Math.cos(a) * rad;
			double pz = cz + Math.sin(a) * rad;
			double py = cy + (rnd.nextDouble() * 2.0D - 1.0D) * vSpread;
			level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, px, py, pz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	// Build every in-sphere offset in CENTRE-OUT order (ascending squared distance) as a packed int[]
	// via a counting sort - so the blast destroys as an expanding sphere. Each entry packs (dx,dy,dz)
	// into one int (10 bits each, biased by 512; valid for |offset| <= 511) instead of a TargetPos
	// object, so even a multi-million-block blast costs ~4 bytes/block, not ~32.
	private static int[] buildPackedCenterOut(ServerLevel level, BlockPos center, int r) {
		int rSq = r * r;
		int minY = level.getMinBuildHeight();
		int maxY = level.getMaxBuildHeight() - 1;
		int cy = center.getY();
		int[] count = new int[rSq + 1]; // count[d] = number of cells at squared distance d
		for (int dx = -r; dx <= r; dx++) {
			int dxSq = dx * dx;
			for (int dz = -r; dz <= r; dz++) {
				int dxz = dxSq + dz * dz;
				if (dxz > rSq) continue;
				for (int dy = -r; dy <= r; dy++) {
					int py = cy + dy;
					if (py < minY || py > maxY) continue;
					int distSq = dxz + dy * dy;
					if (distSq > rSq) continue;
					count[distSq]++;
				}
			}
		}
		int total = 0; // prefix sum: count[d] becomes the start index of squared-distance band d
		for (int d = 0; d <= rSq; d++) {
			int c = count[d];
			count[d] = total;
			total += c;
		}
		int[] packed = new int[total];
		for (int dx = -r; dx <= r; dx++) {
			int dxSq = dx * dx;
			for (int dz = -r; dz <= r; dz++) {
				int dxz = dxSq + dz * dz;
				if (dxz > rSq) continue;
				for (int dy = -r; dy <= r; dy++) {
					int py = cy + dy;
					if (py < minY || py > maxY) continue;
					int distSq = dxz + dy * dy;
					if (distSq > rSq) continue;
					packed[count[distSq]++] = ((dx + 512) << 20) | ((dy + 512) << 10) | (dz + 512);
				}
			}
		}
		return packed;
	}

	private static void tickExplosionJobs(ServerTickEvent.Post event) {
		if (EXPLOSION_JOBS.isEmpty()) return;
		synchronized (EXPLOSION_JOBS) {
			Iterator<ExplosionJob> iterator = EXPLOSION_JOBS.iterator();
			while (iterator.hasNext()) {
				ExplosionJob job = iterator.next();
				ServerLevel level = resolveServerLevel(event.getServer(), job.dimension);
				if (level == null || job.tick(level)) {
					iterator.remove();
				}
			}
		}
	}

	// On shutdown, synchronously restore every block still in a destroyed state so no
	// area is left as a permanent hole across a restart (jobs are in-memory only).
	@SubscribeEvent
	public static void onServerStopping(net.neoforged.neoforge.event.server.ServerStoppingEvent event) {
		synchronized (EXPLOSION_JOBS) {
			for (ExplosionJob job : EXPLOSION_JOBS) {
				ServerLevel level = resolveServerLevel(event.getServer(), job.dimension);
				if (level != null) job.forceRegenAll(level);
			}
			EXPLOSION_JOBS.clear();
		}
	}

	private static String normalizeRegenDirection(String dir) {
		String value = dir == null ? "" : dir.trim().toLowerCase(Locale.ROOT);
		return switch (value) {
			case "outward_in", "upward", "downward" -> value;
			default -> "inward_out";
		};
	}

	private static final class TargetPos {
		final int x;
		final int y;
		final int z;
		final int distSq;

		TargetPos(int x, int y, int z, int distSq) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.distSq = distSq;
		}
	}

	private static final class BlockSnapshot {
		final int x;
		final int y;
		final int z;
		final BlockState state;
		final CompoundTag nbt;
		final boolean contentHolder;
		final int distSq;

		BlockSnapshot(int x, int y, int z, BlockState state, CompoundTag nbt, boolean contentHolder, int distSq) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.state = state;
			this.nbt = nbt;
			this.contentHolder = contentHolder;
			this.distSq = distSq;
		}
	}

	private static final class ExplosionJob {
		String dimension;
		int[] packedTargets;
		int destroyIndex;
		List<BlockSnapshot> removed = new ArrayList<>();
		List<BlockSnapshot> deferred = new ArrayList<>();
		long regenAtGameTime;
		int regenDelayTicks;
		int regenIndex;
		int regenStall;
		boolean regenSorted;
		String regenDirection = "inward_out";
		boolean protectTileEntities;
		boolean saveBlockNbt;
		int regenSpeed = EXPLOSION_BLOCKS_PER_TICK;
		boolean breakFluids;
		boolean sendBlocksFlying;
		boolean permanent;
		boolean dropItems;
		int centerX;
		int centerY;
		int centerZ;
		int radius;
		int flyingCap = EXPLOSION_FLYING_CAP_MAX;
		float flyingChance = 1.0F;
		int flyingSpawned;
		int phase;
		// Streaming destruction (used when the blast is too big to hold a target list): a raster
		// cursor over the bounding box, sphere-tested per cell, so memory stays O(1) at any radius.
		boolean streaming;
		boolean streamStarted;
		boolean streamDone;
		long sRadiusSq;
		int sMinX;
		int sMinY;
		int sMinZ;
		int sMaxX;
		int sMaxY;
		int sMaxZ;
		int sx;
		int sy;
		int sz;
		int sStep;

		boolean tick(ServerLevel level) {
			if (phase == 0) {
				int budget = EXPLOSION_BLOCKS_PER_TICK;
				if (streaming) {
					// The budget counts cells EXAMINED, in or out of the sphere - so a tick's work stays
					// bounded even near the poles where the bounding box is mostly outside the sphere
					// (otherwise one tick could scan a whole empty layer). Such ticks just destroy fewer
					// blocks; they never stall.
					while (budget-- > 0) {
						if (!advanceStreamCursor()) {
							streamDone = true;
							break;
						}
						long ddx = (long) sx - centerX;
						long ddy = (long) sy - centerY;
						long ddz = (long) sz - centerZ;
						long dsq = ddx * ddx + ddy * ddy + ddz * ddz;
						if (dsq > sRadiusSq) continue; // outside the sphere - skip (it still cost a budget unit)
						TargetPos target = new TargetPos(sx, sy, sz, (int) Math.min(Integer.MAX_VALUE, dsq));
						if (destroyBlock(level, target) && (budget & 1023) == 0) {
							level.sendParticles(ParticleTypes.EXPLOSION, sx + 0.5D, sy + 0.5D, sz + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
						}
					}
					if (streamDone) {
						if (permanent) return true; // permanent: no regen, the job is done after destruction
						regenAtGameTime = level.getGameTime() + regenDelayTicks; // delay counts from NOW (destruction done)
						phase = 1;
					}
					return false;
				}
				// Centre-out path: unpack each offset (dx,dy,dz) and destroy it. packedTargets is already in
				// ascending-distance order, so the crater opens as an expanding sphere.
				while (destroyIndex < packedTargets.length && budget-- > 0) {
					int pk = packedTargets[destroyIndex++];
					int dx = ((pk >> 20) & 1023) - 512;
					int dy = ((pk >> 10) & 1023) - 512;
					int dz = (pk & 1023) - 512;
					TargetPos target = new TargetPos(centerX + dx, centerY + dy, centerZ + dz, dx * dx + dy * dy + dz * dz);
					if (destroyBlock(level, target) && (destroyIndex & 1023) == 0) {
						level.sendParticles(ParticleTypes.EXPLOSION, target.x + 0.5D, target.y + 0.5D, target.z + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
					}
				}
				if (destroyIndex >= packedTargets.length) {
					packedTargets = null;
					if (permanent) return true; // permanent: no regen, the job is done after destruction
					regenAtGameTime = level.getGameTime() + regenDelayTicks; // delay counts from NOW (destruction done)
					phase = 1;
				}
				return false;
			}
			if (phase == 1) {
				if (level.getGameTime() >= regenAtGameTime) {
					sortForRegen();
					regenSorted = true;
					phase = 2;
				}
				return false;
			}
			// phase 2: regenerate, deferring positions whose chunk is currently unloaded.
			// regenSpeed = blocks restored per tick (the "regen speed" the block exposes).
			int budget = regenSpeed;
			boolean anyResolved = false;
			while (regenIndex < removed.size() && budget-- > 0) {
				BlockSnapshot snap = removed.get(regenIndex++);
				if (regenBlock(level, snap)) {
					anyResolved = true;
				} else {
					deferred.add(snap); // chunk unloaded; try again when it reloads
				}
			}
			if (regenIndex >= removed.size()) {
				if (deferred.isEmpty()) return true; // everything restored
				removed = deferred;
				deferred = new ArrayList<>();
				regenIndex = 0;
			}
			if (anyResolved) {
				regenStall = 0;
			} else if (++regenStall > EXPLOSION_MAX_STALL_TICKS) {
				return true; // chunks stayed unloaded too long; stop trying
			}
			return false;
		}

		// Advance the raster cursor to the next cell in the bounding box (streaming destruction). Walks
		// X fastest, then Z, then Y, so the destruction sweeps the box once. Returns false when the whole
		// box has been visited. The caller sphere-tests each cell (so the per-tick budget is bounded).
		private boolean advanceStreamCursor() {
			if (!streamStarted) {
				streamStarted = true;
				sStep = -1;
				return nextStreamLayer(); // start on the centre Y layer
			}
			sx++;
			if (sx <= sMaxX) return true;
			sx = sMinX;
			sz++;
			if (sz <= sMaxZ) return true;
			return nextStreamLayer(); // this Y layer is done - move to the next one (centre-out)
		}

		// Step sy through the Y range CENTRE-OUT: centerY, +1, -1, +2, -2, ... so the blast destroys the
		// caster's own layer FIRST (immediately visible) and expands up/down, instead of sweeping
		// bottom-to-top (which looks like nothing is happening for several seconds on a big blast).
		// Skips layers clipped off by world height. Returns false once every layer has been visited.
		private boolean nextStreamLayer() {
			while (true) {
				sStep++;
				if (sStep > 2 * radius) return false;
				int off = (sStep & 1) == 1 ? (sStep + 1) / 2 : -(sStep / 2);
				int y = centerY + off;
				if (y < sMinY || y > sMaxY) continue; // clipped by world height
				sy = y;
				sx = sMinX;
				sz = sMinZ;
				return true;
			}
		}

		void forceRegenAll(ServerLevel level) {
			if (!regenSorted) {
				sortForRegen();
				regenSorted = true;
			}
			for (int i = regenIndex; i < removed.size(); i++) forcePlace(level, removed.get(i));
			for (BlockSnapshot snap : deferred) forcePlace(level, snap);
		}

		private void forcePlace(ServerLevel level, BlockSnapshot snap) {
			try {
				BlockPos pos = new BlockPos(snap.x, snap.y, snap.z);
				level.setBlock(pos, snap.state, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
				restoreNbt(level, pos, snap);
			} catch (Exception ignored) {
			}
		}

		private boolean destroyBlock(ServerLevel level, TargetPos target) {
			BlockPos pos = new BlockPos(target.x, target.y, target.z);
			BlockState state = level.getBlockState(pos);
			// Fluids (standalone water/lava) are skipped unless "break fluids" is on, in which case
			// they are removed and regenerated like any other block (the fluid state is snapshotted).
			if (state.isAir() || (!breakFluids && state.getBlock() instanceof LiquidBlock)) return false;
			if (state.getDestroySpeed(level, pos) < 0.0F) return false; // unbreakable (bedrock, barrier...)
			BlockEntity be = level.getBlockEntity(pos);
			if (be != null && protectTileEntities) return false; // leave block entities intact

			// "Send blocks flying": a capped, sampled subset of simple (non-fluid, non-block-entity)
			// blocks become falling-block debris launched outward. fall() removes the block, so we skip
			// our own removal for those. The sample rate (flyingChance) is set so the cap's worth of
			// debris is spread EVENLY across the whole blast - so blocks keep flying from the expanding
			// front as it carves outward, not only at the start. Temporary debris vanishes mid-flight
			// (no litter, the crater regenerates); permanent debris lands and stays.
			boolean flew = sendBlocksFlying && be == null && flyingSpawned < flyingCap
				&& !(state.getBlock() instanceof LiquidBlock) && level.random.nextFloat() < flyingChance
				&& spawnFlyingBlock(level, pos, state);

			// Permanent + drop items: a real destructive blast - let vanilla break the block so it
			// drops its loot (and any container's contents). No snapshot (nothing regenerates).
			if (permanent && dropItems) {
				if (!flew) level.destroyBlock(pos, true);
				return true;
			}

			CompoundTag nbt = null;
			boolean contentHolder = be instanceof Container || be instanceof Clearable;
			if (be != null) {
				// A temporary explosion snapshots the block entity's NBT - the only way to bring its
				// contents back, and it stops lecterns/campfires/etc. losing data. A permanent (no-drop)
				// blast keeps nothing, so there's no point saving it.
				if (!permanent) {
					try {
						nbt = be.saveWithFullMetadata(level.registryAccess());
					} catch (Exception ignored) {
					}
				}
				// Empty inventories so onRemove drops nothing; for a temporary blast the contents live on
				// in the snapshot, for a permanent no-drop blast they are simply gone (clean removal).
				Clearable.tryClear(be);
			}
			if (!flew) {
				int flags = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS;
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), flags);
			}
			// Only a temporary explosion needs the snapshot (it regenerates); a permanent one keeps
			// nothing, which also keeps memory down on a huge blast.
			if (!permanent) {
				removed.add(new BlockSnapshot(target.x, target.y, target.z, state, nbt, contentHolder, target.distSq));
			}
			return true;
		}

		// Spawns a falling-block entity for the block at pos and throws it outward + up. fall() also
		// removes the block, so the caller skips its own setBlock. The entity drops nothing.
		private boolean spawnFlyingBlock(ServerLevel level, BlockPos pos, BlockState state) {
			try {
				net.minecraft.world.entity.item.FallingBlockEntity fbe = net.minecraft.world.entity.item.FallingBlockEntity.fall(level, pos, state);
				if (fbe == null) return false;
				fbe.dropItem = false;
				fbe.disableDrop();
				// The bigger the blast, the harder + farther the throw and the longer the flight. A high
				// baseline means even a small blast clearly launches blocks (not just a hop); the cap keeps
				// the fastest component (force*1.2 = 3.84) safely under the entity-velocity packet limit
				// (~4.096 blocks/tick) so the throw still syncs to clients instead of clamping to no motion.
				double force = Math.min(3.2D, 1.4D + radius * 0.075D);
				if (!permanent) {
					// Temporary explosion: tag it so CneFlyingDebrisMixin discards it the instant it lands
					// instead of placing a block - the thrown debris leaves no litter and the crater heals
					// cleanly, no matter how far it flies. time is just a backstop in case it never lands
					// (e.g. flies out over a void); a bigger blast gets a longer backstop.
					fbe.getPersistentData().putBoolean("CneNoPlace", true);
					int flightTicks = (int) Math.min(560L, 140L + (long) radius * 6L);
					fbe.time = 600 - flightTicks;
				}
				// Permanent explosion: not tagged + default lifetime, so the thrown block lands and stays
				// (the block is genuinely relocated).
				RandomSource rnd = level.random;
				double dx = pos.getX() + 0.5D - centerX;
				double dz = pos.getZ() + 0.5D - centerZ;
				double dist = Math.sqrt(dx * dx + dz * dz);
				double ux;
				double uz;
				if (dist > 0.75D) {
					ux = dx / dist;
					uz = dz / dist;
				} else {
					// A block at the very center has no outward direction - give it a random one so it
					// actually flies outward instead of straight up (this was why debris "just fell").
					double a = rnd.nextDouble() * Math.PI * 2.0D;
					ux = Math.cos(a);
					uz = Math.sin(a);
				}
				double power = force * (0.55D + rnd.nextDouble() * 0.5D);  // outward push (blocks/tick)
				double up = force * (0.7D + rnd.nextDouble() * 0.5D);      // upward launch (higher arc = longer airtime)
				fbe.setDeltaMovement(ux * power, up, uz * power);
				fbe.hasImpulse = true; // make the thrown velocity sync to clients
				flyingSpawned++;
				return true;
			} catch (Exception ignored) {
				return false;
			}
		}

		private void sortForRegen() {
			// removed is already in centre-out (ascending squared-distance) order from destruction, so
			// "inward_out" is a no-op and "outward_in" is just a reverse; "upward"/"downward" re-sort by
			// height. Bounded by the temp radius cap (a permanent blast never regenerates), so this is a
			// quick one-time step, not the multi-million-entry stall the old streaming path risked.
			switch (regenDirection) {
				case "outward_in" -> removed.sort((a, b) -> Integer.compare(b.distSq, a.distSq));
				case "upward" -> removed.sort((a, b) -> Integer.compare(a.y, b.y));
				case "downward" -> removed.sort((a, b) -> Integer.compare(b.y, a.y));
				default -> removed.sort((a, b) -> Integer.compare(a.distSq, b.distSq));
			}
		}

		// Returns true when the position is resolved (placed, or intentionally skipped to
		// avoid clobbering a player's block); false when the chunk is unloaded so it
		// should be retried later.
		private boolean regenBlock(ServerLevel level, BlockSnapshot snap) {
			BlockPos pos = new BlockPos(snap.x, snap.y, snap.z);
			if (!level.hasChunkAt(pos)) return false;
			BlockState current = level.getBlockState(pos);
			if (current.isAir() || current.canBeReplaced()) {
				level.setBlock(pos, snap.state, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
				restoreNbt(level, pos, snap);
			}
			return true;
		}

		// Inventory/content blocks always come back with their contents; other block
		// entities only restore their NBT when "save block NBT" is enabled.
		private void restoreNbt(ServerLevel level, BlockPos pos, BlockSnapshot snap) {
			if (snap.nbt == null || !(saveBlockNbt || snap.contentHolder)) return;
			BlockEntity be = level.getBlockEntity(pos);
			if (be == null) return;
			try {
				be.loadWithComponents(snap.nbt, level.registryAccess());
				be.setChanged();
			} catch (Exception ignored) {
			}
		}
	}

	private static void tickEntityEmitter(Entity entity) {
		if (entity == null || ENTITY_EMITTERS.isEmpty()) return;
		FxEmitter emitter = ENTITY_EMITTERS.get(entity.getUUID());
		if (emitter == null || !(entity.level() instanceof ServerLevel serverLevel)) return;
		if (entity.isRemoved() || --emitter.ticksLeft < 0) {
			ENTITY_EMITTERS.remove(entity.getUUID());
			return;
		}
		spawnEmitterShape(serverLevel, emitter, entity.getX(), entity.getY(), entity.getZ());
	}

	private static void spawnEmitterShape(ServerLevel level, FxEmitter emitter, double cx, double cy, double cz) {
		net.minecraft.core.particles.ParticleOptions particle = emitter.particleId;
		if (particle == null) return;
		RandomSource random = level.random;
		emitter.age++;
		switch (emitter.shape) {
			case "fountain" -> {
				// count 0 = exact velocity from delta * speed: a real upward jet.
				for (int i = 0; i < emitter.rate; i++) {
					double offX = (random.nextDouble() - 0.5D) * emitter.radius * 0.4D;
					double offZ = (random.nextDouble() - 0.5D) * emitter.radius * 0.4D;
					level.sendParticles(particle, cx + offX, cy + 0.1D, cz + offZ, 0,
						(random.nextDouble() - 0.5D) * 0.2D, 1.0D, (random.nextDouble() - 0.5D) * 0.2D, 0.3D + random.nextDouble() * 0.2D);
				}
			}
			case "shockwave" -> {
				// expanding ring pulse that restarts every 30 ticks
				double progress = (emitter.age % 30L) / 30.0D;
				double ringRadius = Math.max(0.2D, emitter.radius * progress);
				int points = Math.max(8, emitter.rate * 3);
				for (int i = 0; i < points; i++) {
					double angle = Math.PI * 2.0D * i / points;
					level.sendParticles(particle, cx + Math.cos(angle) * ringRadius, cy + 0.1D, cz + Math.sin(angle) * ringRadius, 1, 0.0D, 0.0D, 0.0D, 0.0D);
				}
			}
			case "vortex" -> {
				// rising, narrowing spiral arms
				for (int i = 0; i < emitter.rate; i++) {
					double progress = ((emitter.age * 2L + i * 13L) % 40L) / 40.0D;
					double angle = emitter.age * 0.3D + (Math.PI * 2.0D * i / emitter.rate) + progress * 6.0D;
					double armRadius = emitter.radius * (1.0D - progress * 0.65D);
					level.sendParticles(particle, cx + Math.cos(angle) * armRadius, cy + progress * emitter.radius * 1.6D, cz + Math.sin(angle) * armRadius, 1, 0.0D, 0.0D, 0.0D, 0.0D);
				}
			}
			case "snow" -> {
				// spawn overhead with a slow exact downward velocity
				for (int i = 0; i < emitter.rate; i++) {
					double offX = (random.nextDouble() * 2.0D - 1.0D) * emitter.radius;
					double offZ = (random.nextDouble() * 2.0D - 1.0D) * emitter.radius;
					level.sendParticles(particle, cx + offX, cy + 3.5D, cz + offZ, 0, 0.0D, -1.0D, 0.0D, 0.06D + random.nextDouble() * 0.05D);
				}
			}
			default -> {
				// aura: shimmering points on the sphere shell
				for (int i = 0; i < emitter.rate; i++) {
					double up = random.nextDouble() * 2.0D - 1.0D;
					double theta = random.nextDouble() * Math.PI * 2.0D;
					double ring = Math.sqrt(Math.max(0.0D, 1.0D - up * up));
					level.sendParticles(particle, cx + ring * Math.cos(theta) * emitter.radius, cy + 1.0D + up * emitter.radius, cz + ring * Math.sin(theta) * emitter.radius, 1, 0.0D, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	private static String normalizeEmitterShape(String shape) {
		String value = shape == null ? "" : shape.trim().toLowerCase(Locale.ROOT);
		return switch (value) {
			case "fountain", "shockwave", "vortex", "snow" -> value;
			default -> "aura";
		};
	}

	private static final class FxEmitter {
		String dimension = "";
		String shape = "aura";
		net.minecraft.core.particles.ParticleOptions particleId;
		double x;
		double y;
		double z;
		double radius = 2.0D;
		int rate = 4;
		int ticksLeft;
		long age;
		boolean firstPerson;
	}

	// ---- Bone/weapon position tracking + weapon particle trails ----
	//
	// Exact rendered bone positions only exist on the client inside each model's
	// renderer, so these are GEOMETRY APPROXIMATIONS built from the entity's
	// bounding box, body rotation, and swing animation. That is also exactly why
	// they work with every model: nothing here reads a specific skeleton.

	private static final Map<UUID, FxEmitter> WEAPON_TRAILS = new ConcurrentHashMap<>();

	public static double entityModelSize(Entity entity, String dimension) {
		if (entity == null) return 0.0D;
		String value = dimension == null ? "" : dimension.trim().toLowerCase(Locale.ROOT);
		return switch (value) {
			case "height" -> entity.getBbHeight();
			case "scale" -> entity instanceof LivingEntity living ? living.getScale() : 1.0D;
			default -> entity.getBbWidth();
		};
	}

	public static double approximateBonePosition(Entity entity, String bone, String axis) {
		if (entity == null) return 0.0D;
		Vec3 position = boneVec(entity, bone == null ? "" : bone.trim().toLowerCase(Locale.ROOT));
		return axisValue(position, axis);
	}

	/** Approximate world position of a body part - shared with the hit-zone entity so zones ride the same bones as the particle blocks. */
	public static Vec3 bonePosition(Entity entity, String bone) {
		return bonePosition(entity, bone, false);
	}

	public static Vec3 bonePosition(Entity entity, String bone, boolean limbSwing) {
		if (entity == null) return Vec3.ZERO;
		String key = bone == null ? "chest" : bone.trim().toLowerCase(Locale.ROOT);
		Vec3 pos = boneVec(entity, key);
		if (limbSwing) {
			float theta = limbSwingPitch(entity, key);
			if (theta != 0.0F) {
				// Swing the bone along its arc: the limb hangs ~0.4h below its pivot and
				// rotates forward/back about that pivot by the walk-cycle angle.
				double len = entity.getBbHeight() * 0.4D;
				Vec3 forward = bodyForward(entity);
				pos = pos.add(forward.scale(len * Math.sin(theta))).add(0.0D, len * (1.0D - Math.cos(theta)), 0.0D);
			}
		}
		return pos;
	}

	/**
	 * Walk-cycle limb pitch in radians, replicating HumanoidModel.setupAnim (f=1): right arm
	 * -cos*amt, left arm +cos*amt, right leg +cos*1.4*amt, left leg -cos*1.4*amt. Driven by the
	 * entity's walk state, which ticks on BOTH sides, so the hit-box follows it too. 0 when the
	 * bone isn't a limb or the entity isn't moving.
	 */
	public static float limbSwingPitch(Entity entity, String bone) {
		if (!(entity instanceof LivingEntity living)) return 0.0F;
		float amount = Math.min(living.walkAnimation.speed(), 1.0F);
		if (amount <= 0.001F) return 0.0F;
		float c = (float) Math.cos(living.walkAnimation.position() * 0.6662F);
		boolean mainRight = living.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT;
		return switch (bone == null ? "" : bone) {
			case "main_hand", "right_hand", "hand", "arm" -> (mainRight ? -c : c) * amount;
			case "off_hand", "left_hand" -> (mainRight ? c : -c) * amount;
			case "right_leg" -> c * 1.4F * amount;
			case "left_leg" -> -c * 1.4F * amount;
			default -> 0.0F;
		};
	}

	// ---- Per-bone hit zones ----
	//
	// Each zone is an invisible CneHitZoneEntity child riding a host bone. Direct
	// hits forward to the host (with a multiplier); solid zones also block movement.
	public static void addHitZone(Entity host, String bone, double width, double height, boolean solid, double damageMultiplier, boolean copyRotation, boolean limbSwing, boolean physics) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
		String boneKey = bone == null || bone.isBlank() ? "chest" : bone.trim().toLowerCase(java.util.Locale.ROOT);
		// Reuse an existing zone on the same bone so repeated calls don't pile up entities.
		for (CneHitZoneEntity existing : serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D), z -> z.isHostEntity(host) && boneKey.equals(z.boneName()))) {
			existing.configure(host, boneKey, (float) width, (float) height, solid, (float) damageMultiplier, copyRotation, limbSwing, physics);
			existing.setMainDisabled(host.getPersistentData().getBoolean("cne_main_disabled"));
			return;
		}
		CneHitZoneEntity zone = CneHitZoneRegistration.CNE_HIT_ZONE.create(serverLevel);
		if (zone == null) return;
		zone.configure(host, boneKey, (float) width, (float) height, solid, (float) damageMultiplier, copyRotation, limbSwing, physics);
		// Inherit the host's "main hitbox disabled" state so order doesn't matter (disable before/after adding zones).
		zone.setMainDisabled(host.getPersistentData().getBoolean("cne_main_disabled"));
		serverLevel.addFreshEntity(zone);
	}

	/** Add a hit-zone at a fixed LOCAL offset (right/up/forward, body-yaw-relative) instead of a named
	 *  bone - for custom models the presets can't fit. Read the pivot off your model, /16 = blocks. */
	public static void addHitZoneOffset(Entity host, double dx, double dy, double dz, double width, double height, boolean solid, double damageMultiplier, boolean physics) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
		for (CneHitZoneEntity existing : serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D), z -> z.isHostEntity(host) && z.isManualAt(dx, dy, dz))) {
			existing.configureManual(host, dx, dy, dz, (float) width, (float) height, solid, (float) damageMultiplier, physics);
			existing.setMainDisabled(host.getPersistentData().getBoolean("cne_main_disabled"));
			return;
		}
		CneHitZoneEntity zone = CneHitZoneRegistration.CNE_HIT_ZONE.create(serverLevel);
		if (zone == null) return;
		zone.configureManual(host, dx, dy, dz, (float) width, (float) height, solid, (float) damageMultiplier, physics);
		zone.setMainDisabled(host.getPersistentData().getBoolean("cne_main_disabled"));
		serverLevel.addFreshEntity(zone);
	}

	/** Add a hit-zone that follows the host's ACTUAL animated model bone (EXPERIMENTAL, client-synced via
	 *  CneBoneSyncRuntime). boneName = the raw Blockbench bone/folder name (case-sensitive). Falls back to
	 *  the entity centre when the host isn't being rendered (no client pose). */
	public static void addHitZoneTracked(Entity host, String boneName, double width, double height, boolean solid, double damageMultiplier, boolean physics) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
		String key = (boneName == null || boneName.isBlank()) ? "head" : boneName.trim();
		for (CneHitZoneEntity existing : serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D), z -> z.isHostEntity(host) && z.isBoneTracked() && key.equals(z.trackedBoneName()))) {
			existing.configureTracked(host, key, (float) width, (float) height, solid, (float) damageMultiplier, true, physics);
			existing.setMainDisabled(host.getPersistentData().getBoolean("cne_main_disabled"));
			return;
		}
		CneHitZoneEntity zone = CneHitZoneRegistration.CNE_HIT_ZONE.create(serverLevel);
		if (zone == null) return;
		zone.configureTracked(host, key, (float) width, (float) height, solid, (float) damageMultiplier, true, physics);
		zone.setMainDisabled(host.getPersistentData().getBoolean("cne_main_disabled"));
		serverLevel.addFreshEntity(zone);
	}

	/** Start tracking the host's REAL animated model bone (raw Blockbench folder name) so its live world
	 *  position can be read with trackedBonePosition. Maintains an invisible, non-hittable, non-colliding
	 *  bone-tracked hit-zone on the bone, which drives the SAME client capture + server sync as the
	 *  bone-tracked hit-zones. Call it on the host (e.g. in its on-tick) to keep the bone synced. */
	public static void trackBoneForPosition(Entity host, String boneName) {
		addHitZoneTracked(host, boneName, 0.05, 0.05, false, 0.0, false);
	}

	/** Live world coordinate (x/y/z) of a tracked bone (raw Blockbench folder name) from the client-synced
	 *  capture - the same data the bone-tracked hit-zones use, so it follows the real animation. Falls back
	 *  to the entity centre when the bone isn't tracked yet or the host isn't being rendered. */
	public static double trackedBonePosition(Entity host, String boneName, String axis) {
		if (host == null) return 0.0D;
		String key = boneName == null || boneName.isBlank() ? "head" : boneName.trim();
		double[] p = CneBoneSyncRuntime.pose(host.getId(), key, host.level().getGameTime());
		Vec3 pos = p != null ? offsetPosition(host, p[0], p[1], p[2]) : bonePosition(host, "fullbody");
		String a = axis == null ? "y" : axis.trim().toLowerCase(Locale.ROOT);
		return a.equals("x") ? pos.x : (a.equals("z") ? pos.z : pos.y);
	}

	public static int clearHitZones(Entity host) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return 0;
		int removed = 0;
		for (CneHitZoneEntity zone : serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D), z -> z.isHostEntity(host))) {
			zone.discard();
			removed++;
		}
		return removed;
	}

	/** Remove just the hit-zone(s) on a specific bone, leaving the rest. Returns how many were removed. */
	public static int removeHitZone(Entity host, String bone) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return 0;
		String boneKey = bone == null || bone.isBlank() ? "chest" : bone.trim().toLowerCase(java.util.Locale.ROOT);
		int removed = 0;
		for (CneHitZoneEntity zone : serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D), z -> z.isHostEntity(host) && boneKey.equalsIgnoreCase(z.boneName()))) {
			zone.discard();
			removed++;
		}
		return removed;
	}

	/** Enable/disable the host's MAIN body hitbox for targeting. Disabled = only the hit-zones are
	 *  hittable (crosshair + projectiles skip the body). Flags the host's CURRENT zones, so call it
	 *  after the zones are added; the host needs at least one zone to carry the flag. */
	public static void setMainHitboxEnabled(Entity host, boolean enabled) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
		// Store on the host so zones added LATER inherit it (addHitZone reads this) - order no longer matters.
		host.getPersistentData().putBoolean("cne_main_disabled", !enabled);
		boolean hasAnyZone = false;
		for (CneHitZoneEntity zone : serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D), z -> z.isHostEntity(host))) {
			if (enabled && "cnemarker".equals(zone.boneName())) { zone.discard(); continue; } // re-enabling: drop the hidden marker
			zone.setMainDisabled(!enabled);
			hasAnyZone = true;
		}
		// No hit-zones needed: if the entity has none, spawn a hidden, non-hittable marker zone that just
		// carries the flag, so the body is excluded from the pick (crosshair + projectiles) regardless.
		if (!enabled && !hasAnyZone) {
			CneHitZoneEntity marker = CneHitZoneRegistration.CNE_HIT_ZONE.create(serverLevel);
			if (marker != null) {
				marker.configure(host, "cnemarker", 0.1F, 0.1F, false, 0.0F, false, false, false);
				marker.setMainDisabled(true);
				serverLevel.addFreshEntity(marker);
			}
		}
	}

	private static final String[] CNE_SOLID_BONES = {"head_solid", "chest_solid", "right_leg_solid", "left_leg_solid", "right_hand_solid", "left_hand_solid"};

	/** Make the entity SOLID/collidable using stand-on (block-like) hit-zones. mode: "box" = one zone
	 *  covering the whole bounding box; "bones" = a zone per humanoid bone (approximates the model
	 *  shape, follows the animation); anything else = off (removes them). Zones use distinct "*_solid"
	 *  bone names so they don't disturb any weak-point hit-zones you added. */
	public static void setEntitySolid(Entity host, String mode) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel)) return;
		removeHitZone(host, "fullbody");
		for (String b : CNE_SOLID_BONES) removeHitZone(host, b);
		String m = mode == null ? "off" : mode.trim().toLowerCase(java.util.Locale.ROOT);
		double w = host.getBbWidth(), h = host.getBbHeight();
		if (m.equals("box")) {
			addHitZone(host, "fullbody", w + 0.02D, h, true, 1.0, false, false, true);
		} else if (m.equals("bones")) {
			addHitZone(host, "head_solid", w * 0.55D, h * 0.28D, true, 1.0, false, false, true);
			addHitZone(host, "chest_solid", w * 0.75D, h * 0.5D, true, 1.0, false, false, true);
			addHitZone(host, "right_leg_solid", w * 0.32D, h * 0.5D, true, 1.0, false, false, true);
			addHitZone(host, "left_leg_solid", w * 0.32D, h * 0.5D, true, 1.0, false, false, true);
			addHitZone(host, "right_hand_solid", w * 0.28D, h * 0.45D, true, 1.0, false, false, true);
			addHitZone(host, "left_hand_solid", w * 0.28D, h * 0.45D, true, 1.0, false, false, true);
		}
	}

	/** Find the host's hit-zone on the given bone, or null. Mirrors the addHitZone/removeHitZone lookup
	 *  (zones ride the host bone so they sit within the host bbox inflated 64; bone key normalized). */
	public static CneHitZoneEntity findZone(Entity host, String bone) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return null;
		String boneKey = bone == null || bone.isBlank() ? "chest" : bone.trim().toLowerCase(java.util.Locale.ROOT);
		for (CneHitZoneEntity z : serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D),
				z -> z.isHostEntity(host) && boneKey.equalsIgnoreCase(z.boneName()))) {
			return z;
		}
		return null;
	}

	/** Live entities whose box overlaps the zone (the "touching" trigger). Excludes the host and the
	 *  host's own internal hit-zone entities. */
	public static java.util.List<Entity> entitiesTouchingZone(CneHitZoneEntity zone) {
		if (zone == null) return java.util.Collections.emptyList();
		return zone.level().getEntities(zone, zone.getBoundingBox(),
				e -> e.isAlive() && !zone.isHostEntity(e) && !(e instanceof CneHitZoneEntity));
	}

	/** Live entities standing on the zone's top face (the "stepped on" trigger). No vanilla primitive,
	 *  so: a thin slab just above the top face plus a grounded / feet-near-top test. Reliable for
	 *  stand-on (physics) zones where entities truly rest on the box. */
	public static java.util.List<Entity> entitiesSteppingZone(CneHitZoneEntity zone) {
		if (zone == null) return java.util.Collections.emptyList();
		net.minecraft.world.phys.AABB box = zone.getBoundingBox();
		net.minecraft.world.phys.AABB topSlab = new net.minecraft.world.phys.AABB(
				box.minX, box.maxY - 0.05D, box.minZ, box.maxX, box.maxY + 0.25D, box.maxZ);
		java.util.List<Entity> out = new java.util.ArrayList<>();
		for (Entity e : zone.level().getEntities(zone, topSlab,
				e -> e.isAlive() && !zone.isHostEntity(e) && !(e instanceof CneHitZoneEntity))) {
			boolean standing = e.onGround() || e.getDeltaMovement().y <= 1.0E-3D;
			boolean feetNear = Math.abs(e.getBoundingBox().minY - box.maxY) <= 0.30D;
			if (standing && feetNear) out.add(e);
		}
		return out;
	}

	/** True if the host's hit-zone on this bone forwarded a real hit within the last {@code ticks}
	 *  game-ticks (the "damaged" trigger). Returns false if there's no such zone. */
	public static boolean zoneWasDamaged(Entity host, String bone, long ticks) {
		CneHitZoneEntity z = findZone(host, bone);
		return z != null && z.wasHurtSince(ticks);
	}

	/** The entity that last damaged the host's hit-zone on this bone, or null. Pairs with the
	 *  "damaged" trigger so the body can read who hit the zone via the entity-iterator block. */
	public static Entity damagerOf(Entity host, String bone) {
		CneHitZoneEntity z = findZone(host, bone);
		return z != null ? z.lastAttacker() : null;
	}

	/** Carry entities standing on a moving "platform" (a movable group or a solid/stand-on hit-zone) by
	 *  its per-tick translation + yaw spin - vanilla never moves riders on a moving collision shape, so
	 *  without this you slide/fall off a moving solid. excludeHost (nullable) + its whole riding stack
	 *  are skipped, so a mob never shoves the rider sitting on its own zone. Server-side. */
	public static void carryRiders(Entity platform, Vec3 prevPos, double yawDeltaDeg, Entity excludeHost) {
		if (platform == null || platform.level().isClientSide()) return;
		Vec3 delta = platform.position().subtract(prevPos);
		if (delta.lengthSqr() < 1.0E-9D && Math.abs(yawDeltaDeg) < 1.0E-4D) return;
		if (delta.lengthSqr() > 100.0D) return; // ignore teleports
		net.minecraft.world.phys.AABB box = platform.getBoundingBox();
		double prevTopY = box.maxY - delta.y; // top BEFORE the move - the not-yet-moved rider stands there
		double dyaw = Math.toRadians(yawDeltaDeg), cosd = Math.cos(dyaw), sind = Math.sin(dyaw);
		boolean spinning = Math.abs(yawDeltaDeg) > 1.0E-4D;
		Entity hostRoot = excludeHost == null ? null : excludeHost.getRootVehicle();
		for (Entity e : platform.level().getEntities(platform, box.inflate(0.5D),
				e -> e.isAlive() && !(e instanceof CneMovableBlockGroupEntity) && !(e instanceof CneHitZoneEntity))) {
			if (hostRoot != null && (e == excludeHost || e.getRootVehicle() == hostRoot)) continue;
			net.minecraft.world.phys.AABB eb = e.getBoundingBox();
			if (eb.minY < prevTopY - 0.5D || eb.minY > prevTopY + 0.06D) continue; // only riders on the (old) top
			// Rigid transform: new = newCentre + rotate(rider - oldCentre). When dyaw=0 this is pure
			// translation; matrix [[cos,sin],[-sin,cos]] matches the rig's +YP spin (and MC yRot is -yaw).
			double rx = e.getX() - prevPos.x, rz = e.getZ() - prevPos.z;
			double nx = platform.getX() + (rx * cosd + rz * sind);
			double nz = platform.getZ() + (-rx * sind + rz * cosd);
			e.setPos(nx, e.getY() + delta.y, nz);
			if (delta.y < 0.0D && e.getDeltaMovement().y > delta.y) e.setDeltaMovement(e.getDeltaMovement().x, delta.y, e.getDeltaMovement().z);
			e.fallDistance = 0.0F;
			if (spinning && e instanceof net.minecraft.world.entity.LivingEntity) e.setYRot(e.getYRot() - (float) yawDeltaDeg);
		}
	}

	public static int hitZoneCount(Entity host) {
		if (host == null || !(host.level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return 0;
		return serverLevel.getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(64.0D), z -> z.isHostEntity(host)).size();
	}

	// ---- Hit-zone seating ----
	//
	// Seating goes through VANILLA passenger mechanics so every downstream system just works:
	// passenger mode chains sitter -> zone -> host (the zone re-runs its bone follow after
	// vanilla's rideTick placement, see CneHitZoneEntity.rideTick), so the sitter is among
	// host.getIndirectPassengers(), sitter.getRootVehicle() == host, and sneak-dismount and
	// death/discard ejection are all vanilla. Main-rider mode mounts the HOST directly (plain
	// vanilla first-passenger semantics at the vanilla seat point, NOT bone-positioned).

	/** Seat sitter on the host's hit-zone on the given bone (or on the host itself when mainRider).
	 *  Fails soft like the other hit-zone helpers - it silently does nothing when the zone is
	 *  missing for that bone, the seat already has a rider, or the mount would be circular
	 *  (startRiding refuses chains that loop). A sitter already riding something is dismounted
	 *  first so the seat never silently no-ops on mounted entities. */
	public static void seatOnHitZone(Entity sitter, Entity host, String bone, boolean mainRider) {
		if (sitter == null || host == null || sitter == host) return;
		if (!(host.level() instanceof net.minecraft.server.level.ServerLevel) || sitter.level() != host.level()) return;
		if (sitter instanceof CneHitZoneEntity) return; // zones are internal children, never sitters
		if (mainRider) {
			if (sitter.isPassenger()) sitter.stopRiding();
			// Remember the host so unseatFromHitZone can tell this mount from a horse/boat ride.
			if (sitter.startRiding(host, true)) sitter.getPersistentData().putUUID("cne_seat_host", host.getUUID());
			return;
		}
		CneHitZoneEntity zone = findZone(host, bone);
		if (zone == null || zone.isVehicle()) return; // no zone on that bone, or the seat is taken
		if (sitter.isPassenger()) sitter.stopRiding();
		// Root the chain first (zone rides host), then put the sitter on the zone. Force mounts so
		// canAddPassenger seat counts don't block it; a cycle still fails and we just leave the
		// empty seat to clean itself up (CneHitZoneEntity.rideTick dismounts an unused seat).
		if (!zone.isSeat() && !zone.startRiding(host, true)) return;
		if (sitter.startRiding(zone, true)) sitter.getPersistentData().putUUID("cne_seat_host", host.getUUID());
	}

	/** Dismount an entity seated by seatOnHitZone. Scoped so it never yanks entities off horses,
	 *  boats, or other unrelated mounts: it only stops riding when the vehicle is a seat-mode
	 *  hit-zone, or is the host recorded when the seat was taken (the main-rider case). */
	public static void unseatFromHitZone(Entity sitter) {
		if (sitter == null || sitter.level().isClientSide() || !sitter.isPassenger()) return;
		Entity vehicle = sitter.getVehicle();
		boolean zoneSeat = vehicle instanceof CneHitZoneEntity zone && zone.isSeat();
		boolean mainSeat = vehicle != null && sitter.getPersistentData().hasUUID("cne_seat_host")
			&& sitter.getPersistentData().getUUID("cne_seat_host").equals(vehicle.getUUID());
		if (!zoneSeat && !mainSeat) return;
		sitter.getPersistentData().remove("cne_seat_host");
		sitter.stopRiding();
	}

	/** The entity currently seated on the host's hit-zone on the given bone, or null when the
	 *  seat is empty or no zone exists on that bone. (Main riders mount the host itself, so read
	 *  those through the host's own passengers instead.) */
	public static Entity hitZoneRider(Entity host, String bone) {
		CneHitZoneEntity zone = findZone(host, bone);
		return zone == null ? null : zone.getFirstPassenger();
	}

	/** True when the entity's vehicle is one of the host's seat-mode hit-zones, or the host
	 *  itself (main-rider mode mounts the host directly, so ANY direct mount on the host counts).
	 *  Works on both sides - riding state is synced. */
	public static boolean isSeatedOnHitZone(Entity sitter, Entity host) {
		if (sitter == null || host == null) return false;
		Entity vehicle = sitter.getVehicle();
		if (vehicle == host) return true;
		return vehicle instanceof CneHitZoneEntity zone && zone.isSeat() && zone.isHostEntity(host);
	}

	// ---- Movable blocks ----
	//
	// makeMovable captures the block at a position as a CneMovableBlockEntity; the other
	// methods drive an existing one passed back in as an Entity (resolved via instanceof).
	public static void makeMovable(net.minecraft.world.level.LevelAccessor world, double x, double y, double z, boolean removeOriginal, boolean gravity, boolean breakable, boolean placeable) {
		if (!(world instanceof net.minecraft.server.level.ServerLevel level)) return;
		net.minecraft.core.BlockPos pos = net.minecraft.core.BlockPos.containing(x, y, z);
		net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
		if (state.isAir()) return;
		CneMovableBlockEntity block = CneMovableBlockRegistration.CNE_MOVABLE_BLOCK.create(level);
		if (block == null) return;
		block.configure(state, pos);
		block.setGravityEnabled(gravity);
		block.setBreakable(breakable);
		block.setPlaceable(placeable);
		level.addFreshEntity(block);
		if (removeOriginal) level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), net.minecraft.world.level.block.Block.UPDATE_ALL);
	}

	public static Entity movableBlockAt(net.minecraft.world.level.LevelAccessor world, double x, double y, double z) {
		net.minecraft.core.BlockPos pos = net.minecraft.core.BlockPos.containing(x, y, z);
		net.minecraft.world.phys.AABB box = new net.minecraft.world.phys.AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).inflate(0.25D);
		for (CneMovableBlockEntity block : world.getEntitiesOfClass(CneMovableBlockEntity.class, box)) return block;
		return null;
	}

	public static boolean isMovableBlockAt(net.minecraft.world.level.LevelAccessor world, double x, double y, double z) {
		return movableBlockAt(world, x, y, z) != null;
	}

	public static void setMovableVelocity(Entity block, double vx, double vy, double vz) {
		if (block instanceof CneMovableBlockEntity movable) movable.setDeltaMovement(vx, vy, vz);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setDeltaMovement(vx, vy, vz);
	}

	public static void teleportMovableBlock(Entity block, double x, double y, double z) {
		if (block instanceof CneMovableBlockEntity movable) movable.setPos(x, y, z);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setPos(x, y, z);
	}

	public static void setMovableRotation(Entity block, double rx, double ry, double rz) {
		if (block instanceof CneMovableBlockEntity movable) movable.setRotation((float) rx, (float) ry, (float) rz);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setRotation((float) rx, (float) ry, (float) rz);
	}

	public static void setMovableAngularVelocity(Entity block, double degPerSecX, double degPerSecY, double degPerSecZ) {
		if (block instanceof CneMovableBlockEntity movable) movable.setAngularVelocity((float) degPerSecX, (float) degPerSecY, (float) degPerSecZ);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setAngularVelocity((float) degPerSecX, (float) degPerSecY, (float) degPerSecZ);
	}

	public static void setMovableSolid(Entity block, boolean solid) {
		if (block instanceof CneMovableBlockEntity movable) movable.setSolid(solid);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setSolid(solid);
	}

	public static void setMovableGravity(Entity block, boolean gravity) {
		if (block instanceof CneMovableBlockEntity movable) movable.setGravityEnabled(gravity);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setGravityEnabled(gravity);
	}

	public static void setMovableBreakable(Entity block, boolean breakable) {
		if (block instanceof CneMovableBlockEntity movable) movable.setBreakable(breakable);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setBreakable(breakable);
	}

	public static void setMovablePlaceable(Entity block, boolean placeable) {
		if (block instanceof CneMovableBlockEntity movable) movable.setPlaceable(placeable);
		else if (block instanceof CneMovableBlockGroupEntity group) group.setPlaceable(placeable);
	}

	public static boolean movableHasGravity(Entity block) {
		if (block instanceof CneMovableBlockEntity movable) return movable.isGravityEnabled();
		if (block instanceof CneMovableBlockGroupEntity group) return group.isGravityEnabled();
		return false;
	}

	public static boolean isMovableBreakable(Entity block) {
		if (block instanceof CneMovableBlockEntity movable) return movable.isBreakable();
		if (block instanceof CneMovableBlockGroupEntity group) return group.isBreakable();
		return false;
	}

	public static boolean isMovablePlaceable(Entity block) {
		if (block instanceof CneMovableBlockEntity movable) return movable.isPlaceable();
		if (block instanceof CneMovableBlockGroupEntity group) return group.isPlaceable();
		return false;
	}

	/** Ticks it takes the attacker to break this block - the same time as mining it normally (the
	 *  block's hardness vs the held tool). Player attackers use getDestroyProgress; mobs fall back to
	 *  ~hardness seconds. -1 hardness (bedrock etc.) or 0 progress = effectively unbreakable. */
	public static int blockBreakTimeTicks(Entity attacker, net.minecraft.world.level.Level level, net.minecraft.world.level.block.state.BlockState st, net.minecraft.core.BlockPos pos) {
		if (st == null || st.isAir() || level == null) return 1;
		if (attacker instanceof net.minecraft.world.entity.player.Player player) {
			float progress = st.getDestroyProgress(player, level, pos);
			if (progress <= 0.0F) return 6000;
			return Math.max(1, (int) Math.ceil(1.0F / progress));
		}
		float hardness = st.getDestroySpeed(level, pos);
		if (hardness < 0.0F) return 6000;
		return Math.max(1, (int) (hardness * 20.0F));
	}

	public static void setMovableDespawn(Entity block, double ticks) {
		if (block instanceof CneMovableBlockEntity movable) movable.setDespawnTicks((int) Math.round(ticks));
		else if (block instanceof CneMovableBlockGroupEntity group) group.setDespawnTicks((int) Math.round(ticks));
	}

	public static void placeMovableBlock(Entity block) {
		if (block instanceof CneMovableBlockEntity movable) movable.placeIntoWorld();
		else if (block instanceof CneMovableBlockGroupEntity group) group.placeIntoWorld();
	}

	public static String movableBlockId(Entity block) {
		if (block instanceof CneMovableBlockEntity movable) return movable.getBlockId();
		if (block instanceof CneMovableBlockGroupEntity group) return group.getBlockId();
		return "minecraft:air";
	}

	// ---- Movable block GROUPS ----
	//
	// One rig of many blocks captured from an area, moved/rotated together about its centre.
	// The same set-velocity/rotation/solid/gravity/despawn/teleport/place blocks above drive it.
	public static Entity makeMovableGroup(net.minecraft.world.level.LevelAccessor world, double x1, double y1, double z1, double x2, double y2, double z2, String filter, boolean removeOriginal, boolean gravity, boolean breakable, boolean placeable) {
		if (!(world instanceof net.minecraft.server.level.ServerLevel level)) return null;
		int minX = (int) Math.floor(Math.min(x1, x2)), maxX = (int) Math.floor(Math.max(x1, x2));
		int minY = (int) Math.floor(Math.min(y1, y2)), maxY = (int) Math.floor(Math.max(y1, y2));
		int minZ = (int) Math.floor(Math.min(z1, z2)), maxZ = (int) Math.floor(Math.max(z1, z2));
		java.util.List<net.minecraft.core.BlockPos> worldPositions = new java.util.ArrayList<>();
		java.util.List<net.minecraft.world.level.block.state.BlockState> states = new java.util.ArrayList<>();
		int bMinX = Integer.MAX_VALUE, bMinY = Integer.MAX_VALUE, bMinZ = Integer.MAX_VALUE;
		int scanned = 0;
		outer:
		for (int bx = minX; bx <= maxX; bx++) {
			for (int by = minY; by <= maxY; by++) {
				for (int bz = minZ; bz <= maxZ; bz++) {
					if (++scanned > 32768) break outer; // cap on volume scanned
					net.minecraft.core.BlockPos p = new net.minecraft.core.BlockPos(bx, by, bz);
					net.minecraft.world.level.block.state.BlockState st = level.getBlockState(p);
					if (!matchesGroupFilter(level, p, st, filter)) continue;
					worldPositions.add(p);
					states.add(st);
					bMinX = Math.min(bMinX, bx);
					bMinY = Math.min(bMinY, by);
					bMinZ = Math.min(bMinZ, bz);
					if (worldPositions.size() >= 1024) break outer; // cap on rig size
				}
			}
		}
		if (worldPositions.isEmpty()) return null;
		net.minecraft.core.BlockPos minCorner = new net.minecraft.core.BlockPos(bMinX, bMinY, bMinZ);
		java.util.List<net.minecraft.core.BlockPos> offsets = new java.util.ArrayList<>();
		for (net.minecraft.core.BlockPos p : worldPositions) offsets.add(p.subtract(minCorner));
		CneMovableBlockGroupEntity group = CneMovableBlockGroupRegistration.CNE_MOVABLE_BLOCK_GROUP.create(level);
		if (group == null) return null;
		group.configure(minCorner, offsets, states);
		group.setGravityEnabled(gravity);
		group.setBreakable(breakable);
		group.setPlaceable(placeable);
		level.addFreshEntity(group);
		if (removeOriginal) {
			for (net.minecraft.core.BlockPos p : worldPositions) {
				// Preserve container contents (chests/barrels): move them into the cell, then clear the
				// source so removing the block doesn't spill the items on the ground.
				net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(p);
				if (be instanceof net.minecraft.world.Container c) {
					group.captureCellContainer(p.subtract(minCorner), c);
					c.clearContent();
				}
				level.setBlock(p, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), net.minecraft.world.level.block.Block.UPDATE_ALL);
			}
		}
		return group;
	}

	// Which captured blocks to keep: all (non-air), solid (has a collision shape), liquid (has a
	// fluid), or nonsolid (non-air with neither collision nor fluid - plants, torches, etc.).
	private static boolean matchesGroupFilter(net.minecraft.server.level.ServerLevel level, net.minecraft.core.BlockPos p, net.minecraft.world.level.block.state.BlockState st, String filter) {
		if (st.isAir()) return false;
		String f = filter == null ? "all" : filter.trim().toLowerCase(java.util.Locale.ROOT);
		if (f.equals("all")) return true;
		boolean fluid = !st.getFluidState().isEmpty();
		boolean solid = !st.getCollisionShape(level, p).isEmpty();
		return switch (f) {
			case "solid" -> solid;
			case "liquid" -> fluid;
			case "nonsolid", "non-solid" -> !solid && !fluid;
			default -> true;
		};
	}

	/** Cube of `size` blocks per side CENTRED on (x,y,z), captured as a movable group. */
	public static Entity makeMovableGroupCube(net.minecraft.world.level.LevelAccessor world, double x, double y, double z, double size, String filter, boolean removeOriginal, boolean gravity, boolean breakable, boolean placeable) {
		int cx = (int) Math.floor(x), cy = (int) Math.floor(y), cz = (int) Math.floor(z);
		int n = Math.max(1, (int) Math.round(size));
		int lo = -(n - 1) / 2, hi = n / 2;
		return makeMovableGroup(world, cx + lo, cy + lo, cz + lo, cx + hi, cy + hi, cz + hi, filter, removeOriginal, gravity, breakable, placeable);
	}

	public static Entity movableGroupAt(net.minecraft.world.level.LevelAccessor world, double x, double y, double z) {
		net.minecraft.world.phys.AABB box = new net.minecraft.world.phys.AABB(x, y, z, x, y, z).inflate(0.1D);
		for (CneMovableBlockGroupEntity group : world.getEntitiesOfClass(CneMovableBlockGroupEntity.class, box)) return group;
		return null;
	}

	/** Placing a block on a single movable block grows it into a 2-cell group (the original block
	 *  + the placed block), inheriting its solid/gravity/breakable/rotation; removes the single. */
	public static boolean growMovableBlockIntoGroup(CneMovableBlockEntity single, int nx, int ny, int nz, net.minecraft.world.level.block.state.BlockState placed) {
		if (single == null || placed == null || placed.isAir() || !(single.level() instanceof net.minecraft.server.level.ServerLevel level)) return false;
		net.minecraft.world.level.block.state.BlockState original = single.getBlockState();
		if (original.isAir()) return false;
		net.minecraft.core.BlockPos singleCell = net.minecraft.core.BlockPos.containing(single.getX(), single.getY() + 0.5D, single.getZ());
		net.minecraft.core.BlockPos newCell = singleCell.offset(nx, ny, nz);
		net.minecraft.core.BlockPos minCorner = new net.minecraft.core.BlockPos(Math.min(singleCell.getX(), newCell.getX()), Math.min(singleCell.getY(), newCell.getY()), Math.min(singleCell.getZ(), newCell.getZ()));
		java.util.List<net.minecraft.core.BlockPos> offsets = new java.util.ArrayList<>();
		java.util.List<net.minecraft.world.level.block.state.BlockState> states = new java.util.ArrayList<>();
		offsets.add(singleCell.subtract(minCorner));
		states.add(original);
		offsets.add(newCell.subtract(minCorner));
		states.add(placed);
		CneMovableBlockGroupEntity group = CneMovableBlockGroupRegistration.CNE_MOVABLE_BLOCK_GROUP.create(level);
		if (group == null) return false;
		group.configure(minCorner, offsets, states);
		group.setSolid(single.isSolid());
		group.setGravityEnabled(single.isGravityEnabled());
		group.setBreakable(single.isBreakable());
		group.setPlaceable(true);
		group.setRotation(single.getRotX(), single.getRotY(), single.getRotZ());
		level.addFreshEntity(group);
		single.discard();
		return true;
	}

	// ---- Sit on a block ----
	public static void sitEntityAt(Entity rider, double x, double y, double z) {
		if (rider == null || !(rider.level() instanceof net.minecraft.server.level.ServerLevel level)) return;
		if (rider.isPassenger()) rider.stopRiding();
		CneSeatEntity seat = CneSeatRegistration.CNE_SEAT.create(level);
		if (seat == null) return;
		seat.setPos(x + 0.5D, y + 0.3D, z + 0.5D);
		level.addFreshEntity(seat);
		rider.startRiding(seat);
	}

	// ---- What block is being looked at ----
	private static net.minecraft.world.phys.BlockHitResult lookingAtBlock(Entity entity, double range) {
		if (entity == null) return null;
		net.minecraft.world.phys.Vec3 from = entity.getEyePosition();
		net.minecraft.world.phys.Vec3 to = from.add(entity.getViewVector(1.0F).scale(Math.max(0.5D, Math.min(64.0D, range))));
		net.minecraft.world.phys.BlockHitResult hit = entity.level().clip(new net.minecraft.world.level.ClipContext(from, to, net.minecraft.world.level.ClipContext.Block.OUTLINE, net.minecraft.world.level.ClipContext.Fluid.NONE, entity));
		return hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK ? hit : null;
	}

	public static String lookingAtBlockId(Entity entity, double range) {
		net.minecraft.world.phys.BlockHitResult hit = lookingAtBlock(entity, range);
		if (hit == null) return "minecraft:air";
		net.minecraft.world.level.block.state.BlockState st = entity.level().getBlockState(hit.getBlockPos());
		net.minecraft.resources.ResourceLocation id = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(st.getBlock());
		return id == null ? "minecraft:air" : id.toString();
	}

	public static double lookingAtBlockAxis(Entity entity, double range, String axis) {
		net.minecraft.world.phys.BlockHitResult hit = lookingAtBlock(entity, range);
		if (hit == null) return entity == null ? 0.0D : axisValue(entity.position(), axis);
		net.minecraft.core.BlockPos pos = hit.getBlockPos();
		String a = axis == null ? "" : axis.trim().toLowerCase(Locale.ROOT);
		return switch (a) {
			case "y" -> (double) pos.getY();
			case "z" -> (double) pos.getZ();
			default -> (double) pos.getX();
		};
	}

	public static double weaponTipPosition(Entity entity, String hand, String axis, double bladeLength) {
		if (!(entity instanceof LivingEntity living)) return entity == null ? 0.0D : axisValue(entity.position(), axis);
		boolean mainHand = hand == null || !hand.trim().toLowerCase(Locale.ROOT).startsWith("off");
		Vec3 handPos = handPosition(living, mainHand);
		Vec3 tip = handPos.add(weaponDirection(living).scale(Math.max(0.1D, Math.min(8.0D, bladeLength))));
		return axisValue(tip, axis);
	}

	private static double axisValue(Vec3 position, String axis) {
		String value = axis == null ? "" : axis.trim().toLowerCase(Locale.ROOT);
		return switch (value) {
			case "y" -> position.y;
			case "z" -> position.z;
			default -> position.x;
		};
	}

	private static Vec3 boneVec(Entity entity, String bone) {
		double width = entity.getBbWidth();
		double height = entity.getBbHeight();
		Vec3 base = entity.position();
		Vec3 forward = bodyForward(entity);
		Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
		if (bone.endsWith("_solid")) bone = bone.substring(0, bone.length() - 6); // solid zones reuse real bone spots
		return switch (bone) {
			case "fullbody" -> base.add(0.0D, height * 0.5D, 0.0D);
			case "head" -> new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
			case "chest", "body", "torso" -> base.add(0.0D, height * 0.6D, 0.0D);
			case "main_hand", "right_hand", "hand", "arm" -> entity instanceof LivingEntity living ? handPosition(living, true) : base.add(0.0D, height * 0.5D, 0.0D);
			case "off_hand", "left_hand" -> entity instanceof LivingEntity living ? handPosition(living, false) : base.add(0.0D, height * 0.5D, 0.0D);
			case "right_leg" -> base.add(right.scale(width * 0.22D)).add(0.0D, height * 0.25D, 0.0D);
			case "left_leg" -> base.add(right.scale(-width * 0.22D)).add(0.0D, height * 0.25D, 0.0D);
			case "feet", "foot", "ground" -> base;
			default -> base; // unknown / custom model bone -> entity centre (positions are geometry-approximated, not read from the skeleton)
		};
	}

	/** World position of a manual local offset (right/up/forward) in the host's body-yaw frame. */
	public static Vec3 offsetPosition(Entity host, double dx, double dy, double dz) {
		Vec3 forward = bodyForward(host);
		Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
		return host.position().add(right.scale(dx)).add(0.0D, dy, 0.0D).add(forward.scale(dz));
	}

	private static Vec3 bodyForward(Entity entity) {
		float bodyYaw = entity instanceof LivingEntity living ? living.yBodyRot : entity.getYRot();
		double yaw = Math.toRadians(bodyYaw);
		return new Vec3(-Math.sin(yaw), 0.0D, Math.cos(yaw));
	}

	public static Vec3 handPosition(LivingEntity living, boolean mainHand) {
		boolean rightSide = mainHand == (living.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT);
		Vec3 forward = bodyForward(living);
		// entity-right vector: facing south (+Z) the right hand sits toward -X
		Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
		double width = living.getBbWidth();
		return living.position()
			.add(right.scale((rightSide ? 1.0D : -1.0D) * width * 0.45D))
			.add(forward.scale(width * 0.3D))
			// Hand height ~0.65 of the entity height: matches where the held item actually renders (the arm's
			// hold pose), so the first-person trail lines up with the third-person captured spot instead of
			// sitting low at the hip. Was 0.45 (too low - the first-person trail dropped well below the sword).
			.add(0.0D, living.getBbHeight() * 0.65D, 0.0D);
	}

	// Where the weapon points: BODY-forward (horizontal) angled down at rest, sweeping further down through
	// the swing arc. Deliberately uses body yaw, NOT the look vector, so head PITCH no longer tilts the trail -
	// looking up used to fling the first-person trail straight into the sky and made its angle disagree with
	// the third-person captured blade (the held sword doesn't pitch with your head either). Body yaw still
	// follows where you're facing, so the trail sweeps in your aim direction.
	public static Vec3 weaponDirection(LivingEntity living) {
		Vec3 forward = bodyForward(living);
		float swing = living.getAttackAnim(1.0F);
		if (swing > 0.0F) {
			double sweep = Math.sin(swing * Math.PI);
			return forward.scale(1.0D - 0.6D * sweep).add(0.0D, -0.9D * sweep, 0.0D).normalize();
		}
		return forward.add(0.0D, -0.35D, 0.0D).normalize();
	}

	public static void fxWeaponTrail(Entity entity, String hand, net.minecraft.core.particles.ParticleOptions particleId, double bladeLength, double density, double durationTicks, boolean firstPerson) {
		if (entity == null) return;
		boolean mainHand = hand == null || !hand.trim().toLowerCase(Locale.ROOT).startsWith("off");
		if (durationTicks <= 0 || particleId == null) {
			WEAPON_TRAILS.remove(entity.getUUID());
			broadcastWeaponTrail(entity, particleId, 0.0F, 0, 0, mainHand, false, firstPerson);
			return;
		}
		FxEmitter emitter = new FxEmitter();
		emitter.shape = mainHand ? "weapon_trail_main" : "weapon_trail_off";
		emitter.particleId = particleId;
		emitter.radius = Math.max(0.1D, Math.min(8.0D, bladeLength));
		emitter.rate = (int) Math.max(2, Math.min(32, density));
		emitter.ticksLeft = (int) Math.max(1, Math.min(72000, durationTicks));
		emitter.firstPerson = firstPerson;
		WEAPON_TRAILS.put(entity.getUUID(), emitter);
		broadcastWeaponTrail(entity, particleId, (float) emitter.radius, emitter.rate, emitter.ticksLeft, mainHand, true, firstPerson);
	}

	private static void broadcastWeaponTrail(Entity entity, net.minecraft.core.particles.ParticleOptions particle, float bladeLength, int density, int ticks, boolean mainHand, boolean active, boolean firstPerson) {
		if (entity == null || entity.level().isClientSide()) return;
		net.minecraft.core.particles.ParticleOptions p = particle != null ? particle : ParticleTypes.CRIT;
		net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity,
			new CneWeaponTrailClient.WeaponTrailMessage(entity.getId(), p, bladeLength, density, ticks, mainHand, active, firstPerson));
	}

	// Particles are drawn CLIENT-side now (CneWeaponTrailClient reads the REAL rendered item transform);
	// the server only runs the duration countdown and tells clients to stop on expiry. EntityTickEvent.Post
	// fires on BOTH sides, so the isClientSide guard is required (matches tickEntityEmitter) - without it the
	// client integrated-server tick would corrupt the server-side WEAPON_TRAILS countdown.
	private static void tickWeaponTrail(Entity entity) {
		if (entity == null || WEAPON_TRAILS.isEmpty() || entity.level().isClientSide()) return;
		FxEmitter emitter = WEAPON_TRAILS.get(entity.getUUID());
		if (emitter == null) return;
		boolean mainHand = emitter.shape.endsWith("main");
		if (entity.isRemoved() || !(entity instanceof LivingEntity) || emitter.particleId == null || --emitter.ticksLeft < 0) {
			WEAPON_TRAILS.remove(entity.getUUID());
			broadcastWeaponTrail(entity, emitter.particleId, 0.0F, 0, 0, mainHand, false, emitter.firstPerson);
		}
	}

	// When a player starts seeing an entity that already has an active trail, resend it so the trail
	// appears for the new observer (the initial broadcast only reached players tracking it at start).
	@SubscribeEvent
	public static void onStartTrackingWeaponTrail(net.neoforged.neoforge.event.entity.player.PlayerEvent.StartTracking event) {
		if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getTarget() instanceof LivingEntity)) return;
		Entity target = event.getTarget();
		FxEmitter emitter = WEAPON_TRAILS.get(target.getUUID());
		if (emitter == null || emitter.particleId == null) return;
		boolean mainHand = emitter.shape.endsWith("main");
		net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
			new CneWeaponTrailClient.WeaponTrailMessage(target.getId(), emitter.particleId, (float) emitter.radius, emitter.rate, emitter.ticksLeft, mainHand, true, emitter.firstPerson));
	}

	// When a player starts tracking an entity that already carries persistent vars, send that entity's
	// var compound to just that player. Without this a client that comes into range (or relogs) would
	// see the fallback for that entity until the next var write flips it dirty. Sends only when the
	// entity actually has vars, so trackerless entities cost nothing.
	@SubscribeEvent
	public static void onStartTrackingEntityVars(net.neoforged.neoforge.event.entity.player.PlayerEvent.StartTracking event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) return;
		Entity target = event.getTarget();
		if (target == null) return;
		try {
			CompoundTag vars = collectEntityVars(target);
			if (vars == null) return;
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new EntityVarSyncMessage(target.getId(), vars));
		} catch (Exception ignored) {
		}
	}

	// A player never fires StartTracking for itself, so its OWN client only receives its persistent-var
	// compound via a write or a death/End clone. That leaves two self-read gaps: a fresh login with
	// pre-existing on-disk vars, and a plain portal dimension change (which rebuilds an empty client
	// LocalPlayer without firing PlayerEvent.Clone). Both are closed by marking the player dirty; the
	// flush is deferred to the next server tick, by which point the ordered connection has delivered the
	// login/respawn packets and the client has built its LocalPlayer, so the sync resolves. Death and
	// End-return stay covered by onHealthLockPlayerClone.
	@SubscribeEvent
	public static void onPlayerJoinSyncEntityVars(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) markVarsDirty(player);
	}

	@SubscribeEvent
	public static void onPlayerChangeDimSyncEntityVars(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) markVarsDirty(player);
	}

	public static boolean hasAttackTarget(Entity entity) {
		return getAttackTarget(entity) instanceof LivingEntity target && target.isAlive();
	}

	public static Entity getAttackTarget(Entity entity) {
		return entity instanceof Mob mob ? mob.getTarget() : null;
	}

	public static boolean isTargeting(Entity entity, Entity target) {
		return target != null && getAttackTarget(entity) == target;
	}

	/**
	 * Replaces the entity with a freshly created entity of another type at the same
	 * position/rotation/motion. A Java entity cannot change its class, so "swap the
	 * type" really means: spawn the new one, optionally carry the identity data
	 * over, remove the old one. Players are never converted. Returns the new entity
	 * (or the original when the type id is unknown or creation fails).
	 */
	public static Entity convertEntityType(Entity entity, String typeId, boolean keepData) {
		if (entity == null || entity instanceof net.minecraft.world.entity.player.Player || !(entity.level() instanceof ServerLevel serverLevel)) return entity;
		if (typeId == null || typeId.isBlank()) return entity;
		ResourceLocation id = ResourceLocation.tryParse(typeId.trim().toLowerCase(Locale.ROOT));
		if (id == null || !BuiltInRegistries.ENTITY_TYPE.containsKey(id)) return entity;
		Entity created = BuiltInRegistries.ENTITY_TYPE.get(id).create(serverLevel);
		if (created == null) return entity;
		created.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
		created.setDeltaMovement(entity.getDeltaMovement());
		if (keepData) {
			if (entity.hasCustomName()) {
				created.setCustomName(entity.getCustomName());
				created.setCustomNameVisible(entity.isCustomNameVisible());
			}
			created.getPersistentData().merge(entity.getPersistentData());
			created.setSilent(entity.isSilent());
			created.setInvulnerable(entity.isInvulnerable());
			created.setGlowingTag(entity.hasGlowingTag());
			for (String tag : entity.getTags()) {
				created.addTag(tag);
			}
		}
		serverLevel.addFreshEntity(created);
		entity.discard();
		return created;
	}

	/**
	 * Removes the entity and drops an item stack in its place (keeps the entity's
	 * custom name on the item). Players are never affected. Returns the dropped
	 * item entity, or the original entity when the item id is unknown.
	 */
	public static Entity convertEntityToItem(Entity entity, String itemId, double count) {
		if (entity == null || entity instanceof net.minecraft.world.entity.player.Player || !(entity.level() instanceof ServerLevel serverLevel)) return entity;
		ResourceLocation id = itemId == null ? null : ResourceLocation.tryParse(itemId.trim().toLowerCase(Locale.ROOT));
		if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) return entity;
		net.minecraft.world.item.Item item = BuiltInRegistries.ITEM.get(id);
		if (item == net.minecraft.world.item.Items.AIR) return entity;
		net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(item, (int) Math.max(1, Math.min(64, count)));
		if (entity.hasCustomName()) {
			stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, entity.getCustomName());
		}
		net.minecraft.world.entity.item.ItemEntity dropped = new net.minecraft.world.entity.item.ItemEntity(serverLevel, entity.getX(), entity.getY() + 0.25D, entity.getZ(), stack);
		dropped.setDefaultPickUpDelay();
		serverLevel.addFreshEntity(dropped);
		entity.discard();
		return dropped;
	}

	public static Entity asGenericEntity(Entity entity) {
		return entity;
	}

	public static void setAttackTarget(Entity entity, Entity target) {
		if (entity instanceof Mob mob) {
			mob.setTarget(target instanceof LivingEntity living && living.isAlive() ? living : null);
		}
	}

	public static void flyToward(Entity entity, Entity target, double speed, double stopDistance, double hoverHeight) {
		if (entity == null || target == null || !target.isAlive()) return;

		speed = sane(speed, 1.0D, 0.0D, 8.0D);
		stopDistance = sane(stopDistance, 1.0D, 0.0D, 128.0D);
		Vec3 wanted = target.position().add(0.0D, target.getBbHeight() * 0.5D + hoverHeight, 0.0D);
		Vec3 offset = wanted.subtract(entity.position());
		double distance = offset.length();
		if (distance <= Math.max(stopDistance, 0.0D)) {
			entity.setDeltaMovement(entity.getDeltaMovement().scale(0.72D));
			return;
		}

		Vec3 direction = offset.normalize();
		Vec3 current = entity.getDeltaMovement();
		double horizontal = Math.min(0.65D, 0.11D * speed);
		double vertical = Math.min(0.55D, 0.09D * speed);
		entity.setDeltaMovement(current.scale(0.78D).add(direction.x * horizontal, direction.y * vertical, direction.z * horizontal));
		entity.setNoGravity(true);

		if (entity instanceof Mob mob) {
			mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
			mob.getMoveControl().setWantedPosition(wanted.x, wanted.y, wanted.z, speed);
		}
	}

	public static void glidingToward(Entity entity, Entity target, double speed, double stopDistance, double fallRate, boolean liftWhenLow) {
		if (entity == null || target == null || !target.isAlive()) return;

		speed = sane(speed, 0.8D, 0.0D, 8.0D);
		stopDistance = sane(stopDistance, 4.0D, 0.0D, 128.0D);
		fallRate = sane(fallRate, 0.04D, 0.0D, 1.0D);
		Vec3 offset = target.position().subtract(entity.position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() <= stopDistance * stopDistance) {
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.86D, 1.0D, 0.86D).add(0.0D, -fallRate, 0.0D));
			return;
		}

		Vec3 dir = horizontal.lengthSqr() > MIN_DISTANCE ? horizontal.normalize() : Vec3.ZERO;
		double lift = liftWhenLow && entity.getY() < target.getY() + target.getBbHeight() ? Math.min(0.14D, speed * 0.035D) : -fallRate;
		entity.setDeltaMovement(entity.getDeltaMovement().scale(0.84D).add(dir.x * speed * 0.09D, lift, dir.z * speed * 0.09D));
		if (entity instanceof Mob mob) mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
	}

	public static void groundPathTo(Entity entity, Entity target, double speed, double stopDistance, boolean directFallback) {
		if (!(entity instanceof Mob mob) || target == null || !target.isAlive()) return;

		speed = sane(speed, 1.0D, 0.0D, 8.0D);
		stopDistance = sane(stopDistance, 2.5D, 0.0D, 128.0D);

		// Remember the request so the entity tick handler keeps the mob walking even when
		// this block is only called once and the mob's own AI goals fight the navigation.
		CompoundTag data = mob.getPersistentData();
		data.putInt(PATH_TARGET_TAG, target.getId());
		data.putDouble(PATH_SPEED_TAG, speed);
		data.putDouble(PATH_STOP_TAG, stopDistance);
		data.putBoolean(PATH_FALLBACK_TAG, directFallback);
		data.putLong(PATH_EXPIRY_TAG, mob.level().getGameTime() + PATH_TIMEOUT_TICKS);

		stepGroundPath(mob, target, speed, stopDistance, directFallback);
	}

	public static void cancelGroundPath(Entity entity) {
		if (!(entity instanceof Mob mob)) return;
		clearForcedPath(mob);
		mob.getNavigation().stop();
	}

	private static void clearForcedPath(Mob mob) {
		CompoundTag data = mob.getPersistentData();
		data.remove(PATH_TARGET_TAG);
		data.remove(PATH_SPEED_TAG);
		data.remove(PATH_STOP_TAG);
		data.remove(PATH_FALLBACK_TAG);
		data.remove(PATH_EXPIRY_TAG);
	}

	private static void tickForcedPath(Entity entity) {
		if (!(entity instanceof Mob mob) || mob.level().isClientSide()) return;
		CompoundTag data = mob.getPersistentData();
		if (!data.contains(PATH_TARGET_TAG, net.minecraft.nbt.Tag.TAG_ANY_NUMERIC)) return;

		if (mob.level().getGameTime() > data.getLong(PATH_EXPIRY_TAG)) {
			clearForcedPath(mob);
			return;
		}

		Entity target = mob.level().getEntity(data.getInt(PATH_TARGET_TAG));
		if (target == null || !target.isAlive()) {
			clearForcedPath(mob);
			mob.getNavigation().stop();
			return;
		}

		double speed = sane(data.getDouble(PATH_SPEED_TAG), 1.0D, 0.0D, 8.0D);
		double stopDistance = sane(data.getDouble(PATH_STOP_TAG), 2.5D, 0.0D, 128.0D);
		if (mob.distanceToSqr(target) <= stopDistance * stopDistance) {
			clearForcedPath(mob);
			mob.getNavigation().stop();
			mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
			return;
		}

		stepGroundPath(mob, target, speed, stopDistance, data.getBoolean(PATH_FALLBACK_TAG));
	}

	private static void stepGroundPath(Mob mob, Entity target, double speed, double stopDistance, boolean directFallback) {
		double distanceSqr = mob.distanceToSqr(target);
		if (distanceSqr <= stopDistance * stopDistance) {
			mob.getNavigation().stop();
			mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
			return;
		}

		if (mob.getNavigation().isDone() || mob.tickCount % 10 == 0) {
			mob.getNavigation().moveTo(target, speed);
		}
		mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
		if (directFallback && (mob.getNavigation().isDone() || mob.tickCount % 5 == 0)) {
			Vec3 offset = target.position().subtract(mob.position());
			Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
			if (horizontal.lengthSqr() > MIN_DISTANCE) {
				Vec3 dir = horizontal.normalize();
				double accel = Math.min(0.28D, speed * 0.075D);
				mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.82D, 1.0D, 0.82D).add(dir.x * accel, 0.0D, dir.z * accel));
			}
		}
	}

	public static void swimToward(Entity entity, Entity target, double speed, double stopDistance, boolean directFallback) {
		if (!(entity instanceof Mob mob) || target == null || !target.isAlive()) return;

		speed = sane(speed, 1.35D, 0.0D, 10.0D);
		stopDistance = sane(stopDistance, 2.0D, 0.0D, 128.0D);
		double distanceSqr = mob.distanceToSqr(target);
		if (distanceSqr <= stopDistance * stopDistance) {
			mob.getNavigation().stop();
			mob.setDeltaMovement(mob.getDeltaMovement().scale(0.72D));
			return;
		}

		mob.getNavigation().moveTo(target, speed);
		mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
		if (directFallback || mob.isInWaterOrBubble() || mob.isInFluidType()) {
			Vec3 wanted = target.position().add(0.0D, target.getBbHeight() * 0.35D, 0.0D);
			Vec3 offset = wanted.subtract(mob.position());
			if (offset.lengthSqr() > MIN_DISTANCE) {
				Vec3 dir = offset.normalize();
				double horizontal = Math.min(0.42D, speed * 0.12D);
				double vertical = Math.min(0.32D, speed * 0.09D);
				mob.setDeltaMovement(mob.getDeltaMovement().scale(0.74D).add(dir.x * horizontal, dir.y * vertical, dir.z * horizontal));
			}
		}
	}

	public static void strafeAround(Entity entity, Entity target, double speed, double radius, boolean airborne) {
		if (entity == null || target == null || !target.isAlive()) return;

		double signedSpeed = Double.isFinite(speed) ? Math.max(-8.0D, Math.min(8.0D, speed)) : 0.9D;
		double absSpeed = Math.abs(signedSpeed);
		if (absSpeed <= MIN_DISTANCE) absSpeed = 0.9D;
		radius = sane(radius, 4.0D, 0.5D, 128.0D);
		Vec3 offset = entity.position().subtract(target.position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() <= MIN_DISTANCE) horizontal = new Vec3(1.0D, 0.0D, 0.0D);
		Vec3 radial = horizontal.normalize();
		double direction = (((entity.getId() + entity.tickCount / 60) & 1) == 0 ? 1.0D : -1.0D) * Math.signum(signedSpeed == 0.0D ? 1.0D : signedSpeed);
		Vec3 tangent = new Vec3(-radial.z * direction, 0.0D, radial.x * direction);
		double distance = Math.sqrt(horizontal.lengthSqr());
		double radialCorrection = Math.max(-1.0D, Math.min(1.0D, (distance - radius) / radius));
		Vec3 steer = tangent.scale(absSpeed * 0.11D).subtract(radial.scale(radialCorrection * absSpeed * 0.055D));
		double ySteer = airborne ? Math.max(-0.18D, Math.min(0.18D, (target.getY() + target.getBbHeight() * 0.5D - entity.getY()) * 0.025D)) : 0.0D;
		entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.82D, airborne ? 0.82D : 1.0D, 0.82D).add(steer.x, ySteer, steer.z));
		if (airborne) entity.setNoGravity(true);
		if (entity instanceof Mob mob) mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
	}

	public static void performMultiAttack(Entity entity, Entity target, String mode, double range, int cooldownTicks, int delayTicks, double jumpStrength) {
		if (!(entity instanceof Mob mob) || !(target instanceof LivingEntity living) || !living.isAlive()) return;

		range = sane(range, 3.0D, 0.25D, 128.0D);
		cooldownTicks = Math.max(0, cooldownTicks);
		delayTicks = Math.max(0, delayTicks);
		jumpStrength = sane(jumpStrength, 0.55D, 0.0D, 4.0D);
		CompoundTag data = mob.getPersistentData();
		int now = mob.tickCount;

		if (data.getInt(ATTACK_DUE_TAG) > 0 && now >= data.getInt(ATTACK_DUE_TAG)) {
			if (data.getInt(ATTACK_TARGET_TAG) == living.getId() && mob.distanceToSqr(living) <= (range + 2.0D) * (range + 2.0D)) {
				mob.doHurtTarget(living);
			}
			data.remove(ATTACK_DUE_TAG);
			data.remove(ATTACK_TARGET_TAG);
			data.putInt(ATTACK_COOLDOWN_TAG, now + cooldownTicks);
			return;
		}

		if (now < data.getInt(ATTACK_COOLDOWN_TAG) || mob.distanceToSqr(living) > range * range) return;

		String selected = mode == null ? "hit" : mode;
		if (selected.contains("jump")) {
			jumpToward(mob, living, jumpStrength);
		}
		if ("jump".equals(selected)) {
			data.putInt(ATTACK_COOLDOWN_TAG, now + cooldownTicks);
		} else if ("delayed_hit".equals(selected) || "jump_hit".equals(selected)) {
			data.putInt(ATTACK_DUE_TAG, now + delayTicks);
			data.putInt(ATTACK_TARGET_TAG, living.getId());
		} else {
			mob.doHurtTarget(living);
			data.putInt(ATTACK_COOLDOWN_TAG, now + cooldownTicks);
		}
	}

	public static void playSoundAtEntity(Entity entity, String soundId, String category, float volume, float pitch, boolean localOnly, boolean stopOnDeath) {
		if (entity == null || soundId == null || soundId.isBlank()) return;
		Level level = entity.level();
		ResourceLocation location = ResourceLocation.parse(soundId);
		SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(location);
		SoundSource source = soundSource(category);
		float safeVolume = (float) sane(volume, 1.0D, 0.0D, 64.0D);
		float safePitch = (float) sane(pitch, 1.0D, 0.0D, 8.0D);
		BlockPos pos = entity.blockPosition();

		if (level.isClientSide()) {
			level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), event, source, safeVolume, safePitch, localOnly);
		} else if (localOnly && entity instanceof ServerPlayer player) {
			level.playSound(player, pos, event, source, safeVolume, safePitch);
		} else {
			level.playSound(null, pos, event, source, safeVolume, safePitch);
		}

		recordSound(level, soundId, entity.position());
		if (stopOnDeath) addStopOnDeath(entity, soundId, source);
	}

	public static void stopSound(Entity entity, String soundId, String category) {
		if (entity == null || soundId == null || soundId.isBlank()) return;
		ResourceLocation location = ResourceLocation.parse(soundId);
		SoundSource source = soundSource(category);
		if (entity instanceof ServerPlayer player) {
			player.connection.send(new ClientboundStopSoundPacket(location, source));
		} else if (entity.level() instanceof ServerLevel serverLevel) {
			for (ServerPlayer player : serverLevel.players()) {
				if (player.distanceToSqr(entity) <= 4096.0D) {
					player.connection.send(new ClientboundStopSoundPacket(location, source));
				}
			}
		}
	}

	public static boolean recentSoundNear(Entity entity, String soundId, double radius, int ticks) {
		if (entity == null || soundId == null || soundId.isBlank() || !(entity.level() instanceof Level level)) return false;
		radius = sane(radius, 16.0D, 0.0D, 512.0D);
		long now = level.getGameTime();
		String dimension = level.dimension().location().toString();
		synchronized (RECENT_SOUNDS) {
			trimSoundRecords(now);
			for (SoundRecord record : RECENT_SOUNDS) {
				if (record.soundId.equals(soundId) && record.dimension.equals(dimension) && now - record.tick <= ticks && entity.position().distanceToSqr(record.position) <= radius * radius) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean recentAnySoundNear(Entity entity, double radius, int ticks) {
		if (entity == null || !(entity.level() instanceof Level level)) return false;
		radius = sane(radius, 16.0D, 0.0D, 512.0D);
		long now = level.getGameTime();
		String dimension = level.dimension().location().toString();
		synchronized (RECENT_SOUNDS) {
			trimSoundRecords(now);
			for (SoundRecord record : RECENT_SOUNDS) {
				if (record.dimension.equals(dimension) && now - record.tick <= ticks && entity.position().distanceToSqr(record.position) <= radius * radius) {
					return true;
				}
			}
		}
		return false;
	}

	public static double localAudioInputLevel(Entity entity) {
		if (FMLEnvironment.dist != Dist.CLIENT) return 0.0D;
		return ClientAudio.inputLevel();
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity == null) return;
		// Health floor as a true death guard: even a source that bypassed the incoming
		// damage cap (a direct setHealth(0), an NBT Health write) reaches death through
		// the cancelable LivingDeathEvent, so refuse it and restore health to the floor.
		if (entity instanceof LivingEntity floorLocked) {
			CompoundTag lockData = floorLocked.getPersistentData();
			if (lockData.contains(HEALTH_FLOOR_TAG)) {
				float floor = (float) lockData.getDouble(HEALTH_FLOOR_TAG);
				if (floor > 0.0F) {
					event.setCanceled(true);
					floorLocked.setHealth(Math.min(floor, floorLocked.getMaxHealth()));
					return;
				}
			}
		}
		removeDynamicLightFromEntity(entity);
		String stored = entity.getPersistentData().getString(STOP_ON_DEATH_TAG);
		if (stored.isBlank()) return;
		entity.getPersistentData().remove(STOP_ON_DEATH_TAG);
		for (String entry : stored.split("\\n")) {
			String[] parts = entry.split("\\|", 2);
			if (parts.length == 2) stopSound(entity, parts[0], parts[1]);
		}
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Post event) {
		updateEntityDynamicLight(event.getEntity());
		tickForcedPath(event.getEntity());
		tickOrbitFx(event.getEntity());
		tickEntityEmitter(event.getEntity());
		tickWeaponTrail(event.getEntity());
		if (event.getEntity() instanceof LivingEntity living) {
			clampLockedHealth(living);
		}
	}

	// ---- Health lock (clamp health to a floor and/or ceiling no matter what) ----
	//
	// Floor: the entity cannot drop below this health - incoming damage is capped so a
	// hit can never take it under the floor (totem / invincible-during-cutscene). A
	// floor of -1 disables the floor. Ceiling: the entity cannot rise above this - heal
	// events are capped and a per-tick clamp catches any other source (regeneration,
	// attribute changes, direct setHealth). A ceiling of -1 disables the ceiling.

	private static final String HEALTH_FLOOR_TAG = "CNEHealthFloor";
	private static final String HEALTH_CEILING_TAG = "CNEHealthCeiling";

	public static void lockEntityHealth(Entity entity, double floor, double ceiling) {
		if (!(entity instanceof LivingEntity living)) return;
		// Contradictory bounds (floor above ceiling) cannot both hold; raise the ceiling
		// to the floor so the entity is simply pinned at the floor, instead of silently
		// favouring the floor with a confusing permanent heal-deadlock.
		if (floor >= 0.0D && ceiling >= 0.0D && ceiling < floor) ceiling = floor;
		CompoundTag data = living.getPersistentData();
		if (floor >= 0.0D) {
			data.putDouble(HEALTH_FLOOR_TAG, floor);
		} else {
			data.remove(HEALTH_FLOOR_TAG);
		}
		if (ceiling >= 0.0D) {
			data.putDouble(HEALTH_CEILING_TAG, ceiling);
		} else {
			data.remove(HEALTH_CEILING_TAG);
		}
		clampLockedHealth(living);
	}

	public static void removeHealthLock(Entity entity) {
		if (entity == null) return;
		CompoundTag data = entity.getPersistentData();
		data.remove(HEALTH_FLOOR_TAG);
		data.remove(HEALTH_CEILING_TAG);
	}

	public static boolean hasHealthLock(Entity entity) {
		if (entity == null) return false;
		CompoundTag data = entity.getPersistentData();
		return data.contains(HEALTH_FLOOR_TAG) || data.contains(HEALTH_CEILING_TAG);
	}

	private static void clampLockedHealth(LivingEntity living) {
		if (living.level().isClientSide() || !living.isAlive()) return;
		CompoundTag data = living.getPersistentData();
		boolean hasFloor = data.contains(HEALTH_FLOOR_TAG);
		boolean hasCeiling = data.contains(HEALTH_CEILING_TAG);
		if (!hasFloor && !hasCeiling) return;
		float health = living.getHealth();
		float target = health;
		if (hasCeiling) target = Math.min(target, (float) data.getDouble(HEALTH_CEILING_TAG));
		if (hasFloor) target = Math.max(target, (float) data.getDouble(HEALTH_FLOOR_TAG));
		// setHealth already clamps to the max-health attribute; mirror that here so a
		// floor above the entity's max does not trigger a redundant setHealth each tick.
		target = Math.min(target, living.getMaxHealth());
		if (target > 0.0F && target != health) {
			living.setHealth(target);
			health = target;
		}
		// Absorption hearts count toward effective health, so a "no matter what" ceiling
		// must bound base health + absorption. Golden apples, the Absorption effect and
		// totems set absorption directly (no heal event), so this per-tick catch is the
		// only place that can enforce the cap against them.
		if (hasCeiling) {
			float ceiling = (float) data.getDouble(HEALTH_CEILING_TAG);
			float absorption = living.getAbsorptionAmount();
			float overflow = health + absorption - ceiling;
			if (overflow > 0.0F && absorption > 0.0F) {
				living.setAbsorptionAmount(Math.max(0.0F, absorption - overflow));
			}
		}
	}

	@SubscribeEvent
	public static void onHealthLockIncomingDamage(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		if (living == null) return;
		CompoundTag data = living.getPersistentData();
		if (!data.contains(HEALTH_FLOOR_TAG)) return;
		float floor = (float) data.getDouble(HEALTH_FLOOR_TAG);
		float maxAllowed = Math.max(0.0F, living.getHealth() - floor);
		if (event.getAmount() > maxAllowed) {
			event.setAmount(maxAllowed);
			if (maxAllowed <= 0.0F) event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onHealthLockHeal(LivingHealEvent event) {
		LivingEntity living = event.getEntity();
		if (living == null) return;
		CompoundTag data = living.getPersistentData();
		if (!data.contains(HEALTH_CEILING_TAG)) return;
		float ceiling = (float) data.getDouble(HEALTH_CEILING_TAG);
		float maxAllowed = Math.max(0.0F, ceiling - living.getHealth());
		if (event.getAmount() > maxAllowed) {
			event.setAmount(maxAllowed);
			if (maxAllowed <= 0.0F) event.setCanceled(true);
		}
	}

	// A player's root persistent data is NOT copied across a clone (NeoForge only carries the small
	// "PlayerPersisted" subtag), so on a death-respawn AND on a non-death clone (dimension change /
	// return from the End) we manually carry over the two things stored at the root that must survive:
	//   1. the health lock (a ceiling-only lock does not prevent death, so without this the cap would
	//      silently vanish on respawn), and
	//   2. every ENTITY PERSISTENT variable (CNEEntityVar_*), so "persistent" truly persists across
	//      death - the whole point of the persistent (vs lifetime) block family. Lifetime vars
	//      (CNELifeVar_*) are intentionally NOT carried; they are wiped on death by design.
	// Runs on both death and non-death clones so persistent vars also survive a dimension change.
	@SubscribeEvent
	public static void onHealthLockPlayerClone(net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone event) {
		try {
			CompoundTag from = event.getOriginal().getPersistentData();
			CompoundTag to = event.getEntity().getPersistentData();
			if (from.contains(HEALTH_FLOOR_TAG)) to.putDouble(HEALTH_FLOOR_TAG, from.getDouble(HEALTH_FLOOR_TAG));
			if (from.contains(HEALTH_CEILING_TAG)) to.putDouble(HEALTH_CEILING_TAG, from.getDouble(HEALTH_CEILING_TAG));
			for (String key : from.getAllKeys()) {
				if (!key.startsWith(PERSISTENT_VAR_PREFIX)) continue;
				net.minecraft.nbt.Tag tag = from.get(key);
				if (tag != null) to.put(key, tag.copy());
			}
			// Push the carried vars to the (re)spawned player's client so a GUI/overlay reads them
			// immediately - the fresh client entity starts with an empty persistent map otherwise.
			markVarsDirty(event.getEntity());
		} catch (Exception ignored) {
		}
	}

	private static void jumpToward(Entity entity, Entity target, double jumpStrength) {
		Vec3 offset = target.position().subtract(entity.position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		Vec3 dir = horizontal.lengthSqr() > MIN_DISTANCE ? horizontal.normalize() : Vec3.ZERO;
		entity.setDeltaMovement(entity.getDeltaMovement().add(dir.x * 0.45D, jumpStrength, dir.z * 0.45D));
	}

	private static void recordSound(Level level, String soundId, Vec3 position) {
		if (level == null) return;
		long tick = level.getGameTime();
		synchronized (RECENT_SOUNDS) {
			RECENT_SOUNDS.add(new SoundRecord(level.dimension().location().toString(), soundId, position, tick));
			trimSoundRecords(tick);
		}
	}

	private static void trimSoundRecords(long now) {
		Iterator<SoundRecord> iterator = RECENT_SOUNDS.iterator();
		while (iterator.hasNext()) {
			if (now - iterator.next().tick > 200) iterator.remove();
		}
		while (RECENT_SOUNDS.size() > MAX_SOUND_RECORDS) {
			RECENT_SOUNDS.remove(0);
		}
	}

	private static void addStopOnDeath(Entity entity, String soundId, SoundSource source) {
		CompoundTag data = entity.getPersistentData();
		String entry = soundId + "|" + source.name();
		String stored = data.getString(STOP_ON_DEATH_TAG);
		if (!stored.contains(entry)) {
			data.putString(STOP_ON_DEATH_TAG, stored.isBlank() ? entry : stored + "\n" + entry);
		}
	}

	private static SoundSource soundSource(String category) {
		if (category == null || category.isBlank()) return SoundSource.NEUTRAL;
		try {
			return SoundSource.valueOf(category.trim().toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ignored) {
			return SoundSource.NEUTRAL;
		}
	}

	private static boolean hasLivingTarget(Mob mob) {
		return mob != null && mob.getTarget() != null && mob.getTarget().isAlive();
	}

	private static double sane(double value, double fallback, double min, double max) {
		if (!Double.isFinite(value)) return fallback;
		return Math.max(min, Math.min(max, value));
	}

	private static String persistentEntityKey(String name) {
		String key = String.valueOf(name == null ? "" : name).trim().toLowerCase(Locale.ROOT);
		key = key.replace('\\', '/').replace(' ', '_');
		key = key.replaceAll("[^a-z0-9_./:-]", "_");
		key = key.replaceAll("_+", "_");
		while (key.startsWith("_")) key = key.substring(1);
		while (key.endsWith("_")) key = key.substring(0, key.length() - 1);
		return "CNEEntityVar_" + (key.isBlank() ? "value" : key);
	}

	private record SoundRecord(String dimension, String soundId, Vec3 position, long tick) {
	}

	// ----- ENTITY PERSISTENT variable client sync (server -> tracking clients) -----
	//
	// The persistent vars live in the entity's server-side getPersistentData(), which is never synced
	// on its own, so client-side get-blocks (GUIs, overlays) would only ever see the fallback. This
	// payload carries a compound of just the entity's CNEEntityVar_ keys; the client writes them into
	// the mirrored entity's getPersistentData() so the SAME get-block reads the synced value there.
	// Registered play-to-client via the MOD-bus handler below (mirrors CneBoneItemRuntime).

	@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
	public static final class EntityVarRegistration {
		@SubscribeEvent
		public static void register(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
			event.registrar("euru").playToClient(EntityVarSyncMessage.TYPE, EntityVarSyncMessage.STREAM_CODEC, EntityVarSyncMessage::handleData);
		}
	}

	public record EntityVarSyncMessage(int entityId, CompoundTag vars) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
		public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<EntityVarSyncMessage> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(
			ResourceLocation.fromNamespaceAndPath("euru", "entity_var_sync"));

		public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, EntityVarSyncMessage> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(
			(net.minecraft.network.RegistryFriendlyByteBuf buffer, EntityVarSyncMessage message) -> {
				buffer.writeVarInt(message.entityId());
				buffer.writeNbt(message.vars() == null ? new CompoundTag() : message.vars());
			},
			(net.minecraft.network.RegistryFriendlyByteBuf buffer) -> {
				int entityId = buffer.readVarInt();
				CompoundTag vars = buffer.readNbt();
				return new EntityVarSyncMessage(entityId, vars == null ? new CompoundTag() : vars);
			});

		@Override
		public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<EntityVarSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final EntityVarSyncMessage message, final net.neoforged.neoforge.network.handling.IPayloadContext context) {
			if (context.flow() != net.minecraft.network.protocol.PacketFlow.CLIENTBOUND) return;
			context.enqueueWork(() -> {
				if (FMLEnvironment.dist != Dist.CLIENT) return;
				applyClientEntityVars(message.entityId(), message.vars());
			}).exceptionally(e -> {
				System.err.println("[ChickenNugget Extras] Entity var sync failed: " + e);
				return null;
			});
		}
	}

	// CLIENT: resolve the mirrored entity by network id and overwrite its CNEEntityVar_ keys with the
	// synced compound (any local CNEEntityVar_ not in the payload is dropped, so a server-side remove
	// clears client-side too). Isolated so the payload handler stays inside the enqueued client task.
	private static void applyClientEntityVars(int entityId, CompoundTag vars) {
		try {
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
			if (mc == null || mc.level == null) return;
			Entity entity = mc.level.getEntity(entityId);
			if (entity == null) return;
			CompoundTag data = entity.getPersistentData();
			for (String key : new java.util.HashSet<>(data.getAllKeys())) {
				if (key.startsWith(PERSISTENT_VAR_PREFIX)) data.remove(key);
			}
			if (vars != null) {
				for (String key : vars.getAllKeys()) {
					if (!key.startsWith(PERSISTENT_VAR_PREFIX)) continue;
					net.minecraft.nbt.Tag tag = vars.get(key);
					if (tag != null) data.put(key, tag.copy());
				}
			}
		} catch (Exception ignored) {
		}
	}

	public static class BetterFlightGoal extends Goal {
		protected final Mob mob;
		protected final double speed;
		protected final double stopDistance;
		protected final double hoverHeight;

		public BetterFlightGoal(Mob mob, double speed, double stopDistance, double hoverHeight) {
			this.mob = mob;
			this.speed = speed;
			this.stopDistance = stopDistance;
			this.hoverHeight = hoverHeight;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean canContinueToUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			flyToward(this.mob, this.mob.getTarget(), this.speed, this.stopDistance, this.hoverHeight);
		}

		@Override
		public void stop() {
			this.mob.setNoGravity(false);
		}
	}

	public static class StrafingFlightGoal extends Goal {
		protected final Mob mob;
		protected final double speed;
		protected final double radius;
		protected final double heightOffset;
		protected final int changeTicks;
		private int direction = 1;

		public StrafingFlightGoal(Mob mob, double speed, double radius, double heightOffset, int changeTicks) {
			this.mob = mob;
			this.speed = speed;
			this.radius = radius;
			this.heightOffset = heightOffset;
			this.changeTicks = Math.max(10, changeTicks);
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean canContinueToUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			if (this.mob.tickCount % this.changeTicks == 0) this.direction *= -1;
			LivingEntity target = this.mob.getTarget();
			if (target == null) return;
			strafeAround(this.mob, target, this.speed * this.direction, this.radius, true);
			double yDelta = target.getY() + target.getBbHeight() * 0.5D + this.heightOffset - this.mob.getY();
			this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, Math.max(-0.12D, Math.min(0.12D, yDelta * 0.025D)), 0.0D));
		}

		@Override
		public void stop() {
			this.mob.setNoGravity(false);
		}
	}

	public static class GlidingPathGoal extends Goal {
		protected final Mob mob;
		protected final double speed;
		protected final double stopDistance;
		protected final double fallRate;
		protected final boolean liftWhenLow;

		public GlidingPathGoal(Mob mob, double speed, double stopDistance, double fallRate, boolean liftWhenLow) {
			this.mob = mob;
			this.speed = speed;
			this.stopDistance = stopDistance;
			this.fallRate = fallRate;
			this.liftWhenLow = liftWhenLow;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean canContinueToUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			glidingToward(this.mob, this.mob.getTarget(), this.speed, this.stopDistance, this.fallRate, this.liftWhenLow);
		}
	}

	public static class BetterGroundPathGoal extends Goal {
		protected final Mob mob;
		protected final double speed;
		protected final double stopDistance;
		protected final boolean directFallback;

		public BetterGroundPathGoal(Mob mob, double speed, double stopDistance, boolean directFallback) {
			this.mob = mob;
			this.speed = speed;
			this.stopDistance = stopDistance;
			this.directFallback = directFallback;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean canContinueToUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity target = this.mob.getTarget();
			if (target == null) return;
			stepGroundPath(this.mob, target, sane(this.speed, 1.0D, 0.0D, 8.0D), sane(this.stopDistance, 2.5D, 0.0D, 128.0D), this.directFallback);
		}

		@Override
		public void stop() {
			this.mob.getNavigation().stop();
		}
	}

	public static class GroundStrafeGoal extends Goal {
		protected final Mob mob;
		protected final double speed;
		protected final double radius;
		protected final int changeTicks;
		private int direction = 1;

		public GroundStrafeGoal(Mob mob, double speed, double radius, int changeTicks) {
			this.mob = mob;
			this.speed = speed;
			this.radius = radius;
			this.changeTicks = Math.max(10, changeTicks);
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean canContinueToUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			if (this.mob.tickCount % this.changeTicks == 0) this.direction *= -1;
			strafeAround(this.mob, this.mob.getTarget(), this.speed * this.direction, this.radius, false);
		}
	}

	public static class BetterSwimmingGoal extends Goal {
		protected final Mob mob;
		protected final double speed;
		protected final double stopDistance;
		protected final boolean directFallback;

		public BetterSwimmingGoal(Mob mob, double speed, double stopDistance, boolean directFallback) {
			this.mob = mob;
			this.speed = speed;
			this.stopDistance = stopDistance;
			this.directFallback = directFallback;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean canContinueToUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			swimToward(this.mob, this.mob.getTarget(), this.speed, this.stopDistance, this.directFallback);
		}
	}

	public static class MultiAttackGoal extends Goal {
		protected final Mob mob;
		protected final String mode;
		protected final double speed;
		protected final double range;
		protected final int cooldown;
		protected final int delay;
		protected final double jumpStrength;

		public MultiAttackGoal(Mob mob, String mode, double speed, double range, int cooldown, int delay, double jumpStrength) {
			this.mob = mob;
			this.mode = mode;
			this.speed = speed;
			this.range = range;
			this.cooldown = Math.max(0, cooldown);
			this.delay = Math.max(0, delay);
			this.jumpStrength = jumpStrength;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean canContinueToUse() {
			return hasLivingTarget(this.mob);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity target = this.mob.getTarget();
			if (target == null) return;
			stepGroundPath(this.mob, target, sane(this.speed, 1.0D, 0.0D, 8.0D), Math.max(0.5D, this.range * 0.65D), false);
			performMultiAttack(this.mob, target, this.mode, this.range, this.cooldown, this.delay, this.jumpStrength);
		}
	}
}
