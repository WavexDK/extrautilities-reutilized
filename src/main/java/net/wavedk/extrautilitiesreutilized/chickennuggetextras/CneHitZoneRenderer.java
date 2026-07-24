package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * The hit zone is invisible in normal play. Under the F3+B hitbox debug overlay it
 * draws its OWN box as a red outline - vanilla's hitbox overlay skips invisible
 * entities, so without this you can't see where your zones are or what size they are.
 */
public class CneHitZoneRenderer extends EntityRenderer<CneHitZoneEntity> {
	public CneHitZoneRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(CneHitZoneEntity entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
		if (!this.entityRenderDispatcher.shouldRenderHitBoxes()) return;
		double w = entity.getBbWidth() / 2.0D;
		double h = entity.getBbHeight();
		float[] c = boneColor(entity.boneColorId());
		boolean rotate = entity.hasRotation();
		org.joml.Quaternionf q = entity.trackedRotation();
		if (rotate) {
			// Tilt the (unrotated-size) box about its centre to show the bone's orientation.
			pose.pushPose();
			pose.translate(0.0F, (float) (h / 2.0D), 0.0F);
			if (q != null) {
				// Bone-tracked: tilt by the REAL captured bone orientation quaternion.
				pose.mulPose(q);
			} else {
				pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-entity.getViewYRot(partialTicks)));
				pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(entity.getViewXRot(partialTicks)));
			}
			pose.translate(0.0F, (float) (-h / 2.0D), 0.0F);
		}
		// Box relative to the entity origin (centre-bottom): -w..w on X/Z, 0..h on Y.
		LevelRenderer.renderLineBox(pose, buffer.getBuffer(RenderType.lines()), -w, 0.0D, -w, w, h, w, c[0], c[1], c[2], 1.0F);
		if (rotate) pose.popPose();
	}

	private static float[] boneColor(int id) {
		return switch (id) {
			case 0 -> new float[]{1.0F, 0.25F, 0.25F};  // head - red
			case 1 -> new float[]{1.0F, 0.6F, 0.2F};    // chest - orange
			case 2 -> new float[]{0.3F, 1.0F, 0.3F};    // main hand - green
			case 3 -> new float[]{0.3F, 1.0F, 1.0F};    // off hand - cyan
			case 4 -> new float[]{0.4F, 0.5F, 1.0F};    // right leg - blue
			case 5 -> new float[]{0.85F, 0.4F, 1.0F};   // left leg - purple
			case 6 -> new float[]{1.0F, 1.0F, 0.3F};    // feet - yellow
			default -> new float[]{1.0F, 1.0F, 1.0F};   // other - white
		};
	}

	@Override
	public ResourceLocation getTextureLocation(CneHitZoneEntity entity) {
		return ResourceLocation.withDefaultNamespace("textures/misc/white.png");
	}
}
