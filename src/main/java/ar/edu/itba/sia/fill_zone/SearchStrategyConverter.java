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
			StringBuilder str = new StringBuilder().append("Possible values are ");
			int i = 0;
			List<String> values = Arrays.stream(SearchStrategy.values())
					.map(each -> each.toString().toLowerCase())
					.collect(Collectors.toList());
			str.append(values.get(i++));
			while (i < values.size()) {
				str.append(", ").append(values.get(i++));
			}
			throw new ParameterException(str.toString(), e);
		}
	}
}


