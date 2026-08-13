package net.wavedk.extrautilitiesreutilized.client.renderer;

import net.wavedk.extrautilitiesreutilized.entity.CustomFireballEntity;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(value = Dist.CLIENT)
public class CustomFireballRenderer extends EntityRenderer<CustomFireballEntity> {
	@SubscribeEvent
	public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
		if (CustomFireballEntity.TYPE != null) {
			event.registerEntityRenderer(CustomFireballEntity.TYPE, CustomFireballRenderer::new);
		}
	}

	private final ItemRenderer itemRenderer;

	public CustomFireballRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	protected int getBlockLightLevel(CustomFireballEntity entity, BlockPos blockPos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(CustomFireballEntity entity) {
		return ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");
	}

	@Override
	public void render(CustomFireballEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		float scale = entity.getCustomSize();
		poseStack.scale(scale, scale, scale);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		ItemStack itemStack = entity.getItem();
		if (itemStack.isEmpty()) {
			itemStack = new ItemStack(Items.FIRE_CHARGE);
		}
		this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}
}