package ar.edu.itba.sia.fill_zone.models;

import ar.edu.itba.sia.fill_zone.models.interfaces.Heuristic;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

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

	},;


}
