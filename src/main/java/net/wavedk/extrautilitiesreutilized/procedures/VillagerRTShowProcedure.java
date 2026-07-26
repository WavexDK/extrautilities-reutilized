package net.wavedk.extrautilitiesreutilized.procedures;

import java.util.List;

public class VillagerRTShowProcedure {
	public static boolean execute(List<String> strings) {
		if (strings == null)
			return false;
		boolean correctEntity = false;
		if (strings != null) {
			for (String stringiterator : strings) {
				if ((stringiterator).equals("villager")) {
					correctEntity = true;
				}
			}
		}
		return correctEntity;
	}
}