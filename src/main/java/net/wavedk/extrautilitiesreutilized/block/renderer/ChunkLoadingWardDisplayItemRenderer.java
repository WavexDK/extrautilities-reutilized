package net.wavedk.extrautilitiesreutilized.block.renderer;

import software.bernie.geckolib.renderer.GeoItemRenderer;

import net.wavedk.extrautilitiesreutilized.block.model.ChunkLoadingWardDisplayModel;
import net.wavedk.extrautilitiesreutilized.block.display.ChunkLoadingWardDisplayItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

public class ChunkLoadingWardDisplayItemRenderer extends GeoItemRenderer<ChunkLoadingWardDisplayItem> {
	public ChunkLoadingWardDisplayItemRenderer() {
		super(new ChunkLoadingWardDisplayModel());
	}

	@Override
	public RenderType getRenderType(ChunkLoadingWardDisplayItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}