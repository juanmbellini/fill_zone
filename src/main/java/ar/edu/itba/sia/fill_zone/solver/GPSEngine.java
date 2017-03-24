package ar.edu.itba.sia.fill_zone.solver;

import ar.edu.itba.sia.fill_zone.models.Board;
import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import ar.edu.itba.sia.fill_zone.models.FillZoneProblem;
import ar.edu.itba.sia.fill_zone.models.FillZoneState;
import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;
import ar.edu.itba.sia.fill_zone.solver.api.GPSRule;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

import java.util.*;

public class GPSEngine {

	Queue<GPSNode> open;
	Map<GPSState, Integer> bestCosts;
	GPSProblem problem;
	long explosionCounter;
	boolean finished;
	boolean failed;
	GPSNode solutionNode;

	private final Solver solver;


	// Use this variable in open set order.
	protected SearchStrategy strategy;


	public GPSEngine(GPSProblem problem, SearchStrategy strategy) {
		this.bestCosts = new HashMap<>();
		this.problem = problem;
		this.strategy = strategy;
		this.explosionCounter = 0;
		this.finished = false;
		this.failed = false;
		switch (strategy) {
			case DFS:
				this.solver = new DFSSolver(this);
				break;
			case BFS:
				this.solver = new DFSSolver(this); // TODO: cambiar
				break;
			case IDDFS:
				this.solver = new DFSSolver(this); // TODO: cambiar
				break;
			case GREEDY:
				this.solver = new DFSSolver(this); // TODO: cambiar
				break;
			case ASTAR:
				this.solver = new AStarSolver(this);
				break;
			default:
				throw new RuntimeException();
		}
		this.open = solver.createQueue();
	}

	public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0);
		open.add(rootNode);
		// TODO: ¿Lógica de IDDFS?
		while (!open.isEmpty()) {
			GPSNode currentNode = open.remove();
			if (problem.isGoal(currentNode.getState())) {
				finished = true;
				solutionNode = currentNode;
				return;
			} else {
				explode(currentNode);
			}
		}
		failed = true;
		finished = true;
	}

	private void explode(GPSNode node) {
		Collection<GPSNode> newCandidates;
		solver.explode(node);

		switch (strategy) {
			case BFS:
				if (bestCosts.containsKey(node.getState())) {
					return;
				}
				newCandidates = new ArrayList<>();
				addCandidates(node, newCandidates);
				// TODO: ¿Cómo se agregan los nodos a open en BFS?
				break;
			case DFS:
				if (bestCosts.containsKey(node.getState())) {
					return;
				}
				newCandidates = new ArrayList<>();
				addCandidates(node, newCandidates);
				// TODO: ¿Cómo se agregan los nodos a open en DFS?
				break;
			case IDDFS:
				if (bestCosts.containsKey(node.getState())) {
					return;
				}
				newCandidates = new ArrayList<>();
				addCandidates(node, newCandidates);
				// TODO: ¿Cómo se agregan los nodos a open en IDDFS?
				break;
			case GREEDY:
				newCandidates = new PriorityQueue<>(/* TODO: Comparator! */);
				addCandidates(node, newCandidates);
				// TODO: ¿Cómo se agregan los nodos a open en GREEDY?
				break;
			case ASTAR:
				if (!isBest(node.getState(), node.getCost())) {
					return;
				}
				newCandidates = new ArrayList<>();
				addCandidates(node, newCandidates);
				// TODO: ¿Cómo se agregan los nodos a open en A*?
				break;
		}
	}

	private void addCandidates(GPSNode node, Collection<GPSNode> candidates) {
		explosionCounter++;
		updateBest(node);
		for (GPSRule rule : problem.getRules()) {
			Optional<GPSState> newState = rule.evalRule(node.getState());
			if (newState.isPresent()) {
				GPSNode newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost());
				newNode.setParent(node);
				candidates.add(newNode);
			}
		}
	}

	private boolean isBest(GPSState state, Integer cost) {
		return !bestCosts.containsKey(state) || cost < bestCosts.get(state);
	}

	private void updateBest(GPSNode node) {
		bestCosts.put(node.getState(), node.getCost());
	}


	// GETTERS FOR THE PEOPLE!

	public Queue<GPSNode> getOpen() {
		return open;
	}

	public Map<GPSState, Integer> getBestCosts() {
		return bestCosts;
	}

	public GPSProblem getProblem() {
		return problem;
	}

	public long getExplosionCounter() {
		return explosionCounter;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isFailed() {
		return failed;
	}

	public GPSNode getSolutionNode() {
		return solutionNode;
	}

	public SearchStrategy getStrategy() {
		return strategy;
	}


	/**
	 * Abstract class representing a solver, according to a given search strategy.
	 */
	private static abstract class Solver {

		/**
		 * The searching engine.
		 */
		protected final GPSEngine engine;

		/**
		 * Constructor.
		 *
		 * @param engine The engine.
		 */
		private Solver(GPSEngine engine) {
			this.engine = engine;
		}

		/**
		 * Explodes the given {@link GPSNode}
		 *
		 * @param node The node to be exploded.
		 */
		protected abstract void explode(GPSNode node);

		protected abstract Queue<GPSNode> createQueue();

	}

	private static class DFSSolver extends Solver {


		/**
		 * Constructor.
		 *
		 * @param engine The engine.
		 */
		private DFSSolver(GPSEngine engine) {
			super(engine);
		}

		@Override
		protected void explode(GPSNode node) {
			if (engine.bestCosts.containsKey(node.getState())) {
				return;
			}
			Collection<GPSNode> newCandidates = new ArrayList<>();
			engine.addCandidates(node, newCandidates);


			// TODO: ¿Cómo se agregan los nodos a open en DFS?
		}

		@Override
		protected Queue<GPSNode> createQueue() {
			return null; // TODO: define queue
		}
	}


	/**
	 * Solver to apply the A* searching algorithm.
	 */
	private static class AStarSolver extends Solver {


		/**
		 * Constructor.
		 *
		 * @param engine The engine.
		 */
		private AStarSolver(GPSEngine engine) {
			super(engine);
		}

		@Override
		protected void explode(GPSNode node) {
			if (!engine.isBest(node.getState(), node.getCost())) {
				return;
			}
			Collection<GPSNode> newCandidates = new ArrayList<>();
			engine.addCandidates(node, newCandidates);
			newCandidates.forEach(engine.open::add);
		}

		@Override
		protected Queue<GPSNode> createQueue() {
			return new PriorityQueue<>((o1, o2) -> {
				// TODO: is necessary?
				if (o1 == null || o2 == null) {
					throw new IllegalArgumentException();
				}
				return o1.getCost() - o2.getCost();
			});
		}
	}

}
