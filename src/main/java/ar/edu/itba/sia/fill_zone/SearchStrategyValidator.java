package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.solver.engine.SearchStrategy;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 5/4/17.
 */
public class SearchStrategyValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		List<String> possibleValues = Arrays.stream(SearchStrategy.values()).map(SearchStrategy::toString)
				.collect(Collectors.toList());
		if (!possibleValues.contains(value)) {
			throw new ParameterException(EnumValuesGetter.getValues(possibleValues, name));
		}
	}
}
