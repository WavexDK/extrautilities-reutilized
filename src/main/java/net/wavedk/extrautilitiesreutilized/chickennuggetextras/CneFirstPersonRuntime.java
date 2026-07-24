package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * EXPERIMENTAL first-person realism. Two related effects, both client-local (the first-person view only
 * exists on the owning client) and synced to that player by a server-side procedure call:
 *   1. Viewmodel offset/scale + view-bob dampening (lowers the vanilla HUD-locked item toward the real hand).
 *   2. First-person arms mode (the "world item" toggle): hides the flat vanilla viewmodels and instead draws
 *      the actual third-person player model (BOTH arms + sleeves + the items in each hand) in WORLD space,
 *      posed by the real animation (walk sway + attack swing), via RenderLevelStageEvent.AFTER_ENTITIES.
 * The client listeners are registered lazily (Class.forName("net.minecraft.client.Minecraft") guard) and every
 * client-typed method body lives in the nested FirstPersonAccess class, so this common class both loads AND
 * verifies cleanly on a dedicated server (verifying client-typed bodies would force-load client classes).
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneFirstPersonRuntime {
	private static final double MAX_OFFSET = 4.0D;
	private static final double MIN_SCALE = 0.05D;
	private static final double MAX_SCALE = 8.0D;

	private static volatile double offsetRight = 0.0D;
	private static volatile double offsetUp = 0.0D;
	private static volatile double offsetForward = 0.0D;
	private static volatile double scale = 1.0D;
	private static volatile double bobScale = 1.0D;
	private static volatile boolean worldItem = false;
	private static volatile boolean listenerRegistered;
	// Set true only DURING our own first-person arms render() call, so the RenderPlayerEvent.Pre listener
	// hides the non-arm parts at the one moment that survives PlayerRenderer.setModelProperties (setAllVisible).
	private static volatile boolean hidingArms = false;

	private CneFirstPersonRuntime() {
	}

	// ---- server-side entry points (called from procedures) ----

	public static void setViewmodelOffset(Entity target, double right, double up, double forward, double itemScale) {
		send(target, clamp(right, -MAX_OFFSET, MAX_OFFSET), clamp(up, -MAX_OFFSET, MAX_OFFSET),
			clamp(forward, -MAX_OFFSET, MAX_OFFSET), clamp(itemScale, MIN_SCALE, MAX_SCALE), bobScale, worldItem);
	}

	public static void setBobScale(Entity target, double dampenedBobScale) {
		send(target, offsetRight, offsetUp, offsetForward, scale, clamp(dampenedBobScale, 0.0D, 1.0D), worldItem);
	}

	/** Turns the world-item first-person mode on/off for the target player (synced like the offset). */
	public static void setWorldHeldItem(Entity target, boolean enabled) {
		send(target, offsetRight, offsetUp, offsetForward, scale, bobScale, enabled);
	}

	private static void send(Entity target, double r, double u, double f, double s, double b, boolean world) {
		Entity resolved = target != null ? target : localPlayer();
		if (resolved instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, new FirstPersonMessage(r, u, f, s, b, world));
			return;
		}
		// Single-player client-side call (or no ServerPlayer): apply directly.
		applyLocal(r, u, f, s, b, world);
	}

	// ---- client apply ----

	private static void applyLocal(double right, double up, double forward, double itemScale, double bob, boolean world) {
		ensureListener();
		offsetRight = right;
		offsetUp = up;
		offsetForward = forward;
		scale = itemScale;
		bobScale = bob;
		worldItem = world;
	}

	/** Read by CneViewBobMixin (@ModifyExpressionValue on Mth.lerp in GameRenderer.bobView). Clamped [0,1]. */
	public static double bobScaleFactor() {
		double v = bobScale;
		return v < 0.0D ? 0.0D : (v > 1.0D ? 1.0D : v);
	}

	/** True when world-item first-person mode is on for the local player. Read by the client listeners. */
	public static boolean worldItemEnabled() {
		return worldItem;
	}

	// Lazy client-only registration: the Class.forName guard means the anonymous listeners (and the nested
	// FirstPersonAccess class they call into) never load on a dedicated server. Both first-person listeners live here.
	private static void ensureListener() {
		if (listenerRegistered) return;
		try {
			Class.forName("net.minecraft.client.Minecraft");
			listenerRegistered = true;
			net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void onRenderHand(net.neoforged.neoforge.client.event.RenderHandEvent event) {
					FirstPersonAccess.applyRenderHand(event);
				}

				@SubscribeEvent
				public void onRenderLevelStage(net.neoforged.neoforge.client.event.RenderLevelStageEvent event) {
					// World-space arms render: only in world-item mode, once per frame, after entities flush.
					if (!worldItem) return;
					if (event.getStage() != net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
					FirstPersonAccess.renderFirstPersonArms(event);
				}

				@SubscribeEvent
				public void onRenderPlayerPre(net.neoforged.neoforge.client.event.RenderPlayerEvent.Pre event) {
					// Fires inside PlayerRenderer.render AFTER setModelProperties (which setAllVisible(true)s) but
					// BEFORE the body draws - the one window where hiding the non-arm parts actually sticks. Only
					// act during our own arms render (hidingArms), so normal third-person player renders are untouched.
					if (!hidingArms) return;
					net.minecraft.client.model.PlayerModel<net.minecraft.client.player.AbstractClientPlayer> m = event.getRenderer().getModel();
					m.head.visible = false; m.hat.visible = false; m.body.visible = false; m.jacket.visible = false;
					m.leftLeg.visible = false; m.rightLeg.visible = false; m.leftPants.visible = false; m.rightPants.visible = false;
					// arms + sleeves stay as setModelProperties left them (visible); both held items still render via the layer.
				}
			});
		} catch (Throwable ignored) {
		}
	}

	// All client-typed method BODIES live in this nested class, never on the top-level class: even with the
	// Class.forName listener guard, VERIFYING the top-level class's bytecode on a dedicated server forces the
	// verifier to load client classes for its assignability checks (e.g. the LocalPlayer passed to Entity-typed
	// parameters below), which the RuntimeDistCleaner refuses ("invalid dist DEDICATED_SERVER") - the server
	// then crashes during @EventBusSubscriber registration. This nested class only ever loads on the client,
	// behind ensureListener's Class.forName guard.
	private static final class FirstPersonAccess {
		private FirstPersonAccess() {
		}

		static void applyRenderHand(net.neoforged.neoforge.client.event.RenderHandEvent event) {
			try {
				if (event == null || event.getPoseStack() == null) return;
				// World-item ARM mode: hide BOTH flat vanilla viewmodels in first person; the real arms + items are
				// drawn in WORLD space by onRenderLevelStage. We only CANCEL here, never draw. (Both hands, because
				// the world render draws both arms.) RenderHandEvent implements ICancellableEvent (verified).
				if (worldItem
						&& net.minecraft.client.Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
					event.setCanceled(true);
					return;
				}
				// worldItem OFF, or off-hand, or third person: existing offset + scale path, unchanged.
				double r = offsetRight, u = offsetUp, f = offsetForward, s = scale;
				if (r == 0.0D && u == 0.0D && f == 0.0D && s == 1.0D) return;
				com.mojang.blaze3d.vertex.PoseStack pose = event.getPoseStack();
				// PoseStack here: +X = screen-right, +Y = up, +Z = toward camera (so forward = -Z). Mirror the
				// right offset for the off hand so "right" stays player-right for both hands. Translate to the
				// hand first, then scale about the translated origin.
				boolean offHand = event.getHand() == net.minecraft.world.InteractionHand.OFF_HAND;
				pose.translate(offHand ? -r : r, u, -f);
				if (s != 1.0D && s > 0.0D) pose.scale((float) s, (float) s, (float) s);
			} catch (Throwable ignored) {
			}
		}

		// REAL first-person arms: draw the actual third-person player model (BOTH arms + sleeves) and the items in
		// each hand, in WORLD space, posed by the real animation (walk sway from limbSwing, attack arc from the
		// shoulder swing), so it looks exactly like watching yourself in third person. Head/hat/body/jacket/legs/
		// pants are hidden so only the arms show (cloak/ear are private + not in bodyParts(), so renderToBuffer
		// never draws them). Drawn at RenderLevelStageEvent.AFTER_ENTITIES, into the shared bufferSource, flushed
		// ONCE with endBatch() - never the 0.66 per-frame repeated full flush. All client types are FQN-inline;
		// reached only from the lazily-registered listener, so the common class still loads on a dedicated server.
		static void renderFirstPersonArms(net.neoforged.neoforge.client.event.RenderLevelStageEvent event) {
			try {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				net.minecraft.client.player.LocalPlayer player = mc.player;
				if (player == null || mc.level == null) return;
				if (event.getCamera().isDetached()) return;            // third person -> real entity already drawn
				if (!mc.options.getCameraType().isFirstPerson()) return;
				if (mc.getCameraEntity() != player) return;            // spectating another entity

				net.minecraft.client.renderer.entity.EntityRenderer<? super net.minecraft.client.player.LocalPlayer> baseRenderer =
					mc.getEntityRenderDispatcher().getRenderer(player); // getRenderer(T):EntityRenderer<? super T> -- ONE param
				if (!(baseRenderer instanceof net.minecraft.client.renderer.entity.player.PlayerRenderer)) return;
				net.minecraft.client.renderer.entity.player.PlayerRenderer playerRenderer =
					(net.minecraft.client.renderer.entity.player.PlayerRenderer) baseRenderer;

				float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

				net.minecraft.world.phys.Vec3 cam = event.getCamera().getPosition();
				double px = net.minecraft.util.Mth.lerp((double) partialTick, player.xOld, player.getX());
				double py = net.minecraft.util.Mth.lerp((double) partialTick, player.yOld, player.getY());
				double pz = net.minecraft.util.Mth.lerp((double) partialTick, player.zOld, player.getZ());
				float bodyYaw = net.minecraft.util.Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);

				com.mojang.blaze3d.vertex.PoseStack poseStack = event.getPoseStack();
				poseStack.pushPose();
				// hidingArms gates the RenderPlayerEvent.Pre listener, which hides the non-arm parts AFTER
				// PlayerRenderer.setModelProperties (the only window that sticks). Setting visibility directly
				// here would just be wiped by setModelProperties's setAllVisible(true) inside render().
				hidingArms = true;
				try {
					poseStack.translate(px - cam.x(), py - cam.y(), pz - cam.z()); // feet at real world pos, camera-relative
					// TUNE: poseStack.translate(0.0, EYE_FUDGE, 0.0); if the shoulders mis-align to the eye (start 0)

					// FOV COMPENSATION: the arms render in WORLD space (world FOV), so at higher FOV the projection
					// shrinks them and they feel narrow vs the vanilla viewmodel (which uses a stable hand FOV). Scale
					// them up about ~shoulder height by the FOV ratio tan(fov/2)/tan(35deg) so they keep a roughly
					// constant on-screen size across FOV settings. At the default 70 FOV k=1 (no change); at 110, k~2.
					double fovDeg = mc.options.fov().get().doubleValue();
					float k = (float) (Math.tan(Math.toRadians(fovDeg * 0.5)) / Math.tan(Math.toRadians(35.0)));
					if (Float.isFinite(k) && k > 0.0F) {
						float pivot = 1.4F; // TUNE: height in blocks above the feet (~shoulder) the arms scale about
						poseStack.translate(0.0D, pivot, 0.0D);
						poseStack.scale(k, k, k);
						poseStack.translate(0.0D, -pivot, 0.0D);
					}

					net.minecraft.client.renderer.MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
					int packedLight = mc.getEntityRenderDispatcher().getPackedLightCoords(player, partialTick); // TUNE: 15728880 for full-bright

					// One call: setupAnim (walk sway + attack swing) + ItemInHandLayer (BOTH items). Both arms + both items, animated.
					// The Pre listener (gated by hidingArms) hides head/body/legs so only the arms + sleeves draw.
					playerRenderer.render(player, bodyYaw, partialTick, poseStack, buffers, packedLight);

					buffers.endBatch(); // ONE flush of our queued shared batch. NOT the 0.66 lag (that was repeated/throwaway flushes).
				} finally {
					hidingArms = false;
					poseStack.popPose(); // balanced with pushPose
				}
			} catch (Throwable ignored) {
			}
		}
	}

	// ---- helpers ----

	private static double clamp(double value, double min, double max) {
		if (!Double.isFinite(value)) return min > 0.0D ? min : 0.0D;
		return Math.max(min, Math.min(max, value));
	}

	private static Entity localPlayer() {
		try {
			Object mc = Class.forName("net.minecraft.client.Minecraft").getMethod("getInstance").invoke(null);
			Object player = mc.getClass().getField("player").get(mc);
			return player instanceof Entity entity ? entity : null;
		} catch (Throwable ignored) {
			return null;
		}
	}

	@SubscribeEvent
	public static void registerPayload(RegisterPayloadHandlersEvent event) {
		event.registrar("euru").optional().playToClient(FirstPersonMessage.TYPE, FirstPersonMessage.STREAM_CODEC, FirstPersonMessage::handleData);
	}

	public record FirstPersonMessage(double right, double up, double forward, double scale, double bobScale, boolean worldItem) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<FirstPersonMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_first_person"));
		public static final StreamCodec<RegistryFriendlyByteBuf, FirstPersonMessage> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
			buffer.writeDouble(message.right);
			buffer.writeDouble(message.up);
			buffer.writeDouble(message.forward);
			buffer.writeDouble(message.scale);
			buffer.writeDouble(message.bobScale);
			buffer.writeBoolean(message.worldItem);
		}, buffer -> new FirstPersonMessage(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean()));

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(FirstPersonMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.CLIENTBOUND) return;
			context.enqueueWork(() -> applyLocal(message.right(), message.up(), message.forward(), message.scale(), message.bobScale(), message.worldItem()));
		}
	}
}
