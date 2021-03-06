package ar.edu.itba.sia.fill_zone.models;

import ar.edu.itba.sia.fill_zone.models.interfaces.Heuristic;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.awt.*;
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
						groupRec(groupedBoxes, board, i, j, board.getMatrix()[i][j], new ArrayList<>());
					}
				}
			}

			return h - 1;
		}

	},


    /**
     * Amount of lockers in the board that have not been painted in the current color
     * Non admissible heuristic
     */
    REMAINING_LOCKERS(){
        @Override
        public Integer getHValue(GPSState state) {
            FillZoneState fillZoneState = (FillZoneState) state;
            Board board = fillZoneState.getBoard();
            return board.getRows()*board.getColumns() - paintedLockers(board, board.getMatrix()[0][0], 0, 0, new ArrayList<Point>());
        }

        private int paintedLockers(final Board board, final int color, int row, int col, List<Point> checked){

            if(row >= board.getRows() || col >= board.getColumns() || col<0 || row < 0 || board.getMatrix()[row][col] != color){
                return 0;
            }

            if(!checked.contains(new Point(row, col))){
                checked.add(new Point(row,col));
                return 1 + paintedLockers(board, color, row+1, col, checked) + paintedLockers(board, color, row, col+1, checked) + paintedLockers(board, color, row, col-1, checked)
                        + paintedLockers(board, color, row-1, col, checked) ;
            }

            return 0;
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

			// checking if the initial color is only present on the first group
			if(colorRegister[fillZoneState.getBoard().startingColor()] == firstGroupSize(fillZoneState.getBoard(), 0, 0, new ArrayList<>())){
			    h--;
            }

			return h;
		}

		private int firstGroupSize(final Board board, int row, int col, List<Point> checked){

		    int[][] matrix = board.getMatrix();
            checked.add(new Point(row, col));

            if(board.startingColor() != matrix[row][col]){
                return 0;
            }

            int c = 1;

            //up
            if (row > 0 && !checked.contains(new Point(row-1, col))) {
                c += firstGroupSize(board, row - 1, col, checked);
            }

            //down
            if (row < (board.getRows() - 1) && !checked.contains(new Point(row+1, col))) {
                c += firstGroupSize(board, row + 1, col, checked);
            }

            //left
            if (col > 0 && !checked.contains(new Point(row, col-1))) {
                c += firstGroupSize(board, row, col - 1, checked);
            }

            //right
            if (col < (board.getColumns() - 1) && !checked.contains(new Point(row, col+1))) {
                c += firstGroupSize(board, row, col + 1, checked);
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

			return solveTwoColorMatrix(new Board(realBoard.getRows(), realBoard.getColumns(), realBoard.getColors(), twoColorMatrix));
		}

		private int solveTwoColorMatrix(Board board){
            int h = 0;

            while(!isGoal(board)){
                applyColorRec(board, board.startingColor(), 0, 0, new ArrayList<>());
                h++;
            }

            return h;
        }
	},


	/**
	 * Solve for a number of steps as if only two colors existed, and then check how many colors remain.
	 * Admissible Heuristic.
	 */
	COMBINED(){

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
                applyColorRec(twoColorBoard, twoColorBoard.startingColor(), 0, 0, new ArrayList<>());
                steps--;
                h++;
            }

            if(isGoal(twoColorBoard)){
                return h;
            }

			//combine the two color board with the real board in order to find the amount of remaining colors
            // result at twoColorBoard
			combineBoards(twoColorBoard, realBoard.getMatrix());
            return h + REMAINING_COLORS.getHValue(new FillZoneState(twoColorBoard, ((FillZoneState) state).getMoves()));
		}

        private void combineBoards(Board twoColorBoard, int[][] realMatrix){
            boolean[][] groupedBoxes = new boolean[twoColorBoard.getRows()][twoColorBoard.getColumns()];

            groupRec(groupedBoxes, twoColorBoard, 0, 0, twoColorBoard.startingColor(), new ArrayList<>());

            //public void groupRec(boolean[][] boardAux, final Board board, int row, int col, final int color, List<Point> checked){

            for (int i = 0; i < twoColorBoard.getRows(); i++) {
                for (int j = 0; j < twoColorBoard.getColumns(); j++) {
                    if (!groupedBoxes[i][j]) {
                        twoColorBoard.setColorAtLocker(i,j,realMatrix[i][j]);
                    }
                }
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
			int combined = COMBINED.getHValue(state);
            return remainingColors>twoColors ? (remainingColors>combined ? remainingColors:combined) : (twoColors>combined?twoColors:combined);
        }
    },

	;

	public static FillZoneHeuristic fromString(String name) {
		return valueOf(name.replace('-', '_').trim().toUpperCase());
	}

	@Override
	public String toString() {
		return super.toString().replace('_', '-').toLowerCase();
	}

	/**
	 * Checks if the board has reached a goal state.
     *
	 * @param board
	 * @return a boolean value indicating if the board has reached a goal state.
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


    public void applyColorRec(final Board board, final int oldColor, int row, int col, List<Point> checked) {

        checked.add(new Point(row, col));


        if (board.getMatrix()[row][col] == oldColor) {
            board.setColorAtLocker(row, col, 1 - oldColor);

            //up
            if (row > 0 && !checked.contains(new Point(row-1, col))) {
                applyColorRec(board, oldColor, row - 1, col, checked);
            }

            //down
            if (row < (board.getRows() - 1) && !checked.contains(new Point(row+1, col))) {
                applyColorRec(board, oldColor, row + 1, col, checked);
            }

            //left
            if (col > 0 && !checked.contains(new Point(row, col-1))) {
                applyColorRec(board, oldColor, row, col - 1, checked);
            }

            //right
            if (col < (board.getColumns() - 1) && !checked.contains(new Point(row, col+1))) {
                applyColorRec(board, oldColor, row, col + 1, checked);
            }
        }
    }

    public void groupRec(boolean[][] boardAux, final Board board, int row, int col, final int color, List<Point> checked){

        if(board.getMatrix()[row][col] == color){

            checked.add(new Point(row, col));
            boardAux[row][col] = true;

            //up
            if(row>0 && !checked.contains(new Point(row-1, col))){
                groupRec(boardAux, board, row-1, col, color, checked);
            }

            //down
            if(row<board.getRows()-1 && !checked.contains(new Point(row+1, col))){
                groupRec(boardAux, board, row+1, col, color, checked);
            }

            //left
            if(col>0 && !checked.contains(new Point(row, col-1))){
                groupRec(boardAux, board, row, col-1, color, checked);
            }

            //right
            if(col<board.getColumns()-1 && !checked.contains(new Point(row, col+1))){
                groupRec(boardAux, board, row, col+1, color, checked);
            }
        }
    }


}

