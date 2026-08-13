package net.wavedk.extrautilitiesreutilized.world;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class PortalSavedData extends SavedData {
	private static final String DATA_NAME = "teleport_block_portals";
	private final ConcurrentHashMap<TeleportBlockHandler.PortalKey, TeleportBlockHandler.TeleportData> portals = new ConcurrentHashMap<>();
	public static final SavedData.Factory<PortalSavedData> FACTORY = new SavedData.Factory<>(PortalSavedData::new, PortalSavedData::load, null);

	public static PortalSavedData get(ServerLevel level) {
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
		if (overworld == null)
			overworld = level;
		return overworld.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
	}

	public PortalSavedData() {
	}

	public ConcurrentHashMap<TeleportBlockHandler.PortalKey, TeleportBlockHandler.TeleportData> getPortals() {
		return portals;
	}

	public static PortalSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
		PortalSavedData data = new PortalSavedData();

		if (tag.contains("Portals", Tag.TAG_LIST)) {
			ListTag list = tag.getList("Portals", Tag.TAG_COMPOUND);
			for (int i = 0; i < list.size(); i++) {
				CompoundTag pTag = list.getCompound(i);
				String dimStr = pTag.contains("Dimension") ? pTag.getString("Dimension") : "minecraft:overworld";
				long posLong = pTag.getLong("Pos");
				String targetDimStr = pTag.contains("TargetDimension") ? pTag.getString("TargetDimension") : "minecraft:overworld";
				ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(dimStr.isEmpty() ? "minecraft:overworld" : dimStr));
				BlockPos pos = BlockPos.of(posLong);
				ResourceKey<Level> targetDim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(targetDimStr.isEmpty() ? "minecraft:overworld" : targetDimStr));
				double tx = pTag.getDouble("TargetX");
				double ty = pTag.getDouble("TargetY");
				double tz = pTag.getDouble("TargetZ");
				int ticks = pTag.getInt("RequiredTicks");
				int offsetY = pTag.getInt("TriggerOffsetY");
				String soundStr = pTag.getString("Sound");
				SoundEvent sound = soundStr.isEmpty() ? null : BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(soundStr));
				String particleStr = pTag.getString("Particle");
				ParticleOptions particle = null;
				if (!particleStr.isEmpty()) {
					ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particleStr));
					if (type instanceof ParticleOptions opt) {
						particle = opt;
					}
				}
				TeleportBlockHandler.PortalKey key = new TeleportBlockHandler.PortalKey(dim, pos);
				TeleportBlockHandler.TeleportData tData = new TeleportBlockHandler.TeleportData(key, targetDim, tx, ty, tz, sound, particle, ticks, offsetY);
				data.portals.put(key, tData);
			}
		}
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		ListTag list = new ListTag();
		for (Map.Entry<TeleportBlockHandler.PortalKey, TeleportBlockHandler.TeleportData> entry : portals.entrySet()) {
			CompoundTag pTag = new CompoundTag();
			pTag.putString("Dimension", entry.getKey().dimension().location().toString());
			pTag.putLong("Pos", entry.getKey().pos().asLong());
			pTag.putString("TargetDimension", entry.getValue().targetDimension().location().toString());
			pTag.putDouble("TargetX", entry.getValue().targetX());
			pTag.putDouble("TargetY", entry.getValue().targetY());
			pTag.putDouble("TargetZ", entry.getValue().targetZ());
			pTag.putInt("RequiredTicks", entry.getValue().requiredTicks());
			pTag.putInt("TriggerOffsetY", entry.getValue().triggerOffsetY());
			if (entry.getValue().sound() != null) {
				ResourceLocation soundId = BuiltInRegistries.SOUND_EVENT.getKey(entry.getValue().sound());
				if (soundId != null) {
					pTag.putString("Sound", soundId.toString());
				}
			}
			if (entry.getValue().particle() != null) {
				ResourceLocation particleId = BuiltInRegistries.PARTICLE_TYPE.getKey(entry.getValue().particle().getType());
				if (particleId != null) {
					pTag.putString("Particle", particleId.toString());
				}
			}
			list.add(pTag);
		}
		tag.put("Portals", list);
		return tag;
	}
}