package net.wavedk.extrautilitiesreutilized.block.renderer;

import software.bernie.geckolib.renderer.GeoBlockRenderer;

import net.wavedk.extrautilitiesreutilized.block.model.ChunkLoadingWardBlockModel;
import net.wavedk.extrautilitiesreutilized.block.entity.ChunkLoadingWardTileEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

public class ChunkLoadingWardTileRenderer extends GeoBlockRenderer<ChunkLoadingWardTileEntity> {
	public ChunkLoadingWardTileRenderer() {
		super(new ChunkLoadingWardBlockModel());
	}

	@Override
	public RenderType getRenderType(ChunkLoadingWardTileEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}