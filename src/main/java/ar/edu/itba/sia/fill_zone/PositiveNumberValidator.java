package ar.edu.itba.sia.fill_zone;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by Juan Marcos Bellini on 4/4/17.
 */
public class PositiveNumberValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		int number;
		try {
			number = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ParameterException("Parameter " + name + " must be an integer.");
		}
		if (number <= 0) {
			throw new ParameterException("Parameter " + name + " must be positive.");
		}
	}
}
