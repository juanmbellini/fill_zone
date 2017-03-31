package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

/**
 * Created by Juan Marcos Bellini on 30/3/17.
 */
public final class GreedySolver extends PriorityQueueSolver {


	/**
	 * Constructor.
	 *
	 * @param problem The problem to solve.
	 */
	public GreedySolver(GPSProblem problem) {
		super(problem, (o1, o2) -> {
			if (o1 == null || o2 == null) {
				throw new IllegalArgumentException();
			}
			// TODO: what if h are the same?
			return problem.getHValue(o1.getState()) - problem.getHValue(o2.getState());
		});
	}
}
