package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

@net.neoforged.fml.common.EventBusSubscriber(modid = "euru", bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD)
public final class CneCameraRuntime {
	private static final int ACTION_CLEAR = 0;
	private static final int ACTION_SHAKE = 1;
	private static final int ACTION_POSITION = 2;
	private static final int ACTION_ANGLE = 3;
	private static final int ACTION_POSITION_ABSOLUTE = 4;
	private static final double MAX_POSITION_OFFSET = 256.0D;
	private static final double MAX_ANGLE_OFFSET = 360.0D;
	private static final double MAX_SHAKE_STRENGTH = 32.0D;
	private static final double CAMERA_COLLISION_PADDING = 0.35D;
	private static final double CAMERA_MIN_COLLISION_DISTANCE = 0.25D;
	private static final double CAMERA_FLOOR_PADDING = 0.08D;
	private static final double PLAYER_HEAD_VIEW_MIN_FORWARD_OFFSET = 0.35D;
	private static final double PLAYER_HEAD_VIEW_BODY_ANCHOR_HEIGHT = 0.72D;
	private static final int KEY_SYNC_MAX_ENTRIES = 192;
	private static final int KEY_SYNC_INTERVAL_TICKS = 2;
	private static final String KEY_STATES_TAG = "CNEKeybindStates";
	private static final String FOLLOW_MODE_WORLD = "world";
	private static final String FOLLOW_MODE_TARGET_BODY = "target_body";
	private static final String FOLLOW_MODE_TARGET_HEAD = "target_head";
	private static final String FOLLOW_MODE_VIEWER_CAMERA = "viewer_camera";
	private static final String FOLLOW_MODE_PLAYER_ORBIT = "player_orbit";
	private static final String FOLLOW_MODE_PLAYER_HEAD_VIEW = "player_head_view";
	private static volatile boolean listenerRegistered;
	private static volatile boolean positionMixinSeen;

	private CneCameraRuntime() {
	}

	public static void shake(double strength, int ticks) {
		shake(null, strength, ticks);
	}

	public static void shake(net.minecraft.world.entity.Entity target, double strength, int ticks) {
		applyToTarget(target, ACTION_SHAKE, Math.max(0.0D, strength), 0.0D, 0.0D, Math.max(0, ticks));
	}

	public static void setPositionOffset(double x, double y, double z) {
		setPositionOffset(null, x, y, z);
	}

	public static void setPositionOffset(net.minecraft.world.entity.Entity target, double x, double y, double z) {
		applyToTarget(target, ACTION_POSITION, x, y, z, 0);
	}

	public static void setPosition(double x, double y, double z) {
		setPosition(null, x, y, z);
	}

	public static void setPosition(net.minecraft.world.entity.Entity target, double x, double y, double z) {
		applyToTarget(target, ACTION_POSITION_ABSOLUTE, x, y, z, 0);
	}

	public static void followEntity(net.minecraft.world.entity.Entity viewer, net.minecraft.world.entity.Entity cameraTarget, double xOffset, double yOffset, double zOffset) {
		followEntity(viewer, cameraTarget, xOffset, yOffset, zOffset, FOLLOW_MODE_WORLD);
	}

	public static void followEntity(net.minecraft.world.entity.Entity viewer, net.minecraft.world.entity.Entity cameraTarget, double xOffset, double yOffset, double zOffset, String mode) {
		if (cameraTarget == null) {
			clear(viewer);
			return;
		}
		applyFollowToTarget(viewer, cameraTarget, xOffset, yOffset, zOffset, mode);
	}

	public static void orbitEntity(net.minecraft.world.entity.Entity viewer, net.minecraft.world.entity.Entity cameraTarget, double distance, double height, double sideOffset) {
		orbitEntity(viewer, cameraTarget, distance, height, sideOffset, false);
	}

	public static void orbitEntity(net.minecraft.world.entity.Entity viewer, net.minecraft.world.entity.Entity cameraTarget, double distance, double height, double sideOffset, boolean headFollowsPitch) {
		String mode = viewer != null && cameraTarget != null && viewer.getUUID().equals(cameraTarget.getUUID()) ? FOLLOW_MODE_PLAYER_ORBIT : FOLLOW_MODE_VIEWER_CAMERA;
		if (cameraTarget == null) {
			clear(viewer);
			return;
		}
		applyFollowToTarget(viewer, cameraTarget, sideOffset, height, -Math.abs(distance), mode, headFollowsPitch);
	}

	public static void playerHeadView(net.minecraft.world.entity.Entity viewer, double forwardOffset, double heightOffset, double sideOffset) {
		if (viewer == null) {
			clear(null);
			return;
		}
		applyFollowToTarget(viewer, viewer, sideOffset, heightOffset, forwardOffset, FOLLOW_MODE_PLAYER_HEAD_VIEW);
	}

	public static void setAngleOffset(double yaw, double pitch, double roll) {
		setAngleOffset(null, yaw, pitch, roll);
	}

	public static void setAngleOffset(net.minecraft.world.entity.Entity target, double yaw, double pitch, double roll) {
		applyToTarget(target, ACTION_ANGLE, yaw, pitch, roll, 0);
	}

	public static void clear() {
		clear(null);
	}

	public static void clear(net.minecraft.world.entity.Entity target) {
		applyToTarget(target, ACTION_CLEAR, 0.0D, 0.0D, 0.0D, 0);
	}

	public static boolean isShaking() {
		return isShaking(null);
	}

	public static boolean isShaking(net.minecraft.world.entity.Entity target) {
		net.minecraft.nbt.CompoundTag data = dataFor(target);
		return data != null && data.getDouble("CNECameraShakeStrength") > 0.0D && data.getInt("CNECameraShakeTicks") > 0;
	}

	public static double positionOffset(net.minecraft.world.entity.Entity target, String axis) {
		net.minecraft.nbt.CompoundTag data = dataFor(target);
		if (data == null)
			return 0.0D;
		String key = String.valueOf(axis == null ? "" : axis);
		if ("x".equals(key))
			return data.getDouble("CNECameraOffsetX");
		if ("y".equals(key))
			return data.getDouble("CNECameraOffsetY");
		if ("z".equals(key))
			return data.getDouble("CNECameraOffsetZ");
		return 0.0D;
	}

	public static double value(String value) {
		return value(null, value);
	}

	public static double value(net.minecraft.world.entity.Entity target, String value) {
		net.minecraft.nbt.CompoundTag data = dataFor(target);
		String key = String.valueOf(value == null ? "" : value);
		if (data != null) {
			if ("shake_strength".equals(key))
				return data.getDouble("CNECameraShakeStrength");
			if ("shake_ticks".equals(key))
				return data.getInt("CNECameraShakeTicks");
			if ("offset_x".equals(key))
				return data.getDouble("CNECameraOffsetX");
			if ("offset_y".equals(key))
				return data.getDouble("CNECameraOffsetY");
			if ("offset_z".equals(key))
				return data.getDouble("CNECameraOffsetZ");
			if ("yaw_offset".equals(key))
				return data.getDouble("CNECameraYawOffset");
			if ("pitch_offset".equals(key))
				return data.getDouble("CNECameraPitchOffset");
			if ("roll_offset".equals(key))
				return data.getDouble("CNECameraRollOffset");
		}
		if (target != null && !isLocalPlayer(target))
			return 0.0D;
		return CameraAccess.value(key);
	}

	public static boolean isKeyDown(net.minecraft.world.entity.Entity player, String keyId) {
		net.minecraft.nbt.CompoundTag state = keyState(player, keyId, false);
		return state != null && state.getBoolean("down");
	}

	public static boolean wasKeyPressed(net.minecraft.world.entity.Entity player, String keyId) {
		net.minecraft.nbt.CompoundTag state = keyState(player, keyId, false);
		if (state == null)
			return false;
		int pressed = state.getInt("pressed");
		if (pressed <= 0)
			return false;
		state.putInt("pressed", pressed - 1);
		return true;
	}

	public static boolean wasKeyReleased(net.minecraft.world.entity.Entity player, String keyId) {
		net.minecraft.nbt.CompoundTag state = keyState(player, keyId, false);
		if (state == null)
			return false;
		int released = state.getInt("released");
		if (released <= 0)
			return false;
		state.putInt("released", released - 1);
		return true;
	}

	public static double keyHoldTicks(net.minecraft.world.entity.Entity player, String keyId) {
		net.minecraft.nbt.CompoundTag state = keyState(player, keyId, false);
		return state == null ? 0.0D : state.getInt("holdTicks");
	}

	public static boolean isRawKeyDown(net.minecraft.world.entity.Entity player, String keyId) {
		return isKeyDown(player, "raw." + String.valueOf(keyId == null ? "" : keyId));
	}

	public static boolean wasRawKeyPressed(net.minecraft.world.entity.Entity player, String keyId) {
		return wasKeyPressed(player, "raw." + String.valueOf(keyId == null ? "" : keyId));
	}

	public static boolean wasRawKeyReleased(net.minecraft.world.entity.Entity player, String keyId) {
		return wasKeyReleased(player, "raw." + String.valueOf(keyId == null ? "" : keyId));
	}

	public static double rawKeyHoldTicks(net.minecraft.world.entity.Entity player, String keyId) {
		return keyHoldTicks(player, "raw." + String.valueOf(keyId == null ? "" : keyId));
	}

	public static void apply() {
		ensureListener();
	}

	@net.neoforged.bus.api.SubscribeEvent
	public static void registerPayload(net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent event) {
		addNetworkMessage(CameraMessage.TYPE, CameraMessage.STREAM_CODEC, CameraMessage::handleData);
		addNetworkMessage(CameraFollowMessage.TYPE, CameraFollowMessage.STREAM_CODEC, CameraFollowMessage::handleData);
		addNetworkMessage(KeySyncMessage.TYPE, KeySyncMessage.STREAM_CODEC, KeySyncMessage::handleData);
	}

	// Keybind state sync must work even when no camera feature is used, so the client
	// tick listener is registered at client setup instead of waiting for the first
	// camera action to reach this client. Fires on the physical client only.
	@net.neoforged.bus.api.SubscribeEvent
	public static void registerClientListener(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
		event.enqueueWork(CneCameraRuntime::ensureListener);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <T extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> void addNetworkMessage(net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<T> type,
			net.minecraft.network.codec.StreamCodec<? extends net.minecraft.network.FriendlyByteBuf, T> codec, net.neoforged.neoforge.network.handling.IPayloadHandler<T> handler) {
		try {
			Class<?> modClass = Class.forName(modClassName());
			for (java.lang.reflect.Method method : modClass.getMethods()) {
				if ("addNetworkMessage".equals(method.getName()) && method.getParameterCount() == 3) {
					method.invoke(null, type, codec, handler);
					return;
				}
			}
			System.err.println("[ChickenNugget Extras] Could not find addNetworkMessage on " + modClass.getName());
		} catch (Throwable error) {
			System.err.println("[ChickenNugget Extras] Could not register camera network messages: " + error);
		}
	}

	private static String modClassName() {
		String packageName = CneCameraRuntime.class.getPackageName();
		String basePackage = packageName.endsWith(".chickennuggetextras") ? packageName.substring(0, packageName.length() - ".chickennuggetextras".length()) : packageName;
		return basePackage + "." + javaModClassName("euru");
	}

	private static String javaModClassName(String modid) {
		StringBuilder name = new StringBuilder();
		boolean capitalizeNext = true;
		for (char c : String.valueOf(modid == null ? "" : modid).toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				name.append(capitalizeNext ? Character.toUpperCase(c) : c);
				capitalizeNext = false;
			} else {
				capitalizeNext = true;
			}
		}
		if (name.length() == 0)
			name.append("Mod");
		return name + "Mod";
	}

	private static void applyToTarget(net.minecraft.world.entity.Entity target, int action, double a, double b, double c, int ticks) {
		net.minecraft.world.entity.Entity resolved = target != null ? target : localPlayer();
		if (resolved instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			store(serverPlayer.getPersistentData(), action, a, b, c, ticks);
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new CameraMessage(action, a, b, c, ticks));
			return;
		}
		if (resolved != null && resolved.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
			store(resolved.getPersistentData(), action, a, b, c, ticks);
			double radius = 192.0D;
			for (net.minecraft.server.level.ServerPlayer player : serverLevel.players()) {
				if (player.distanceToSqr(resolved) <= radius * radius) {
					store(player.getPersistentData(), action, a, b, c, ticks);
					net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new CameraMessage(action, a, b, c, ticks));
				}
			}
			return;
		}
		if (resolved != null && resolved.level() != null && resolved.level().isClientSide()) {
			applyLocal(action, a, b, c, ticks);
			return;
		}
		if (resolved != null && !isLocalPlayer(resolved)) {
			store(resolved.getPersistentData(), action, a, b, c, ticks);
			return;
		}
		applyLocal(action, a, b, c, ticks);
	}

	private static void applyFollowToTarget(net.minecraft.world.entity.Entity viewer, net.minecraft.world.entity.Entity cameraTarget, double xOffset, double yOffset, double zOffset, String mode) {
		applyFollowToTarget(viewer, cameraTarget, xOffset, yOffset, zOffset, mode, false);
	}

	private static void applyFollowToTarget(net.minecraft.world.entity.Entity viewer, net.minecraft.world.entity.Entity cameraTarget, double xOffset, double yOffset, double zOffset, String mode, boolean headFollowsPitch) {
		java.util.UUID targetUuid = cameraTarget.getUUID();
		String followMode = normalizeFollowMode(mode);
		net.minecraft.world.entity.Entity resolved = viewer != null ? viewer : localPlayer();
		if (resolved instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			storeFollow(serverPlayer.getPersistentData(), targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch);
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new CameraFollowMessage(targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch));
			return;
		}
		if (resolved != null && resolved.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
			storeFollow(resolved.getPersistentData(), targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch);
			double radius = 192.0D;
			for (net.minecraft.server.level.ServerPlayer player : serverLevel.players()) {
				if (player.distanceToSqr(resolved) <= radius * radius) {
					storeFollow(player.getPersistentData(), targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch);
					net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new CameraFollowMessage(targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch));
				}
			}
			return;
		}
		if (resolved != null && resolved.level() != null && resolved.level().isClientSide()) {
			applyFollowLocal(targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch);
			return;
		}
		if (resolved != null && !isLocalPlayer(resolved)) {
			storeFollow(resolved.getPersistentData(), targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch);
			return;
		}
		applyFollowLocal(targetUuid, xOffset, yOffset, zOffset, followMode, headFollowsPitch);
	}

	private static void applyLocal(int action, double a, double b, double c, int ticks) {
		ensureListener();
		net.minecraft.nbt.CompoundTag data = data();
		if (data != null)
			store(data, action, a, b, c, ticks);
	}

	private static void applyFollowLocal(java.util.UUID targetUuid, double xOffset, double yOffset, double zOffset, String mode) {
		applyFollowLocal(targetUuid, xOffset, yOffset, zOffset, mode, false);
	}

	private static void applyFollowLocal(java.util.UUID targetUuid, double xOffset, double yOffset, double zOffset, String mode, boolean headFollowsPitch) {
		ensureListener();
		net.minecraft.nbt.CompoundTag data = data();
		if (data != null)
			storeFollow(data, targetUuid, xOffset, yOffset, zOffset, mode, headFollowsPitch);
	}

	private static void store(net.minecraft.nbt.CompoundTag data, int action, double a, double b, double c, int ticks) {
		if (data == null)
			return;
		if (action == ACTION_CLEAR) {
			for (String key : java.util.List.of("CNECameraShakeStrength", "CNECameraShakeTicks", "CNECameraLastShakeTick", "CNECameraOffsetX", "CNECameraOffsetY", "CNECameraOffsetZ", "CNECameraAbsoluteActive", "CNECameraAbsoluteX",
					"CNECameraAbsoluteY", "CNECameraAbsoluteZ", "CNECameraFollowUUID", "CNECameraFollowOffsetX", "CNECameraFollowOffsetY", "CNECameraFollowOffsetZ", "CNECameraFollowMode", "CNECameraYawOffset", "CNECameraPitchOffset",
					"CNECameraRollOffset", "CNECameraPlayerOrbitActive", "CNECameraPlayerOrbitHeadPitch", "CNECameraPlayerOrbitYaw", "CNECameraPlayerOrbitPitch", "CNECameraPlayerOrbitLastYaw", "CNECameraPlayerOrbitLastPitch",
					"CNECameraPlayerOrbitLastRawYaw", "CNECameraPlayerOrbitLastRawPitch"))
				data.remove(key);
		} else if (action == ACTION_SHAKE) {
			data.putDouble("CNECameraShakeStrength", clamp(a, 0.0D, MAX_SHAKE_STRENGTH));
			data.putInt("CNECameraShakeTicks", Math.max(0, ticks));
		} else if (action == ACTION_POSITION) {
			clearAbsoluteAndFollow(data);
			data.putDouble("CNECameraOffsetX", clamp(a, -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET));
			data.putDouble("CNECameraOffsetY", clamp(b, -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET));
			data.putDouble("CNECameraOffsetZ", clamp(c, -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET));
		} else if (action == ACTION_ANGLE) {
			data.putDouble("CNECameraYawOffset", clamp(a, -MAX_ANGLE_OFFSET, MAX_ANGLE_OFFSET));
			data.putDouble("CNECameraPitchOffset", clamp(b, -MAX_ANGLE_OFFSET, MAX_ANGLE_OFFSET));
			data.putDouble("CNECameraRollOffset", clamp(c, -MAX_ANGLE_OFFSET, MAX_ANGLE_OFFSET));
		} else if (action == ACTION_POSITION_ABSOLUTE) {
			clearOffsetAndFollow(data);
			data.putBoolean("CNECameraAbsoluteActive", true);
			data.putDouble("CNECameraAbsoluteX", finiteOrZero(a));
			data.putDouble("CNECameraAbsoluteY", finiteOrZero(b));
			data.putDouble("CNECameraAbsoluteZ", finiteOrZero(c));
		}
	}

	private static void storeFollow(net.minecraft.nbt.CompoundTag data, java.util.UUID targetUuid, double xOffset, double yOffset, double zOffset, String mode) {
		storeFollow(data, targetUuid, xOffset, yOffset, zOffset, mode, false);
	}

	private static void storeFollow(net.minecraft.nbt.CompoundTag data, java.util.UUID targetUuid, double xOffset, double yOffset, double zOffset, String mode, boolean headFollowsPitch) {
		if (data == null || targetUuid == null)
			return;
		String followMode = normalizeFollowMode(mode);
		boolean continuingPlayerOrbit = FOLLOW_MODE_PLAYER_ORBIT.equals(followMode) && FOLLOW_MODE_PLAYER_ORBIT.equals(data.getString("CNECameraFollowMode")) && targetUuid.toString().equals(data.getString("CNECameraFollowUUID"))
				&& data.getBoolean("CNECameraPlayerOrbitActive");
		clearOffsetAndAbsolute(data);
		data.putString("CNECameraFollowUUID", targetUuid.toString());
		data.putDouble("CNECameraFollowOffsetX", clamp(xOffset, -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET));
		data.putDouble("CNECameraFollowOffsetY", clamp(yOffset, -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET));
		data.putDouble("CNECameraFollowOffsetZ", clamp(zOffset, -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET));
		data.putString("CNECameraFollowMode", followMode);
		if (FOLLOW_MODE_PLAYER_ORBIT.equals(followMode)) {
			data.putBoolean("CNECameraPlayerOrbitActive", true);
			data.putBoolean("CNECameraPlayerOrbitHeadPitch", headFollowsPitch);
			net.minecraft.world.entity.Entity local = localPlayer();
			if (!continuingPlayerOrbit && local != null && local.getUUID().equals(targetUuid)) {
				data.putDouble("CNECameraPlayerOrbitYaw", local.getYRot());
				data.putDouble("CNECameraPlayerOrbitPitch", local.getXRot());
				data.putDouble("CNECameraPlayerOrbitLastYaw", local.getYRot());
				data.putDouble("CNECameraPlayerOrbitLastPitch", local.getXRot());
				data.putDouble("CNECameraPlayerOrbitLastRawYaw", local.getYRot());
				data.putDouble("CNECameraPlayerOrbitLastRawPitch", local.getXRot());
			}
		} else {
			data.remove("CNECameraPlayerOrbitActive");
			data.remove("CNECameraPlayerOrbitHeadPitch");
			data.remove("CNECameraPlayerOrbitYaw");
			data.remove("CNECameraPlayerOrbitPitch");
			data.remove("CNECameraPlayerOrbitLastYaw");
			data.remove("CNECameraPlayerOrbitLastPitch");
			data.remove("CNECameraPlayerOrbitLastRawYaw");
			data.remove("CNECameraPlayerOrbitLastRawPitch");
		}
	}

	private static void clearOffsetAndAbsolute(net.minecraft.nbt.CompoundTag data) {
		data.remove("CNECameraOffsetX");
		data.remove("CNECameraOffsetY");
		data.remove("CNECameraOffsetZ");
		data.remove("CNECameraAbsoluteActive");
		data.remove("CNECameraAbsoluteX");
		data.remove("CNECameraAbsoluteY");
		data.remove("CNECameraAbsoluteZ");
	}

	private static void clearOffsetAndFollow(net.minecraft.nbt.CompoundTag data) {
		data.remove("CNECameraOffsetX");
		data.remove("CNECameraOffsetY");
		data.remove("CNECameraOffsetZ");
		clearFollow(data);
	}

	private static void clearAbsoluteAndFollow(net.minecraft.nbt.CompoundTag data) {
		data.remove("CNECameraAbsoluteActive");
		data.remove("CNECameraAbsoluteX");
		data.remove("CNECameraAbsoluteY");
		data.remove("CNECameraAbsoluteZ");
		clearFollow(data);
	}

	private static void clearFollow(net.minecraft.nbt.CompoundTag data) {
		data.remove("CNECameraFollowUUID");
		data.remove("CNECameraFollowOffsetX");
		data.remove("CNECameraFollowOffsetY");
		data.remove("CNECameraFollowOffsetZ");
		data.remove("CNECameraFollowMode");
		data.remove("CNECameraPlayerOrbitActive");
		data.remove("CNECameraPlayerOrbitHeadPitch");
		data.remove("CNECameraPlayerOrbitYaw");
		data.remove("CNECameraPlayerOrbitPitch");
		data.remove("CNECameraPlayerOrbitLastYaw");
		data.remove("CNECameraPlayerOrbitLastPitch");
		data.remove("CNECameraPlayerOrbitLastRawYaw");
		data.remove("CNECameraPlayerOrbitLastRawPitch");
	}

	private static String normalizeFollowMode(String mode) {
		String key = String.valueOf(mode == null ? "" : mode);
		if (FOLLOW_MODE_TARGET_BODY.equals(key) || FOLLOW_MODE_TARGET_HEAD.equals(key) || FOLLOW_MODE_VIEWER_CAMERA.equals(key) || FOLLOW_MODE_PLAYER_ORBIT.equals(key) || FOLLOW_MODE_PLAYER_HEAD_VIEW.equals(key))
			return key;
		return FOLLOW_MODE_WORLD;
	}

	private static double finiteOrZero(double value) {
		return Double.isFinite(value) ? value : 0.0D;
	}

	private static double clamp(double value, double min, double max) {
		if (!Double.isFinite(value))
			return 0.0D;
		return Math.max(min, Math.min(max, value));
	}

	private static net.minecraft.nbt.CompoundTag dataFor(net.minecraft.world.entity.Entity target) {
		net.minecraft.world.entity.Entity resolved = target != null ? target : localPlayer();
		return resolved == null ? null : resolved.getPersistentData();
	}

	private static net.minecraft.nbt.CompoundTag data() {
		net.minecraft.world.entity.Entity player = localPlayer();
		return player == null ? null : player.getPersistentData();
	}

	private static net.minecraft.world.entity.Entity localPlayer() {
		Object player = CameraAccess.localPlayer();
		return player instanceof net.minecraft.world.entity.Entity entity ? entity : null;
	}

	private static boolean isLocalPlayer(net.minecraft.world.entity.Entity entity) {
		net.minecraft.world.entity.Entity local = localPlayer();
		return entity != null && local != null && entity.getUUID().equals(local.getUUID());
	}

	private static void ensureListener() {
		if (listenerRegistered)
			return;
		try {
			Class.forName("net.minecraft.client.Minecraft");
			listenerRegistered = true;
			net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new Object() {
				@net.neoforged.bus.api.SubscribeEvent
				public void onCamera(net.neoforged.neoforge.client.event.ViewportEvent.ComputeCameraAngles event) {
					CameraAccess.apply(event);
				}

				@net.neoforged.bus.api.SubscribeEvent
				public void onMovementInput(net.neoforged.neoforge.client.event.MovementInputUpdateEvent event) {
					CameraAccess.onMovementInput(event);
				}

				@net.neoforged.bus.api.SubscribeEvent
				public void onClientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
					CameraAccess.syncKeyStates();
				}
			});
		} catch (Throwable ignored) {
		}
	}

	// Called by the client-only CneCameraMixin (a mixin into net.minecraft.client.Camera). The parameter is
	// Object, NOT the Camera type: this top-level class is @EventBusSubscriber, so during mod construction the
	// event bus calls Class.getDeclaredMethods() on it on BOTH dists, which resolves every declared method
	// signature - a client-only type in any signature makes the dedicated server load net.minecraft.client.Camera
	// and die (RuntimeDistCleaner: "invalid dist DEDICATED_SERVER"). The cast to Camera happens inside
	// CameraAccess, which only ever loads on the client.
	public static void applyCameraPosition(Object camera) {
		applyCameraPosition(camera, 1.0F);
	}

	public static void applyCameraPosition(Object camera, float partialTick) {
		CameraAccess.applyPosition(camera, partialTick);
	}

	public record CameraMessage(int action, double a, double b, double c, int ticks) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

		public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<CameraMessage> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(
				net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("euru", "cne_camera"));
		public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, CameraMessage> STREAM_CODEC = net.minecraft.network.codec.StreamCodec
				.of((net.minecraft.network.RegistryFriendlyByteBuf buffer, CameraMessage message) -> {
					buffer.writeInt(message.action);
					buffer.writeDouble(message.a);
					buffer.writeDouble(message.b);
					buffer.writeDouble(message.c);
					buffer.writeInt(message.ticks);
				}, (net.minecraft.network.RegistryFriendlyByteBuf buffer) -> new CameraMessage(buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt()));
		@Override
		public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<CameraMessage> type() {
			return TYPE;
		}

		public static void handleData(final CameraMessage message, final net.neoforged.neoforge.network.handling.IPayloadContext context) {
			if (context.flow() == net.minecraft.network.protocol.PacketFlow.CLIENTBOUND) {
				context.enqueueWork(() -> applyLocal(message.action, message.a, message.b, message.c, message.ticks)).exceptionally(e -> {
					System.err.println("[ChickenNugget Extras] Camera payload failed: " + e);
					return null;
				});
			}
		}
	}

	public record CameraFollowMessage(java.util.UUID targetUuid, double xOffset, double yOffset, double zOffset, String mode, boolean headFollowsPitch) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

		public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<CameraFollowMessage> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(
				net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("euru", "cne_camera_follow"));
		public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, CameraFollowMessage> STREAM_CODEC = net.minecraft.network.codec.StreamCodec
				.of((net.minecraft.network.RegistryFriendlyByteBuf buffer, CameraFollowMessage message) -> {
					buffer.writeUUID(message.targetUuid);
					buffer.writeDouble(message.xOffset);
					buffer.writeDouble(message.yOffset);
					buffer.writeDouble(message.zOffset);
					buffer.writeUtf(normalizeFollowMode(message.mode));
					buffer.writeBoolean(message.headFollowsPitch);
				}, (net.minecraft.network.RegistryFriendlyByteBuf buffer) -> new CameraFollowMessage(buffer.readUUID(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readUtf(), buffer.readBoolean()));

		@Override
		public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<CameraFollowMessage> type() {
			return TYPE;
		}

		public static void handleData(final CameraFollowMessage message, final net.neoforged.neoforge.network.handling.IPayloadContext context) {
			if (context.flow() == net.minecraft.network.protocol.PacketFlow.CLIENTBOUND) {
				context.enqueueWork(() -> applyFollowLocal(message.targetUuid, message.xOffset, message.yOffset, message.zOffset, message.mode, message.headFollowsPitch)).exceptionally(e -> {
					System.err.println("[ChickenNugget Extras] Camera follow payload failed: " + e);
					return null;
				});
			}
		}
	}

	public record KeySyncMessage(java.util.List<KeyState> entries) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
		public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<KeySyncMessage> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(
				net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("euru", "cne_key_state_sync"));
		public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, KeySyncMessage> STREAM_CODEC = net.minecraft.network.codec.StreamCodec
				.of((net.minecraft.network.RegistryFriendlyByteBuf buffer, KeySyncMessage message) -> {
					java.util.List<KeyState> safeEntries = message.entries == null ? java.util.List.of() : message.entries;
					java.util.ArrayList<KeyState> filtered = new java.util.ArrayList<>();
					for (KeyState entry : safeEntries) {
						if (entry == null)
							continue;
						if (filtered.size() >= KEY_SYNC_MAX_ENTRIES)
							break;
						filtered.add(entry);
					}
					buffer.writeVarInt(filtered.size());
					for (KeyState entry : filtered) {
						buffer.writeUtf(cleanNetworkString(entry.keyId), 160);
						buffer.writeUtf(cleanNetworkString(entry.boundKey), 160);
						buffer.writeBoolean(entry.down);
						buffer.writeBoolean(entry.pressed);
						buffer.writeVarInt(Math.max(0, Math.min(72000, entry.holdTicks)));
						buffer.writeBoolean(entry.released);
					}
				}, (net.minecraft.network.RegistryFriendlyByteBuf buffer) -> {
					int count = Math.max(0, Math.min(KEY_SYNC_MAX_ENTRIES, buffer.readVarInt()));
					java.util.ArrayList<KeyState> entries = new java.util.ArrayList<>();
					for (int i = 0; i < count; i++) {
						entries.add(new KeyState(buffer.readUtf(160), buffer.readUtf(160), buffer.readBoolean(), buffer.readBoolean(), buffer.readVarInt(), buffer.readBoolean()));
					}
					return new KeySyncMessage(entries);
				});

		@Override
		public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<KeySyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final KeySyncMessage message, final net.neoforged.neoforge.network.handling.IPayloadContext context) {
			if (context.flow() == net.minecraft.network.protocol.PacketFlow.SERVERBOUND) {
				context.enqueueWork(() -> {
					if (context.player() instanceof net.minecraft.server.level.ServerPlayer player)
						storeKeyStates(player, message.entries);
				}).exceptionally(e -> {
					System.err.println("[ChickenNugget Extras] Key state sync failed: " + e);
					return null;
				});
			}
		}

		public record KeyState(String keyId, String boundKey, boolean down, boolean pressed, int holdTicks, boolean released) {
		}
	}

	private static void storeKeyStates(net.minecraft.server.level.ServerPlayer player, java.util.List<KeySyncMessage.KeyState> entries) {
		if (player == null || entries == null || entries.isEmpty())
			return;
		net.minecraft.nbt.CompoundTag root = player.getPersistentData();
		net.minecraft.nbt.CompoundTag states = root.getCompound(KEY_STATES_TAG);
		long updatedAt = player.level() == null ? 0L : player.level().getGameTime();
		int stored = 0;
		for (KeySyncMessage.KeyState entry : entries) {
			if (entry == null || stored++ >= KEY_SYNC_MAX_ENTRIES)
				break;
			java.util.LinkedHashSet<String> aliases = keyAliases(entry.keyId(), entry.boundKey());
			for (String alias : aliases) {
				if (alias.isBlank())
					continue;
				net.minecraft.nbt.CompoundTag state = states.getCompound(alias);
				state.putBoolean("down", entry.down());
				state.putInt("holdTicks", Math.max(0, Math.min(72000, entry.holdTicks())));
				state.putLong("updatedAt", updatedAt);
				if (entry.pressed())
					state.putInt("pressed", Math.min(128, state.getInt("pressed") + 1));
				else if (!entry.down())
					state.putInt("pressed", 0);
				if (entry.released())
					state.putInt("released", Math.min(128, state.getInt("released") + 1));
				else if (entry.down())
					state.putInt("released", 0);
				states.put(alias, state);
			}
		}
		root.put(KEY_STATES_TAG, states);
	}

	private static net.minecraft.nbt.CompoundTag keyState(net.minecraft.world.entity.Entity player, String keyId, boolean create) {
		if (player == null)
			return null;
		net.minecraft.nbt.CompoundTag root = player.getPersistentData();
		net.minecraft.nbt.CompoundTag states = root.getCompound(KEY_STATES_TAG);
		java.util.LinkedHashSet<String> aliases = keyAliases(keyId, "");
		for (String alias : aliases) {
			if (states.contains(alias, net.minecraft.nbt.Tag.TAG_COMPOUND))
				return states.getCompound(alias);
		}
		if (!create)
			return null;
		String alias = aliases.isEmpty() ? "" : aliases.iterator().next();
		if (alias.isBlank())
			return null;
		net.minecraft.nbt.CompoundTag state = new net.minecraft.nbt.CompoundTag();
		states.put(alias, state);
		root.put(KEY_STATES_TAG, states);
		return state;
	}

	private static java.util.LinkedHashSet<String> keyAliases(String keyId, String boundKey) {
		java.util.LinkedHashSet<String> aliases = new java.util.LinkedHashSet<>();
		addKeyAlias(aliases, keyId);
		addKeyAlias(aliases, boundKey);
		return aliases;
	}

	private static void addKeyAlias(java.util.LinkedHashSet<String> aliases, String raw) {
		String key = normalizeKeyId(raw);
		if (key.isBlank())
			return;
		aliases.add(key);
		if (key.startsWith("key."))
			aliases.add(key.substring("key.".length()));
		String modPrefix = "euru.";
		if (key.startsWith(modPrefix))
			aliases.add(key.substring(modPrefix.length()));
		String keyModPrefix = "key.euru.";
		if (key.startsWith(keyModPrefix))
			aliases.add(key.substring(keyModPrefix.length()));
		if (key.startsWith("raw."))
			aliases.add(key.substring("raw.".length()));
		int dot = key.lastIndexOf('.');
		if (dot >= 0 && dot + 1 < key.length())
			aliases.add(key.substring(dot + 1));
		if (key.startsWith("key.keyboard."))
			aliases.add(key.substring("key.keyboard.".length()));
		if (key.startsWith("keyboard."))
			aliases.add(key.substring("keyboard.".length()));
	}

	private static String normalizeKeyId(String raw) {
		String key = cleanNetworkString(raw).trim().toLowerCase(java.util.Locale.ROOT);
		key = key.replace('\\', '/').replace(' ', '_');
		key = key.replaceAll("[^a-z0-9_./:-]", "_");
		key = key.replaceAll("_+", "_");
		while (key.startsWith("_"))
			key = key.substring(1);
		while (key.endsWith("_"))
			key = key.substring(0, key.length() - 1);
		return key;
	}

	private static String cleanNetworkString(String raw) {
		String value = String.valueOf(raw == null ? "" : raw);
		return value.length() > 160 ? value.substring(0, 160) : value;
	}

	private static final class CameraAccess {
		private static boolean warnedPositionAccess;
		private static boolean warnedListener;
		private static boolean warnedCameraMode;
		private static boolean forcedThirdPersonBack;
		private static String previousCameraTypeName;
		private static final java.util.HashMap<String, Boolean> keySyncLastDown = new java.util.HashMap<>();
		private static final java.util.HashMap<String, Integer> keySyncHoldTicks = new java.util.HashMap<>();
		private static final java.util.LinkedHashMap<String, Integer> RAW_KEYBOARD_KEYS = rawKeyboardKeys();
		private static final java.util.LinkedHashMap<String, Integer> RAW_MOUSE_BUTTONS = rawMouseButtons();
		private static int keySyncTicks;

		static Object localPlayer() {
			try {
				Object mc = Class.forName("net.minecraft.client.Minecraft").getMethod("getInstance").invoke(null);
				return mc.getClass().getField("player").get(mc);
			} catch (Throwable ignored) {
				return null;
			}
		}

		static Object camera() {
			try {
				Object mc = Class.forName("net.minecraft.client.Minecraft").getMethod("getInstance").invoke(null);
				Object gameRenderer = mc.getClass().getField("gameRenderer").get(mc);
				return gameRenderer.getClass().getMethod("getMainCamera").invoke(gameRenderer);
			} catch (Throwable ignored) {
				return null;
			}
		}

		static void apply(net.neoforged.neoforge.client.event.ViewportEvent.ComputeCameraAngles event) {
			try {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc == null || mc.player == null || event == null || event.getCamera() == null)
					return;
				net.minecraft.nbt.CompoundTag data = mc.player.getPersistentData();
				enforceOffsetCameraType(data);
				if (isPlayerOrbitActive(mc, data)) {
					updatePlayerOrbitCameraFromRawAngles(mc, data, event.getYaw(), event.getPitch());
					event.setYaw(orbitYaw(mc, data));
					event.setPitch(orbitPitch(mc, data));
				}
				double shake = clamp(data.getDouble("CNECameraShakeStrength"), 0.0D, MAX_SHAKE_STRENGTH);
				int ticks = data.getInt("CNECameraShakeTicks");
				float syaw = 0.0F;
				float spitch = 0.0F;
				float sroll = 0.0F;
				if (shake > 0.0D && ticks > 0) {
					net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create(System.nanoTime());
					syaw = (float) ((random.nextDouble() - 0.5D) * shake * 12.0D);
					spitch = (float) ((random.nextDouble() - 0.5D) * shake * 8.0D);
					sroll = (float) ((random.nextDouble() - 0.5D) * shake * 10.0D);
					long now = mc.level == null ? System.currentTimeMillis() : mc.level.getGameTime();
					if (data.getLong("CNECameraLastShakeTick") != now) {
						data.putLong("CNECameraLastShakeTick", now);
						data.putInt("CNECameraShakeTicks", Math.max(0, ticks - 1));
					}
				}
				event.setYaw(event.getYaw() + (float) clamp(data.getDouble("CNECameraYawOffset"), -MAX_ANGLE_OFFSET, MAX_ANGLE_OFFSET) + syaw);
				event.setPitch(event.getPitch() + (float) clamp(data.getDouble("CNECameraPitchOffset"), -MAX_ANGLE_OFFSET, MAX_ANGLE_OFFSET) + spitch);
				event.setRoll(event.getRoll() + (float) clamp(data.getDouble("CNECameraRollOffset"), -MAX_ANGLE_OFFSET, MAX_ANGLE_OFFSET) + sroll);
			} catch (Throwable error) {
				if (!warnedListener) {
					warnedListener = true;
					System.err.println("[ChickenNugget Extras] Camera listener disabled after an error: " + error);
				}
			}
		}

		static void onMovementInput(net.neoforged.neoforge.client.event.MovementInputUpdateEvent event) {
			try {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc == null || mc.player == null || event == null || event.getInput() == null || event.getEntity() != mc.player)
					return;
				updatePlayerOrbitCameraAndFacing(mc, mc.player.getPersistentData(), event.getInput());
			} catch (Throwable error) {
				if (!warnedListener) {
					warnedListener = true;
					System.err.println("[ChickenNugget Extras] Camera orbit movement handler disabled after an error: " + error);
				}
			}
		}

		static void syncKeyStates() {
			try {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc == null || mc.player == null || mc.options == null || mc.level == null)
					return;
				keySyncTicks++;
				java.util.ArrayList<KeySyncMessage.KeyState> entries = new java.util.ArrayList<>();
				net.minecraft.client.KeyMapping[] mappings = mc.options.keyMappings;
				if (mappings == null)
					return;
				for (net.minecraft.client.KeyMapping mapping : mappings) {
					if (mapping == null)
						continue;
					String keyId = mapping.getName();
					boolean down = mapping.isDown();
					boolean wasDown = Boolean.TRUE.equals(keySyncLastDown.get(keyId));
					int holdTicks = down ? Math.min(72000, keySyncHoldTicks.getOrDefault(keyId, 0) + 1) : 0;
					keySyncLastDown.put(keyId, down);
					keySyncHoldTicks.put(keyId, holdTicks);
					boolean pressed = down && !wasDown;
					boolean released = !down && wasDown;
					boolean periodic = down && keySyncTicks % KEY_SYNC_INTERVAL_TICKS == 0;
					if (pressed || released || periodic)
						entries.add(new KeySyncMessage.KeyState(keyId, boundKeyName(mapping), down, pressed, holdTicks, released));
					if (entries.size() >= KEY_SYNC_MAX_ENTRIES)
						break;
				}
				syncRawInputStates(mc, entries);
				if (!entries.isEmpty())
					net.neoforged.neoforge.network.PacketDistributor.sendToServer(new KeySyncMessage(entries));
			} catch (Throwable error) {
				if (!warnedListener) {
					warnedListener = true;
					System.err.println("[ChickenNugget Extras] Key state sync disabled after an error: " + error);
				}
			}
		}

		private static void syncRawInputStates(net.minecraft.client.Minecraft mc, java.util.ArrayList<KeySyncMessage.KeyState> entries) {
			long window = mc.getWindow() == null ? 0L : mc.getWindow().getWindow();
			if (window == 0L)
				return;
			for (java.util.Map.Entry<String, Integer> entry : RAW_KEYBOARD_KEYS.entrySet()) {
				if (entries.size() >= KEY_SYNC_MAX_ENTRIES)
					return;
				boolean down = org.lwjgl.glfw.GLFW.glfwGetKey(window, entry.getValue()) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
				addRawKeyState(entries, entry.getKey(), "key.keyboard." + entry.getKey(), down);
			}
			for (java.util.Map.Entry<String, Integer> entry : RAW_MOUSE_BUTTONS.entrySet()) {
				if (entries.size() >= KEY_SYNC_MAX_ENTRIES)
					return;
				boolean down = org.lwjgl.glfw.GLFW.glfwGetMouseButton(window, entry.getValue()) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
				addRawKeyState(entries, "mouse." + entry.getKey(), "key.mouse." + entry.getKey(), down);
			}
		}

		private static void addRawKeyState(java.util.ArrayList<KeySyncMessage.KeyState> entries, String rawName, String boundName, boolean down) {
			String keyId = "raw." + rawName;
			boolean wasDown = Boolean.TRUE.equals(keySyncLastDown.get(keyId));
			int holdTicks = down ? Math.min(72000, keySyncHoldTicks.getOrDefault(keyId, 0) + 1) : 0;
			keySyncLastDown.put(keyId, down);
			keySyncHoldTicks.put(keyId, holdTicks);
			boolean pressed = down && !wasDown;
			boolean released = !down && wasDown;
			boolean periodic = down && keySyncTicks % KEY_SYNC_INTERVAL_TICKS == 0;
			if (pressed || released || periodic)
				entries.add(new KeySyncMessage.KeyState(keyId, boundName, down, pressed, holdTicks, released));
		}

		private static java.util.LinkedHashMap<String, Integer> rawKeyboardKeys() {
			java.util.LinkedHashMap<String, Integer> keys = new java.util.LinkedHashMap<>();
			keys.put("space", org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE);
			keys.put("apostrophe", org.lwjgl.glfw.GLFW.GLFW_KEY_APOSTROPHE);
			keys.put("comma", org.lwjgl.glfw.GLFW.GLFW_KEY_COMMA);
			keys.put("minus", org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS);
			keys.put("period", org.lwjgl.glfw.GLFW.GLFW_KEY_PERIOD);
			keys.put("slash", org.lwjgl.glfw.GLFW.GLFW_KEY_SLASH);
			for (int i = 0; i <= 9; i++)
				keys.put(String.valueOf(i), org.lwjgl.glfw.GLFW.GLFW_KEY_0 + i);
			keys.put("semicolon", org.lwjgl.glfw.GLFW.GLFW_KEY_SEMICOLON);
			keys.put("equal", org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL);
			for (int i = 0; i < 26; i++)
				keys.put(String.valueOf((char) ('a' + i)), org.lwjgl.glfw.GLFW.GLFW_KEY_A + i);
			keys.put("left_bracket", org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_BRACKET);
			keys.put("backslash", org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSLASH);
			keys.put("right_bracket", org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_BRACKET);
			keys.put("grave_accent", org.lwjgl.glfw.GLFW.GLFW_KEY_GRAVE_ACCENT);
			keys.put("escape", org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE);
			keys.put("enter", org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER);
			keys.put("tab", org.lwjgl.glfw.GLFW.GLFW_KEY_TAB);
			keys.put("backspace", org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE);
			keys.put("insert", org.lwjgl.glfw.GLFW.GLFW_KEY_INSERT);
			keys.put("delete", org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE);
			keys.put("right", org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT);
			keys.put("left", org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT);
			keys.put("down", org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN);
			keys.put("up", org.lwjgl.glfw.GLFW.GLFW_KEY_UP);
			keys.put("page_up", org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP);
			keys.put("page_down", org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN);
			keys.put("home", org.lwjgl.glfw.GLFW.GLFW_KEY_HOME);
			keys.put("end", org.lwjgl.glfw.GLFW.GLFW_KEY_END);
			keys.put("caps_lock", org.lwjgl.glfw.GLFW.GLFW_KEY_CAPS_LOCK);
			keys.put("scroll_lock", org.lwjgl.glfw.GLFW.GLFW_KEY_SCROLL_LOCK);
			keys.put("num_lock", org.lwjgl.glfw.GLFW.GLFW_KEY_NUM_LOCK);
			keys.put("print_screen", org.lwjgl.glfw.GLFW.GLFW_KEY_PRINT_SCREEN);
			keys.put("pause", org.lwjgl.glfw.GLFW.GLFW_KEY_PAUSE);
			for (int i = 1; i <= 12; i++)
				keys.put("f" + i, org.lwjgl.glfw.GLFW.GLFW_KEY_F1 + i - 1);
			keys.put("left_shift", org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT);
			keys.put("left_control", org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL);
			keys.put("left_alt", org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT);
			keys.put("right_shift", org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT);
			keys.put("right_control", org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL);
			keys.put("right_alt", org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_ALT);
			return keys;
		}

		private static java.util.LinkedHashMap<String, Integer> rawMouseButtons() {
			java.util.LinkedHashMap<String, Integer> buttons = new java.util.LinkedHashMap<>();
			buttons.put("left", org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT);
			buttons.put("right", org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT);
			buttons.put("middle", org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
			return buttons;
		}

		private static String boundKeyName(net.minecraft.client.KeyMapping mapping) {
			try {
				return String.valueOf(mapping.getClass().getMethod("saveString").invoke(mapping));
			} catch (Throwable ignored) {
				return "";
			}
		}

		// Takes Object so the dist-safe top-level applyCameraPosition never mentions the Camera type; the cast
		// lives here, and CameraAccess is only ever loaded on the client.
		static void applyPosition(Object cameraObject, float partialTick) {
			try {
				net.minecraft.client.Camera camera = (net.minecraft.client.Camera) cameraObject;
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc == null || mc.player == null || camera == null)
					return;
				if (!positionMixinSeen) {
					positionMixinSeen = true;
					System.out.println("[ChickenNugget Extras] Camera position mixin is active");
				}
				net.minecraft.nbt.CompoundTag data = mc.player.getPersistentData();
				enforceOffsetCameraType(data);
				net.minecraft.world.phys.Vec3 desired = desiredCameraPosition(mc, data, partialTick);
				if (desired != null) {
					setPosition(camera, desired.x, desired.y, desired.z);
					return;
				}
				double ox = clamp(data.getDouble("CNECameraOffsetX"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET);
				double oy = clamp(data.getDouble("CNECameraOffsetY"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET);
				double oz = clamp(data.getDouble("CNECameraOffsetZ"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET);
				if (ox == 0.0D && oy == 0.0D && oz == 0.0D)
					return;
				net.minecraft.world.phys.Vec3 pos = camera.getPosition();
				net.minecraft.world.phys.Vec3 desiredOffset = avoidCameraObstructions(mc, pos, pos.add(ox, oy, oz));
				setPosition(camera, desiredOffset.x, desiredOffset.y, desiredOffset.z);
			} catch (Throwable error) {
				if (!warnedListener) {
					warnedListener = true;
					System.err.println("[ChickenNugget Extras] Camera position listener disabled after an error: " + error);
				}
			}
		}

		private static net.minecraft.world.phys.Vec3 desiredCameraPosition(net.minecraft.client.Minecraft mc, net.minecraft.nbt.CompoundTag data, float partialTick) {
			if (data == null)
				return null;
			String targetId = data.getString("CNECameraFollowUUID");
			if (targetId != null && !targetId.isBlank() && mc.level != null) {
				try {
					java.util.UUID targetUuid = java.util.UUID.fromString(targetId);
					for (net.minecraft.world.entity.Entity target : mc.level.entitiesForRendering()) {
						if (target != null && target.getUUID().equals(targetUuid)) {
							net.minecraft.world.phys.Vec3 base = interpolatedPosition(target, partialTick);
							net.minecraft.world.phys.Vec3 offset = new net.minecraft.world.phys.Vec3(clamp(data.getDouble("CNECameraFollowOffsetX"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET),
									clamp(data.getDouble("CNECameraFollowOffsetY"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET), clamp(data.getDouble("CNECameraFollowOffsetZ"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET));
							String followMode = normalizeFollowMode(data.getString("CNECameraFollowMode"));
							boolean playerHeadView = FOLLOW_MODE_PLAYER_HEAD_VIEW.equals(followMode);
							net.minecraft.world.phys.Vec3 focus = playerHeadView ? playerBodyViewFocus(base, target) : cameraFocusPosition(base, target);
							net.minecraft.world.phys.Vec3 anchor = playerHeadView ? playerBodyViewAnchor(base, target) : base;
							net.minecraft.world.phys.Vec3 desired = anchor.add(orientedOffset(mc, target, playerHeadViewOffset(offset), followMode, partialTick));
							if (!playerHeadView)
								desired = clampToEntityFloor(focus, desired, base.y + CAMERA_FLOOR_PADDING);
							return avoidCameraObstructions(mc, focus, desired);
						}
					}
				} catch (IllegalArgumentException ignored) {
					clearFollow(data);
				}
			}
			if (data.getBoolean("CNECameraAbsoluteActive")) {
				return new net.minecraft.world.phys.Vec3(data.getDouble("CNECameraAbsoluteX"), data.getDouble("CNECameraAbsoluteY"), data.getDouble("CNECameraAbsoluteZ"));
			}
			return null;
		}

		private static net.minecraft.world.phys.Vec3 interpolatedPosition(net.minecraft.world.entity.Entity target, float partialTick) {
			float frame = safePartialTick(partialTick);
			return new net.minecraft.world.phys.Vec3(net.minecraft.util.Mth.lerp(frame, target.xo, target.getX()), net.minecraft.util.Mth.lerp(frame, target.yo, target.getY()), net.minecraft.util.Mth.lerp(frame, target.zo, target.getZ()));
		}

		private static net.minecraft.world.phys.Vec3 cameraFocusPosition(net.minecraft.world.phys.Vec3 base, net.minecraft.world.entity.Entity target) {
			double height = target == null ? 0.0D : Math.max(0.25D, target.getBbHeight() * 0.75D);
			return base.add(0.0D, height, 0.0D);
		}

		private static net.minecraft.world.phys.Vec3 cameraEyePosition(net.minecraft.world.phys.Vec3 base, net.minecraft.world.entity.Entity target) {
			double eyeHeight = target instanceof net.minecraft.world.entity.LivingEntity living ? living.getEyeHeight() : target == null ? 0.0D : Math.max(0.25D, target.getBbHeight() * 0.85D);
			return base.add(0.0D, eyeHeight, 0.0D);
		}

		private static net.minecraft.world.phys.Vec3 playerBodyViewFocus(net.minecraft.world.phys.Vec3 base, net.minecraft.world.entity.Entity target) {
			double height = target == null ? 1.0D : Math.max(0.5D, target.getBbHeight() * 0.9D);
			return base.add(0.0D, height, 0.0D);
		}

		private static net.minecraft.world.phys.Vec3 playerBodyViewAnchor(net.minecraft.world.phys.Vec3 base, net.minecraft.world.entity.Entity target) {
			double height = target == null ? 1.0D : Math.max(0.45D, target.getBbHeight() * PLAYER_HEAD_VIEW_BODY_ANCHOR_HEIGHT);
			return base.add(0.0D, height, 0.0D);
		}

		private static net.minecraft.world.phys.Vec3 playerHeadViewOffset(net.minecraft.world.phys.Vec3 offset) {
			if (offset == null)
				return new net.minecraft.world.phys.Vec3(0.0D, 0.0D, PLAYER_HEAD_VIEW_MIN_FORWARD_OFFSET);
			double forward = Math.abs(offset.z) < PLAYER_HEAD_VIEW_MIN_FORWARD_OFFSET ? Math.copySign(PLAYER_HEAD_VIEW_MIN_FORWARD_OFFSET, offset.z == 0.0D ? 1.0D : offset.z) : offset.z;
			return new net.minecraft.world.phys.Vec3(offset.x, offset.y, forward);
		}

		private static net.minecraft.world.phys.Vec3 clampToEntityFloor(net.minecraft.world.phys.Vec3 focus, net.minecraft.world.phys.Vec3 desired, double floorY) {
			if (focus == null || desired == null || desired.y >= floorY)
				return desired;
			net.minecraft.world.phys.Vec3 delta = desired.subtract(focus);
			if (delta.y >= -1.0E-6D)
				return new net.minecraft.world.phys.Vec3(desired.x, floorY, desired.z);
			double scale = (floorY - focus.y) / delta.y;
			if (!Double.isFinite(scale))
				return desired;
			return focus.add(delta.scale(net.minecraft.util.Mth.clamp(scale, 0.0D, 1.0D)));
		}

		private static net.minecraft.world.phys.Vec3 avoidCameraObstructions(net.minecraft.client.Minecraft mc, net.minecraft.world.phys.Vec3 focus, net.minecraft.world.phys.Vec3 desired) {
			if (mc == null || mc.level == null || mc.player == null || focus == null || desired == null)
				return desired;
			net.minecraft.world.phys.Vec3 delta = desired.subtract(focus);
			double distance = delta.length();
			if (!Double.isFinite(distance) || distance <= CAMERA_MIN_COLLISION_DISTANCE)
				return desired;
			net.minecraft.world.phys.BlockHitResult hit = mc.level.clip(new net.minecraft.world.level.ClipContext(focus, desired, net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, mc.player));
			if (hit == null || hit.getType() != net.minecraft.world.phys.HitResult.Type.BLOCK)
				return desired;
			double hitDistance = hit.getLocation().distanceTo(focus);
			if (!Double.isFinite(hitDistance) || hitDistance >= distance)
				return desired;
			double safeDistance = Math.max(CAMERA_MIN_COLLISION_DISTANCE, hitDistance - CAMERA_COLLISION_PADDING);
			return focus.add(delta.scale(Math.min(1.0D, safeDistance / distance)));
		}

		private static net.minecraft.world.phys.Vec3 orientedOffset(net.minecraft.client.Minecraft mc, net.minecraft.world.entity.Entity target, net.minecraft.world.phys.Vec3 offset, String mode, float partialTick) {
			if (FOLLOW_MODE_PLAYER_ORBIT.equals(mode))
				return rotateLocalOffset(offset, orbitYaw(mc, mc == null || mc.player == null ? null : mc.player.getPersistentData()), orbitPitch(mc, mc == null || mc.player == null ? null : mc.player.getPersistentData()));
			if (FOLLOW_MODE_PLAYER_HEAD_VIEW.equals(mode))
				return rotateLocalOffset(offset, targetYaw(target, partialTick, true), targetPitch(target, partialTick));
			if (FOLLOW_MODE_TARGET_HEAD.equals(mode))
				return rotateLocalOffset(offset, targetYaw(target, partialTick, true), targetPitch(target, partialTick));
			if (FOLLOW_MODE_TARGET_BODY.equals(mode))
				return rotateLocalOffset(offset, targetYaw(target, partialTick, false), 0.0F);
			if (FOLLOW_MODE_VIEWER_CAMERA.equals(mode)) {
				net.minecraft.client.Camera camera = mc.gameRenderer == null ? null : mc.gameRenderer.getMainCamera();
				if (camera != null)
					return rotateLocalOffset(offset, camera.getYRot(), camera.getXRot());
				if (mc.player != null)
					return rotateLocalOffset(offset, mc.player.getYRot(), mc.player.getXRot());
			}
			return offset;
		}

		private static float targetYaw(net.minecraft.world.entity.Entity target, float partialTick, boolean head) {
			float frame = safePartialTick(partialTick);
			if (head && target instanceof net.minecraft.world.entity.LivingEntity living)
				return net.minecraft.util.Mth.rotLerp(frame, living.yHeadRotO, living.yHeadRot);
			return net.minecraft.util.Mth.rotLerp(frame, target.yRotO, target.getYRot());
		}

		private static float targetPitch(net.minecraft.world.entity.Entity target, float partialTick) {
			return net.minecraft.util.Mth.lerp(safePartialTick(partialTick), target.xRotO, target.getXRot());
		}

		private static float safePartialTick(float partialTick) {
			return Float.isFinite(partialTick) ? net.minecraft.util.Mth.clamp(partialTick, 0.0F, 1.0F) : 1.0F;
		}

		private static net.minecraft.world.phys.Vec3 rotateLocalOffset(net.minecraft.world.phys.Vec3 offset, float yaw, float pitch) {
			double yawRadians = Math.toRadians(yaw);
			double pitchRadians = Math.toRadians(pitch);
			double sinYaw = Math.sin(yawRadians);
			double cosYaw = Math.cos(yawRadians);
			double sinPitch = Math.sin(pitchRadians);
			double cosPitch = Math.cos(pitchRadians);
			double rightX = cosYaw;
			double rightY = 0.0D;
			double rightZ = sinYaw;
			double upX = -sinPitch * sinYaw;
			double upY = cosPitch;
			double upZ = sinPitch * cosYaw;
			double forwardX = -sinYaw * cosPitch;
			double forwardY = -sinPitch;
			double forwardZ = cosYaw * cosPitch;
			return new net.minecraft.world.phys.Vec3(rightX * offset.x + upX * offset.y + forwardX * offset.z, rightY * offset.x + upY * offset.y + forwardY * offset.z, rightZ * offset.x + upZ * offset.y + forwardZ * offset.z);
		}

		private static boolean isPlayerOrbitActive(net.minecraft.client.Minecraft mc, net.minecraft.nbt.CompoundTag data) {
			if (mc == null || mc.player == null || data == null || !data.getBoolean("CNECameraPlayerOrbitActive") || !FOLLOW_MODE_PLAYER_ORBIT.equals(normalizeFollowMode(data.getString("CNECameraFollowMode"))))
				return false;
			try {
				return mc.player.getUUID().equals(java.util.UUID.fromString(data.getString("CNECameraFollowUUID")));
			} catch (IllegalArgumentException ignored) {
				return false;
			}
		}

		private static void updatePlayerOrbitCameraAndFacing(net.minecraft.client.Minecraft mc, net.minecraft.nbt.CompoundTag data, net.minecraft.client.player.Input input) {
			if (!isPlayerOrbitActive(mc, data) || input == null)
				return;
			net.minecraft.client.player.LocalPlayer player = mc.player;
			float previousFacingYaw = data.contains("CNECameraPlayerOrbitLastYaw") ? (float) data.getDouble("CNECameraPlayerOrbitLastYaw") : player.getYRot();
			double strength = movementStrength(input);
			float facingYaw = previousFacingYaw;
			if (strength > 1.0E-4D)
				facingYaw = smoothYaw(previousFacingYaw, movementFacingYaw(input, orbitYaw(mc, data), previousFacingYaw), 0.28F);
			float facingPitch = data.getBoolean("CNECameraPlayerOrbitHeadPitch") ? orbitPitch(mc, data) : 0.0F;
			steerPlayer(player, facingYaw, facingPitch);
			data.putDouble("CNECameraPlayerOrbitLastYaw", player.getYRot());
			data.putDouble("CNECameraPlayerOrbitLastPitch", player.getXRot());
			data.putDouble("CNECameraPlayerOrbitLastRawYaw", player.getYRot());
			data.putDouble("CNECameraPlayerOrbitLastRawPitch", player.getXRot());
		}

		private static void updatePlayerOrbitCameraFromRawAngles(net.minecraft.client.Minecraft mc, net.minecraft.nbt.CompoundTag data, float rawYaw, float rawPitch) {
			if (!isPlayerOrbitActive(mc, data))
				return;
			if (!data.contains("CNECameraPlayerOrbitLastRawYaw") || !data.contains("CNECameraPlayerOrbitLastRawPitch")) {
				data.putDouble("CNECameraPlayerOrbitLastRawYaw", rawYaw);
				data.putDouble("CNECameraPlayerOrbitLastRawPitch", rawPitch);
				if (!data.contains("CNECameraPlayerOrbitYaw"))
					data.putDouble("CNECameraPlayerOrbitYaw", rawYaw);
				if (!data.contains("CNECameraPlayerOrbitPitch"))
					data.putDouble("CNECameraPlayerOrbitPitch", rawPitch);
				return;
			}
			float lastRawYaw = (float) data.getDouble("CNECameraPlayerOrbitLastRawYaw");
			float lastRawPitch = (float) data.getDouble("CNECameraPlayerOrbitLastRawPitch");
			float yawDelta = net.minecraft.util.Mth.wrapDegrees(rawYaw - lastRawYaw);
			float pitchDelta = rawPitch - lastRawPitch;
			data.putDouble("CNECameraPlayerOrbitYaw", net.minecraft.util.Mth.wrapDegrees(orbitYaw(mc, data) + yawDelta));
			data.putDouble("CNECameraPlayerOrbitPitch", net.minecraft.util.Mth.clamp(orbitPitch(mc, data) + pitchDelta, -80.0F, 80.0F));
			data.putDouble("CNECameraPlayerOrbitLastRawYaw", rawYaw);
			data.putDouble("CNECameraPlayerOrbitLastRawPitch", rawPitch);
		}

		private static float movementFacingYaw(net.minecraft.client.player.Input input, float cameraYaw, float fallbackYaw) {
			float forward = input.forwardImpulse;
			float right = input.leftImpulse;
			double strength = movementStrength(input);
			if (strength <= 1.0E-4D)
				return fallbackYaw;
			double yawRadians = Math.toRadians(cameraYaw);
			double dx = Math.cos(yawRadians) * right - Math.sin(yawRadians) * forward;
			double dz = Math.sin(yawRadians) * right + Math.cos(yawRadians) * forward;
			float facingYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
			input.forwardImpulse = (float) Math.min(1.0D, strength);
			input.leftImpulse = 0.0F;
			input.up = true;
			input.down = false;
			input.left = false;
			input.right = false;
			return facingYaw;
		}

		private static double movementStrength(net.minecraft.client.player.Input input) {
			float forward = input.forwardImpulse;
			float right = input.leftImpulse;
			return Math.sqrt(forward * forward + right * right);
		}

		private static float smoothYaw(float currentYaw, float targetYaw, float amount) {
			float delta = net.minecraft.util.Mth.wrapDegrees(targetYaw - currentYaw);
			return net.minecraft.util.Mth.wrapDegrees(currentYaw + delta * net.minecraft.util.Mth.clamp(amount, 0.0F, 1.0F));
		}

		private static void steerPlayer(net.minecraft.client.player.LocalPlayer player, float yaw, float pitch) {
			float wrappedYaw = net.minecraft.util.Mth.wrapDegrees(yaw);
			float clampedPitch = net.minecraft.util.Mth.clamp(pitch, -80.0F, 80.0F);
			player.setYRot(wrappedYaw);
			player.setXRot(clampedPitch);
			player.setYHeadRot(wrappedYaw);
			player.setYBodyRot(wrappedYaw);
		}

		private static float orbitYaw(net.minecraft.client.Minecraft mc, net.minecraft.nbt.CompoundTag data) {
			if (data != null && data.contains("CNECameraPlayerOrbitYaw"))
				return (float) data.getDouble("CNECameraPlayerOrbitYaw");
			net.minecraft.client.Camera camera = mc == null || mc.gameRenderer == null ? null : mc.gameRenderer.getMainCamera();
			if (camera != null)
				return camera.getYRot();
			return mc != null && mc.player != null ? mc.player.getYRot() : 0.0F;
		}

		private static float orbitPitch(net.minecraft.client.Minecraft mc, net.minecraft.nbt.CompoundTag data) {
			if (data != null && data.contains("CNECameraPlayerOrbitPitch"))
				return (float) data.getDouble("CNECameraPlayerOrbitPitch");
			net.minecraft.client.Camera camera = mc == null || mc.gameRenderer == null ? null : mc.gameRenderer.getMainCamera();
			if (camera != null)
				return camera.getXRot();
			return mc != null && mc.player != null ? mc.player.getXRot() : 0.0F;
		}

		private static boolean hasPositionOffset(net.minecraft.nbt.CompoundTag data) {
			return data != null && (clamp(data.getDouble("CNECameraOffsetX"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET) != 0.0D || clamp(data.getDouble("CNECameraOffsetY"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET) != 0.0D
					|| clamp(data.getDouble("CNECameraOffsetZ"), -MAX_POSITION_OFFSET, MAX_POSITION_OFFSET) != 0.0D || data.getBoolean("CNECameraAbsoluteActive") || !data.getString("CNECameraFollowUUID").isBlank());
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		private static Object cameraTypeValue(Class<?> cameraTypeClass, String name) {
			return Enum.valueOf((Class) cameraTypeClass.asSubclass(Enum.class), name);
		}

		private static void enforceOffsetCameraType(net.minecraft.nbt.CompoundTag data) {
			try {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc == null || mc.options == null)
					return;
				Object current = mc.options.getCameraType();
				Class<?> cameraTypeClass = current != null ? current.getClass() : Class.forName("net.minecraft.client.CameraType");
				Object thirdPersonBack = cameraTypeValue(cameraTypeClass, "THIRD_PERSON_BACK");
				boolean offsetActive = hasPositionOffset(data);
				if (offsetActive) {
					if (!forcedThirdPersonBack) {
						previousCameraTypeName = current instanceof Enum<?> currentEnum ? currentEnum.name() : null;
						forcedThirdPersonBack = true;
					}
					if (current != thirdPersonBack)
						mc.options.setCameraType((net.minecraft.client.CameraType) thirdPersonBack);
				} else if (forcedThirdPersonBack) {
					Object restore = thirdPersonBack;
					if (previousCameraTypeName != null) {
						try {
							restore = cameraTypeValue(cameraTypeClass, previousCameraTypeName);
						} catch (Throwable ignored) {
						}
					}
					mc.options.setCameraType((net.minecraft.client.CameraType) restore);
					forcedThirdPersonBack = false;
					previousCameraTypeName = null;
				}
			} catch (Throwable error) {
				if (!warnedCameraMode) {
					warnedCameraMode = true;
					System.err.println("[ChickenNugget Extras] Could not enforce third-person camera mode: " + error);
				}
			}
		}

		static double value(String key) {
			Object camera = camera();
			if (camera == null)
				return 0.0D;
			try {
				if ("camera_yaw".equals(key))
					return ((Number) camera.getClass().getMethod("getYRot").invoke(camera)).doubleValue();
				if ("camera_pitch".equals(key))
					return ((Number) camera.getClass().getMethod("getXRot").invoke(camera)).doubleValue();
				Object position = camera.getClass().getMethod("getPosition").invoke(camera);
				if ("camera_x".equals(key))
					return ((Number) position.getClass().getField("x").get(position)).doubleValue();
				if ("camera_y".equals(key))
					return ((Number) position.getClass().getField("y").get(position)).doubleValue();
				if ("camera_z".equals(key))
					return ((Number) position.getClass().getField("z").get(position)).doubleValue();
			} catch (Throwable ignored) {
			}
			return 0.0D;
		}

		static void setPosition(Object camera, double x, double y, double z) {
			if (camera == null || !Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z))
				return;
			try {
				java.lang.reflect.Method method = camera.getClass().getDeclaredMethod("setPosition", double.class, double.class, double.class);
				method.setAccessible(true);
				method.invoke(camera, x, y, z);
				return;
			} catch (Throwable ignored) {
			}
			try {
				java.lang.reflect.Method method = camera.getClass().getDeclaredMethod("setPosition", net.minecraft.world.phys.Vec3.class);
				method.setAccessible(true);
				method.invoke(camera, new net.minecraft.world.phys.Vec3(x, y, z));
			} catch (Throwable error) {
				if (!warnedPositionAccess) {
					warnedPositionAccess = true;
					System.err.println("[ChickenNugget Extras] Camera position offset is unavailable on this Minecraft build: " + error);
				}
			}
		}
	}
}
