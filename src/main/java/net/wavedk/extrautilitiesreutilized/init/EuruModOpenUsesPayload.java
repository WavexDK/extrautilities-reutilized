package net.wavedk.extrautilitiesreutilized.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public record EuruModOpenUsesPayload(ItemStack stack) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
	public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<EuruModOpenUsesPayload> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(ResourceLocation.parse("euru:open_uses"));
	public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, EuruModOpenUsesPayload> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(ItemStack.OPTIONAL_STREAM_CODEC,
			EuruModOpenUsesPayload::stack, EuruModOpenUsesPayload::new);

	@Override
	public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<EuruModOpenUsesPayload> type() {
		return TYPE;
	}
}