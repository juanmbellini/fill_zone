package ar.edu.itba.sia.fill_zone.models;


import ar.edu.itba.sia.fill_zone.solver.api.GPSRule;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;
import ar.edu.itba.sia.fill_zone.solver.exception.NotAppliableException;

import java.util.ArrayList;
import java.util.List;

public class ChangeColor implements GPSRule {

	private int rows;

	private int columns;

	private int color;

	private List<int[]> checked = new ArrayList<>();

	public ChangeColor(int color, int rows, int columns) {
		this.color = color;
		this.rows = rows;
		this.columns = columns;
	}

	//TODO: esta bien? el costo deberia ser la cantidad de movimientos?
	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return "Change to color " + color;
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		FillZoneState fillZoneState = (FillZoneState) state;

		if (color == fillZoneState.startingColor())
			throw new NotAppliableException();

		int[][] newBoard = duplicateBoard(fillZoneState.getBoard());
		applyColor(newBoard);
		return new FillZoneState(newBoard, fillZoneState.getMoves() + 1);
	}

	private void applyColor(int[][] board) {
		applyColorRec(board, board[0][0], 0, 0);
		checked.clear();
	}

	private void applyColorRec(int[][] board, int oldColor, int row, int col) {
		if (board[row][col] == oldColor) {
			board[row][col] = color;
			checked.add(new int[]{row, col});

			//up
			if (row > 0 && !checked.contains(new int[]{row - 1, col})) {
				applyColorRec(board, oldColor, row - 1, col);
			}
			//down
			if (row < (rows - 1) && !checked.contains(new int[]{row + 1, col})) {
				applyColorRec(board, oldColor, row + 1, col);
			}
			//left
			if (col > 0 && !checked.contains(new int[]{row, col - 1})) {
				applyColorRec(board, oldColor, row, col - 1);
			}
			//right
			if (col < (columns - 1) && !checked.contains(new int[]{row, col + 1})) {
				applyColorRec(board, oldColor, row, col + 1);
			}
		}
	}

	//TODO: es necesario?
	private int[][] duplicateBoard(int[][] oldBoard) {
		int[][] newBoard = new int[rows][columns];

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				newBoard[row][col] = oldBoard[row][col];
			}
		}

		return newBoard;
	}


}
