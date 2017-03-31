package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

import java.util.*;

/**
 * A simple solver that uses a {@link PriorityQueue} as the queue that holds the opened nodes.
 * Greedy and A* solvers must extend this class, indicating how to compare the "priority" of each node.
 * <p>
 * Created by Juan Marcos Bellini on 30/3/17.
 */
public abstract class PriorityQueueSolver extends SimpleSolver {

	/**
	 * Priority queue to store nodes according to their "priority" in each strategy.
	 */
	private final PriorityQueue<GPSNode> priorityQueue;


	/**
	 * Constructor.
	 *
	 * @param problem The problem to be solved.
	 * @param cmp     The comparator to use in the priority queue.
	 */
	protected PriorityQueueSolver(GPSProblem problem, Comparator<GPSNode> cmp) {
		super(problem);
		this.priorityQueue = new PriorityQueue<>(cmp);
	}

	@Override
	protected Queue<GPSNode> getQueue() {
		return priorityQueue;
	}

	@Override
	protected void explode(GPSNode node) {
		applyRules(node).forEach(priorityQueue::add);
	}


	@Override
	public List<GPSNode> getOpenedNodes() {
		return Arrays.asList(priorityQueue.toArray(new GPSNode[priorityQueue.size()]));
	}

	@Override
	protected boolean mustExplode(GPSNode node) {
		return !isBest(node, node.getCost());
	}
}
