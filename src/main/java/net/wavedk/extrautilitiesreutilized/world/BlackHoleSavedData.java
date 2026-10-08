package net.wavedk.extrautilitiesreutilized.world;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class BlackHoleSavedData extends SavedData {
	private static final String DATA_NAME = "black_hole_manager_data";
	private final ConcurrentHashMap<BlackHoleManager.BlackHoleKey, BlackHoleManager.BlackHoleData> blackHoles = new ConcurrentHashMap<>();
	public static final SavedData.Factory<BlackHoleSavedData> FACTORY = new SavedData.Factory<>(BlackHoleSavedData::new, BlackHoleSavedData::load, null);

	public static BlackHoleSavedData get(ServerLevel level) {
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
		if (overworld == null)
			overworld = level;
		return overworld.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
	}

	public BlackHoleSavedData() {
	}

	public ConcurrentHashMap<BlackHoleManager.BlackHoleKey, BlackHoleManager.BlackHoleData> getBlackHoles() {
		return blackHoles;
	}

	public static BlackHoleSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
		BlackHoleSavedData data = new BlackHoleSavedData();
		ListTag list = tag.getList("BlackHoles", Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			CompoundTag bhTag = list.getCompound(i);
			String dimStr = bhTag.contains("Dimension") ? bhTag.getString("Dimension") : "minecraft:overworld";
			long posLong = bhTag.getLong("Pos");
			ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(dimStr.isEmpty() ? "minecraft:overworld" : dimStr));
			BlockPos pos = BlockPos.of(posLong);
			int stackSize = bhTag.contains("StackSize") ? bhTag.getInt("StackSize") : 1;
			double blockRadius = bhTag.contains("BlockRadius") ? bhTag.getDouble("BlockRadius") : 10.0;
			boolean pullEntities = !bhTag.contains("PullEntities") || bhTag.getBoolean("PullEntities");
			double entityRadius = bhTag.contains("EntityRadius") ? bhTag.getDouble("EntityRadius") : 15.0;
			double power = bhTag.contains("Power") ? bhTag.getDouble("Power") : 1.0;
			boolean killEntities = !bhTag.contains("KillEntities") || bhTag.getBoolean("KillEntities");
			BlackHoleManager.BlackHoleKey key = new BlackHoleManager.BlackHoleKey(dim, pos);
			BlackHoleManager.BlackHoleData bhData = new BlackHoleManager.BlackHoleData(key, stackSize, blockRadius, pullEntities, entityRadius, power, killEntities);
			data.blackHoles.put(key, bhData);
		}
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		ListTag list = new ListTag();
		for (Map.Entry<BlackHoleManager.BlackHoleKey, BlackHoleManager.BlackHoleData> entry : blackHoles.entrySet()) {
			CompoundTag bhTag = new CompoundTag();
			bhTag.putString("Dimension", entry.getKey().dimension().location().toString());
			bhTag.putLong("Pos", entry.getKey().pos().asLong());
			bhTag.putInt("StackSize", entry.getValue().stackSize());
			bhTag.putDouble("BlockRadius", entry.getValue().blockRadius());
			bhTag.putBoolean("PullEntities", entry.getValue().pullEntities());
			bhTag.putDouble("EntityRadius", entry.getValue().entityRadius());
			bhTag.putDouble("Power", entry.getValue().power());
			bhTag.putBoolean("KillEntities", entry.getValue().killEntities());
			list.add(bhTag);
		}
		tag.put("BlackHoles", list);
		return tag;
	}
}