package ar.edu.itba.sia.fill_zone.models;


import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

/**
 * A fill zone state.
 */
public class FillZoneState implements GPSState {

	/**
	 * The board according to the state.
	 */
	private final Board board;

	/**
	 * The amount of moves done when this state is reached.
	 */
	private final int moves;

	/**
	 * Constructor.
	 *
	 * @param board The board according to the state.
	 * @param moves The amount of moves done when this state is reached.
	 */
	public FillZoneState(Board board, int moves) {
		this.board = board;
		this.moves = moves;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FillZoneState)) return false;

		FillZoneState that = (FillZoneState) o;

		return board.equals(that.board);

	}

	@Override
	public int hashCode() {
		return board.hashCode();
	}

	/**
	 * Board getter.
	 *
	 * @return The board.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Moves getter.
	 *
	 * @return The moves.
	 */
	public int getMoves() {
		return moves;
	}
}
