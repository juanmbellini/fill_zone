package ar.edu.itba.sia.fill_zone.models;


import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.Arrays;

public class FillZoneState implements GPSState {

	private Board board;

	private int moves;

	public FillZoneState(Board board, int moves) {
		this.board = board;
		this.moves = moves;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FillZoneState that = (FillZoneState) o;

		return Arrays.deepEquals(board.getMatrix(), that.getBoard().getMatrix());
	}

	public Board getBoard() {
		return board;
	}

	public int getMoves() {
		return moves;
	}
}
