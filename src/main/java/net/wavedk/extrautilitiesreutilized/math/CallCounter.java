package net.wavedk.extrautilitiesreutilized.math;

import java.util.concurrent.ConcurrentHashMap;

public class CallCounter {
	private static final ConcurrentHashMap<String, Long> LAST_EXECUTION = new ConcurrentHashMap<>();

	public static boolean shouldExecute(String id, long intervalTicks, long currentTick) {
		if (intervalTicks <= 1)
			return true;
		Long last = LAST_EXECUTION.get(id);
		if (last == null) {
			LAST_EXECUTION.put(id, currentTick);
			return true;
		}
		if (currentTick - last >= intervalTicks) {
			LAST_EXECUTION.put(id, currentTick);
			return true;
		}
		return false;
	}
}