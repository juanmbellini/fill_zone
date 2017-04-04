package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class implementing the IDDFS searching strategy using the border approach.
 */
public class IDDFSBorderSolver extends Solver {

	/**
	 * Map containing the depth of each node.
	 */
	private final Map<GPSNode, Long> depths;

	/**
	 * The actual depth of the algorithm.
	 */
	private long actualDepth;

	public IDDFSBorderSolver(GPSProblem problem) {
		super(problem);
		depths = new HashMap<>();
		clearSolver();
	}

	@Override
	public void findSolution() {
		clearSolver();
		depths.put(initialNode, 0L);
		while (actualDepth < Long.MAX_VALUE) {
			List<GPSNode> actualDepthNodes = depths.keySet().stream()
					.filter(node -> depths.get(node) == actualDepth)
					.collect(Collectors.toList());
			final long childDepth = actualDepth + 1;
			for (GPSNode node : actualDepthNodes) {
				if (problem.isGoal(node.getState())) {
					succeed(node);
					return;
				}
				applyRules(node).stream()
						.filter(child -> depths.get(child) == null || childDepth < depths.get(child))
						.forEach(child -> depths.put(child, childDepth));
			}
			actualDepth++;
		}
		fail();
	}


	@Override
	protected void clearSolver() {
		super.clearSolver();
		depths.clear();
		actualDepth = 0;
	}

	@Override
	public List<GPSNode> getOpenedNodes() {
		return null;
	}

	@Override
	protected void succeed(GPSNode solutionNode) {
		super.succeed(solutionNode);
		GPSNode node = solutionNode;
		while (node != null) {
			updateBest(node);
			node = node.getParent();
		}
	}
}
