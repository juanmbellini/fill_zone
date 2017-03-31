package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

import java.util.*;

/**
 * A simple solver that uses a {@link Deque} as the queue that holds the opened nodes.
 * DFS and BFS solvers must extend this class.
 * <p>
 * Created by Juan Marcos Bellini on 30/3/17.
 */
public abstract class DequeSimpleSolver extends SimpleSolver {

	/**
	 * Deque to be used as a queue or a stack, according to the strategy.
	 */
	protected final Deque<GPSNode> deque;

	/**
	 * Constructor.
	 *
	 * @param problem The problem to be solved.
	 */
	protected DequeSimpleSolver(GPSProblem problem) {
		super(problem);
		this.deque = new LinkedList<>();
	}

	@Override
	protected Queue<GPSNode> getQueue() {
		return deque;
	}


	@Override
	public List<GPSNode> getOpenedNodes() {
		return Arrays.asList(deque.toArray(new GPSNode[deque.size()]));
	}
}
