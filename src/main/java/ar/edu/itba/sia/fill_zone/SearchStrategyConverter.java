package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.solver.engine.SearchStrategy;
import com.beust.jcommander.IStringConverter;

/**
 * Converts a string into a {@link SearchStrategy}.
 */
public class SearchStrategyConverter implements IStringConverter<SearchStrategy> {

	@Override
	public SearchStrategy convert(String value) {
		return SearchStrategy.fromString(value);
	}
}


