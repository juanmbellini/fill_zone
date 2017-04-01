package ar.edu.itba.sia.fill_zone;

import ar.edu.itba.sia.fill_zone.io.BoardReader;
import ar.edu.itba.sia.fill_zone.io.exception.WrongBoardFormatException;
import ar.edu.itba.sia.fill_zone.models.Board;
import ar.edu.itba.sia.fill_zone.models.FillZoneHeuristic;
import ar.edu.itba.sia.fill_zone.models.FillZoneProblem;
import ar.edu.itba.sia.fill_zone.models.FillZoneState;
import ar.edu.itba.sia.fill_zone.solver.engine.GPSEngine;
import ar.edu.itba.sia.fill_zone.solver.engine.GPSNode;
import ar.edu.itba.sia.fill_zone.solver.engine.SearchStrategy;

import java.io.IOException;

/**
 * Main class!
 */
public class Main {

	private static final String USAGE_MESSAGE =
			"Usage: <rows> <columns> <colors> <dfs|bfs|iddfs|greedy|astar> <heuristic> <output>";


	public static void main(String[] args) throws IOException, WrongBoardFormatException {
		Board board = BoardReader.readBoard("/Users/natinavas/Documents/ITBA/SIA/fill_zone/src/main/resources/Board2");
//      if (args == null || args.length < 6) {
//          System.err.println(USAGE_MESSAGE);
//          System.exit(1);
//      }
//      try {
//          final int rows = Integer.parseInt(args[0]);
//          final int columns = Integer.parseInt(args[1]);
//          final int colors = Integer.parseInt(args[2]);
		//final SearchStrategy searchStrategy = SearchStrategy.fromString(args[3]);
		//final FillZoneHeuristic heuristic = FillZoneHeuristic.values()[Integer.parseInt(args[4])];
		final GPSEngine engine =
				//new GPSEngine(new FillZoneProblem(rows, columns, colors, heuristic), searchStrategy);
				new GPSEngine(new FillZoneProblem(board, FillZoneHeuristic.REMAINING_GROUPS), SearchStrategy.GREEDY);
		System.out.print("Now processing the problem... ");
		engine.findSolution();
		System.out.println("Finished");
//      } catch (Throwable e) {
//          System.err.println(USAGE_MESSAGE);
//          System.exit(1);
//      }
		printNodes(engine.getSolutionNode());
		System.out.println("node count " + nodeCount(engine.getSolutionNode()));
		System.out.println("nodos explotados " + engine.getExplosionCounter());
	}

}
