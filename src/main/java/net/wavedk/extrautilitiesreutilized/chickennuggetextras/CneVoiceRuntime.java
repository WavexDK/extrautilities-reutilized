package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Proximity voice chat: the client captures the microphone (gated by the
 * push-to-talk/mute settings), encodes 100 ms frames as G.711 mu-law and sends
 * them up; the server relays each frame to every other player within the
 * speaker's voice range; receiving clients stream the audio into a positional
 * mono OpenAL source at the speaker's location, so voices are directional and
 * fade with distance. No procedure blocks required - it just works in game.
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneVoiceRuntime {
	// Server-side record of the last game time each player sent a voice frame, so
	// "is player talking" and mob reactions can tell who is actually speaking.
	private static final ConcurrentHashMap<UUID, Long> TALKING = new ConcurrentHashMap<>();

	private CneVoiceRuntime() {
	}

	/** True while the player has transmitted voice within roughly the last third of a second. */
	public static boolean isEntityTalking(Entity entity) {
		if (entity == null || entity.level() == null) return false;
		Long last = TALKING.get(entity.getUUID());
		return last != null && entity.level().getGameTime() - last <= 7L;
	}

	@SubscribeEvent
	public static void registerPayload(RegisterPayloadHandlersEvent event) {
		event.registrar("euru").playToServer(VoiceUpMessage.TYPE, VoiceUpMessage.STREAM_CODEC, VoiceUpMessage::handleData);
		event.registrar("euru").playToClient(VoiceDownMessage.TYPE, VoiceDownMessage.STREAM_CODEC, VoiceDownMessage::handleData);
	}

	/** One 100 ms voice frame from a client microphone (mu-law, 16 kHz mono). */
	public record VoiceUpMessage(float range, byte[] data) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<VoiceUpMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_voice_up"));
		public static final StreamCodec<RegistryFriendlyByteBuf, VoiceUpMessage> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
			buffer.writeFloat(message.range);
			buffer.writeByteArray(message.data);
		}, buffer -> new VoiceUpMessage(buffer.readFloat(), buffer.readByteArray(8192)));

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(VoiceUpMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.SERVERBOUND) {
				return;
			}
			context.enqueueWork(() -> {
				if (!(context.player() instanceof ServerPlayer speaker) || message.data().length == 0) {
					return;
				}
				ServerLevel level = speaker.serverLevel();
				TALKING.put(speaker.getUUID(), level.getGameTime());
				float range = Mth.clamp(message.range(), 1.0F, 256.0F);
				double rangeSq = (double) range * range;
				VoiceDownMessage relay = new VoiceDownMessage(speaker.getUUID(), speaker.getX(), speaker.getEyeY(), speaker.getZ(), range, message.data());
				for (ServerPlayer listener : level.players()) {
					if (listener != speaker && listener.distanceToSqr(speaker) <= rangeSq) {
						PacketDistributor.sendToPlayer(listener, relay);
					}
				}
				// Mobs can react to talking via the recent-sound blocks; throttled so
				// continuous speech does not flood the sound record list.
				if (level.getGameTime() % 20L == 0L) {
					CneExtrasRuntime.recordVoiceForMobs(speaker);
				}
			});
		}
	}

	/** A relayed voice frame with the speaker's identity and position. */
	public record VoiceDownMessage(UUID speaker, double x, double y, double z, float range, byte[] data) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<VoiceDownMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_voice_down"));
		public static final StreamCodec<RegistryFriendlyByteBuf, VoiceDownMessage> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
			buffer.writeUUID(message.speaker);
			buffer.writeDouble(message.x);
			buffer.writeDouble(message.y);
			buffer.writeDouble(message.z);
			buffer.writeFloat(message.range);
			buffer.writeByteArray(message.data);
		}, buffer -> new VoiceDownMessage(buffer.readUUID(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readFloat(), buffer.readByteArray(8192)));

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(VoiceDownMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.CLIENTBOUND) {
				return;
			}
			context.enqueueWork(() -> {
				if (FMLEnvironment.dist == Dist.CLIENT) {
					ClientVoice.enqueue(message);
				}
			});
		}
	}

	private static final class ClientVoice {
		static void enqueue(VoiceDownMessage message) {
			CneAudioClientRuntime.enqueueIncomingVoice(message.speaker(), message.x(), message.y(), message.z(), message.range(), message.data());
		}
	}
}
