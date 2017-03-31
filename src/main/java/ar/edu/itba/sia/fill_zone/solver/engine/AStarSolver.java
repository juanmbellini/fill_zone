package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

/**
 * Class implementing the A* searching strategy.
 * <p>
 * Created by Juan Marcos Bellini on 30/3/17.
 */
public final class AStarSolver extends PriorityQueueSolver {


	/**
	 * Constructor.
	 *
	 * @param problem The problem to be solved.
	 */
	public AStarSolver(GPSProblem problem) {
		super(problem, (o1, o2) -> {
			if (o1 == null || o2 == null) {
				throw new IllegalArgumentException();
			}
			//TODO si f=0 devolver la resta de los h, y tirar la moneda
			return ((o1.getCost() + problem.getHValue(o1.getState()))
					- (o2.getCost() + problem.getHValue(o2.getState())));
		});
	}

}
