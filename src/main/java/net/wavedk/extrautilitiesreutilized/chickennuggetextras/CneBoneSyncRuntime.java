package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * EXPERIMENTAL bone-tracking. The model's animated bone pivots only exist on the CLIENT (the server
 * has no model), so {@link CneBoneSyncClient} samples them each frame and ships them here via a
 * serverbound payload; bone-tracked hit-zones then follow the synced transform. The position is stored
 * as a LOCAL offset in the host's body-yaw frame (so the zone rides the host even when unobserved,
 * instead of freezing at an absolute world point), plus the bone's world orientation quaternion.
 * Limitations: standard ModelPart models only (no GeckoLib), ~1 tick + ping of lag, and it falls back
 * to the named-bone path when no client is rendering the host.
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneBoneSyncRuntime {
	// hostEntityId -> (boneName -> {localX, localY, localZ, qx, qy, qz, qw, gameTimeStamp})
	// local = host body-yaw frame (see CneExtrasRuntime.offsetPosition); q = bone world orientation.
	private static final Map<Integer, Map<String, double[]>> POSES = new ConcurrentHashMap<>();

	private CneBoneSyncRuntime() {
	}

	@SubscribeEvent
	public static void registerPayload(RegisterPayloadHandlersEvent event) {
		event.registrar("euru").optional().playToServer(BonePoseMessage.TYPE, BonePoseMessage.STREAM_CODEC, BonePoseMessage::handleData);
	}

	/** Store a captured pose. Called by the client (its own copy, so the client zone matches) and by the
	 *  server payload handler (so server collision/damage matches). Shared map handles both in singleplayer. */
	public static void putLocal(int hostId, String bone, double x, double y, double z, double qx, double qy, double qz, double qw, long now) {
		POSES.computeIfAbsent(hostId, k -> new ConcurrentHashMap<>()).put(bone, new double[]{x, y, z, qx, qy, qz, qw, now});
	}

	/** Clear all synced poses. Called on client disconnect so the cache never leaks across world loads. */
	public static void clearAll() {
		POSES.clear();
	}

	/** Latest synced LOCAL offset + orientation {dx,dy,dz,qx,qy,qz,qw,stamp} for a host's bone, or null
	 *  if missing/stale (>3 ticks old). Short window: a fresh pose tracks the live animation while the host
	 *  is observed; past that we fall back to the host-relative geometry path (so nothing ever floats free). */
	public static double[] pose(int hostId, String bone, long now) {
		Map<String, double[]> m = POSES.get(hostId);
		if (m == null) return null;
		double[] p = m.get(bone);
		return (p != null && now - (long) p[7] <= 3L) ? p : null;
	}

	public record BonePoseMessage(int hostId, List<String> bones, float[] data) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<BonePoseMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_bone_pose"));
		public static final StreamCodec<RegistryFriendlyByteBuf, BonePoseMessage> STREAM_CODEC = StreamCodec.of((buf, msg) -> {
			buf.writeVarInt(msg.hostId);
			buf.writeVarInt(msg.bones.size());
			for (String b : msg.bones) buf.writeUtf(b, 64);
			for (float f : msg.data) buf.writeFloat(f);
		}, buf -> {
			int id = buf.readVarInt();
			int n = buf.readVarInt();
			List<String> bones = new ArrayList<>(n);
			for (int i = 0; i < n; i++) bones.add(buf.readUtf(64));
			float[] data = new float[n * 7];
			for (int i = 0; i < data.length; i++) data[i] = buf.readFloat();
			return new BonePoseMessage(id, bones, data);
		});

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(BonePoseMessage msg, IPayloadContext context) {
			if (context.flow() != PacketFlow.SERVERBOUND) return;
			context.enqueueWork(() -> {
				if (!(context.player() instanceof ServerPlayer player)) return;
				Entity host = player.level().getEntity(msg.hostId());
				if (host == null) return;
				long now = player.level().getGameTime();
				Map<String, double[]> m = POSES.computeIfAbsent(msg.hostId(), k -> new ConcurrentHashMap<>());
				List<String> bones = msg.bones();
				float[] data = msg.data();
				// Anti-spoof: values are a LOCAL offset in the host's body-yaw frame, so clamp each component
				// to the host's reach (its largest dimension) plus a margin, not an absolute world AABB. The
				// quaternion slots need no check - a rotation can't teleport anything.
				double maxOffset = Math.max(host.getBbWidth(), host.getBbHeight()) + 8.0D;
				for (int i = 0; i < bones.size() && (i * 7 + 6) < data.length; i++) {
					double dx = data[i * 7], dy = data[i * 7 + 1], dz = data[i * 7 + 2];
					if (!Double.isFinite(dx) || !Double.isFinite(dy) || !Double.isFinite(dz)) continue;
					if (Math.abs(dx) > maxOffset || Math.abs(dy) > maxOffset || Math.abs(dz) > maxOffset) continue;
					m.put(bones.get(i), new double[]{dx, dy, dz, data[i * 7 + 3], data[i * 7 + 4], data[i * 7 + 5], data[i * 7 + 6], now});
				}
			});
		}
	}
}
