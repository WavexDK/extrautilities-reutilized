package net.wavedk.extrautilitiesreutilized.procedures;

import java.util.List;

public class ShowDE2Procedure {
	public static boolean execute(List<String> strings) {
		if (strings == null)
			return false;
		String cString = "";
		double cNum = 0;
		cNum = 2;
		if (strings != null) {
			for (String stringiterator : strings) {
				if (!stringiterator.contains("NATURAL MAX")) {
					cString = stringiterator;
				}
			}
		}
		if (new Object() {
			double convert(String s) {
				try {
					return Double.parseDouble(s.trim());
				} catch (Exception e) {
				}
				return 0;
			}
		}.convert(cString) >= cNum) {
			return true;
		}
		return false;
	}
}