package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.*;

/**
 * Class representing a problem solver.
 * This class must be extended in order to implement different types of solving strategies (i.e. searching algorithms)
 * <p>
 * Created by Juan Marcos Bellini on 30/3/17.
 */
public abstract class Solver {

	/**
	 * Indicates whether the engine finished finding the solution.
	 */
	private boolean finished;

	/**
	 * Indicates whether the engine failed while finding the solution.
	 */
	private boolean failed;

	/**
	 * The problem to be solved.
	 */
	protected final GPSProblem problem;

	/**
	 * The initial node of the problem.
	 */
	protected final GPSNode initialNode;

	/**
	 * Holds the solution node (if it exists).
	 */
	protected GPSNode solutionNode;

	/**
	 * Map containing states and costs to reach the said state.
	 */
	private final Map<GPSState, Integer> bestCosts;

	/**
	 * Contains how many explosions have been made.
	 */
	private long explosionCounter;


	/**
	 * Constructor.
	 *
	 * @param problem The problem to be solved.
	 */
	protected Solver(GPSProblem problem) {
		this.problem = problem;
		this.initialNode = new GPSNode(problem.getInitState(), 0, null, null);
		this.bestCosts = new HashMap<>();
		clearSolver();
	}


	/**
	 * Gets the solution.
	 */
	public abstract void findSolution();

	/**
	 * Returns a list of opened nodes from initial node to the solution node,
	 * or an empty list if no solution is reached.
	 *
	 * @return A list of opened nodes from the initial node the solution node,
	 * or an empty list if no solution is reached.
	 */
	public abstract List<GPSNode> getOpenedNodes();


	/**
	 * Indicates whether the engine has finished finding the solution
	 *
	 * @return {@code true} if it has finished, or {@code false} otherwise.
	 */
	public final boolean isFinished() {
		return finished;
	}

	/**
	 * Indicates whether the engine has failed finding the solution
	 *
	 * @return {@code true} if it has failed, or {@code false} otherwise.
	 */
	public final boolean isFailed() {
		return failed;
	}

	/**
	 * Returns the solution node (if it exists).
	 *
	 * @return The solution node, or {@code null} if no solution exists for the problem.
	 */
	public GPSNode getSolutionNode() {
		return solutionNode;
	}

	/**
	 * Returns a map of nodes and their costs to be reached from the initial node.
	 *
	 * @return The said map.
	 */
	public Map<GPSState, Integer> getBestCosts() {
		return bestCosts;
	}

	/**
	 * Returns the problem to be solved.
	 *
	 * @return The problem to be solved.
	 */
	public GPSProblem getProblem() {
		return problem;
	}

	/**
	 * Returns how much explosion has been made.
	 *
	 * @return The number of explosions made.
	 */
	public long getExplosionCounter() {
		return explosionCounter;
	}

	/**
	 * Sets the solver in a state in which it can start solving the problem.
	 */
	protected void clearSolver() {
		finished = false;
		failed = false;
		solutionNode = null;
		explosionCounter = 0;
	}

	/**
	 * v * Checks if the given node was reached.
	 *
	 * @param node The node to be checked.
	 * @return {@code true} if the state was reached, or {@code false} otherwise.
	 */
	protected boolean nodeWasReached(GPSNode node) {
		return bestCosts.containsKey(node.getState());
	}

	/**
	 * Checks if the given node is best.
	 *
	 * @param node The node to be checked.
	 * @return {@code true} if is best, or {@code false} otherwise.
	 */
	protected boolean isBest(GPSNode node, Integer cost) {
		GPSState state = node.getState();
		return !bestCosts.containsKey(state) || cost < bestCosts.get(state);
	}

	/**
	 * Updates the cost of the given {@code node}, which is best.
	 *
	 * @param node The node whose cost must be updated.
	 */
	protected void updateBest(GPSNode node) {
		bestCosts.put(node.getState(), node.getCost());
	}

	/**
	 * Applies all appliable rules from the given {@code node}, returning a {@link List} of children nodes.
	 *
	 * @param node The node from which rules must be applied.
	 */
	protected List<GPSNode> applyRules(GPSNode node) {
		explosionCounter++;
//		updateBest(node);
		final List<GPSNode> candidates = new LinkedList<>();
		problem.getRules().forEach(rule -> {
			Optional<GPSState> newState = rule.evalRule(node.getState());
			if (newState.isPresent()) {
				GPSNode newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost(), rule, node);
				candidates.add(newNode);
			}
		});

		return candidates;
	}


	/**
	 * Sets the solver in fail state.
	 */
	protected void fail() {
		failed = true;
		finished = true;
	}

	/**
	 * Sets the solver to success state.
	 *
	 * @param solutionNode The node that is solution of the problem.
	 */
	protected void succeed(GPSNode solutionNode) {
		finished = true;
		this.solutionNode = solutionNode;
	}


}
