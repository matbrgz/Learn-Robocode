package mega;

import java.util.List;

/**
 * Interface representing a node in a search space for algorithms like
 * Greedy Best-First Search or A*.
 * Each node typically represents a state in the problem and can provide
 * information about its neighbors and other properties relevant to the search.
 * 
 * @author Gates
 */
public interface SearchNode {

    /**
     * Returns a list of neighboring nodes that can be reached from this node.
     * These neighbors represent valid transitions or actions in the search space.
     * @return A {@link List} of {@link SearchNode}s.
     */
    List<? extends SearchNode> getNeighbors();

    /**
     * Checks if this node is equal to another node.
     * This is crucial for determining if the goal node has been reached
     * and for managing visited nodes in the search algorithm.
     * @param other The other object to compare.
     * @return {@code true} if the nodes are considered equal, {@code false} otherwise.
     */
    @Override
    boolean equals(Object other);

    /**
     * Returns a hash code value for the node.
     * This method must be consistent with the {@code equals} method for
     * proper functioning in data structures like {@link java.util.HashSet}
     * and {@link java.util.HashMap}.
     * @return The hash code for the node.
     */
    @Override
    int hashCode();
}
