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
public class EnumValidator<T extends Enum> implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		List<String> possibleValues = Arrays.stream(SearchStrategy.values()).map(SearchStrategy::toString)
				.collect(Collectors.toList());
		if (!possibleValues.contains(name)) {
			int i = 0;
			StringBuilder stringBuilder = new StringBuilder()
					.append("Parameter ").append(name).append(" must be one of: [")
					.append(possibleValues.get(i++));
			while (i < possibleValues.size()) {
				stringBuilder.append(", ").append(possibleValues.get(i++));
			}
			stringBuilder.append("].");
			throw new ParameterException(stringBuilder.toString());
		}
	}
}
