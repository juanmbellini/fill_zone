package ar.edu.itba.sia.fill_zone.models;


import ar.edu.itba.sia.fill_zone.models.interfaces.Heuristic;
import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;
import ar.edu.itba.sia.fill_zone.solver.api.GPSRule;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class FillZoneProblem implements GPSProblem {

	/**
	 * The heuristic to be used to solve the problem.
	 */
	private final Heuristic heuristic;

	/**
	 * The initial board of the problem.
	 */
	private final Board initialBoard;

	/**
	 * The rules of the problem.
	 */
	private final List<GPSRule> rules;

	/**
	 * The initial state of the problem.
	 */
	private final GPSState initialState;


	//TODO: check if the board is valid

	/**
	 * Constructor.
	 *
	 * @param board     The initial board for the problem.
	 * @param heuristic The heuristic to be used to solve the problem.
	 */
	public FillZoneProblem(Board board, Heuristic heuristic) {
		this.initialBoard = board;
		this.heuristic = heuristic;
		this.rules = new ArrayList<>();
		IntStream.range(0, board.getColors()).forEach(color -> rules.add(new ChangeColorRule(color)));
		this.initialState = new FillZoneState(initialBoard, 0);
	}

	/**
	 * Constructor. Generates a random board according to the given values.
	 *
	 * @param rows      The amount of rows of the randomly generated board.
	 * @param columns   The amount of columns of the randomly generated board.
	 * @param colors    The amount of colors of the randomly generated board.
	 * @param heuristic The heuristic to be used to solve the problem.
	 */
	public FillZoneProblem(int rows, int columns, int colors, Heuristic heuristic) {
		this(Board.random(rows, columns, colors), heuristic);
	}

	@Override
	public GPSState getInitState() {
		return initialState;
	}


	/**
	 * Checks if the board has reached a goal state.
     *
	 * @param state The state to establish if it is a goal state.
	 * @return a boolean indicating if the current state is a goal state.
	 */
	@Override
	public boolean isGoal(GPSState state) {
		FillZoneState fillZoneState = (FillZoneState) state;
		Board board = fillZoneState.getBoard();
		final int[] colors = board.getAmountOfEachColor();
		boolean oneColor = false;

		for(int i = 0; i<colors.length; i++){
			if(colors[i] != 0){
				if(colors[i] == board.getColumns()*board.getRows() && !oneColor){
					oneColor = true;
				}else{
					return false;
				}
			}
		}

		return oneColor;
	}

	/**
	 *
	 * @return a List containing the rules.
	 */
	@Override
	public List<GPSRule> getRules() {
		return rules;
	}


	/**
	 *
	 * @param state The state where the Heuristic should be computed.
	 * @return the heuristic value
	 */
	@Override
	public Integer getHValue(GPSState state) {
		return heuristic.getHValue(state);
	}

}
