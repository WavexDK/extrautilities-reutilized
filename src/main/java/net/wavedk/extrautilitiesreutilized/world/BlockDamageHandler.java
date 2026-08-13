package net.wavedk.extrautilitiesreutilized.world;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber
public class BlockDamageHandler {
	private record DamageData(double currentDamage, long lastDamageTick, int recoverTicks, boolean dropItems) {
	}

	private static final ConcurrentHashMap<Long, DamageData> DAMAGED_BLOCKS = new ConcurrentHashMap<>();

	public static void applyDamage(ServerLevel level, BlockPos pos, double addedDamage, int recoverSeconds, boolean dropItems) {
		long posKey = pos.asLong();
		long currentTick = level.getGameTime();
		int recoverTicks = recoverSeconds * 20;
		int breakerId = (int) (posKey ^ (posKey >>> 32));
		DamageData existing = DAMAGED_BLOCKS.get(posKey);
		double totalDamage = addedDamage;
		if (existing != null) {
			totalDamage += existing.currentDamage();
		}
		if (totalDamage >= 100.0) {
			DAMAGED_BLOCKS.remove(posKey);
			level.destroyBlockProgress(breakerId, pos, -1);
			level.destroyBlock(pos, dropItems);
		} else {
			DAMAGED_BLOCKS.put(posKey, new DamageData(totalDamage, currentTick, recoverTicks, dropItems));
			int progress = (int) Math.min(9, Math.max(0, Math.floor((totalDamage / 100.0) * 10.0)));
			level.destroyBlockProgress(breakerId, pos, progress);
		}
	}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		if (!(event.getLevel() instanceof ServerLevel level))
			return;
		long currentTick = level.getGameTime();
		DAMAGED_BLOCKS.forEach((posKey, data) -> {
			if (data.recoverTicks() > 0 && (currentTick - data.lastDamageTick()) >= data.recoverTicks()) {
				BlockPos pos = BlockPos.of(posKey);
				DAMAGED_BLOCKS.remove(posKey);
				int breakerId = (int) (posKey ^ (posKey >>> 32));
				level.destroyBlockProgress(breakerId, pos, -1);
			}
		});
	}
}