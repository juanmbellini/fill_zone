package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import com.beust.jcommander.IStringConverter;

/**
 * Converts a string into a {@link FillZoneHeuristic}.
 */
public class FillZoneHeuristicConverter implements IStringConverter<FillZoneHeuristic> {

	@Override
	public FillZoneHeuristic convert(String value) {
		return FillZoneHeuristic.fromString(value);
	}
}
