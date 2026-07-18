package net.wavedk.extrautilitiesreutilized.block.listener;

import net.wavedk.extrautilitiesreutilized.init.EuruModBlockEntities;
import net.wavedk.extrautilitiesreutilized.block.renderer.ChunkLoadingWardTileRenderer;
import net.wavedk.extrautilitiesreutilized.block.entity.ChunkLoadingWardTileEntity;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.entity.BlockEntityType;

@EventBusSubscriber(modid = EuruMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientListener {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer((BlockEntityType<ChunkLoadingWardTileEntity>) EuruModBlockEntities.CHUNK_LOADING_WARD.get(), context -> new ChunkLoadingWardTileRenderer());
	}
}