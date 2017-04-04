package ar.edu.itba.sia.fill_zone;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by Juan Marcos Bellini on 3/4/17.
 */
public class NumberOfBoardValidator implements IParameterValidator {


	private static final int AMOUNT_OF_BOARDS = 7;

	@Override
	public void validate(String name, String value) throws ParameterException {


		int number;
		try {
			number = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ParameterException("Parameter " + name + " must be an integer.");
		}
		if (number < 1 || number > AMOUNT_OF_BOARDS) {
			throw new ParameterException("Parameter " + name + " must be between 1 and " + AMOUNT_OF_BOARDS + ".");
		}
	}
}
