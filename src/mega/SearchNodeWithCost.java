package mega;

/**
 * Interface representing a node in a search space for algorithms like A*,
 * which requires tracking the cost from the start node to the current node.
 * Extends {@link SearchNode} to include cost-related methods.
 * 
 * @author Gates
 */
public interface SearchNodeWithCost extends SearchNode {

    /**
     * Gets the actual cost from the start node to this node (g-cost).
     * @return The g-cost.
     */
    double getGCost();

    /**
     * Sets the actual cost from the start node to this node (g-cost).
     * @param gCost The new g-cost value.
     */
    void setGCost(double gCost);

    /**
     * Estimates the cost from this node to a given neighboring node.
     * This represents the cost of moving between this node and its neighbor.
     * @param neighbor The neighboring node.
     * @return The cost to move from this node to the neighbor.
     */
    double getCostTo(SearchNodeWithCost neighbor);
}
