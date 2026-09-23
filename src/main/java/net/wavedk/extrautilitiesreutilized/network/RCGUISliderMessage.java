package net.wavedk.extrautilitiesreutilized.network;

import net.wavedk.extrautilitiesreutilized.procedures.RCWhenSliderMovesProcedure;
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
public record RCGUISliderMessage(int sliderID, int x, int y, int z, double value) implements CustomPacketPayload {
	public static final Type<RCGUISliderMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EuruMod.MODID, "rcgui_sliders"));
	public static final StreamCodec<RegistryFriendlyByteBuf, RCGUISliderMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, RCGUISliderMessage message) -> {
		buffer.writeInt(message.sliderID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
		buffer.writeDouble(message.value);
	}, (RegistryFriendlyByteBuf buffer) -> new RCGUISliderMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readDouble()));

	@Override
	public Type<RCGUISliderMessage> type() {
		return TYPE;
	}

	public static void handleData(final RCGUISliderMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> handleSliderAction(context.player(), message.sliderID, message.x, message.y, message.z, message.value)).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleSliderAction(Player entity, int sliderID, int x, int y, int z, double value) {
		Level world = entity.level();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (sliderID == 0) {

			RCWhenSliderMovesProcedure.execute(world, x, y, z, value);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		EuruMod.addNetworkMessage(RCGUISliderMessage.TYPE, RCGUISliderMessage.STREAM_CODEC, RCGUISliderMessage::handleData);
	}
}