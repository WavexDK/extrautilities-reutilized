package net.wavedk.extrautilitiesreutilized.client.renderer;

import net.wavedk.extrautilitiesreutilized.entity.CustomEyeOfEnderEntity;

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
public class CustomEyeOfEnderRenderer extends EntityRenderer<CustomEyeOfEnderEntity> {
	@SubscribeEvent
	public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
		if (CustomEyeOfEnderEntity.TYPE != null) {
			event.registerEntityRenderer(CustomEyeOfEnderEntity.TYPE, CustomEyeOfEnderRenderer::new);
		}
	}

	private final ItemRenderer itemRenderer;

	public CustomEyeOfEnderRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	protected int getBlockLightLevel(CustomEyeOfEnderEntity entity, BlockPos blockPos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(CustomEyeOfEnderEntity entity) {
		return ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");
	}

	@Override
	public void render(CustomEyeOfEnderEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		poseStack.scale(2.0F, 2.0F, 2.0F);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		ItemStack itemStack = entity.getItem();
		if (itemStack.isEmpty()) {
			itemStack = new ItemStack(Items.ENDER_EYE);
		}
		this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}
}