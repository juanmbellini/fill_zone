package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSProblem;

/**
 * Different search strategy algorithm types
 */
public enum SearchStrategy {
	BFS {
		@Override
		Solver createSolver(GPSProblem problem) {
			return new BFSSolver(problem);
		}
	}, DFS {
		@Override
		Solver createSolver(GPSProblem problem) {
			return new DFSSolver(problem);
		}
	}, IDDFS {
		@Override
		Solver createSolver(GPSProblem problem) {
			return new IDDFSSolver(problem);
		}
	}, IDDFS_BORDER {
		@Override
		Solver createSolver(GPSProblem problem) {
			return new IDDFSBorderSolver(problem);
		}
	}, GREEDY {
		@Override
		Solver createSolver(GPSProblem problem) {
			return new GreedySolver(problem);
		}
	}, ASTAR {
		@Override
		Solver createSolver(GPSProblem problem) {
			return new AStarSolver(problem);
		}
	};

	/**
	 * Creates a solver according to the strategy to be used.
	 *
	 * @param problem The problem to be solved.
	 * @return The solver.
	 */
	/* package */
	abstract Solver createSolver(GPSProblem problem);

	public static SearchStrategy fromString(String name) {
		return valueOf(name.replace('-', '_').toUpperCase());
	}

	@Override
	public String toString() {
		return super.toString().replace('_', '-').toLowerCase();
	}
}
