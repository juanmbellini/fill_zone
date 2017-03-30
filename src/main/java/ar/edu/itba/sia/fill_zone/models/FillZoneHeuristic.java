package ar.edu.itba.sia.fill_zone.models;

import ar.edu.itba.sia.fill_zone.models.interfaces.Heuristic;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.ArrayList;
import java.util.List;

public enum FillZoneHeuristic implements Heuristic {

	/**
	 * Amount of groups of colors in the board.
	 * Non admissible Heuristic.
	 */
	REMAINING_GROUPS() {
		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;
			final Board board = fillZoneState.getBoard();

			boolean[][] groupedBoxes = new boolean[board.getRows()][board.getColumns()];
			int h = 0;

			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (!groupedBoxes[i][j]) {
						h++;
						groupedBoxes[i][j] = true;
						groupRec(groupedBoxes, board, i, j, board.getMatrix()[i][j]);
					}
				}
			}

			return h;
		}

		private void groupRec(boolean[][] boardAux, final Board board, int row, int col, final int color){
            if(board.getMatrix()[row][col] == color){
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
	 * Amount of colors remaining in the board.
	 * Admissible Heuristic.
	 */
	REMAINING_COLORS() {
		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;

			if(isGoal(fillZoneState.getBoard())){
				return 0;
			}

			final int[] colorRegister = fillZoneState.getBoard().getAmountOfEachColor();

			int h = 0;
			for (int i = 0; i < colorRegister.length; i++) {
				if (colorRegister[i] > 0)
					h++;
			}
			return h;
		}
	},

    /**
     * Exact solution for a two color board.
	 * Admissible Heuristic.
     */
	TWO_COLORS() {
		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;
			Board realBoard = fillZoneState.getBoard();

			if(isGoal(realBoard)){
				return 0;
			}

			int[][] realMatrix = realBoard.getMatrix();
			int[][] twoColorMatrix = new int[realBoard.getRows()][realBoard.getColumns()];

            //create the two color matrix
			for(int i = 0; i < realBoard.getRows(); i++){
				for(int j = 0; j < realBoard.getColumns(); j++){
                    twoColorMatrix[i][j] = realMatrix[i][j] % 2;
				}
			}

			return solveTwoColorMatrix(new Board(realBoard.getRows(), realBoard.getColumns(), realBoard.getColors(), twoColorMatrix), realBoard.getRows(), realBoard.getColumns());
		}

		private int solveTwoColorMatrix(Board board, int rows, int columns){
            int h = 0;

            while(!isGoal(board)){
                applyColorRec(board, board.startingColor(), 0, 0, new ArrayList<>());
                h++;
            }

            return h;
        }

        private void applyColorRec(Board board, int oldColor, int row, int col, List<int[]> checked) {
            if (board.getMatrix()[row][col] == oldColor) {
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
	 * Solve for a number of steps as if only two colors existed, and then check how many colors remain.
	 * Admissible Heuristic.
	 */
	COMBINED_TWO_COLORS_AND_REMAINING_COLORS(){

		private int steps = 1;

		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;
			final Board realBoard = fillZoneState.getBoard();
            int h = 0;

			if(isGoal(fillZoneState.getBoard())){
				return 0;
			}

			//create the two color matrix
			int [][] twoColorMatrix = new int[realBoard.getRows()][realBoard.getColumns()];
			for(int i = 0; i < realBoard.getRows(); i++){
				for(int j = 0; j < realBoard.getColumns(); j++){
					twoColorMatrix[i][j] = realBoard.getMatrix()[i][j] % 2;
				}
			}
            Board twoColorBoard = new Board(realBoard.getRows(), realBoard.getColumns(), realBoard.getColors(), twoColorMatrix);

			//solve the two color board for a limited amount of steps
            while(steps > 0 && !isGoal(twoColorBoard)){
                applyColorRec(twoColorBoard, twoColorBoard.startingColor(), twoColorBoard.getRows(), twoColorBoard.getColumns(), new ArrayList<>());
                steps--;
                h++;
            }

            if(isGoal(twoColorBoard)){
                return h;
            }

			//combine the two color board with the real board in order to find the amount of remaining colors
            // result at twoColorBoard
			combineBoards(twoColorBoard, realBoard.getMatrix(), 0, 0, twoColorBoard.startingColor(), true, new ArrayList<>());

            return h + REMAINING_COLORS.getHValue(new FillZoneState(twoColorBoard, ((FillZoneState) state).getMoves()));
		}

        private void applyColorRec(Board board, int oldColor, int row, int col, List<int[]> checked) {
            if (board.getMatrix()[row][col] == oldColor) {
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

		private void combineBoards(Board twoColorBoard, int[][] realMatrix, int row, int col, int initialColor, boolean initialGroup, List<int[]> checked){

		    if(!initialGroup){
		        twoColorBoard.setColorAtLocker(row, col, realMatrix[row][col]);
            } else {
                initialGroup = initialColor == twoColorBoard.getMatrix()[row][col];
                if(!initialGroup){
                    twoColorBoard.setColorAtLocker(row, col, realMatrix[row][col]);
                }
            }
            checked.add(new int[]{row, col});

            //up
            if (row > 0 && !checked.contains(new int[]{row - 1, col})) {
                combineBoards(twoColorBoard, realMatrix, row - 1, col, initialColor, initialGroup, checked);
            }
            //down
            if (row < (twoColorBoard.getRows() - 1) && !checked.contains(new int[]{row + 1, col})) {
                combineBoards(twoColorBoard, realMatrix, row + 1, col, initialColor, initialGroup, checked);
            }
            //left
            if (col > 0 && !checked.contains(new int[]{row, col - 1})) {
                combineBoards(twoColorBoard, realMatrix, row, col - 1, initialColor, initialGroup, checked);
            }
            //right
            if (col < (twoColorBoard.getColumns() - 1) && !checked.contains(new int[]{row, col + 1})) {
                combineBoards(twoColorBoard, realMatrix, row, col + 1, initialColor, initialGroup, checked);
            }

		}
	},


    /**
     * Maximum value within the other admissible heuristics.
	 * Admissible Heuristic
     */
    MAX_ADMISSIBLE(){
        @Override
        public Integer getHValue(GPSState state) {
            final FillZoneState fillZoneState = (FillZoneState) state;

            if(isGoal(fillZoneState.getBoard())){
                return 0;
            }

            int remainingColors = REMAINING_COLORS.getHValue(state);
            int twoColors = TWO_COLORS.getHValue(state);
			int combined = COMBINED_TWO_COLORS_AND_REMAINING_COLORS.getHValue(state);
            return remainingColors>twoColors ? (remainingColors>combined ? remainingColors:combined) : (twoColors>combined?twoColors:combined);
        }
    },

	;

	/**
	 *
	 * @param board
	 * @return a boolean value indicating if the board has reached a winning state.
	 */
	public boolean isGoal(Board board) {
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

}
