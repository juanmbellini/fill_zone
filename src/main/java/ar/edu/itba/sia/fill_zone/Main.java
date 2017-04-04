package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.io.BoardReader;
import ar.edu.itba.sia.fill_zone.io.SolutionWriter;
import ar.edu.itba.sia.fill_zone.io.exception.WrongBoardFormatException;
import ar.edu.itba.sia.fill_zone.models.Board;
import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import ar.edu.itba.sia.fill_zone.models.FillZoneProblem;
import ar.edu.itba.sia.fill_zone.solver.engine.GPSEngine;
import ar.edu.itba.sia.fill_zone.solver.engine.SearchStrategy;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Main class!
 */
public class Main {

	private static final String MISSING_INPUT_ERROR_MESSAGE =
			"An input must be specified.";

	private static final String MORE_THAN_ONE_INPUT_ERROR_MESSAGE =
			"Only one input must be specified.";

	private static final String RANDOM_BOARD_PARAMETERS_ERROR_MESSAGE =
			"Rows, Columns and Colors must be specified with random board option.";

	private static final String FILE_BOARD_PARAMETER_ERROR_MESSAGE =
			"A path to a file must be specified with board file option.";

	private static final String STORED_BOARD_PARAMETER_ERROR_MESSAGE =
			"A number of board must be specified with board file option.";

	private static final String MISSING_OUTPUT_ERROR_MESSAGE =
			"An output must be specified.";

	private static final String MORE_THAN_ONE_OUTPUT_ERROR_MESSAGE =
			"Only one output must be specified.";

	private static final String SOLUTION_OUTPUT_PARAMETER_ERROR_MESSAGE =
			"A path to a file must be specified with save solution to file option.";




	/*
	 * Execution parameters
	 */

	@Parameter(names = {"-h", "--heuristic",}, description = "Heuristic to use.")
	private FillZoneHeuristic heuristic = FillZoneHeuristic.REMAINING_COLORS;

	@Parameter(names = {"-s", "--strategy",}, description = "Searching strategy.")
	private SearchStrategy strategy = SearchStrategy.ASTAR;


	@Parameter(names = {"-rB", "--random-board",}, description = "Enable random board option.")
	private boolean random = false;

	@Parameter(names = {"-fB", "--file-board",}, description = "Enable board from file option.")
	private boolean file = false;

	@Parameter(names = {"-sB", "--stored-board",}, description = "Enable stored board option.")
	private boolean stored = false;

	@Parameter(names = {"-cO", "--console",}, description = "Enable output to console option.")
	private boolean console = false;

	@Parameter(names = {"-fO", "--save",}, description = "Enable output to file option.")
	private boolean saveToFile = false;


	@Parameter(names = {"-r", "--rows",}, description = "Amount of rows for the random board.",
			validateWith = PositiveNumberValidator.class)
	private Integer rows = 8;

	@Parameter(names = {"-c", "--columns",}, description = "Amount of columns for the random board.",
			validateWith = PositiveNumberValidator.class)
	private Integer columns = 8;

	@Parameter(names = {"-x", "--colors",}, description = "Amount of colors for the random board.",
			validateWith = PositiveNumberValidator.class)
	private Integer colors = 8;


	@Parameter(names = {"-i", "--input",}, description = "Path to the board file to solve.")
	private String pathToBoard = null;

	@Parameter(names = {"-b", "--board-number",}, description = "Number of board to solve.",
			validateWith = NumberOfBoardValidator.class)
	private Integer boardNumber = null;


	@Parameter(names = {"-o", "--output"}, description = "Path of the solution file.")
	private String solutionFilePath = null;


	public static void main(String[] args) {

		Main main = new Main();
		try {
			new JCommander(main, args);
			main.execute();
		} catch (IOException e) {
			System.err.println("Couldn't open board file in path " + main.boardNumber + ".");
			System.exit(1);
		} catch (WrongBoardFormatException e) {
			System.err.println("Wrong board file format.");
			System.exit(1);
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (URISyntaxException ignored) {

		}
	}


	/**
	 * Executes the program.
	 *
	 * @throws ParameterException        If there are parameters errors.
	 * @throws IOException               If IO error occurs while reading the board file (when option is enabled).
	 * @throws WrongBoardFormatException If board file does has a wrong format.
	 */
	private void execute() throws ParameterException, IOException, WrongBoardFormatException, URISyntaxException {
		globalValidationOfParameters();

		Board board = null;

		if (random) {
			board = Board.random(rows, columns, colors);
		} else if (file) {
			board = BoardReader.readBoardFromFile(pathToBoard);
		} else if (stored) {
			board = BoardReader.readResourceBoard(boardNumber);
		}
		final GPSEngine engine = new GPSEngine(new FillZoneProblem(board, heuristic), strategy);
		engine.findSolution();

		if (console) {
			SolutionWriter.printToConsole(engine);
		} else if (saveToFile) {
			SolutionWriter.printToFile(engine, solutionFilePath);
		}

	}

	/**
	 * Makes a global validation of parameters.
	 *
	 * @throws ParameterException If any error is encountered while checking params.
	 */
	private void globalValidationOfParameters() throws ParameterException {

		// Check at least one input is specified
		if (!inputOptionSpecifiedAsBoolean()) {
			throw new ParameterException(MISSING_INPUT_ERROR_MESSAGE);
		}
		// Check no more than one input is specified
		if ((random && (file || stored)) || (file && stored)) {
			throw new ParameterException(MORE_THAN_ONE_INPUT_ERROR_MESSAGE);
		}

		// Check random board parameters
		if (random && !randomParametersAreSpecified()) {
			throw new ParameterException(RANDOM_BOARD_PARAMETERS_ERROR_MESSAGE);
		}
		// Check file board parameter
		if (file && !boardFileParameterIsSpecified()) {
			throw new ParameterException(FILE_BOARD_PARAMETER_ERROR_MESSAGE);
		}

		// Check stored board parameter
		if (stored && !storedBoardParameterIsSpecified()) {
			throw new ParameterException(FILE_BOARD_PARAMETER_ERROR_MESSAGE);
		}

		// Check at least one output is specified
		if (!outputOptionSpecifiedAsBoolean()) {
			throw new ParameterException(MISSING_OUTPUT_ERROR_MESSAGE);
		}
		// Check no more than one output is specified
		if ((console && saveToFile)) {
			throw new ParameterException(MORE_THAN_ONE_OUTPUT_ERROR_MESSAGE);
		}

		// Check save to file parameters
		if (saveToFile && !outputParameterIsSpecified()) {
			throw new ParameterException(SOLUTION_OUTPUT_PARAMETER_ERROR_MESSAGE);
		}

	}


	/**
	 * Indicates whether any of the input parameters is specified.
	 *
	 * @return {@code true} if at least one input parameters (i.e. random, file or stored) is specified,
	 * or {@code false} otherwise.
	 */
	private boolean inputOptionSpecifiedAsBoolean() {
		return random || file || stored;
	}

	/**
	 * Indicates whether all of the randomly generated board parameters are specified.
	 *
	 * @return {@code true} if all are specified, or {@code false} otherwise.
	 */
	private boolean randomParametersAreSpecified() {
		return !(rows == null || columns == null || colors == null);
	}

	/**
	 * Indicates whether the board file path is specified.
	 *
	 * @return {@code true} if it's specified, or {@code false} otherwise.
	 */
	private boolean boardFileParameterIsSpecified() {
		return pathToBoard != null && !pathToBoard.isEmpty();
	}

	/**
	 * Indicates whether the stored board parameter is specified.
	 *
	 * @return {@code true} if it's specified, or {@code false} otherwise.
	 */
	private boolean storedBoardParameterIsSpecified() {
		return boardNumber != null;
	}

	/**
	 * Indicates whether any of the output parameters is specified.
	 *
	 * @return {@code true} if at least one output parameters (i.e. console or file) is specified,
	 * or {@code false} otherwise.
	 */
	private boolean outputOptionSpecifiedAsBoolean() {
		return console || saveToFile;
	}

	/**
	 * Indicates whether the solution file path is specified.
	 *
	 * @return {@code true} if it's specified, or {@code false} otherwise.
	 */
	private boolean outputParameterIsSpecified() {
		return solutionFilePath != null && !solutionFilePath.isEmpty();
	}


}
