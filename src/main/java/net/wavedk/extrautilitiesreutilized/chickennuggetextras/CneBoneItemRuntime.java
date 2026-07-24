package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
 * EXPERIMENTAL bone-attached items (Phase 1). An entity may carry a small list of attachments, each an
 * ItemStack pinned to a named Blockbench bone (raw, case-sensitive) at a chosen scale. The model's live
 * animated bone pivots only exist on the CLIENT, so the SERVER owns the data (stored in the host's
 * persistent NBT, so it survives save/reload and host unload), and pushes the list to every player
 * tracking the host (and again whenever a player starts tracking it). The CLIENT renderer
 * (CneBoneItemClient, RenderLivingEvent.Post) reads {@link #clientAttachments(int)} and draws each stack at
 * its bone using the SAME ModelPart-walk the bone-sync uses. Standard ModelPart models only (no GeckoLib).
 */
@EventBusSubscriber(modid = "euru")
public final class CneBoneItemRuntime {
	private static final String LIST_TAG = "CNEBoneItems";
	private static final String STACK_TAG = "stack";
	private static final String BONE_TAG = "bone";
	private static final String SCALE_TAG = "scale";
	private static final String ROTX_TAG = "rotX";
	private static final String ROTY_TAG = "rotY";
	private static final String ROTZ_TAG = "rotZ";
	private static final String ARMOR_TAG = "armor";
	private static final String SLOT_TAG = "slot";

	// Client-only cache: hostEntityId -> attachments, refreshed by the playToClient payload and read by the
	// render layer. Keyed by network id (the render hot path has it); cleared on disconnect so a reused id
	// can never paint a previous world's items.
	private static final Map<Integer, List<Attachment>> CLIENT_ATTACHMENTS = new ConcurrentHashMap<>();

	private CneBoneItemRuntime() {
	}

	/** One pinned attachment: a stack, the raw Blockbench bone name (case-sensitive), a render scale, and a
	 *  user rotation in DEGREES (x/y/z). {@code armor}=true means draw it as an armor MODEL for {@code slot}
	 *  ("head"/"chest"/"legs"/"feet") instead of as a held item; held-item attachments use armor=false, slot="". */
	public record Attachment(ItemStack stack, String bone, float scale, float rotX, float rotY, float rotZ,
			boolean armor, String slot) {
	}

	// ----- SERVER: data model (persistent NBT on the host) -----

	/** Attach (or, for an already-attached bone, REPLACE) an item on the host's bone. Server-side; broadcasts. */
	public static void attachBoneItem(Entity host, ItemStack stack, String bone, double scale, double rotX, double rotY, double rotZ) {
		if (host == null || host.level().isClientSide()) return;
		String key = (bone == null) ? "" : bone.trim();
		if (key.isEmpty() || stack == null || stack.isEmpty()) return;
		float s = (float) scale;
		if (!Float.isFinite(s) || s <= 0.0F) s = 1.0F;
		float rx = (float) rotX; if (!Float.isFinite(rx)) rx = 0.0F;
		float ry = (float) rotY; if (!Float.isFinite(ry)) ry = 0.0F;
		float rz = (float) rotZ; if (!Float.isFinite(rz)) rz = 0.0F;

		CompoundTag root = host.getPersistentData();
		ListTag list = root.getList(LIST_TAG, Tag.TAG_COMPOUND);
		// Replace an existing entry for the same bone, otherwise append.
		int replaceAt = -1;
		for (int i = 0; i < list.size(); i++) {
			if (key.equals(list.getCompound(i).getString(BONE_TAG))) {
				replaceAt = i;
				break;
			}
		}
		CompoundTag entry = new CompoundTag();
		entry.putString(BONE_TAG, key);
		entry.putFloat(SCALE_TAG, s);
		entry.putFloat(ROTX_TAG, rx);
		entry.putFloat(ROTY_TAG, ry);
		entry.putFloat(ROTZ_TAG, rz);
		entry.putBoolean(ARMOR_TAG, false);
		entry.putString(SLOT_TAG, "");
		entry.put(STACK_TAG, stack.copy().save(host.level().registryAccess()));
		if (replaceAt >= 0) {
			list.set(replaceAt, entry);
		} else {
			list.add(list.size(), entry);
		}
		root.put(LIST_TAG, list);
		broadcast(host);
	}

	/** Map "head"/"chest"/"legs"/"feet" to the armor EquipmentSlot; null for anything else.
	 *  (Deliberately NOT EquipmentSlot.byName, which throws IllegalArgumentException on bad input.) */
	static EquipmentSlot armorSlot(String slot) {
		if (slot == null) return null;
		switch (slot.trim().toLowerCase(java.util.Locale.ROOT)) {
			case "head": return EquipmentSlot.HEAD;
			case "chest": return EquipmentSlot.CHEST;
			case "legs": return EquipmentSlot.LEGS;
			case "feet": return EquipmentSlot.FEET;
			default: return null;
		}
	}

	/** Put an armor stack into one of the host's four real armor slots (server-side). No-op for a non-living
	 *  host or an unknown slot name. Pass an empty stack to clear the slot. */
	public static void setEntityArmor(Entity host, String slot, ItemStack stack) {
		if (host == null || host.level().isClientSide()) return;
		if (!(host instanceof LivingEntity living)) return;
		if (stack == null) stack = ItemStack.EMPTY;
		EquipmentSlot es = armorSlot(slot);
		if (es == null) return;
		living.setItemSlot(es, stack.copy());
	}

	/** Attach (or REPLACE, per bone) an ARMOR piece rendered as its 3D model at a bone, for the given slot. */
	public static void attachBoneArmor(Entity host, ItemStack stack, String bone, double scale, String slot) {
		if (host == null || host.level().isClientSide()) return;
		String key = (bone == null) ? "" : bone.trim();
		if (key.isEmpty() || stack == null || stack.isEmpty()) return;
		EquipmentSlot es = armorSlot(slot);
		if (es == null) return;
		String slotName = es.getSerializedName();
		float s = (float) scale;
		if (!Float.isFinite(s) || s <= 0.0F) s = 1.0F;

		CompoundTag root = host.getPersistentData();
		ListTag list = root.getList(LIST_TAG, Tag.TAG_COMPOUND);
		int replaceAt = -1;
		for (int i = 0; i < list.size(); i++) {
			if (key.equals(list.getCompound(i).getString(BONE_TAG))) { replaceAt = i; break; }
		}
		CompoundTag entry = new CompoundTag();
		entry.putString(BONE_TAG, key);
		entry.putFloat(SCALE_TAG, s);
		entry.putFloat(ROTX_TAG, 0.0F);
		entry.putFloat(ROTY_TAG, 0.0F);
		entry.putFloat(ROTZ_TAG, 0.0F);
		entry.putBoolean(ARMOR_TAG, true);
		entry.putString(SLOT_TAG, slotName);
		entry.put(STACK_TAG, stack.copy().save(host.level().registryAccess()));
		if (replaceAt >= 0) list.set(replaceAt, entry); else list.add(list.size(), entry);
		root.put(LIST_TAG, list);
		broadcast(host);
	}

	/** Remove every attachment from the host. Server-side; broadcasts the now-empty list. */
	public static void clearBoneItems(Entity host) {
		if (host == null || host.level().isClientSide()) return;
		CompoundTag root = host.getPersistentData();
		if (!root.contains(LIST_TAG)) return;
		root.remove(LIST_TAG);
		broadcast(host);
	}

	/** Read the host's attachments off its persistent NBT (server side). Returns an empty list if none. */
	public static List<Attachment> readAttachments(Entity host) {
		List<Attachment> out = new ArrayList<>();
		if (host == null) return out;
		CompoundTag root = host.getPersistentData();
		if (!root.contains(LIST_TAG)) return out;
		ListTag list = root.getList(LIST_TAG, Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			CompoundTag entry = list.getCompound(i);
			String bone = entry.getString(BONE_TAG);
			if (bone == null || bone.isEmpty()) continue;
			ItemStack stack = ItemStack.parseOptional(host.level().registryAccess(), entry.getCompound(STACK_TAG));
			if (stack.isEmpty()) continue;
			float scale = entry.contains(SCALE_TAG) ? entry.getFloat(SCALE_TAG) : 1.0F;
			if (!Float.isFinite(scale) || scale <= 0.0F) scale = 1.0F;
			float rotX = entry.contains(ROTX_TAG) ? entry.getFloat(ROTX_TAG) : 0.0F;
			float rotY = entry.contains(ROTY_TAG) ? entry.getFloat(ROTY_TAG) : 0.0F;
			float rotZ = entry.contains(ROTZ_TAG) ? entry.getFloat(ROTZ_TAG) : 0.0F;
			boolean armor = entry.contains(ARMOR_TAG) ? entry.getBoolean(ARMOR_TAG) : false;
			String slot = entry.contains(SLOT_TAG) ? entry.getString(SLOT_TAG) : "";
			out.add(new Attachment(stack, bone, scale, rotX, rotY, rotZ, armor, slot));
		}
		return out;
	}

	private static void broadcast(Entity host) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(host, new BoneItemsMessage(host.getId(), readAttachments(host)));
	}

	// A player starting to track the host (enters range / relogs) must receive its current attachments.
	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) return;
		Entity host = event.getTarget();
		List<Attachment> attachments = readAttachments(host);
		if (!attachments.isEmpty()) {
			PacketDistributor.sendToPlayer(player, new BoneItemsMessage(host.getId(), attachments));
		}
	}

	// ----- CLIENT: cache the render layer reads -----

	/** Client-side: the attachments currently synced for this host id (empty list if none). The render
	 *  layer calls this in RenderLivingEvent.Post and draws each stack at its bone. */
	public static List<Attachment> clientAttachments(int hostId) {
		List<Attachment> a = CLIENT_ATTACHMENTS.get(hostId);
		return a == null ? java.util.Collections.emptyList() : a;
	}

	/** Client-side: true if any entity currently has attachments (lets the render layer early-out cheaply). */
	public static boolean hasAnyClientAttachments() {
		return !CLIENT_ATTACHMENTS.isEmpty();
	}

	@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
	public static final class Registration {
		@SubscribeEvent
		public static void register(RegisterPayloadHandlersEvent event) {
			event.registrar("euru").playToClient(BoneItemsMessage.TYPE, BoneItemsMessage.STREAM_CODEC, BoneItemsMessage::handleData);
		}
	}

	@EventBusSubscriber(modid = "euru", value = Dist.CLIENT)
	public static final class ClientEvents {
		@SubscribeEvent
		public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
			CLIENT_ATTACHMENTS.clear();
		}
	}

	public record BoneItemsMessage(int entityId, List<Attachment> attachments) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<BoneItemsMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_bone_items"));

		// ItemStack.OPTIONAL_STREAM_CODEC : StreamCodec<RegistryFriendlyByteBuf, ItemStack> (verified) - encodes
		// empty stacks safely. The bone is a raw name (cap length like the bone-sync payload), scale a float.
		public static final StreamCodec<RegistryFriendlyByteBuf, BoneItemsMessage> STREAM_CODEC = StreamCodec.of(
			(buffer, message) -> {
				buffer.writeVarInt(message.entityId());
				List<Attachment> list = message.attachments();
				buffer.writeVarInt(list.size());
				for (Attachment a : list) {
					ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, a.stack());
					buffer.writeUtf(a.bone(), 64);
					buffer.writeFloat(a.scale());
					buffer.writeFloat(a.rotX());
					buffer.writeFloat(a.rotY());
					buffer.writeFloat(a.rotZ());
					buffer.writeBoolean(a.armor());
					buffer.writeUtf(a.slot(), 16);
				}
			},
			buffer -> {
				int entityId = buffer.readVarInt();
				int n = buffer.readVarInt();
				List<Attachment> list = new ArrayList<>(n);
				for (int i = 0; i < n; i++) {
					ItemStack stack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
					String bone = buffer.readUtf(64);
					float scale = buffer.readFloat();
					float rotX = buffer.readFloat();
					float rotY = buffer.readFloat();
					float rotZ = buffer.readFloat();
					boolean armor = buffer.readBoolean();
					String slot = buffer.readUtf(16);
					list.add(new Attachment(stack, bone, scale, rotX, rotY, rotZ, armor, slot));
				}
				return new BoneItemsMessage(entityId, list);
			});

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(BoneItemsMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.CLIENTBOUND) return;
			context.enqueueWork(() -> {
				if (FMLEnvironment.dist != Dist.CLIENT) return;
				if (message.attachments().isEmpty()) {
					CLIENT_ATTACHMENTS.remove(message.entityId());
				} else {
					CLIENT_ATTACHMENTS.put(message.entityId(), List.copyOf(message.attachments()));
				}
			});
		}
	}
}
