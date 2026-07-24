package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * EXPERIMENTAL client-rendered weapon trail. The held item's real rendered transform only exists on
 * the client (inside ItemInHandRenderer), so the server just broadcasts which entities have a trail
 * (CneExtrasRuntime.fxWeaponTrail) and the CneItemInHandTrailMixin feeds the live item matrix here;
 * we draw the trail along the ACTUAL blade instead of a bounding-box guess.
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneWeaponTrailClient {
	private static final Map<Integer, Trail> TRAILS = new ConcurrentHashMap<>();

	private CneWeaponTrailClient() {
	}

	private static final class Trail {
		final ParticleOptions particle;
		final float bladeLength;
		final int density;
		final boolean mainHand;
		final boolean firstPerson;
		long expireGameTime;
		// Latest hand world point + blade direction captured during render; the particles are spawned during
		// the client TICK (not during render), so spawning never races the render thread or shader mods.
		double handX, handY, handZ;
		double bladeX, bladeY, bladeZ;
		long captureTick = Long.MIN_VALUE;

		Trail(ParticleOptions particle, float bladeLength, int density, boolean mainHand, boolean firstPerson, long expireGameTime) {
			this.particle = particle;
			this.bladeLength = bladeLength;
			this.density = density;
			this.mainHand = mainHand;
			this.firstPerson = firstPerson;
			this.expireGameTime = expireGameTime;
		}
	}

	/** Called by CneItemInHandTrailMixin each time a held item is rendered (third-person). */
	public static void onItemRendered(LivingEntity entity, ItemDisplayContext ctx, Matrix4f pose) {
		if (FMLEnvironment.dist != Dist.CLIENT || entity == null || pose == null || ctx == null) return;
		if (TRAILS.isEmpty()) return;
		Trail trail = TRAILS.get(entity.getId());
		if (trail == null) return;

		Minecraft mc = Minecraft.getInstance();
		if (mc == null || mc.level == null) return;

		long now = mc.level.getGameTime();
		if (now > trail.expireGameTime) {
			TRAILS.remove(entity.getId());
			return;
		}

		if (ctx != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND && ctx != ItemDisplayContext.THIRD_PERSON_LEFT_HAND) return;
		boolean renderingMain = ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
		boolean wantMain = trail.mainHand == (entity.getMainArm() == HumanoidArm.RIGHT);
		if (renderingMain != wantMain) return;

		// CAPTURE ONLY - do NOT spawn particles here. renderItem runs on the render thread mid-render, and
		// spawning particles there can race the particle engine / shader mods (Photon/Sodium). Just store the
		// hand point + blade direction; ClientEvents.onClientTick spawns them at the safe time (the tick).
		// The matrix is camera-relative (no camera rotation), so cam + (m30,m31,m32) is the hand world point
		// and getNormalizedRotation is the item world orientation. Blade = item local +Y.
		Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
		Quaternionf rot = pose.getNormalizedRotation(new Quaternionf());
		Vector3f blade = rot.transform(new Vector3f(0.0F, 1.0F, 0.0F));
		if (!blade.isFinite()) return;
		trail.handX = cam.x + pose.m30();
		trail.handY = cam.y + pose.m31();
		trail.handZ = cam.z + pose.m32();
		trail.bladeX = blade.x();
		trail.bladeY = blade.y();
		trail.bladeZ = blade.z();
		trail.captureTick = now;
	}

	private static void apply(WeaponTrailMessage message) {
		Minecraft mc = Minecraft.getInstance();
		if (mc == null || mc.level == null) return;
		if (!message.active() || message.particle() == null || message.ticks() <= 0) {
			TRAILS.remove(message.entityId());
			return;
		}
		long expire = mc.level.getGameTime() + Math.max(1, message.ticks());
		TRAILS.put(message.entityId(),
			new Trail(message.particle(), message.bladeLength(), Math.max(2, message.density()), message.mainHand(), message.firstPerson(), expire));
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		event.registrar("euru").playToClient(WeaponTrailMessage.TYPE, WeaponTrailMessage.STREAM_CODEC, WeaponTrailMessage::handleData);
	}

	@EventBusSubscriber(modid = "euru", value = Dist.CLIENT)
	public static final class ClientEvents {
		@SubscribeEvent
		public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
			TRAILS.clear();
		}

		// Spawn the captured trails HERE, on the client TICK - the safe place to add particles (never during
		// render). Each visible trail spawns once per tick along its last-captured blade; trails for entities
		// not being rendered (no fresh capture) are skipped, and expired trails are dropped.
		@SubscribeEvent
		public static void onClientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
			if (TRAILS.isEmpty()) return;
			Minecraft mc = Minecraft.getInstance();
			if (mc == null || mc.level == null) return;
			long now = mc.level.getGameTime();
			// In first person the local player's OWN held item is the camera-attached viewmodel, which has no
			// world position; the REAL held item sits at the player's hand. So for the local player while
			// first-person, compute the hand camera-free from geometry (handPosition/weaponDirection) instead
			// of the render capture. Other entities + the third-person view use the captured real rendered spot.
			boolean firstPersonSelf = mc.options.getCameraType().isFirstPerson();
			int localId = mc.player != null ? mc.player.getId() : Integer.MIN_VALUE;
			java.util.Iterator<Map.Entry<Integer, Trail>> it = TRAILS.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Trail> entry = it.next();
				Trail trail = entry.getValue();
				if (now > trail.expireGameTime) {
					it.remove();
					continue;
				}
				double hx, hy, hz, dx, dy, dz;
				if (firstPersonSelf && mc.player != null && localId == entry.getKey()) {
					if (!trail.firstPerson) continue; // first-person trail disabled on this block
					// No item in the swinging hand -> no trail. The third-person path is gated implicitly (the
					// capture mixin only fires while the item actually renders), but this geometry path draws
					// from the hand position regardless, so it must check the held item itself.
					net.minecraft.world.item.ItemStack held = trail.mainHand ? mc.player.getMainHandItem() : mc.player.getOffhandItem();
					if (held.isEmpty()) continue;
					Vec3 hp = CneExtrasRuntime.handPosition(mc.player, trail.mainHand);
					Vec3 wd = CneExtrasRuntime.weaponDirection(mc.player);
					hx = hp.x; hy = hp.y; hz = hp.z;
					dx = wd.x; dy = wd.y; dz = wd.z;
				} else {
					if (now - trail.captureTick > 2L) continue; // host not rendered recently -> nothing to draw
					hx = trail.handX; hy = trail.handY; hz = trail.handZ;
					dx = trail.bladeX; dy = trail.bladeY; dz = trail.bladeZ;
				}
				float len = trail.bladeLength;
				int n = Math.max(2, trail.density);
				for (int i = 0; i < n; i++) {
					float t = i / (float) (n - 1);
					mc.level.addParticle(trail.particle,
						hx + dx * len * t,
						hy + dy * len * t,
						hz + dz * len * t,
						0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	public record WeaponTrailMessage(int entityId, ParticleOptions particle, float bladeLength, int density,
			int ticks, boolean mainHand, boolean active, boolean firstPerson) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<WeaponTrailMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_weapon_trail"));

		// 8 fields exceed StreamCodec.composite's max arity (6 / Function6), so encode/decode manually.
		// ParticleTypes.STREAM_CODEC is StreamCodec<RegistryFriendlyByteBuf, ParticleOptions> (verified).
		public static final StreamCodec<RegistryFriendlyByteBuf, WeaponTrailMessage> STREAM_CODEC = StreamCodec.of(
			(buffer, message) -> {
				buffer.writeVarInt(message.entityId());
				ParticleTypes.STREAM_CODEC.encode(buffer, message.particle());
				buffer.writeFloat(message.bladeLength());
				buffer.writeVarInt(message.density());
				buffer.writeVarInt(message.ticks());
				buffer.writeBoolean(message.mainHand());
				buffer.writeBoolean(message.active());
				buffer.writeBoolean(message.firstPerson());
			},
			buffer -> {
				int entityId = buffer.readVarInt();
				ParticleOptions particle = ParticleTypes.STREAM_CODEC.decode(buffer);
				float bladeLength = buffer.readFloat();
				int density = buffer.readVarInt();
				int ticks = buffer.readVarInt();
				boolean mainHand = buffer.readBoolean();
				boolean active = buffer.readBoolean();
				boolean firstPerson = buffer.readBoolean();
				return new WeaponTrailMessage(entityId, particle, bladeLength, density, ticks, mainHand, active, firstPerson);
			});

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(WeaponTrailMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.CLIENTBOUND) return;
			context.enqueueWork(() -> {
				if (FMLEnvironment.dist != Dist.CLIENT) return;
				apply(message);
			});
		}
	}
}
