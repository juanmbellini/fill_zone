package ar.edu.itba.sia.fill_zone.models;

import ar.edu.itba.sia.fill_zone.models.interfaces.Heuristic;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

public enum FillZoneHeuristic implements Heuristic{


    REMAINING_COLORS(){
        @Override
        public Integer getHValue(GPSState state) {
            FillZoneState fillZoneState = (FillZoneState) state;
            int[] colorRegister = fillZoneState.getBoard().getColorRegister();

            int c = 0;
            for(int i = 0; i < colorRegister.length; i++){
                if(colorRegister[i] > 0)
                    c++;
            }
            return c;
        }
    },
    ;



}
