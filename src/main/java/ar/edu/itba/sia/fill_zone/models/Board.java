package ar.edu.itba.sia.fill_zone.models;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Class representing a fill zone board.
 */
public class Board {

	/**
	 * The number of rows of the board.
	 */
	private final int rows;

	/**
	 * The number of columns of the board.
	 */
	private final int columns;

	/**
	 * The amount of colors in the pallet.
	 */
	private final int colors;

	/**
	 * The board matrix.
	 */
	private final int[][] board;

	/**
	 * Counts the amount of colors in the board.
	 */
	private final int[] amountOfEachColor;

	/**
	 * Constructor.
	 *
	 * @param rows    The amount of rows.
	 * @param columns The amount of columns.
	 * @param colors  The amount of colors in the pallet.
	 * @param board   The board matrix.
	 */
	public Board(int rows, int columns, int colors, int[][] board) {
		this.rows = rows;
		this.columns = columns;
		this.colors = colors;
		this.board = board;
		this.amountOfEachColor = new int[colors];

//		IntStream.range(0, rows).forEach(row ->
//				IntStream.range(0, columns).forEach(column ->
//						this.amountOfEachColor[board[row][column]]++));


		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				amountOfEachColor[board[row][col]]++;
			}
		}
	}

	public int startingColor() {
		return board[0][0];
	}

	public void setColorAtLocker(int row, int column, int color) {
		if (color > this.colors || color < 0) {
			throw new IllegalArgumentException();
		}

		amountOfEachColor[board[row][column]]--;
		board[row][column] = color;
		amountOfEachColor[color]++;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int getColors() {
		return colors;
	}

	public int[][] getBoard() {
		return board;
	}

	public int[] getAmountOfEachColor() {
		return amountOfEachColor.clone();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Board that = (Board) o;

		return Arrays.deepEquals(board, that.board);
	}

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(board);
	}
}
