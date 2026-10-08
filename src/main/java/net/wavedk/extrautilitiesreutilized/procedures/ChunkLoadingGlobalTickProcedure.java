package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.network.EuruModVariables;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ChunkLoadingGlobalTickProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (!("" + EuruModVariables.WorldVariables.get(world).chunkloadingward_old).equals("" + EuruModVariables.WorldVariables.get(world).chunkloadingward)) {
			if (!EuruModVariables.WorldVariables.get(world).chunkloadingward_old.isEmpty()) {
				for (Object arraylistiterator : EuruModVariables.WorldVariables.get(world).chunkloadingward_old) {
					if (world instanceof ServerLevel _serverLevel) {
						_serverLevel.setChunkForced(
								(EuruModVariables.WorldVariables.get(world).chunkloadingward_old.get((int) EuruModVariables.WorldVariables.get(world).chunkloadingward_old.indexOf(arraylistiterator)) instanceof LevelChunk _obj3 ? _obj3 : null)
										.getPos().x,
								(EuruModVariables.WorldVariables.get(world).chunkloadingward_old.get((int) EuruModVariables.WorldVariables.get(world).chunkloadingward_old.indexOf(arraylistiterator)) instanceof LevelChunk _obj3 ? _obj3 : null)
										.getPos().z,
								(true == false));
					}
				}
				for (Object arraylistiterator : EuruModVariables.WorldVariables.get(world).chunkloadingward) {
					if (world instanceof ServerLevel _serverLevel) {
						_serverLevel.setChunkForced(
								(EuruModVariables.WorldVariables.get(world).chunkloadingward.get((int) EuruModVariables.WorldVariables.get(world).chunkloadingward.indexOf(arraylistiterator)) instanceof LevelChunk _obj8 ? _obj8 : null).getPos().x,
								(EuruModVariables.WorldVariables.get(world).chunkloadingward.get((int) EuruModVariables.WorldVariables.get(world).chunkloadingward.indexOf(arraylistiterator)) instanceof LevelChunk _obj8 ? _obj8 : null).getPos().z,
								true);
					}
				}
				EuruModVariables.WorldVariables.get(world).chunkloadingward_old.clear();
				EuruModVariables.WorldVariables.get(world).chunkloadingward_old.addAll(EuruModVariables.WorldVariables.get(world).chunkloadingward);
			} else if (!EuruModVariables.WorldVariables.get(world).chunkloadingward.isEmpty()) {
				EuruModVariables.WorldVariables.get(world).chunkloadingward_old.addAll(EuruModVariables.WorldVariables.get(world).chunkloadingward);
			}
		}
	}
}