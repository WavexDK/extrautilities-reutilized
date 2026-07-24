package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * EXPERIMENTAL client half of bone tracking. After each living entity renders (model already posed),
 * read the requested bones' world positions off the live ModelPart tree and ship them to the server
 * once per tick so bone-tracked hit-zones can follow the real animation. Standard ModelPart models
 * only (no GeckoLib). See {@link CneBoneSyncRuntime}.
 */
@EventBusSubscriber(modid = "euru", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class CneBoneSyncClient {
	// hostId -> (boneName -> {worldX, worldY, worldZ, yaw, pitch}) captured this frame, flushed each tick.
	private static final Map<Integer, Map<String, float[]>> CAPTURED = new HashMap<>();
	// hostId -> bone names that have a tracked zone, refreshed every ~10 ticks to keep the render hot path cheap.
	private static final Map<Integer, String[]> BONE_CACHE = new HashMap<>();
	private static final Map<Integer, Long> BONE_CACHE_TICK = new HashMap<>();
	private static Field childrenField;

	private CneBoneSyncClient() {
	}

	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
		LivingEntity host = event.getEntity();
		String[] bones = trackedBones(host);
		if (bones.length == 0) return;
		EntityModel<?> model = event.getRenderer().getModel();
		Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		float partial = event.getPartialTick();
		float bodyYaw = Mth.rotLerp(partial, host.yBodyRotO, host.yBodyRot);
		PoseStack ps = event.getPoseStack();
		Map<String, float[]> out = CAPTURED.computeIfAbsent(host.getId(), k -> new HashMap<>());
		for (String bone : bones) {
			List<ModelPart> path = findBonePath(model, bone);
			if (path == null) continue;
			ps.pushPose();
			// Replicate LivingEntityRenderer's model transform, then walk down to the bone.
			ps.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
			ps.scale(-1.0F, -1.0F, 1.0F);
			ps.translate(0.0F, -1.501F, 0.0F);
			for (ModelPart part : path) part.translateAndRotate(ps);
			org.joml.Matrix4f m = ps.last().pose();
			org.joml.Quaternionf q = m.getNormalizedRotation(new org.joml.Quaternionf());
			ps.popPose();
			// The matrix translation is camera-relative; add the camera world position to get the bone's world point.
			double wx = cam.x + m.m30(), wy = cam.y + m.m31(), wz = cam.z + m.m32();
			// Store the bone RELATIVE TO THE HOST in its body-yaw frame (the exact inverse of
			// CneExtrasRuntime.offsetPosition) so reposition is always host.position()+rotate(offset) and the
			// zone rides the host even when unobserved, instead of freezing at this absolute world point. Use the
			// SAME raw yBodyRot basis + host position anchor the reposition uses, so a fresh frame reproduces this
			// exact point. q = the bone's world orientation (the matrix carries no camera rotation, so it IS world).
			double rawYaw = Math.toRadians(host.yBodyRot);
			double fx = -Math.sin(rawYaw), fz = Math.cos(rawYaw);  // forward
			double rx = -fz, rz = fx;                              // right = (-forward.z, 0, forward.x)
			double ddx = wx - host.getX(), ddy = wy - host.getY(), ddz = wz - host.getZ();
			double dx = ddx * rx + ddz * rz;  // delta . right
			double dy = ddy;                  // delta . up
			double dz = ddx * fx + ddz * fz;  // delta . forward
			out.put(bone, new float[]{(float) dx, (float) dy, (float) dz, q.x, q.y, q.z, q.w});
			// Populate the shared cache locally too, so this client's own zone follows without waiting for the round-trip.
			CneBoneSyncRuntime.putLocal(host.getId(), bone, dx, dy, dz, q.x, q.y, q.z, q.w, host.level().getGameTime());
		}
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (CAPTURED.isEmpty()) return;
		// Teardown guard: PacketDistributor.sendToServer dereferences Minecraft.getInstance().getConnection()
		// via Objects.requireNonNull. On "Save and quit to title" the connection is null but a final
		// ClientTickEvent.Post can still fire, so bail (and drop the buffered frame) when there is no connection.
		if (Minecraft.getInstance().getConnection() == null) {
			CAPTURED.clear();
			return;
		}
		for (Map.Entry<Integer, Map<String, float[]>> e : CAPTURED.entrySet()) {
			Map<String, float[]> poses = e.getValue();
			if (poses.isEmpty()) continue;
			List<String> bones = new ArrayList<>(poses.size());
			float[] data = new float[poses.size() * 7];
			int i = 0;
			for (Map.Entry<String, float[]> p : poses.entrySet()) {
				bones.add(p.getKey());
				System.arraycopy(p.getValue(), 0, data, i * 7, 7);
				i++;
			}
			PacketDistributor.sendToServer(new CneBoneSyncRuntime.BonePoseMessage(e.getKey(), bones, data));
		}
		CAPTURED.clear();
	}

	/** On disconnect / "Save and quit", drop all captured client state so nothing leaks across world loads
	 *  or fires a serverbound send during teardown. Client-only (LoggingOut carries a LocalPlayer). */
	@SubscribeEvent
	public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
		CAPTURED.clear();
		BONE_CACHE.clear();
		BONE_CACHE_TICK.clear();
		CneBoneSyncRuntime.clearAll();
	}

	/** Bone names this host has tracked zones for (client-side synced zones), cached for ~10 ticks. */
	private static String[] trackedBones(LivingEntity host) {
		long now = host.level().getGameTime();
		Long last = BONE_CACHE_TICK.get(host.getId());
		String[] cached = BONE_CACHE.get(host.getId());
		if (cached != null && last != null && now - last < 10L) return cached;
		List<String> names = new ArrayList<>();
		for (CneHitZoneEntity z : host.level().getEntitiesOfClass(CneHitZoneEntity.class, host.getBoundingBox().inflate(8.0D),
				z -> z.isBoneTracked() && z.isHostEntity(host))) {
			String b = z.trackedBoneName();
			if (b != null && !b.isEmpty() && !names.contains(b)) names.add(b);
		}
		String[] arr = names.toArray(new String[0]);
		BONE_CACHE.put(host.getId(), arr);
		BONE_CACHE_TICK.put(host.getId(), now);
		return arr;
	}

	/** Path from the top model part down to the named bone (so each part's transform can be applied in order).
	 *  Package-visible so the item-attach renderer ({@link CneBoneItemClient}) can reuse the exact same walk. */
	static List<ModelPart> findBonePath(EntityModel<?> model, String name) {
		List<ModelPart> best = null;
		if (model instanceof HierarchicalModel<?> hm) {
			List<ModelPart> path = new ArrayList<>();
			if (buildPath(hm.root(), name, path)) best = path;
		}
		if (best == null) {
			for (Field f : model.getClass().getFields()) {
				if (f.getType() != ModelPart.class) continue;
				try {
					ModelPart top = (ModelPart) f.get(model);
					List<ModelPart> path = new ArrayList<>();
					if (top != null && buildPath(top, name, path) && (best == null || path.size() > best.size())) best = path;
				} catch (Exception ignored) {
				}
			}
		}
		return best;
	}

	private static boolean buildPath(ModelPart part, String name, List<ModelPart> path) {
		path.add(part);
		Map<String, ModelPart> children = childrenOf(part);
		if (children != null) {
			for (Map.Entry<String, ModelPart> e : children.entrySet()) {
				if (e.getKey().equals(name)) {
					path.add(e.getValue());
					return true;
				}
				if (buildPath(e.getValue(), name, path)) return true;
			}
		}
		path.remove(path.size() - 1);
		return false;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, ModelPart> childrenOf(ModelPart part) {
		try {
			if (childrenField == null) {
				childrenField = ModelPart.class.getDeclaredField("children");
				childrenField.setAccessible(true);
			}
			return (Map<String, ModelPart>) childrenField.get(part);
		} catch (Exception e) {
			return null;
		}
	}
}
