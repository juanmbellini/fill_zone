package ar.edu.itba.sia.fill_zone.io;

import ar.edu.itba.sia.fill_zone.models.Board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public static Board readBoard(String path) throws IOException {
		final Stream<String> fileStream = Files.lines(Paths.get(path));
		List<String> lines = fileStream.collect(Collectors.toList());
		final int rows = Integer.parseInt(lines.remove(0));
		final int columns = Integer.parseInt(lines.remove(0));
		final int colors = Integer.parseInt(lines.remove(0));
		if (rows < 0 || columns < 0 || colors < 0) {
			throw new IllegalArgumentException();
		}
		lines.remove(0); // Removes blank line

		// Checks if amount of rows is the specified in headers.
		if (lines.size() != rows) {
			throw new IllegalArgumentException();
		}
		final int[][] matrix = new int[rows][columns];

		IntStream.range(0, rows).forEach(row -> {
			final String[] line = lines.remove(0).split(" ");
			// Checks if the amount of columns is the specified in headers.
			if (line.length != columns) {
				throw new IllegalArgumentException();
			}
			IntStream.range(0, columns).forEach(col -> {
				int color = Integer.parseInt(line[col]);
				// Checks that the color is not greater than the specified in headers or negative.
				if (color >= colors || colors < 0) {
					throw new IllegalArgumentException();
				}
				matrix[row][col] = color;
			});
		});
		return new Board(rows, columns, colors, matrix);
	}

}
