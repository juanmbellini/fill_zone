package ar.edu.itba.sia.fill_zone.models;


import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;
import ar.edu.itba.sia.fill_zone.solver.api.GPSRule;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.ArrayList;
import java.util.List;

public class FillZoneProblem implements GPSProblem {

	private int rows;

	private int columns;

	private int colors;

	List<GPSRule> rules;

	public FillZoneProblem(int rows, int columns, int colors) {
		this.rows = rows;
		this.columns = columns;
		this.colors = colors;

		rules = new ArrayList<>();
		for (int color = 1; color <= colors; color++) {
			rules.add(new ChangeColor(color, rows, columns));
		}
	}

	@Override
	public GPSState getInitState() {
		return new FillZoneState(newRandomBoard(), 0);
	}

	@Override
	public boolean isGoal(GPSState state) {
		FillZoneState fillZoneState = (FillZoneState) state;

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				if (fillZoneState.getBoard()[row][col] != fillZoneState.startingColor()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public List<GPSRule> getRules() {
		return rules;
	}

	//TODO
	@Override
	public Integer getHValue(GPSState state) {
		return null;
	}

	private int[][] newRandomBoard() {
		int[][] board = new int[rows][columns];

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				board[row][col] = 1 + (int) (Math.random() * colors);
			}
		}

		return board;
	}
}
