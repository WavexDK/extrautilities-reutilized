package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Lifts the vanilla structure block 48-block size limit to {@link #MAX_SIZE}.
 *
 * The vanilla ServerboundSetStructureBlockPacket stores offset/size as single
 * BYTES and clamps them to 48 while reading, so a bigger size can never survive
 * the trip from the edit screen to the server. The CneStructureScreenMixin
 * cancels the vanilla send and routes the full integer values through this
 * payload instead; the server side below replicates exactly what the vanilla
 * packet handler does, minus the clamps. CneStructureBlockLimitMixin lifts the
 * matching clamps in StructureBlockEntity.loadAdditional so the values also
 * survive chunk reloads.
 */
@EventBusSubscriber(modid = "euru", bus = EventBusSubscriber.Bus.MOD)
public final class CneStructureBlockRuntime {
	public static final int MAX_SIZE = 580;

	private CneStructureBlockRuntime() {
	}

	@SubscribeEvent
	public static void registerPayload(RegisterPayloadHandlersEvent event) {
		event.registrar("euru").playToServer(StructureBlockMessage.TYPE, StructureBlockMessage.STREAM_CODEC, StructureBlockMessage::handleData);
	}

	/** Called from the StructureBlockEditScreen mixin in place of the vanilla packet send. */
	public static void sendFromScreen(StructureBlockEntity structure, StructureBlockEntity.UpdateType updateType,
			String name, String posX, String posY, String posZ, String sizeX, String sizeY, String sizeZ,
			String integrity, String seed, String data) {
		PacketDistributor.sendToServer(new StructureBlockMessage(
			structure.getBlockPos(), updateType.name(), structure.getMode().name(), name,
			parseInt(posX), parseInt(posY), parseInt(posZ),
			parseInt(sizeX), parseInt(sizeY), parseInt(sizeZ),
			structure.getMirror().name(), structure.getRotation().name(), data == null ? "" : data,
			structure.isIgnoreEntities(), structure.getShowAir(), structure.getShowBoundingBox(),
			parseFloat(integrity), parseLong(seed)));
	}

	private static int parseInt(String value) {
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static float parseFloat(String value) {
		try {
			return Float.parseFloat(value.trim());
		} catch (Exception ignored) {
			return 1.0F;
		}
	}

	private static long parseLong(String value) {
		try {
			return Long.parseLong(value.trim());
		} catch (Exception ignored) {
			return 0L;
		}
	}

	private static <T extends Enum<T>> T parseEnum(Class<T> type, String name, T fallback) {
		try {
			return Enum.valueOf(type, name);
		} catch (Exception ignored) {
			return fallback;
		}
	}

	/**
	 * Vanilla detectSize() streams every block position in an 80-block radius,
	 * which cannot scale to {@link #MAX_SIZE}. Instead, walk the block-entity maps
	 * of the loaded chunks in range - corner blocks are block entities, so this
	 * finds them instantly no matter how large the build is.
	 */
	private static boolean detectSizeLarge(ServerLevel level, StructureBlockEntity structure) {
		if (structure.getMode() != StructureMode.SAVE) return false;
		BlockPos center = structure.getBlockPos();
		String name = structure.getStructureName();
		int chunkRadius = (MAX_SIZE >> 4) + 2;
		int centerChunkX = center.getX() >> 4;
		int centerChunkZ = center.getZ() >> 4;
		List<BlockPos> corners = new ArrayList<>();
		for (int cx = centerChunkX - chunkRadius; cx <= centerChunkX + chunkRadius; cx++) {
			for (int cz = centerChunkZ - chunkRadius; cz <= centerChunkZ + chunkRadius; cz++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
				if (chunk == null) continue;
				for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
					if (blockEntity instanceof StructureBlockEntity corner
							&& corner != structure
							&& corner.getMode() == StructureMode.CORNER
							&& Objects.equals(name, corner.getStructureName())) {
						corners.add(corner.getBlockPos());
					}
				}
			}
		}
		if (corners.isEmpty()) return false;
		BoundingBox box = new BoundingBox(corners.get(0));
		if (corners.size() > 1) {
			for (int i = 1; i < corners.size(); i++) {
				box.encapsulate(corners.get(i));
			}
		} else {
			box.encapsulate(center);
		}
		int sizeX = box.maxX() - box.minX();
		int sizeY = box.maxY() - box.minY();
		int sizeZ = box.maxZ() - box.minZ();
		if (sizeX <= 1 || sizeY <= 1 || sizeZ <= 1) return false;
		structure.setStructurePos(new BlockPos(box.minX() - center.getX() + 1, box.minY() - center.getY() + 1, box.minZ() - center.getZ() + 1));
		structure.setStructureSize(new Vec3i(sizeX - 1, sizeY - 1, sizeZ - 1));
		return true;
	}

	public record StructureBlockMessage(BlockPos pos, String updateType, String mode, String name,
			int offsetX, int offsetY, int offsetZ, int sizeX, int sizeY, int sizeZ,
			String mirror, String rotation, String data,
			boolean ignoreEntities, boolean showAir, boolean showBoundingBox,
			float integrity, long seed) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<StructureBlockMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("euru", "cne_structure_block"));
		public static final StreamCodec<RegistryFriendlyByteBuf, StructureBlockMessage> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
			buffer.writeBlockPos(message.pos);
			buffer.writeUtf(message.updateType);
			buffer.writeUtf(message.mode);
			buffer.writeUtf(message.name);
			buffer.writeInt(message.offsetX);
			buffer.writeInt(message.offsetY);
			buffer.writeInt(message.offsetZ);
			buffer.writeInt(message.sizeX);
			buffer.writeInt(message.sizeY);
			buffer.writeInt(message.sizeZ);
			buffer.writeUtf(message.mirror);
			buffer.writeUtf(message.rotation);
			buffer.writeUtf(message.data, 128);
			buffer.writeBoolean(message.ignoreEntities);
			buffer.writeBoolean(message.showAir);
			buffer.writeBoolean(message.showBoundingBox);
			buffer.writeFloat(message.integrity);
			buffer.writeLong(message.seed);
		}, buffer -> new StructureBlockMessage(buffer.readBlockPos(), buffer.readUtf(), buffer.readUtf(), buffer.readUtf(),
			buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(),
			buffer.readUtf(), buffer.readUtf(), buffer.readUtf(128),
			buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(),
			buffer.readFloat(), buffer.readLong()));

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public static void handleData(StructureBlockMessage message, IPayloadContext context) {
			if (context.flow() != PacketFlow.SERVERBOUND) {
				return;
			}
			context.enqueueWork(() -> {
				if (!(context.player() instanceof ServerPlayer player) || !player.canUseGameMasterBlocks()) {
					return;
				}
				ServerLevel level = player.serverLevel();
				BlockPos pos = message.pos();
				BlockState blockState = level.getBlockState(pos);
				if (!(level.getBlockEntity(pos) instanceof StructureBlockEntity structure)) {
					return;
				}
				structure.setMode(parseEnum(StructureMode.class, message.mode(), StructureMode.DATA));
				structure.setStructureName(message.name());
				structure.setStructurePos(new BlockPos(
					Mth.clamp(message.offsetX(), -MAX_SIZE, MAX_SIZE),
					Mth.clamp(message.offsetY(), -MAX_SIZE, MAX_SIZE),
					Mth.clamp(message.offsetZ(), -MAX_SIZE, MAX_SIZE)));
				structure.setStructureSize(new Vec3i(
					Mth.clamp(message.sizeX(), 0, MAX_SIZE),
					Mth.clamp(message.sizeY(), 0, MAX_SIZE),
					Mth.clamp(message.sizeZ(), 0, MAX_SIZE)));
				structure.setMirror(parseEnum(Mirror.class, message.mirror(), Mirror.NONE));
				structure.setRotation(parseEnum(Rotation.class, message.rotation(), Rotation.NONE));
				structure.setMetaData(message.data());
				structure.setIgnoreEntities(message.ignoreEntities());
				structure.setShowAir(message.showAir());
				structure.setShowBoundingBox(message.showBoundingBox());
				structure.setIntegrity(Mth.clamp(message.integrity(), 0.0F, 1.0F));
				structure.setSeed(message.seed());
				StructureBlockEntity.UpdateType updateType = parseEnum(StructureBlockEntity.UpdateType.class, message.updateType(), StructureBlockEntity.UpdateType.UPDATE_DATA);
				if (structure.hasStructureName()) {
					String structureName = structure.getStructureName();
					if (updateType == StructureBlockEntity.UpdateType.SAVE_AREA) {
						if (structure.saveStructure()) {
							player.displayClientMessage(Component.translatable("structure_block.save_success", structureName), false);
						} else {
							player.displayClientMessage(Component.translatable("structure_block.save_failure", structureName), false);
						}
					} else if (updateType == StructureBlockEntity.UpdateType.LOAD_AREA) {
						if (!structure.isStructureLoadable()) {
							player.displayClientMessage(Component.translatable("structure_block.load_not_found", structureName), false);
						} else if (structure.placeStructureIfSameSize(level)) {
							player.displayClientMessage(Component.translatable("structure_block.load_success", structureName), false);
						} else {
							player.displayClientMessage(Component.translatable("structure_block.load_prepare", structureName), false);
						}
					} else if (updateType == StructureBlockEntity.UpdateType.SCAN_AREA) {
						if (detectSizeLarge(level, structure)) {
							player.displayClientMessage(Component.translatable("structure_block.size_success", structureName), false);
						} else {
							player.displayClientMessage(Component.translatable("structure_block.size_failure"), false);
						}
					}
				} else {
					player.displayClientMessage(Component.translatable("structure_block.invalid_structure_name", message.name()), false);
				}
				structure.setChanged();
				level.sendBlockUpdated(pos, blockState, blockState, 3);
			});
		}
	}
}
