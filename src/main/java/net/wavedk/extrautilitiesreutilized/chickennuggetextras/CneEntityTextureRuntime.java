package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Runtime texture override for living entities (mobs and players).
 *
 * The texture used while rendering is decided on the CLIENT, but an entity's
 * persistent data does not sync, so the override is kept server-side (for
 * persistence) and pushed to clients with a packet: every player tracking the
 * entity, and again whenever a player starts tracking it (enters range / relogs)
 * or a tagged player respawns/changes dimension/logs in. The client cache is
 * keyed by ENTITY UUID (never reused, unlike the network id) so a stale entry can
 * never paint onto a different entity, and it is cleared when the client
 * disconnects. CneEntityTextureMixin reads the override during rendering.
 *
 * Note: this overrides the base body/skin texture only; equipped armor, elytra and
 * capes keep their own textures (those are separate render layers).
 */
@EventBusSubscriber(modid = "euru")
public final class CneEntityTextureRuntime {
	private static final String TEXTURE_TAG = "CNEEntityTexture";
	private static final Map<UUID, ResourceLocation> CLIENT_OVERRIDES = new ConcurrentHashMap<>();

	private CneEntityTextureRuntime() {
	}

	public static void setEntityTexture(Entity entity, String texture) {
		if (!(entity instanceof LivingEntity living) || living.level().isClientSide()) return;
		String value = texture == null ? "" : texture.trim();
		if (value.isEmpty() || ResourceLocation.tryParse(value) == null) {
			resetEntityTexture(entity);
			return;
		}
		living.getPersistentData().putString(TEXTURE_TAG, value);
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(living, new EntityTextureMessage(living.getUUID(), value));
	}

	public static void resetEntityTexture(Entity entity) {
		if (!(entity instanceof LivingEntity living) || living.level().isClientSide()) return;
		living.getPersistentData().remove(TEXTURE_TAG);
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(living, new EntityTextureMessage(living.getUUID(), ""));
	}

	/** Procedure 'custom skin of entity' block: the custom skin/texture set on this entity, or "".
	 * Reads the override stored in persistent data (only the CUSTOM skin set by these blocks -
	 * a vanilla mob's or a real player's default texture is client-side and not readable here). */
	public static String getEntityTexture(Entity entity) {
		if (!(entity instanceof LivingEntity living)) return "";
		String value = living.getPersistentData().getString(TEXTURE_TAG);
		return value == null ? "" : value;
	}

	/** Procedure 'set skin to source's skin' block: copy the source entity's custom skin onto the
	 * target (synced). If the source has no custom skin, the target's skin is reset to normal. */
	public static void copyEntitySkin(Entity target, Entity source) {
		if (!(source instanceof LivingEntity src)) return;
		String texture = src.getPersistentData().getString(TEXTURE_TAG);
		if (texture == null || texture.isEmpty()) {
			resetEntityTexture(target);
		} else {
			setEntityTexture(target, texture);
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof LivingEntity living) {
			rebroadcastTo(player, living);
		}
	}

	// A player does not "track" itself and gets a brand-new entity each respawn /
	// dimension change / login, so re-send the override for these so the player's own
	// view (and everyone else's) stays correct.
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		rebroadcastSelf(event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		rebroadcastSelf(event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		rebroadcastSelf(event.getEntity());
	}

	// A player's root persistent data is NOT carried across the death/dimension clone,
	// so copy the override tag onto the recreated player (covers death-respawn and the
	// non-death End-return clone).
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		try {
			CompoundTag from = event.getOriginal().getPersistentData();
			if (from.contains(TEXTURE_TAG)) {
				event.getEntity().getPersistentData().putString(TEXTURE_TAG, from.getString(TEXTURE_TAG));
			}
		} catch (Exception ignored) {
		}
	}

	private static void rebroadcastSelf(Entity entity) {
		if (entity instanceof ServerPlayer player) rebroadcastTo(player, player);
	}

	private static void rebroadcastTo(ServerPlayer recipient, LivingEntity subject) {
		String value = subject.getPersistentData().getString(TEXTURE_TAG);
		if (!value.isEmpty()) {
			PacketDistributor.sendToPlayer(recipient, new EntityTextureMessage(subject.getUUID(), value));
		}
	}

	/** Called from CneEntityTextureMixin (client) to swap the rendered texture. */
	public static ResourceLocation getOverrideTexture(LivingEntity entity) {
		if (entity == null) return null;
		ResourceLocation override = CLIENT_OVERRIDES.isEmpty() ? null : CLIENT_OVERRIDES.get(entity.getUUID());
		if (override != null) return override;
		// "Change my skin" tool: a client-only override for the local player (never networked).
		if (FMLEnvironment.dist == Dist.CLIENT) return ClientEvents.localPlayerSkinFor(entity);
		return null;
	}

	@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
	public static final class Registration {
		@SubscribeEvent
		public static void register(RegisterPayloadHandlersEvent event) {
			event.registrar("euru").playToClient(EntityTextureMessage.TYPE, EntityTextureMessage.STREAM_CODEC, EntityTextureMessage::handleData);
		}
	}

	// Client-only: drop the whole cache when leaving a server/world so overrides from a
	// previous session can never linger and paint onto entities in the next one.
	@EventBusSubscriber(modid = "euru", value = Dist.CLIENT)
	public static final class ClientEvents {
		private static volatile ResourceLocation localSkin;
		private static volatile boolean localSkinLoaded;

		@SubscribeEvent
		public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
			CLIENT_OVERRIDES.clear();
			localSkinLoaded = false; // re-read the local-skin marker on the next world
		}

		// The "Player Skin" tool (Extra Tools) writes assets/<modid>/cne_local_skin/config.json
		// {"skin":"<resource location>"} plus the skin PNG into the workspace. When present, the
		// LOCAL player on this client renders with that skin - a dev/testing convenience that is
		// never sent to anyone else.
		static ResourceLocation localPlayerSkinFor(LivingEntity entity) {
			ensureLoaded();
			ResourceLocation skin = localSkin;
			if (skin == null) return null;
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
			return (mc != null && entity == mc.player) ? skin : null;
		}

		private static void ensureLoaded() {
			if (localSkinLoaded) return;
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
			if (mc == null || mc.getResourceManager() == null) return; // try again once resources are up
			localSkinLoaded = true;
			localSkin = null;
			try {
				ResourceLocation cfg = ResourceLocation.fromNamespaceAndPath("euru", "cne_local_skin/config.json");
				java.util.Optional<net.minecraft.server.packs.resources.Resource> res = mc.getResourceManager().getResource(cfg);
				if (res.isEmpty()) return;
				try (java.io.InputStreamReader reader = new java.io.InputStreamReader(res.get().open(), java.nio.charset.StandardCharsets.UTF_8)) {
					com.google.gson.JsonObject root = com.google.gson.JsonParser.parseReader(reader).getAsJsonObject();
					if (root.has("skin")) {
						ResourceLocation id = ResourceLocation.tryParse(root.get("skin").getAsString());
						if (id != null) localSkin = id;
					}
				}
			} catch (Exception ignored) {
				localSkin = null;
			}
		}
	}

	public record EntityTextureMessage(UUID entityUuid, String texture) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<EntityTextureMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_entity_texture"));
		public static final StreamCodec<RegistryFriendlyByteBuf, EntityTextureMessage> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
			buffer.writeUUID(message.entityUuid);
			buffer.writeUtf(message.texture);
		}, buffer -> new EntityTextureMessage(buffer.readUUID(), buffer.readUtf()));

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(EntityTextureMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.CLIENTBOUND) {
				return;
			}
			context.enqueueWork(() -> {
				if (FMLEnvironment.dist != Dist.CLIENT) return;
				String texture = message.texture();
				if (texture == null || texture.isEmpty()) {
					CLIENT_OVERRIDES.remove(message.entityUuid());
					return;
				}
				ResourceLocation id = ResourceLocation.tryParse(texture);
				if (id != null) {
					CLIENT_OVERRIDES.put(message.entityUuid(), id);
				} else {
					CLIENT_OVERRIDES.remove(message.entityUuid());
				}
			});
		}
	}
}
