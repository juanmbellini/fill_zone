package ar.edu.itba.sia.fill_zone.io;

import ar.edu.itba.sia.fill_zone.models.Board;
import ar.edu.itba.sia.fill_zone.models.FillZoneState;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;
import ar.edu.itba.sia.fill_zone.solver.engine.GPSEngine;
import ar.edu.itba.sia.fill_zone.solver.engine.GPSNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;
import java.util.stream.IntStream;

/**
 * Created by Juan Marcos Bellini on 1/4/17.
 */
public class SolutionWriter {


	/**
	 * Prints the solution the given {@code engine} has found.
	 *
	 * @param engine The engine providing the solution to be printed.
	 */
	public static void printToConsole(GPSEngine engine) {
		printToStream(engine, System.out);
	}

	/**
	 * Saves the solution the given {@code engine} has found in a file according to the given {@code path}.
	 *
	 * @param engine The engine providing the solution to be printed.
	 * @param path   The path of the file.
	 */
	public static void printToFile(GPSEngine engine, String path) {
		printToStream(engine, createFilePrintStream(path), false);
	}


	/**
	 * Creates a {@link PrintStream} (specifically, a file stream if no IO error is encountered,
	 * or {@link System#out} otherwise).
	 *
	 * @param path The path of the file.
	 * @return A {@link PrintStream} (specifically, a file stream if no IO error is encountered,
	 * or {@link System#out} otherwise).
	 */
	private static PrintStream createFilePrintStream(String path) {
		try {
			final File file = new File(path);
			final File parent = file.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IOException();
			}
			if (!file.exists() && !file.createNewFile()) {
				throw new IOException();
			}
			return new PrintStream(new FileOutputStream(file, false));
		} catch (IOException e) {
			System.err.println("Solution could not be saved into file.");
			System.err.println("Fallback into stdout...");
			return System.out;
		}

	}

	/**
	 * Prints the solution the given {@code engine} has found into the given {@code printStream},
	 * letting the {@link #printBoard(Board, PrintStream)} method decide if colors must be included.
	 *
	 * @param engine      The engine providing the solution to be printed.
	 * @param printStream The stream to which the solution will be printed.
	 */
	private static void printToStream(GPSEngine engine, PrintStream printStream) {
		printToStream(engine, printStream, null);
	}

	/**
	 * Prints the solution the given {@code engine} has found into the given {@code printStream}.
	 *
	 * @param engine        The engine providing the solution to be printed.
	 * @param printStream   The stream to which the solution will be printed.
	 * @param includeColors Indicates if colors must be included in the printed solution.
	 */
	private static void printToStream(GPSEngine engine, PrintStream printStream, Boolean includeColors) {
		if (engine.isFailed()) {
			printStream.println("No solution was found.");
			return;
		}

		final long explosionCounter = engine.getExplosionCounter();
		if (explosionCounter == 0) {
			printStream.println("Engine was not executed yet.");
			return;
		}
		printStream.println("A solution was found.");
		printStream.println();
		printStream.println();

		final Stack<GPSNode> pathToSolution = getPathToSolution(engine.getSolutionNode());
		final int depth = pathToSolution.size() - 1;

		printStream.println("Initial board: ");
		printFillZoneState(pathToSolution.pop().getState(), printStream, includeColors);
		printStream.println();
		while (!pathToSolution.isEmpty()) {
			GPSNode node = pathToSolution.pop();
			printStream.println("Applied rule: " + node.getGenerationRule().getName());
			printStream.println("Board: ");
			printFillZoneState(node.getState(), printStream, includeColors);
			printStream.println();
		}

		printStream.println();
		printStream.println();
		printStream.println("Exploded nodes: " + explosionCounter);
		printStream.println("Solution depth: " + depth);
	}

	/**
	 * Prints a fill zone state (the board).
	 *
	 * @param state         The state to be printed.
	 * @param printStream   The stream to which the state will be printed.
	 * @param includeColors Indicates if colors must be included.
	 */
	private static void printFillZoneState(GPSState state, PrintStream printStream, Boolean includeColors) {
		Board board = ((FillZoneState) state).getBoard();
		if (includeColors == null) {
			printBoard(board, printStream);
		} else {
			printBoard(board, printStream, includeColors);
		}
		printStream.println("----------");
	}

	/**
	 * Calculates the depth of the solution and the path from the initial node to the solution node.
	 *
	 * @param solutionNode The solution node.
	 * @return A class wrapping the depth and the path.
	 */
	private static Stack<GPSNode> getPathToSolution(GPSNode solutionNode) {
		if (solutionNode == null) {
			throw new IllegalArgumentException();
		}
		Stack<GPSNode> pathToSolution = new Stack<>();
		while (solutionNode != null) {
			pathToSolution.push(solutionNode);
			solutionNode = solutionNode.getParent();
		}
		return pathToSolution;
	}


	/**
	 * Prints the board, stating as condition for including colors the amount of colors in the board being less than 10.
	 *
	 * @param board  The board to be printed.
	 * @param stream The stream to which the board will be printed.
	 */
	private static void printBoard(Board board, PrintStream stream) {
		printBoard(board, stream, board.getColors() <= SolutionWriter.ColorsCode.values().length);
	}

	/**
	 * Prints the board, including colors according to {@code includeColors} parameter.
	 *
	 * @param board         The board to be printed.
	 * @param stream        The stream to which the board will be printed.
	 * @param includeColors Indicates whether colors must be included.
	 */
	private static void printBoard(Board board, PrintStream stream, boolean includeColors) {
		if (includeColors) {
			IntStream.range(0, board.getRows())
					.forEach(row -> {
						IntStream.range(0, board.getColumns())
								.forEach(col -> stream.print(ColorsCode
										.values()[board.getMatrix()[row][col]] +
										"" +
										board.getMatrix()[row][col] +
										"\t" +
										ColorsCode.RESET));
						stream.println();
					});
			return;
		}
		IntStream.range(0, board.getRows())
				.forEach(row -> {
					IntStream.range(0, board.getColumns())
							.forEach(col -> stream.print(board.getMatrix()[row][col] + "\t"));
					stream.println();
				});

	}


	// TODO: change names
	// TODO: javadocs
	private enum ColorsCode {
		RESET("\u001B[0m"),
		ONE("\u001B[31m"),
		TWO("\u001B[36m"),
		THREE("\u001B[34m"),
		FOUR("\u001B[35m"),
		FIVE("\u001B[38m"),
		SIX("\u001B[33m"),
		SEVEN("\u001B[32m"),
		EIGHT("\u001B[38m"),
		NINE("\u001B[30m");

		private final String color;

		ColorsCode(String color) {
			this.color = color;
		}

		public String toString() {
			return this.color;
		}

	}

}
