package ar.edu.itba.sia.fill_zone.solver;

public enum SearchStrategy {
	BFS, DFS, IDDFS, GREEDY, ASTAR;

	public static SearchStrategy fromString(String name) {
		return SearchStrategy.valueOf(name.toUpperCase());
	}
}
