package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import ar.edu.itba.sia.fill_zone.models.FillZoneProblem;
import ar.edu.itba.sia.fill_zone.solver.engine.GPSEngine;
import ar.edu.itba.sia.fill_zone.solver.engine.SearchStrategy;

/**
 * Main class!
 */
public class Main {

	private static final String USAGE_MESSAGE =
			"Usage: <rows> <columns> <colors> <dfs|bfs|iddfs|greedy|astar> <heuristic> <output>";


	public static void main(String[] args) {
		if (args == null || args.length < 6) {
			System.err.println(USAGE_MESSAGE);
			System.exit(1);
		}

		try {
			final int rows = Integer.parseInt(args[0]);
			final int columns = Integer.parseInt(args[1]);
			final int colors = Integer.parseInt(args[2]);
			final SearchStrategy searchStrategy = SearchStrategy.fromString(args[3]);
			final FillZoneHeuristic heuristic = FillZoneHeuristic.values()[Integer.parseInt(args[4])];
			final GPSEngine engine =
					new GPSEngine(new FillZoneProblem(rows, columns, colors, heuristic), searchStrategy);
			System.out.print("Now processing the problem... ");
			engine.findSolution();
			System.out.println("Finished");
			// TODO: print solution according to the selected output
		} catch (Throwable e) {
			System.err.println(USAGE_MESSAGE);
			System.exit(1);
		}
	}

}
