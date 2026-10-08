package net.wavedk.extrautilitiesreutilized.network;

import net.wavedk.extrautilitiesreutilized.client.CameraShakeManager;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;

@EventBusSubscriber
public record CameraShakePayload(float power, int durationTicks) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<CameraShakePayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "camera_shake"));
	public static final StreamCodec<FriendlyByteBuf, CameraShakePayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, CameraShakePayload::power, ByteBufCodecs.INT, CameraShakePayload::durationTicks, CameraShakePayload::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handleData(final CameraShakePayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
			CameraShakeManager.getInstance().shake(data.power(), data.durationTicks());
		});
	}

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(TYPE, STREAM_CODEC, CameraShakePayload::handleData);
	}
}