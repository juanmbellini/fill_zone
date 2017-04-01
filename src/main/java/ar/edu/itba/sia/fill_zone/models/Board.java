package ar.edu.itba.sia.fill_zone.models;

import java.awt.*;
import java.util.*;
import java.util.List;
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
	 * The amount of colorsCodes in the pallet.
	 */
	private final int colors;

	/**
	 * The board matrix.
	 */
	private final int[][] matrix;

	/**
	 * Counts the amount of ColorsCode in the board.
	 */
	private final int[] amountOfEachColor;

	/**
	 * Matrix that has true value in the first group lockers and false in the others.
	 */
	private final boolean[][] booleanMatrix;


	/**
	 * Constructor.
	 *
	 * @param rows    The amount of rows.
	 * @param columns The amount of columns.
	 * @param colors  The amount of colors in the pallet.
	 * @param matrix  The board matrix.
	 */
	public Board(int rows, int columns, int colors, int[][] matrix) {
		this.rows = rows;
		this.columns = columns;
		this.colors = colors;
		this.matrix = matrix;
		this.amountOfEachColor = new int[colors];
		this.booleanMatrix = new boolean[rows][columns];

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				amountOfEachColor[matrix[row][col]]++;
			}
		}

		paintBooleanMatrix(0, 0, startingColor(), new ArrayList<>());
	}


	/**
	 * @return the first color in the board, top left.
	 */
	public int startingColor() {
		return matrix[0][0];
	}


	/**
	 * Updates the color in a specific locker of the board
	 *
	 * @param row
	 * @param column
	 * @param color
	 */
	public void setColorAtLocker(int row, int column, int color) {
		if (color > this.colors || color < 0) {
			throw new IllegalArgumentException();
		}

		amountOfEachColor[matrix[row][column]]--;
		matrix[row][column] = color;
		amountOfEachColor[color]++;
	}

	/**
	 * Rows getter.
	 *
	 * @return The amount of rows.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Columns getter.
	 *
	 * @return The amount of columns.
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Colors getter.
	 *
	 * @return The amount of colors.
	 */
	public int getColors() {
		return colors;
	}

	/**
	 * Matrix getter.
	 *
	 * @return The matrix actually representing the board.
	 */
	public int[][] getMatrix() {
		return matrix;
	}

	/**
	 * Amount of each colors getter.
	 *
	 * @return The amount of each color.
	 */
	public int[] getAmountOfEachColor() {
		return amountOfEachColor.clone();
	}

	public Set<Integer> getFrontierColors(){
		paintBooleanMatrix(0, 0, startingColor(), new ArrayList<>());
		Set<Integer> ret = new HashSet<>();
		getFrontierColorsRec(0, 0, ret, new ArrayList<>());
		return ret;
	}

	private void getFrontierColorsRec(int row, int column, Set<Integer> frontierColors, List<Point> checked){
		checked.add(new Point(row, column));

		if(booleanMatrix[row][column]){
			//up
			if (row > 0 && !checked.contains(new Point(row-1, column))) {
				getFrontierColorsRec(row - 1, column, frontierColors, checked);
			}

			//down
			if (row < (rows - 1) && !checked.contains(new Point(row+1, column))) {
				getFrontierColorsRec(row + 1, column, frontierColors, checked);
			}

			//left
			if (column > 0 && !checked.contains(new Point(row, column-1))) {
				getFrontierColorsRec(row, column - 1, frontierColors, checked);
			}

			//right
			if (column < (columns - 1) && !checked.contains(new Point(row, column+1))) {
				getFrontierColorsRec(row, column + 1, frontierColors, checked);
			}
		} else {
			frontierColors.add(matrix[row][column]);
		}
	}


	private void paintBooleanMatrix(int row, int column, int color, List<Point> checked){
		checked.add(new Point(row, column));

		if(matrix[row][column] == color){
			booleanMatrix[row][column] = true;

			//up
			if(row>0 && !checked.contains(new Point(row - 1, column))){
				paintBooleanMatrix(row - 1, column, color, checked);
			}

			//down
			if(row<rows-1 && !checked.contains(new Point(row + 1, column))){
				paintBooleanMatrix(row + 1, column, color, checked);
			}

			//left
			if(column>0 && !checked.contains(new Point(row, column - 1))){
				paintBooleanMatrix(row, column - 1, color, checked);
			}

			//right
			if(column<columns-1 && !checked.contains(new Point(row, column + 1))){
				paintBooleanMatrix(row, column + 1, color, checked);
			}
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Board that = (Board) o;

		return Arrays.deepEquals(matrix, that.matrix);
	}

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(matrix);
	}


	/**
	 * Creates a random board according to the given parameters.
	 *
	 * @param rows    The amount of rows.
	 * @param columns The amount of columns.
	 * @param colors  The amount of colors.
	 * @return The generated board.
	 */
	static Board random(int rows, int columns, int colors) {
		final int[][] board = new int[rows][columns];
		IntStream.range(0, rows)
				.forEach(row -> IntStream.range(0, columns)
						.forEach(col -> board[row][col] = (int) (Math.random() * colors)));
		return new Board(rows, columns, colors, board);
	}


}
