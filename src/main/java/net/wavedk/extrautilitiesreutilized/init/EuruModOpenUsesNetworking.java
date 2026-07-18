package net.wavedk.extrautilitiesreutilized.init;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;

@EventBusSubscriber
public class EuruModOpenUsesNetworking {
	@SubscribeEvent
	public static void register(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
		net.neoforged.neoforge.network.registration.PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(EuruModOpenUsesPayload.TYPE, EuruModOpenUsesPayload.STREAM_CODEC, (payload, context) -> context.enqueueWork(() -> {
			if (ModList.get().isLoaded("jei")) {
				EuruModJeiRuntimeHolder.showUses(payload.stack());
			}
		}));
	}

	public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, ItemStack stack) {
		net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new EuruModOpenUsesPayload(stack));
	}
}