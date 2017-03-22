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

	//TODO
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

			for (int i = 0; i < board.getColumns(); i++) {
				for (int j = 0; j < board.getRows(); j++) {
					if (!groupedBoxes[i][j]) {
						c++;
						groupedBoxes[i][j] = true;
						//TODO funcion para agregar coordinadas true a los grouped boxes
					}
				}
			}

			return c;
		}
	},;


}
