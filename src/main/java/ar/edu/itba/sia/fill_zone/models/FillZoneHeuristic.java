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
	 * Amount of colors remaining in the board.
	 * Admissible Heuristic.
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
     * Exact solution for a two color board.
	 * Admissible Heuristic.
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
	 * Solve for a number of steps as if only two colors existed, and then check how many colors remain.
	 * Admissible Heuristic.
	 */
	//TODO :CAMBIAR NOMBRE
	COMBINED_ADMISSIBLE(){
		private int steps = 1;

		@Override
		public Integer getHValue(GPSState state) {
			final FillZoneState fillZoneState = (FillZoneState) state;
			final Board realBoard = fillZoneState.getBoard();

			//create the two color board
			int [][] twoColorBoard = new int[realBoard.getRows()][realBoard.getColumns()];
			for(int i = 0; i < realBoard.getRows(); i++){
				for(int j = 0; j < realBoard.getColumns(); j++){
					twoColorBoard[i][j] = realBoard.getBoard()[i][j] % 2;
				}
			}

			//solve the two color board for a limited amount of steps
			solveTwoColorBoard(twoColorBoard, twoColorBoard[0][0], steps, 0, 0, realBoard.getRows(), realBoard.getColumns());


			//combine the two color board with the real board in order to find the amount of remaining colors
			int[][] combinedBoard = new int[realBoard.getRows()][realBoard.getColumns()];
			int [][] finalBoard = realBoard.getBoard();
			combineBoards(combinedBoard, finalBoard, 0, 0, realBoard.getRows(), realBoard.getColumns());


			//count how many slots of each color remain
			int[] colors = new int[realBoard.getColors()];
			for(int i=0; i<realBoard.getRows(); i++){
				for(int j=0; j<realBoard.getColumns(); j++){
					colors[combinedBoard[i][j]]++;
				}
			}

			//count the amount of remaining colors
			int c = 0;
			for(int i=0; i<realBoard.getColors(); i++){
				if(colors[i]>0){
					c++;
				}
			}

			return c;


		}

		private void solveTwoColorBoard(int[][] board, int color, int steps, int row, int col, int maxRows, int maxCols){

			if(steps == 0 || row>=maxRows || col >= maxCols || row<0 || col<0){
				return;
			}

			if(board[row][col] == color) {

				//change to the opposite color
				board[row][col] = 1 - color;

				//up
				solveTwoColorBoard(board, color, steps - 1, row - 1, col, maxRows, maxCols);

				//down
				solveTwoColorBoard(board, color, steps - 1, row + 1, col, maxRows, maxCols);

				//left
				solveTwoColorBoard(board, color, steps - 1, row, col - 1, maxRows, maxCols);

				//right
				solveTwoColorBoard(board, color, steps - 1, row, col + 1, maxRows, maxCols);

			}
		}

		private void combineBoards(int[][] twoColor, int[][] real, int row, int col, int maxRow, int maxCol){
			if(row == maxRow || col == maxCol){
				return;
			}
			if(twoColor[row][col] == twoColor[0][0]){
				real[row][col] = real[0][0];
				combineBoards(twoColor, real, row+1, col, maxRow, maxCol);
				combineBoards(twoColor,real,row, col+1, maxRow, maxCol);
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
            int remainingColors = REMAINING_COLORS.getHValue(state);
            int twoColors = TWO_COLORS.getHValue(state);
			int combined = COMBINED_ADMISSIBLE.getHValue(state);
            return remainingColors>twoColors ? (remainingColors>combined ? remainingColors:combined) : (twoColors>combined?twoColors:combined);
        }
    },
    
	;


}
