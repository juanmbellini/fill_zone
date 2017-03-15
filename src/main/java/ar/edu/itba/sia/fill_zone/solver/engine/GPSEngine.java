package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.interfaces.GPSProblem;
import ar.edu.itba.sia.fill_zone.solver.interfaces.GPSRule;
import ar.edu.itba.sia.fill_zone.solver.interfaces.GPSState;
import ar.edu.itba.sia.fill_zone.solver.interfaces.NotApplicableException;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;


public abstract class GPSEngine {

	protected Queue<GPSNode> open;
	protected Set<GPSState> bestCosts = new HashSet<GPSState>();

	protected GPSProblem problem;
	long explosionCounter;

	// Use this variable in open set order.
	protected SearchStrategy strategy;

	public void engine(GPSProblem myProblem, SearchStrategy myStrategy) {

		problem = myProblem;
		strategy = myStrategy;

		GPSNode rootNode = new GPSNode(problem.getInitState(), 0);
		explosionCounter = 0;
		boolean finished = false;
		boolean failed = false;
		open.add(rootNode);
		while (!failed && !finished) {
			if (open.size() <= 0) {
				failed = true;
			} else {
				GPSNode currentNode = open.remove();
				if (problem.isGoal(currentNode.getState())) {
					finished = true;
					System.out.println(currentNode.getSolution());
					System.out.println("Expanded nodes: " + explosionCounter);
					System.out.println("Solution cost: " + currentNode.getCost());
				} else {
					explode(currentNode);
				}
			}
		}
		if (finished) {
			System.out.println("OK! solution found!");
		} else if (failed) {
			System.err.println("FAILED! solution not found!");
		}
	}

	private void explode(GPSNode node) {
		switch (strategy) {
			case BFS:
			case DFS:
				if (bestCosts.contains(node.getState())) {
					return;
				}
		}
		explosionCounter++;
		updateBest(node);
		for (GPSRule rule : problem.getRules()) {
			try {
				GPSNode newNode = new GPSNode(rule.evalRule(node.getState()), node.getCost() + rule.getCost());
				newNode.setParent(node);
				open.add(newNode);
			} catch (NotApplicableException e) {
				// Si no es aplicable, se saltea.
			}
		}
	}

	private void updateBest(GPSNode node) {
		bestCosts.add(node.getState());
	}

}
