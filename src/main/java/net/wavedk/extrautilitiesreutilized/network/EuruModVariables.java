package net.wavedk.extrautilitiesreutilized.network;

import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import java.util.function.Supplier;
import java.util.ArrayList;

@EventBusSubscriber
public class EuruModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, EuruMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(PlayerVariables::new).build());
	public static double cVer = 1.8;

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		EuruMod.addNetworkMessage(SavedDataSyncMessage.TYPE, SavedDataSyncMessage.STREAM_CODEC, SavedDataSyncMessage::handleData);
		EuruMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
	}

	@SubscribeEvent
	public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES)));
	}

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES)));
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES)));
	}

	@SubscribeEvent
	public static void onPlayerTickUpdateSyncPlayerVariables(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player && player.getData(PLAYER_VARIABLES)._syncDirty) {
			PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES)));
			player.getData(PLAYER_VARIABLES)._syncDirty = false;
		}
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {
		PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
		PlayerVariables clone = new PlayerVariables();
		clone.playerGP_Total = original.playerGP_Total;
		clone.playerGP_Used = original.playerGP_Used;
		clone.playerGPTickUpdateCounter = original.playerGPTickUpdateCounter;
		clone.playerGPChecking = original.playerGPChecking;
		clone.playerGPUpdateTotal = original.playerGPUpdateTotal;
		clone.group_raw_solarpanels = original.group_raw_solarpanels;
		clone.group_count_solarpanels = original.group_count_solarpanels;
		clone.playerGP_Used_Update = original.playerGP_Used_Update;
		clone.group_cutoff_solarpanels = original.group_cutoff_solarpanels;
		clone.playerAB1 = original.playerAB1;
		clone.playerAB2 = original.playerAB2;
		clone.changeAB = original.changeAB;
		clone.overlayCounter = original.overlayCounter;
		clone.group_update_solarpanels = original.group_update_solarpanels;
		clone.group_efficiency_solarpanels = original.group_efficiency_solarpanels;
		clone.changingAB1 = original.changingAB1;
		clone.changingAB2 = original.changingAB2;
		clone.playerGP_Total_SI = original.playerGP_Total_SI;
		clone.playerGP_Used_SI = original.playerGP_Used_SI;
		clone.group_efficiency_mills = original.group_efficiency_mills;
		clone.group_update_mills = original.group_update_mills;
		clone.group_raw_mills = original.group_raw_mills;
		clone.group_count_mills = original.group_count_mills;
		clone.group_cutoff_mills = original.group_cutoff_mills;
		clone.updateab2 = original.updateab2;
		clone.updateAB1 = original.updateAB1;
		clone.oldAB2 = original.oldAB2;
		clone.oldAB1 = original.oldAB1;
		clone.updateMultipliers = original.updateMultipliers;
		if (!event.isWasDeath()) {
		}
		event.getEntity().setData(PLAYER_VARIABLES, clone);
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			SavedData mapdata = MapVariables.get(player.level());
			SavedData worlddata = WorldVariables.get(player.level());
			if (mapdata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(0, mapdata));
			if (worlddata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			SavedData worlddata = WorldVariables.get(player.level());
			if (worlddata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
		}
	}

	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel level) {
			WorldVariables worldVariables = WorldVariables.get(level);
			if (worldVariables._syncDirty) {
				PacketDistributor.sendToPlayersInDimension(level, new SavedDataSyncMessage(1, worldVariables));
				worldVariables._syncDirty = false;
			}
			MapVariables mapVariables = MapVariables.get(level);
			if (mapVariables._syncDirty) {
				PacketDistributor.sendToAllPlayers(new SavedDataSyncMessage(0, mapVariables));
				mapVariables._syncDirty = false;
			}
		}
	}

	public static class WorldVariables extends SavedData {
		public static final String DATA_NAME = "euru_worldvars";
		boolean _syncDirty = false;
		public ArrayList<Object> doe_drops_from = new ArrayList<>();
		public boolean doeList_empty = false;
		public double doeUpdateCounter = 0;

		public static WorldVariables load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
			WorldVariables data = new WorldVariables();
			data.read(tag, lookupProvider);
			return data;
		}

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			doe_drops_from = NbtArrayLists.loadGlobalWorld(nbt.getList("doe_drops_from", Tag.TAG_COMPOUND), lookupProvider);
			doeList_empty = nbt.getBoolean("doeList_empty");
			doeUpdateCounter = nbt.getDouble("doeUpdateCounter");
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.put("doe_drops_from", NbtArrayLists.saveGlobalWorld(doe_drops_from, lookupProvider));
			nbt.putBoolean("doeList_empty", doeList_empty);
			nbt.putDouble("doeUpdateCounter", doeUpdateCounter);
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			this._syncDirty = true;
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(WorldVariables::new, WorldVariables::load), DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final String DATA_NAME = "euru_mapvars";
		boolean _syncDirty = false;

		public static MapVariables load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
			MapVariables data = new MapVariables();
			data.read(tag, lookupProvider);
			return data;
		}

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			_syncDirty = true;
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAcc) {
				return serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(new SavedData.Factory<>(MapVariables::new, MapVariables::load), DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public record SavedDataSyncMessage(int dataType, SavedData data) implements CustomPacketPayload {
		public static final Type<SavedDataSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EuruMod.MODID, "saved_data_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SavedDataSyncMessage message) -> {
			buffer.writeInt(message.dataType);
			if (message.data != null)
				buffer.writeNbt(message.data.save(new CompoundTag(), buffer.registryAccess()));
		}, (RegistryFriendlyByteBuf buffer) -> {
			int dataType = buffer.readInt();
			CompoundTag nbt = buffer.readNbt();
			SavedData data = null;
			if (nbt != null) {
				data = dataType == 0 ? new MapVariables() : new WorldVariables();
				if (data instanceof MapVariables mapVariables)
					mapVariables.read(nbt, buffer.registryAccess());
				else if (data instanceof WorldVariables worldVariables)
					worldVariables.read(nbt, buffer.registryAccess());
			}
			return new SavedDataSyncMessage(dataType, data);
		});

		@Override
		public Type<SavedDataSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final SavedDataSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					if (message.dataType == 0)
						MapVariables.clientSide.read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
					else
						WorldVariables.clientSide.read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		boolean _syncDirty = false;
		public double playerGP_Total = 0;
		public double playerGP_Used = 0;
		public double playerGPTickUpdateCounter = 0;
		public boolean playerGPChecking = false;
		public double playerGPUpdateTotal = 0;
		public double group_raw_solarpanels = 0;
		public double group_count_solarpanels = 0;
		public double playerGP_Used_Update = 0;
		public double group_cutoff_solarpanels = 0;
		public String playerAB1 = "\"\"";
		public String playerAB2 = "\"\"";
		public boolean changeAB = false;
		public double overlayCounter = 0;
		public double group_update_solarpanels = 0;
		public double group_efficiency_solarpanels = 0;
		public String changingAB1 = "\"\"";
		public String changingAB2 = "\"\"";
		public double playerGP_Total_SI = 0;
		public double playerGP_Used_SI = 0;
		public double group_efficiency_mills = 0;
		public double group_update_mills = 0;
		public double group_raw_mills = 0;
		public double group_count_mills = 0;
		public double group_cutoff_mills = 0;
		public boolean updateab2 = false;
		public boolean updateAB1 = false;
		public String oldAB2 = "\"\"";
		public String oldAB1 = "\"\"";
		public boolean updateMultipliers = true;

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putDouble("playerGP_Total", playerGP_Total);
			nbt.putDouble("playerGP_Used", playerGP_Used);
			nbt.putDouble("playerGPTickUpdateCounter", playerGPTickUpdateCounter);
			nbt.putBoolean("playerGPChecking", playerGPChecking);
			nbt.putDouble("playerGPUpdateTotal", playerGPUpdateTotal);
			nbt.putDouble("group_raw_solarpanels", group_raw_solarpanels);
			nbt.putDouble("group_count_solarpanels", group_count_solarpanels);
			nbt.putDouble("playerGP_Used_Update", playerGP_Used_Update);
			nbt.putDouble("group_cutoff_solarpanels", group_cutoff_solarpanels);
			nbt.putString("playerAB1", playerAB1);
			nbt.putString("playerAB2", playerAB2);
			nbt.putBoolean("changeAB", changeAB);
			nbt.putDouble("overlayCounter", overlayCounter);
			nbt.putDouble("group_update_solarpanels", group_update_solarpanels);
			nbt.putDouble("group_efficiency_solarpanels", group_efficiency_solarpanels);
			nbt.putString("changingAB1", changingAB1);
			nbt.putString("changingAB2", changingAB2);
			nbt.putDouble("playerGP_Total_SI", playerGP_Total_SI);
			nbt.putDouble("playerGP_Used_SI", playerGP_Used_SI);
			nbt.putDouble("group_efficiency_mills", group_efficiency_mills);
			nbt.putDouble("group_update_mills", group_update_mills);
			nbt.putDouble("group_raw_mills", group_raw_mills);
			nbt.putDouble("group_count_mills", group_count_mills);
			nbt.putDouble("group_cutoff_mills", group_cutoff_mills);
			nbt.putBoolean("updateab2", updateab2);
			nbt.putBoolean("updateAB1", updateAB1);
			nbt.putString("oldAB2", oldAB2);
			nbt.putString("oldAB1", oldAB1);
			nbt.putBoolean("updateMultipliers", updateMultipliers);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			playerGP_Total = nbt.getDouble("playerGP_Total");
			playerGP_Used = nbt.getDouble("playerGP_Used");
			playerGPTickUpdateCounter = nbt.getDouble("playerGPTickUpdateCounter");
			playerGPChecking = nbt.getBoolean("playerGPChecking");
			playerGPUpdateTotal = nbt.getDouble("playerGPUpdateTotal");
			group_raw_solarpanels = nbt.getDouble("group_raw_solarpanels");
			group_count_solarpanels = nbt.getDouble("group_count_solarpanels");
			playerGP_Used_Update = nbt.getDouble("playerGP_Used_Update");
			group_cutoff_solarpanels = nbt.getDouble("group_cutoff_solarpanels");
			playerAB1 = nbt.getString("playerAB1");
			playerAB2 = nbt.getString("playerAB2");
			changeAB = nbt.getBoolean("changeAB");
			overlayCounter = nbt.getDouble("overlayCounter");
			group_update_solarpanels = nbt.getDouble("group_update_solarpanels");
			group_efficiency_solarpanels = nbt.getDouble("group_efficiency_solarpanels");
			changingAB1 = nbt.getString("changingAB1");
			changingAB2 = nbt.getString("changingAB2");
			playerGP_Total_SI = nbt.getDouble("playerGP_Total_SI");
			playerGP_Used_SI = nbt.getDouble("playerGP_Used_SI");
			group_efficiency_mills = nbt.getDouble("group_efficiency_mills");
			group_update_mills = nbt.getDouble("group_update_mills");
			group_raw_mills = nbt.getDouble("group_raw_mills");
			group_count_mills = nbt.getDouble("group_count_mills");
			group_cutoff_mills = nbt.getDouble("group_cutoff_mills");
			updateab2 = nbt.getBoolean("updateab2");
			updateAB1 = nbt.getBoolean("updateAB1");
			oldAB2 = nbt.getString("oldAB2");
			oldAB1 = nbt.getString("oldAB1");
			updateMultipliers = nbt.getBoolean("updateMultipliers");
		}

		public void markSyncDirty() {
			_syncDirty = true;
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EuruMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec
				.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> buffer.writeNbt(message.data().serializeNBT(buffer.registryAccess())), (RegistryFriendlyByteBuf buffer) -> {
					PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables());
					message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
					return message;
				});

		@Override
		public Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> context.player().getData(PLAYER_VARIABLES).deserializeNBT(context.player().registryAccess(), message.data.serializeNBT(context.player().registryAccess()))).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}