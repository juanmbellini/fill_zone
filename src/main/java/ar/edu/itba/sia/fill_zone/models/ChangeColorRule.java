package ar.edu.itba.sia.fill_zone.models;


import ar.edu.itba.sia.fill_zone.solver.api.GPSRule;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChangeColorRule implements GPSRule {

	private int color;

	private List<ArrayList<Integer>> checked = new ArrayList<>();

	public ChangeColorRule(int color) {
		this.color = color;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return "Change to color " + color;
	}

	@Override
	public Optional<GPSState> evalRule(GPSState state) {
		FillZoneState fillZoneState = (FillZoneState) state;
		Board oldBoard = fillZoneState.getBoard();

		if (color == oldBoard.startingColor() || oldBoard.getAmountOfEachColor()[color] == 0 || !oldBoard.getFrontierColors().contains(color)) {
			return Optional.empty();
		}

		Board newBoard = new Board(oldBoard.getRows(), oldBoard.getColumns(), oldBoard.getColors(),
				duplicateBoard(fillZoneState.getBoard().getMatrix(), oldBoard.getRows(), oldBoard.getColumns()));
		applyColor(newBoard);
		return Optional.of(new FillZoneState(newBoard, fillZoneState.getMoves() + 1));
	}

	private void applyColor(Board board) {
		applyColorRec(board, board.startingColor(), 0, 0);
		checked.clear();
	}

	private void applyColorRec(Board board, int oldColor, int row, int col) {
		ArrayList<Integer> a = new ArrayList<>();
		ArrayList<Integer> b = new ArrayList<>();
		a.add(row);
		a.add(col);
		checked.add(a);

		if (board.getMatrix()[row][col] == oldColor) {
			board.setColorAtLocker(row, col, color);

			b.add(row-1);
			b.add(col);
			//up
			if (row > 0 && !checked.contains(b)) {
				applyColorRec(board, oldColor, row - 1, col);
			}
			b.clear();
			b.add(row+1);
			b.add(col);
			//down
			if (row < (board.getRows() - 1) && !checked.contains(b)) {
				applyColorRec(board, oldColor, row + 1, col);
			}
			b.clear();
			b.add(row);
			b.add(col-1);
			//left
			if (col > 0 && !checked.contains(b)) {
				applyColorRec(board, oldColor, row, col - 1);
			}
			b.clear();
			b.add(row);
			b.add(col+1);
			//right
			if (col < (board.getColumns() - 1) && !checked.contains(b)) {
				applyColorRec(board, oldColor, row, col + 1);
			}
		}
	}

	private int[][] duplicateBoard(int[][] oldBoard, int rows, int columns) {
		int[][] newBoard = new int[rows][columns];

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				newBoard[row][col] = oldBoard[row][col];
			}
		}

		return newBoard;
	}


}
