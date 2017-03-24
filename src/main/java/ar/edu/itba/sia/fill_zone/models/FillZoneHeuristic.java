package ar.edu.itba.sia.fill_zone.models;

import ar.edu.itba.sia.fill_zone.models.interfaces.Heuristic;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.ArrayList;
import java.util.List;

public enum FillZoneHeuristic implements Heuristic {

	/**
	 * Amount of colors remaining in the board.
	 */
	REMAINING_COLORS() {
		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;
			final int[] colorRegister = fillZoneState.getBoard().getAmountOfEachColor();

			int c = 0;
			for (int i = 0; i < colorRegister.length; i++) {
				if (colorRegister[i] > 0)
					c++;
			}
			return c;
		}
	},

	/**
	 * Amount of groups of colors in the board.
	 */
	REMAINING_GROUPS() {
		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;
			final Board board = fillZoneState.getBoard();

			boolean[][] groupedBoxes = new boolean[board.getRows()][board.getColumns()];
			int c = 0;

			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (!groupedBoxes[i][j]) {
						c++;
						groupedBoxes[i][j] = true;
						groupRec(groupedBoxes, board, i, j, board.getBoard()[i][j]);
					}
				}
			}

			return c;
		}

		private void groupRec(boolean[][] boardAux, final Board board, int row, int col, final int color){
            if(board.getBoard()[row][col] == color){
                boardAux[row][col] = true;

                //up
                if(row>0){
                    groupRec(boardAux, board, row-1, col, color);
                }

                //down
                if(row<board.getRows()-1){
                    groupRec(boardAux, board, row+1, col, color);
                }

                //left
                if(col>0){
                    groupRec(boardAux, board, row, col-1, color);
                }

                //right
                if(col<board.getColumns()-1){
                    groupRec(boardAux, board, row, col+1, color);
                }
            }
        }

	},

    /**
     * Exact solution for a two color board.
     */
	TWO_COLORS() {
		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;
			Board realBoard = fillZoneState.getBoard();
            int[][] realMatrix = realBoard.getBoard();
			int[][] twoColorMatrix = new int[realBoard.getRows()][realBoard.getColumns()];

			for(int i = 0; i < realBoard.getRows(); i++){
				for(int j = 0; j < realBoard.getColumns(); j++){
                    twoColorMatrix[i][j] = realMatrix[i][j] % 2;
				}
			}

			return solveTwoColorMatrix(new Board(realBoard.getRows(), realBoard.getColumns(), realBoard.getColors(), twoColorMatrix), realBoard.getRows(), realBoard.getColumns());
		}

		private int solveTwoColorMatrix(Board board, int rows, int columns){
		    int firstColor = board.startingColor();
            boolean isGoal = true;

            // check if the board is a goal state
            for(int i = 0; i < rows && isGoal; i++){
                for(int j = 0; j < columns && isGoal; j++){
                    isGoal = board.getBoard()[i][j] == firstColor;
                }
            }

            if(isGoal){
                return 0;
            }

            applyColorRec(board, firstColor, 0, 0, new ArrayList<>());
            return 1 + solveTwoColorMatrix(board, rows, columns);

        }

        private void applyColorRec(Board board, int oldColor, int row, int col, List<int[]> checked) {
            if (board.getBoard()[row][col] == oldColor) {
                board.setColorAtLocker(1 - oldColor, row, col);
                checked.add(new int[]{row, col});

                //up
                if (row > 0 && !checked.contains(new int[]{row - 1, col})) {
                    applyColorRec(board, oldColor, row - 1, col, checked);
                }
                //down
                if (row < (board.getRows() - 1) && !checked.contains(new int[]{row + 1, col})) {
                    applyColorRec(board, oldColor, row + 1, col, checked);
                }
                //left
                if (col > 0 && !checked.contains(new int[]{row, col - 1})) {
                    applyColorRec(board, oldColor, row, col - 1, checked);
                }
                //right
                if (col < (board.getColumns() - 1) && !checked.contains(new int[]{row, col + 1})) {
                    applyColorRec(board, oldColor, row, col + 1, checked);
                }
            } else {
                checked.add(new int[]{row, col});
            }
        }
	},

    /**
     * Maximum value within the other admissible heuristics.
     */
    MAX_ADMISSIBLE(){
        @Override
        public Integer getHValue(GPSState state) {
            int remainingColors = REMAINING_COLORS.getHValue(state);
            int twoColors = TWO_COLORS.getHValue(state);

            return remainingColors > twoColors ? remainingColors : twoColors;
        }
    },
    
	;


}
