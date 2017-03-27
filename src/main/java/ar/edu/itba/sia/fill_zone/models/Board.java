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
	 * The amount of colorsCode in the pallet.
	 */
	private final int colors;

	/**
	 * The board matrix.
	 */
	private final int[][] board;

	/**
	 * Counts the amount of colorsCode in the board.
	 */
	private final int[] amountOfEachColor;


	//colorsCode that will be used to print the board in the standard output



	/**
	 * Constructor.
	 *
	 * @param rows    The amount of rows.
	 * @param columns The amount of columns.
	 * @param colors  The amount of colorsCode in the pallet.
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


	/**
	 * Prints Board, if the board has less than 9 colors then it will print it with colors, if not it will print it in black
	 */
	public void printBoard(){

		for(int i=0; i<colorsCode.values().length; i++){
			System.out.println(colorsCode.values()[i] + "holaholahola " + i + colorsCode.RESET);
		}

		if(colors > 9){
			for(int i=0; i < rows; i ++){
				for(int j=0; j<columns; j++){
					if(board[i][j] <= 9)
						System.out.println(" " + board[i][j]);
					else
						System.out.println(board[i][j]);
				}
				System.out.println();
			}

		}else{
			for(int i=0; i<rows; i++){
				for(int j=0; j<columns; j++){
					System.out.print(colorsCode.values()[board[i][j]] + "" + board[i][j] + "\t" + colorsCode.RESET);
				}
				System.out.println();
			}


		}
	}

	/**
	 * @return the first color in the board, top left.
	 */
	public int startingColor() {
		return board[0][0];
	}


	/**
	 * Updates the color in a specific locker of the board
	 * @param row
	 * @param column
	 * @param color
	 */
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


	private enum colorsCode {
		RESET("\u001B[0m"),
		ONE("\u001B[31m"),
		TWO("\u001B[36m"),
		THREE("\u001B[34m"),
		FOUR("\u001B[35m"),
		FIVE("\u001B[38m"),
		SIX("\u001B[33m"),
		SEVEN("\u001B[32m"),
		EIGHT("\u001B[38m"),
		NINE("\u001B[30m"),
		;

		private final String color;

		private colorsCode(String colorI){
			color = colorI;
		}

		public String toString(){
			return this.color;
		}

	}
}
