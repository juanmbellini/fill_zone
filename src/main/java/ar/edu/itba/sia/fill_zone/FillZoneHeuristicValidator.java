package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 5/4/17.
 */
public class FillZoneHeuristicValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		List<String> possibleValues = Arrays.stream(FillZoneHeuristic.values()).map(FillZoneHeuristic::toString)
				.collect(Collectors.toList());
		if (!possibleValues.contains(value)) {
			throw new ParameterException(EnumValuesGetter.getValues(possibleValues, name));
		}
	}
}
