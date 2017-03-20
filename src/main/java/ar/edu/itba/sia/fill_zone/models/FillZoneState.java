package ar.edu.itba.sia.fill_zone.models;


import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.Arrays;

public class FillZoneState implements GPSState {

	private int[][] board;

	private int moves;

	public FillZoneState(int[][] board, int moves) {
		this.board = board;
		this.moves = moves;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FillZoneState that = (FillZoneState) o;

		return Arrays.deepEquals(board, that.board);

	}

	public int startingColor() {
		return board[0][0];
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int getMoves() {
		return moves;
	}
}
