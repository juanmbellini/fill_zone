package ar.edu.itba.sia.fill_zone.solver.engine;

import ar.edu.itba.sia.fill_zone.solver.api.GPSRule;
import ar.edu.itba.sia.fill_zone.solver.api.GPSState;

public class GPSNode {

	/**
	 * The state this node holds.
	 */
	private final GPSState state;

	/**
	 * The node that gave origin to this node.
	 */
	private final GPSNode parent;

	/**
	 * The cost of reaching this node from the initial node.
	 */
	private final Integer cost;

	/**
	 * The rule appplied from the parent node to give origin to this node.
	 */
	private final GPSRule generationRule;

	/**
	 * Constructor.
	 *
	 * @param state          The state to hold in the new node.
	 * @param cost           The cost of reaching the new node.
	 * @param generationRule The rule that gives origin to the new node.
	 * @param parent         The new node's parent.
	 */
	public GPSNode(GPSState state, Integer cost, GPSRule generationRule, GPSNode parent) {
		this.state = state;
		this.cost = cost;
		this.generationRule = generationRule;
		this.parent = parent;
	}

	/**
	 * Parent getter.
	 *
	 * @return This node parent.
	 */
	public GPSNode getParent() {
		return parent;
	}


	/**
	 * State getter.
	 *
	 * @return Thie state this node holds.
	 */
	public GPSState getState() {
		return state;
	}

	/**
	 * Cost getter.
	 *
	 * @return The cost of reaching this node from the initial node.
	 */
	public Integer getCost() {
		return cost;
	}

	/**
	 * Generation rule getter.
	 *
	 * @return The rule that gives origin to this node.
	 */
	public GPSRule getGenerationRule() {
		return generationRule;
	}

	@Override
	public String toString() {
		return state.toString();
	}

	/**
	 * Returns a {@link String} representation of nodes from the initial node to this node.
	 *
	 * @return A {@link String} representation of nodes from the initial node to this node.
	 */
	public String getSolution() {
		if (this.parent == null) {
			return this.state.toString();
		}
		return this.parent.getSolution() + this.state.toString();
	}



	/*
	 * Equals and hashCode based on GPSState.
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPSNode other = (GPSNode) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return state.hashCode();
	}
}
