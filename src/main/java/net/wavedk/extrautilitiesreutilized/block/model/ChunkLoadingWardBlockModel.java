package net.wavedk.extrautilitiesreutilized.block.model;

import software.bernie.geckolib.model.GeoModel;

import net.wavedk.extrautilitiesreutilized.block.entity.ChunkLoadingWardTileEntity;

import net.minecraft.resources.ResourceLocation;

public class ChunkLoadingWardBlockModel extends GeoModel<ChunkLoadingWardTileEntity> {
	@Override
	public ResourceLocation getAnimationResource(ChunkLoadingWardTileEntity animatable) {
		return ResourceLocation.parse("euru:animations/gecko-chunkloader.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ChunkLoadingWardTileEntity animatable) {
		return ResourceLocation.parse("euru:geo/gecko-chunkloader.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ChunkLoadingWardTileEntity animatable) {
		return ResourceLocation.parse("euru:textures/block/chunkloader.png");
	}
}