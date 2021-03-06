package ar.edu.itba.sia.fill_zone.solver.api;

/**
 * GPSState interface.
 */
public interface GPSState {

	/**
	 * Compares self to another state to determine whether they are the same or not.
	 *
	 * @param state The state to compare to.
	 * @return true if self is the same as the state given, false if they are different.
	 */
	boolean equals(Object state);

}
