package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

import java.util.HashSet;
import java.util.List;

/**
 * Class implementing the IDDFS searching strategy.
 */
public final class IDDFSSolver extends Solver {

	/**
	 * Indicates the actual depth in the iteration
	 */
	private long actualDepth;

	/**
	 * Contains the nodes in the actual path.
	 */
	private final HashSet<GPSNode> actualBranchNodes;


	/**
	 * Constructor.
	 *
	 * @param problem The problem to solve.
	 */
	public IDDFSSolver(GPSProblem problem) {
		super(problem);
		actualBranchNodes = new HashSet<>();
		clearSolver();
	}


	@Override
	public void findSolution() {
		clearSolver();

		while (actualDepth < Long.MAX_VALUE) {
			GPSNode result = recursiveSolve(initialNode, 0);
			if (result != null) {
				succeed(result);
				return;
			}
			actualDepth++;
		}
		fail();
	}

	/**
	 * Finds the solution using recursion.
	 *
	 * @param node  The actual node.
	 * @param depth The actual depth.
	 * @return The next node in the solution path.
	 */
	private GPSNode recursiveSolve(GPSNode node, int depth) {
		if (depth > actualDepth || actualBranchNodes.contains(node)) {
			return null;
		}
		actualBranchNodes.add(node);
		if (problem.isGoal(node.getState())) {
			return node;
		}
		List<GPSNode> children = applyRules(node);

		for (GPSNode child : children) {
			GPSNode result = recursiveSolve(child, depth + 1);
			if (result != null) {
				updateBest(node);
				return result;
			}
		}
		actualBranchNodes.remove(node);
		return null;
	}


	@Override
	public List<GPSNode> getOpenedNodes() {
		return null;
	}


	@Override
	protected final void clearSolver() {
		super.clearSolver();
		actualDepth = 0;
		actualBranchNodes.clear();
	}


}
