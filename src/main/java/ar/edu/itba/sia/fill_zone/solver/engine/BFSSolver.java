package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

/**
 * Class implementing the BFS searching strategy.
 */
public final class BFSSolver extends DequeSimpleSolver {


	/**
	 * Constructor.
	 *
	 * @param problem The problem to be solved.
	 */
	public BFSSolver(GPSProblem problem) {
		super(problem);
	}

	@Override
	protected void explode(GPSNode node) {
		applyRules(node).forEach(deque::offer);
	}

	@Override
	protected boolean mustExplode(GPSNode node) {
		return !nodeWasReached(node);
	}
}
