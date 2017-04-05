package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.solver.engine.SearchStrategy;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts a string into a {@link SearchStrategy}.
 */
public class SearchStrategyConverter implements IStringConverter<SearchStrategy> {

	@Override
	public SearchStrategy convert(String value) {
		try {

			return SearchStrategy.fromString(value);
		} catch (IllegalArgumentException e) {
			List<String> possibleValues = Arrays.stream(SearchStrategy.values()).map(SearchStrategy::toString)
					.collect(Collectors.toList());
			throw new ParameterException(EnumValuesGetter.getValues(possibleValues, "-S"));
		}
	}
}


