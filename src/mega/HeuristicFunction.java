package mega;

/**
 * Interface for a heuristic function used in informed search algorithms
 * like Greedy Best-First Search and A*.
 * A heuristic function estimates the cost from a given node to the goal node.
 * For these algorithms to perform well, the heuristic should be admissible
 * (never overestimate the cost) and consistent.
 * 
 * @param <T> The type of {@link SearchNode} that the heuristic will evaluate.
 * 
 * @author Gates
 */
public interface HeuristicFunction<T extends SearchNode> {

    /**
     * Evaluates the heuristic cost from a given node to the goal node.
     * @param node The {@link SearchNode} for which to estimate the cost to the goal.
     * @return A double representing the estimated cost from the node to the goal.
     */
    double evaluate(T node);
}
