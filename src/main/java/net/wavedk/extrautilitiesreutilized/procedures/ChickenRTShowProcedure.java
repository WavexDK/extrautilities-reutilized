package net.wavedk.extrautilitiesreutilized.procedures;

import java.util.List;

public class ChickenRTShowProcedure {
	public static boolean execute(List<String> strings) {
		if (strings == null)
			return false;
		boolean correctEntity = false;
		if (strings != null) {
			for (String stringiterator : strings) {
				if ((stringiterator).equals("chicken")) {
					correctEntity = true;
				}
			}
		}
		return correctEntity;
	}
}