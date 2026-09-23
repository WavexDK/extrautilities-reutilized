package net.wavedk.extrautilitiesreutilized.network;

import net.wavedk.extrautilitiesreutilized.procedures.ShadyMerchantNethPickButtonClickedProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.ShadyMerchantNethButtonClickedProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.ShadyMerchantNSButtonClickedProcedure;
import net.wavedk.extrautilitiesreutilized.procedures.ShadyMerchantDiaButtonClickedProcedure;
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
public record ShadyMerchantGUIButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<ShadyMerchantGUIButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EuruMod.MODID, "shady_merchant_gui_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ShadyMerchantGUIButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ShadyMerchantGUIButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new ShadyMerchantGUIButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<ShadyMerchantGUIButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final ShadyMerchantGUIButtonMessage message, final IPayloadContext context) {
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

			ShadyMerchantNSButtonClickedProcedure.execute(entity);
		}
		if (buttonID == 1) {

			ShadyMerchantDiaButtonClickedProcedure.execute(entity);
		}
		if (buttonID == 2) {

			ShadyMerchantNethButtonClickedProcedure.execute(entity);
		}
		if (buttonID == 3) {

			ShadyMerchantNethPickButtonClickedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EuruMod.addNetworkMessage(ShadyMerchantGUIButtonMessage.TYPE, ShadyMerchantGUIButtonMessage.STREAM_CODEC, ShadyMerchantGUIButtonMessage::handleData);
	}
}