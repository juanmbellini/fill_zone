package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

import java.util.Random;

/**
 * Class implementing the Greedy searching strategy.
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

			int hValueDiff = problem.getHValue(o1.getState()) - problem.getHValue(o2.getState());
			if (hValueDiff == 0) {
				Random random = new Random();
				return (random.nextInt(2)) == 0 ? 1 : -1;
			}
			return hValueDiff;
		});
	}
}
