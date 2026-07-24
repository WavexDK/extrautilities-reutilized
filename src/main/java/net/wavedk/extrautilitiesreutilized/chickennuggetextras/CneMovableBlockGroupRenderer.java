package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Draws every cell of the group as its real block model, with the whole rig rotated about
 * the entity origin (the rig centre / pivot). Each cell is offset from the centre by its
 * 0-based offset minus half the rig dimensions - the same convention the entity uses for
 * collision and place-back, so the visual always matches where blocks actually are.
 */
public class CneMovableBlockGroupRenderer extends EntityRenderer<CneMovableBlockGroupEntity> {
	private final BlockRenderDispatcher blocks;

	public CneMovableBlockGroupRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.blocks = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(CneMovableBlockGroupEntity entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
		List<BlockPos> offsets = entity.cellOffsets();
		if (offsets.isEmpty()) return;
		List<BlockState> states = entity.cellStates();
		double hx = entity.dimX() / 2.0D, hy = entity.dimY() / 2.0D, hz = entity.dimZ() / 2.0D;
		pose.pushPose();
		pose.mulPose(Axis.XP.rotationDegrees(entity.getRotX() + entity.getAvX() * partialTicks));
		pose.mulPose(Axis.YP.rotationDegrees(entity.getRotY() + entity.getAvY() * partialTicks));
		pose.mulPose(Axis.ZP.rotationDegrees(entity.getRotZ() + entity.getAvZ() * partialTicks));
		// Under F3+B, outline each real block (the actual per-block collision), instead of the
		// whole-rig box (the entity is invisible so vanilla skips that misleading outline).
		boolean hitboxes = this.entityRenderDispatcher.shouldRenderHitBoxes();
		int n = Math.min(offsets.size(), states.size());
		for (int i = 0; i < n; i++) {
			BlockState state = states.get(i);
			if (state.isAir()) continue;
			BlockPos o = offsets.get(i);
			pose.pushPose();
			pose.translate(o.getX() - hx, o.getY() - hy, o.getZ() - hz);
			this.blocks.renderSingleBlock(state, pose, buffer, packedLight, OverlayTexture.NO_OVERLAY);
			if (hitboxes) LevelRenderer.renderLineBox(pose, buffer.getBuffer(RenderType.lines()), 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 0.3F, 1.0F, 0.3F, 1.0F);
			pose.popPose();
		}
		pose.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(CneMovableBlockGroupEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
