package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Draws the movable block as its real block model, rotated about the block centre by
 * the entity's three rotation angles. Uses the simplest renderSingleBlock overload (no
 * Level needed); biome tint falls back to the default colour.
 */
public class CneMovableBlockRenderer extends EntityRenderer<CneMovableBlockEntity> {
	private final BlockRenderDispatcher blocks;

	public CneMovableBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.blocks = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(CneMovableBlockEntity entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
		BlockState state = entity.getBlockState();
		if (state.isAir()) return;
		pose.pushPose();
		// Entity origin is the block centre-bottom: go up to the centre, rotate, then drop
		// to the min corner where renderSingleBlock draws its [0,1] cube.
		pose.translate(0.0F, 0.5F, 0.0F);
		pose.mulPose(Axis.XP.rotationDegrees(entity.getRotX() + entity.getAvX() * partialTicks));
		pose.mulPose(Axis.YP.rotationDegrees(entity.getRotY() + entity.getAvY() * partialTicks));
		pose.mulPose(Axis.ZP.rotationDegrees(entity.getRotZ() + entity.getAvZ() * partialTicks));
		pose.translate(-0.5F, -0.5F, -0.5F);
		this.blocks.renderSingleBlock(state, pose, buffer, packedLight, OverlayTexture.NO_OVERLAY);
		pose.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(CneMovableBlockEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
