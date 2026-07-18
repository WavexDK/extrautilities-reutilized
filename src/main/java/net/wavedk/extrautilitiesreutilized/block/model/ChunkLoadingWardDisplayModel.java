package net.wavedk.extrautilitiesreutilized.block.model;

import software.bernie.geckolib.model.GeoModel;

import net.wavedk.extrautilitiesreutilized.block.display.ChunkLoadingWardDisplayItem;

import net.minecraft.resources.ResourceLocation;

public class ChunkLoadingWardDisplayModel extends GeoModel<ChunkLoadingWardDisplayItem> {
	@Override
	public ResourceLocation getAnimationResource(ChunkLoadingWardDisplayItem animatable) {
		return ResourceLocation.parse("euru:animations/gecko-chunkloader.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ChunkLoadingWardDisplayItem animatable) {
		return ResourceLocation.parse("euru:geo/gecko-chunkloader.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ChunkLoadingWardDisplayItem entity) {
		return ResourceLocation.parse("euru:textures/block/chunkloader.png");
	}
}