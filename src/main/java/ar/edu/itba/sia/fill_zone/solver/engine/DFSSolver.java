package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

/**
 * Class implementing the DFS searching strategy.
 */
public final class DFSSolver extends DequeSimpleSolver {


	/**
	 * Constructor.
	 *
	 * @param problem The problem to be solved.
	 */
	public DFSSolver(GPSProblem problem) {
		super(problem);
	}


	@Override
	protected void explode(GPSNode node) {
		applyRules(node).forEach(deque::push);
	}

	@Override
	protected boolean mustExplode(GPSNode node) {
		return !nodeWasReached(node);
	}
}
