package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * Converts a string into a {@link FillZoneHeuristic}.
 */
public class FillZoneHeuristicConverter implements IStringConverter<FillZoneHeuristic> {

	@Override
	public FillZoneHeuristic convert(String value) {
		try {
			return FillZoneHeuristic.fromString(value);
		} catch (IllegalArgumentException e) {
			throw new ParameterException("Possible values are remaining-groups, remaining-lockers, remaining-colors, " +
					"two-colors, combined or max-admissible", e);
		}

	}
}
