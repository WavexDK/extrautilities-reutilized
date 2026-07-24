package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

/**
 * EXPERIMENTAL client half of "attach an item to a bone". After each living entity renders (its model is
 * already posed for this frame), walk the PoseStack down to a named Blockbench bone exactly the way
 * {@link CneBoneSyncClient} does, then render an attached ItemStack at that bone in FIXED display context,
 * scaled by a user value, so the item rides the live animation. Standard ModelPart models only (no GeckoLib).
 *
 * <p>The attachments come from {@link CneBoneItemRuntime} (the server-synced client cache keyed by host
 * entity id). This class only READS that cache on the render hot path.</p>
 */
@EventBusSubscriber(modid = "euru", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class CneBoneItemClient {

	private CneBoneItemClient() {
	}

	// A misspelled bone name silently skips the attachment on the render path - which is invisible and very
	// hard to debug from in-game. Warn ONCE per (model, bone) so a typo shows up in the log immediately.
	private static final org.slf4j.Logger CNE_LOG = com.mojang.logging.LogUtils.getLogger();
	private static final java.util.Set<String> MISSING_BONES = java.util.concurrent.ConcurrentHashMap.newKeySet();

	private static void warnBoneMissing(EntityModel<?> model, String bone) {
		if (MISSING_BONES.add(model.getClass().getName() + "#" + bone)) {
			CNE_LOG.warn("[CNE] bone '{}' not found on model {} - attachment skipped (bone names are the raw Blockbench folder names, case-sensitive)",
				bone, model.getClass().getSimpleName());
		}
	}

	// Lazily-baked vanilla armor base models (inner = legs layer, outer = everything else), cached like the
	// vanilla HumanoidArmorLayer does - bake once, reuse. Only touched on the client render path.
	private static HumanoidModel<net.minecraft.world.entity.LivingEntity> INNER_ARMOR;
	private static HumanoidModel<net.minecraft.world.entity.LivingEntity> OUTER_ARMOR;

	private static HumanoidModel<net.minecraft.world.entity.LivingEntity> armorBase(boolean inner) {
		if (inner) {
			if (INNER_ARMOR == null) INNER_ARMOR = new HumanoidModel<>(
				Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
			return INNER_ARMOR;
		}
		if (OUTER_ARMOR == null) OUTER_ARMOR = new HumanoidModel<>(
			Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
		return OUTER_ARMOR;
	}

	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
		if (!CneBoneItemRuntime.hasAnyClientAttachments()) return;
		LivingEntity host = event.getEntity();
		List<CneBoneItemRuntime.Attachment> list = CneBoneItemRuntime.clientAttachments(host.getId());
		if (list.isEmpty()) return;

		EntityModel<?> model = event.getRenderer().getModel();
		float partial = event.getPartialTick();
		float bodyYaw = Mth.rotLerp(partial, host.yBodyRotO, host.yBodyRot);

		PoseStack ps = event.getPoseStack();
		int light = event.getPackedLight();

		for (CneBoneItemRuntime.Attachment att : list) {
			if (att.stack() == null || att.stack().isEmpty()) continue;
			if (att.armor()) {
				// Draw this attachment as a 3D armor MODEL for its slot instead of a held item.
				renderArmorAtBone(host, att, model, ps, event.getMultiBufferSource(), light, bodyYaw);
				continue;
			}
			// REUSE the exact same bone resolution CneBoneSyncClient uses (reflects the ModelPart children tree).
			List<ModelPart> path = CneBoneSyncClient.findBonePath(model, att.bone());
			if (path == null) { warnBoneMissing(model, att.bone()); continue; }

			ps.pushPose();
			// ---- IDENTICAL bone walk to CneBoneSyncClient.onRenderLivingPost ----
			// Replicate LivingEntityRenderer's model transform, then walk down to the bone. After this the
			// PoseStack sits AT the bone, but in the MODEL render frame: Y/Z are flipped by scale(-1,-1,1)
			// and everything is in block units (1.0 == 16 model px), with +X to the model's left.
			ps.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
			ps.scale(-1.0F, -1.0F, 1.0F);
			ps.translate(0.0F, -1.501F, 0.0F);
			for (ModelPart part : path) part.translateAndRotate(ps);
			// ---- end bone walk; PoseStack is now AT the bone ----

			// ---- CORRECTIVE TRANSFORM + USER ROTATION ----
			// 1) Undo the -1,-1,1 model flip so the item is upright and not mirrored. Applying scale(-1,-1,1)
			//    again exactly cancels the earlier flip (two reflections == identity on those axes), leaving a
			//    proper right-handed, Y-up frame for the item renderer.
			ps.scale(-1.0F, -1.0F, 1.0F);
			// 2) Small lift along the bone's up so the item isn't buried in the limb. Constant (block units),
			//    applied before rotate/scale so it stays a fixed nudge regardless of the user's rotation/scale.
			ps.translate(0.0F, 0.15F, 0.0F);
			// 3) USER rotation (degrees) - aim the item along whichever bone direction it should face. The
			//    attach-item block feeds rotX/rotY/rotZ; order is Y (spin), then X (tilt), then Z (roll).
			ps.mulPose(Axis.YP.rotationDegrees(att.rotY()));
			ps.mulPose(Axis.XP.rotationDegrees(att.rotX()));
			ps.mulPose(Axis.ZP.rotationDegrees(att.rotZ()));
			// 4) User scale (uniform), last so it only sizes the item geometry. FIXED draws the item ~1 block tall.
			ps.scale(att.scale(), att.scale(), att.scale());
			// ---- end transform ----

			// Seed 0: FIXED-context items have no random model variants, so a constant seed is stable and fine.
			Minecraft.getInstance().getItemRenderer().renderStatic(
					att.stack(),
					ItemDisplayContext.FIXED,
					light,
					OverlayTexture.NO_OVERLAY,
					ps,
					event.getMultiBufferSource(),
					host.level(),
					0);

			ps.popPose();
		}
	}

	// Draw an equipped armor piece as its real 3D armor MODEL at the bone (custom MCreator model if the item
	// overrides it, else the vanilla humanoid armor model), scaled a bit larger. Matches vanilla
	// HumanoidArmorLayer: pick inner/outer base, per-slot part visibility, then each material layer with its
	// resolved texture + tint. Reuses the exact CneBoneItemClient bone-walk.
	private static void renderArmorAtBone(LivingEntity host, CneBoneItemRuntime.Attachment att,
			EntityModel<?> model, PoseStack ps, MultiBufferSource buffer, int light, float bodyYaw) {
		ItemStack stack = att.stack();
		if (!(stack.getItem() instanceof ArmorItem armorItem)) return;
		EquipmentSlot slot = CneBoneItemRuntime.armorSlot(att.slot());
		if (slot == null) return;

		List<ModelPart> path = CneBoneSyncClient.findBonePath(model, att.bone());
		if (path == null) { warnBoneMissing(model, att.bone()); return; }

		boolean inner = (slot == EquipmentSlot.LEGS);
		HumanoidModel<net.minecraft.world.entity.LivingEntity> base = armorBase(inner);

		// Vanilla-faithful model selection: the item's custom (MCreator) armor model if it overrides the hook,
		// else the vanilla one. ClientHooks.getArmorModel also copies model properties (unlike a raw call).
		Model renderModel = net.neoforged.neoforge.client.ClientHooks.getArmorModel(host, stack, slot, base);

		// CRITICAL: EntityModel.young defaults to TRUE (verified in source), and vanilla only clears it by
		// copying properties from the entity's model every frame - which this bone-attach path skips. Left
		// alone, AgeableListModel.renderToBuffer applies the BABY transform: head scaled 0.75 and translated
		// a full block down, so the piece renders displaced off the bone (looked like "the helmet is gone").
		// Force the adult, neutral state before rendering.
		if (renderModel instanceof net.minecraft.client.model.EntityModel<?> em) {
			em.young = false;
			em.riding = false;
			em.attackTime = 0.0F;
		}

		if (renderModel instanceof HumanoidModel<?> hm) {
			hm.setAllVisible(false);
			switch (slot) {
				case HEAD:  hm.head.visible = true;  hm.hat.visible = true;  break;
				case CHEST: hm.body.visible = true;  hm.rightArm.visible = true; hm.leftArm.visible = true; break;
				case LEGS:  hm.body.visible = true;  hm.rightLeg.visible = true; hm.leftLeg.visible = true; break;
				case FEET:  hm.rightLeg.visible = true; hm.leftLeg.visible = true; break;
				default: break;
			}
		}

		ps.pushPose();
		// IDENTICAL bone walk to the held-item path above.
		ps.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
		ps.scale(-1.0F, -1.0F, 1.0F);
		ps.translate(0.0F, -1.501F, 0.0F);
		for (ModelPart part : path) part.translateAndRotate(ps);
		// FLAG (TUNE): the armor HumanoidModel is authored for this flipped LivingEntityRenderer frame, so we do
		// NOT un-flip (unlike the item path). If it renders inside-out/mirrored in-game, add ps.scale(-1,-1,1) here.
		ps.mulPose(Axis.YP.rotationDegrees(att.rotY()));
		ps.mulPose(Axis.XP.rotationDegrees(att.rotX()));
		ps.mulPose(Axis.ZP.rotationDegrees(att.rotZ()));
		float s = (Float.isFinite(att.scale()) && att.scale() > 0.0F) ? att.scale() : 1.0F;
		ps.scale(s, s, s);

		// Per-material-layer render (tint 0 == skip), matching HumanoidArmorLayer.
		net.neoforged.neoforge.client.extensions.common.IClientItemExtensions ext =
				net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(stack);
		int fallbackColor = ext.getDefaultDyeColor(stack);
		Holder<ArmorMaterial> materialHolder = armorItem.getMaterial();
		List<ArmorMaterial.Layer> layers = materialHolder.value().layers();
		for (int i = 0; i < layers.size(); i++) {
			ArmorMaterial.Layer layer = layers.get(i);
			int tint = ext.getArmorLayerTintColor(stack, host, layer, i, fallbackColor);
			if (tint == 0) continue;
			ResourceLocation texture =
					net.neoforged.neoforge.client.ClientHooks.getArmorTexture(host, stack, layer, inner, slot);
			VertexConsumer vc = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
			renderModel.renderToBuffer(ps, vc, light, OverlayTexture.NO_OVERLAY, tint);
		}
		ps.popPose();
	}
}
