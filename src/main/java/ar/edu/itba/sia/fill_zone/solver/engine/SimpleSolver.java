package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

import java.util.Queue;

/**
 * A simple solver that partially implements some algorithms that share some logic (DFS, BFS, GREEDY and A*).
 */
public abstract class SimpleSolver extends Solver {


	/**
	 * Constructor.
	 *
	 * @param problem The problem to be solved.
	 */
	protected SimpleSolver(GPSProblem problem) {
		super(problem);
		clearSolver();
	}

	/**
	 * Explodes the given node.
	 *
	 * @param node The node to be exploded.
	 */
	protected abstract void explode(GPSNode node);

	/**
	 * Returns the queue that holds the opened stated.
	 *
	 * @return The said queue.
	 */
	protected abstract Queue<GPSNode> getQueue();

	/**
	 * Indicates whether the given {@code node} must be exploded.
	 *
	 * @param node The node to be checked.
	 * @return {@code true} if the node must be exploded, or {@code false} otherwise.
	 */
	protected abstract boolean mustExplode(GPSNode node);

	@Override
	public void findSolution() {
		clearSolver();
		getQueue().add(initialNode);

		while (!getQueue().isEmpty()) {
			GPSNode currentNode = getQueue().remove();
			if (problem.isGoal(currentNode.getState())) {
				succeed(currentNode);
				return;
			}
			if (mustExplode(currentNode)) {
				updateBest(currentNode);
				explode(currentNode);
			}
		}
		fail();
	}

	@Override
	protected final void clearSolver() {
		super.clearSolver();
		Queue<GPSNode> queue = this.getQueue();
		if (queue != null) {
			queue.clear();
		}
	}


}
