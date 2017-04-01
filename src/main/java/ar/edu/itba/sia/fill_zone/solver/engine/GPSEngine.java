package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.List;
import java.util.Map;

public class GPSEngine {

	/**
	 * The solver to be used to solve the problem.
	 */
	private final Solver solver;

	// Use this variable in open set order. TODO: ?!?!
	/**
	 * The searching strategy to be applied.
	 */
	private final SearchStrategy strategy;


	/**
	 * Constructor.
	 *
	 * @param problem  The problem to be solved.
	 * @param strategy The strategy to be used to solve the problem.
	 */
	public GPSEngine(GPSProblem problem, SearchStrategy strategy) {
		this.strategy = strategy;
		this.solver = strategy.createSolver(problem);
	}

	/**
	 * Finds the solution.
	 */
	public void findSolution() {
		solver.findSolution();
	}


	// GETTERS FOR THE PEOPLE!

	public List<GPSNode> getOpen() {
		return solver.getOpenedNodes();
	}

	public Map<GPSState, Integer> getBestCosts() {
		return solver.getBestCosts();
	}

	public GPSProblem getProblem() {
		return solver.getProblem();
	}

	public long getExplosionCounter() {
		return solver.getExplosionCounter();
	}

	public boolean isFinished() {
		return solver.isFinished();
	}

	public boolean isFailed() {
		return solver.isFailed();
	}

	public GPSNode getSolutionNode() {
		return solver.getSolutionNode();
	}

	public SearchStrategy getStrategy() {
		return strategy;
	}

}
