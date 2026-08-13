package net.wavedk.extrautilitiesreutilized.init;

import net.wavedk.extrautilitiesreutilized.block.entity.TransferCableBlockEntity;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = EuruMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class TransferCableCableCapabilities {
	private TransferCableCableCapabilities() {
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, EuruModBlockEntities.TRANSFER_CABLE.get(), (blockEntity, side) -> ((TransferCableBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, EuruModBlockEntities.TRANSFER_CABLE.get(), (blockEntity, side) -> ((TransferCableBlockEntity) blockEntity).getFluidHandler());
	}
}