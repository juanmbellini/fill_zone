package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 30/3/17.
 */
public final class IDDFSSolver extends Solver {

	private long actualDepth;



	/**
	 * Constructor.
	 *
	 * @param problem The problem to solve.
	 */
	public IDDFSSolver(GPSProblem problem) {
		super(problem);
		clearSolver();
	}


	@Override
	public void findSolution() {
		clearSolver();

		while (actualDepth < Long.MAX_VALUE) {
//			getBestCosts().clear(); // Remove costs in order to set as not reached all nodes
			IDDFSNode result = recursiveSolve(new IDDFSNode(initialNode), 0);
			if (result != null) {
				succeed(result.getGPSNode());
				return;
			}
			actualDepth++;
		}
		fail();
	}


	private IDDFSNode recursiveSolve(IDDFSNode node, int depth) {

		if (depth > actualDepth || node.isMarked()) {
			return null;
		}
		node.mark();
		if (problem.isGoal(node.getGPSNode().getState())) {
			return node;
		}
		List<IDDFSNode> children = applyRules(node.getGPSNode()).stream()
				.map(IDDFSNode::new)
				.collect(Collectors.toList());

		for (IDDFSNode child : children) {
			IDDFSNode result = recursiveSolve(child, depth + 1);
			if (result != null) {
				updateBest(result.getGPSNode());
				return result;
			}
		}
		node.unMark();
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
	}


	private static class IDDFSNode {

		private boolean marked;

		private final GPSNode node;


		private IDDFSNode(GPSNode node) {
			this.node = node;
			marked = false;
		}


		public GPSNode getGPSNode() {
			return node;
		}

		public boolean isMarked() {
			return marked;
		}

		private void mark() {
			this.marked = true;
		}

		private void unMark() {
			this.marked = false;
		}


	}


}
