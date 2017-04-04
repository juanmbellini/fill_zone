package ar.edu.itba.sia.fill_zone.io;

import ar.edu.itba.sia.fill_zone.io.exception.WrongBoardFormatException;
import ar.edu.itba.sia.fill_zone.models.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class implementing a method to read boards from a text file.
 * <p>
 * The file must follow the next format:
 * <rows>
 * <columns>
 * <colors>
 * <blank-line>
 * <board-row-1>
 * <board-row-2>
 * ...
 * <board-row-n>
 */
public class BoardReader {

	/**
	 * Reads the board file according to the given path.
	 *
	 * @param path The path to the file.
	 * @return The read board.
	 * @throws IOException               If an I/O error occurs while reading the file.
	 * @throws WrongBoardFormatException If file's format is incorrect.
	 */
	public static Board readBoardFromFile(String path) throws IOException, WrongBoardFormatException {
		return readBoard(Files.lines(Paths.get(path)).collect(Collectors.toList()));
	}

	/**
	 * Load a board according to the given {@code boardNumber} from resources.
	 *
	 * @param boardNumber The board number to load.
	 * @return The loaded board
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public static Board readResourceBoard(int boardNumber) throws IOException {
		InputStream in = BoardReader.class.getResourceAsStream("/Board" + boardNumber + ".brd");
		return readBoard(createStringListFromInputStream(in));
	}


	/**
	 * Creates a board from the given list of lines.
	 *
	 * @param lines The list of {@link String} with the lines representing the board.
	 * @return The created board.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	private static Board readBoard(List<String> lines) throws IOException {
		final int rows = Integer.parseInt(lines.remove(0));
		final int columns = Integer.parseInt(lines.remove(0));
		final int colors = Integer.parseInt(lines.remove(0));
		if (rows < 0 || columns < 0 || colors < 0) {
			throw new WrongBoardFormatException();
		}
		lines.remove(0); // Removes blank line

		// Checks if amount of rows is the specified in headers.
		if (lines.size() != rows) {
			throw new WrongBoardFormatException();
		}
		final int[][] matrix = new int[rows][columns];

		IntStream.range(0, rows).forEach(row -> {
			final String[] line = lines.remove(0).trim().split("\\s+");
			// Checks if the amount of columns is the specified in headers.
			if (line.length != columns) {
				throw new WrongBoardFormatException();
			}
			IntStream.range(0, columns).forEach(col -> {
				int color = Integer.parseInt(line[col]);
				// Checks that the color is not greater than the specified in headers or negative.
				if (color >= colors || colors < 0) {
					throw new WrongBoardFormatException();
				}
				matrix[row][col] = color;
			});

		});

		return new Board(rows, columns, colors, matrix);
	}

	/**
	 * Creates a {@link List} of {@link String} based on the given {@link InputStream}.
	 *
	 * @param in The input stream.
	 * @return The list of strings read as lines from the input stream.
	 * @throws IOException If an I/O error occurs while reading the input stream.
	 */
	private static List<String> createStringListFromInputStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<String> lines = new LinkedList<>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		return lines;

	}

}


