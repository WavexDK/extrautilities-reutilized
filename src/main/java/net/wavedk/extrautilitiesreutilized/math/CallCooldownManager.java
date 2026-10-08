package net.wavedk.extrautilitiesreutilized.math;

import java.util.concurrent.ConcurrentHashMap;

public class CallCooldownManager {
	private static final ConcurrentHashMap<String, Long> LAST_EXECUTION = new ConcurrentHashMap<>();

	public static boolean shouldExecute(String key, long intervalTicks, long currentTick) {
		if (intervalTicks <= 0)
			return true;
		Long last = LAST_EXECUTION.get(key);

		if (last == null || (currentTick - last) >= intervalTicks) {
			LAST_EXECUTION.put(key, currentTick);
			return true;
		}
		return false;
	}
}