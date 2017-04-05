package ar.edu.itba.sia.fill_zone;

import java.util.List;

/**
 * Created by Juan Marcos Bellini on 5/4/17.
 */
public class EnumValuesGetter {

	public static String getValues(List<String> possibleValues, String name) {

		int i = 0;
		StringBuilder stringBuilder = new StringBuilder()
				.append("Parameter ").append(name).append(" must be one of: [")
				.append(possibleValues.get(i++));
		while (i < possibleValues.size()) {
			stringBuilder.append(", ").append(possibleValues.get(i++));
		}
		stringBuilder.append("].");
		return stringBuilder.toString();

	}

}
