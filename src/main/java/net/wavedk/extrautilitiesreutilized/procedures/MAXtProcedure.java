package net.wavedk.extrautilitiesreutilized.procedures;

import java.util.List;

public class MAXtProcedure {
	public static String execute(List<String> strings) {
		if (strings == null)
			return "";
		String cString = "";
		cString = "[ NATURAL MAX: ?? ]";
		if (strings != null) {
			for (String stringiterator : strings) {
				if (stringiterator.contains("NATURAL MAX")) {
					cString = stringiterator;
					break;
				}
			}
		}
		return cString;
	}
}