package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts a string into a {@link FillZoneHeuristic}.
 */
public class FillZoneHeuristicConverter implements IStringConverter<FillZoneHeuristic> {

	@Override
	public FillZoneHeuristic convert(String value) {
		try {
			return FillZoneHeuristic.fromString(value);
		} catch (IllegalArgumentException e) {
			List<String> possibleValues = Arrays.stream(FillZoneHeuristic.values()).map(FillZoneHeuristic::toString)
					.collect(Collectors.toList());
			throw new ParameterException(EnumValuesGetter.getValues(possibleValues, "-H"));
		}
	}
}
