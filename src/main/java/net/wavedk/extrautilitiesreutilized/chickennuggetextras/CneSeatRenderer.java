package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/** The seat is invisible - this renderer draws nothing; it only exists so the entity has a renderer. */
public class CneSeatRenderer extends EntityRenderer<CneSeatEntity> {
	public CneSeatRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(CneSeatEntity entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
	}

	@Override
	public ResourceLocation getTextureLocation(CneSeatEntity entity) {
		return ResourceLocation.withDefaultNamespace("textures/misc/white.png");
	}
}
