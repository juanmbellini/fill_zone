package ar.edu.itba.sia.fill_zone.models.interfaces;

import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

public interface Heuristic {

    /**
     * Computes the value of the Heuristic for the given state.
     *
     * @param state The state where the Heuristic should be computed.
     * @return The value of the Heuristic.
     */
    Integer getHValue(GPSState state);

}
