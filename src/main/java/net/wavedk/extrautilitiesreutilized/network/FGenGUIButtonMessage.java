package net.wavedk.extrautilitiesreutilized.network;

import net.wavedk.extrautilitiesreutilized.procedures.RedstoneGearRightclickedProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.ChangeRedstoneModeProcedure;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.SectionPos;

@EventBusSubscriber
public record FGenGUIButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<FGenGUIButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EuruMod.MODID, "f_gen_gui_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, FGenGUIButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, FGenGUIButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new FGenGUIButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<FGenGUIButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final FGenGUIButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> handleButtonAction(context.player(), message.buttonID, message.x, message.y, message.z)).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (buttonID == 0) {

			RedstoneGearRightclickedProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 1) {

			RedstoneGearRightclickedProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 2) {

			ChangeRedstoneModeProcedure.execute(world, x, y, z);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EuruMod.addNetworkMessage(FGenGUIButtonMessage.TYPE, FGenGUIButtonMessage.STREAM_CODEC, FGenGUIButtonMessage::handleData);
	}
}