package mega;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Implements the Greedy Best-First Search (GBFS) algorithm.
 * GBFS is an informed search algorithm that expands nodes based on an
 * evaluation function (heuristic) that estimates the cost to reach the goal.
 * It always selects the node that appears "closest" to the goal, making it
 * greedy. Unlike A*, it does not consider the cost from the start node,
 * only the estimated cost to the goal.
 * 
 * In Robocode, GBFS could be used for pathfinding (e.g., finding a path
 * to a safe location or a strategic position) where an efficient, though
 * not necessarily optimal, solution is desired.
 * 
 * @param <T> The type of {@link SearchNode} representing states in the search space.
 * 
 * @author Gates
 */
public class GreedyBestFirstSearch<T extends SearchNode> {

    private HeuristicFunction<T> heuristic;

    /**
     * Constructs a new GreedyBestFirstSearch instance with a specified heuristic function.
     * @param heuristic The {@link HeuristicFunction} used to estimate the cost from a node to the goal.
     */
    public GreedyBestFirstSearch(HeuristicFunction<T> heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Finds a path from a start node to a goal node using the Greedy Best-First Search algorithm.
     * @param startNode The starting node of the search.
     * @param goalNode The target goal node.
     * @return A {@link List} of {@link SearchNode}s representing the path from start to goal,
     *         or {@code null} if no path is found.
     */
    public List<T> findPath(T startNode, T goalNode) {
        // Priority queue to store nodes to be evaluated, ordered by heuristic value
        PriorityQueue<T> openSet = new PriorityQueue<>(Comparator.comparingDouble(heuristic::evaluate));
        openSet.add(startNode);

        // Set to store already evaluated nodes
        Set<T> closedSet = new HashSet<>();

        // Map to reconstruct the path
        // For GBFS, we only need to store the predecessor for path reconstruction,
        // as we don't track the g-score (cost from start).
        // This is a simplified conceptual implementation. A real path reconstruction
        // would typically be part of the SearchNode or a separate PathReconstructor.

        System.out.println("Conceptual: Performing Greedy Best-First Search from " + startNode + " to " + goalNode);

        while (!openSet.isEmpty()) {
            T currentNode = openSet.poll();

            System.out.println("  Expanding node: " + currentNode);

            if (currentNode.equals(goalNode)) {
                System.out.println("  Goal reached!");
                // Path reconstruction (conceptual)
                return reconstructPath(currentNode, startNode); // Needs a way to store predecessors
            }

            closedSet.add(currentNode);

            for (T neighbor : currentNode.getNeighbors()) {
                if (closedSet.contains(neighbor)) {
                    continue; // Skip already evaluated nodes
                }

                if (!openSet.contains(neighbor)) {
                    // This is a new node, add it to the open set
                    // In a full implementation, `neighbor` would need to store `currentNode` as its predecessor
                    // to allow for path reconstruction.
                    openSet.add(neighbor);
                    System.out.println("    Adding neighbor to open set: " + neighbor + " (heuristic: " + heuristic.evaluate(neighbor) + ")");
                }
            }
        }

        System.out.println("No path found.");
        return null; // No path found
    }

    /**
     * (Conceptual) Reconstructs the path from the goal node back to the start node.
     * This method assumes that each {@link SearchNode} can somehow store its predecessor
     * in the path found by the search algorithm.
     * @param endNode The goal node from which to start reconstruction.
     * @param startNode The start node, to terminate reconstruction.
     * @return A {@link List} of {@link SearchNode}s representing the path.
     */
    private List<T> reconstructPath(T endNode, T startNode) {
        List<T> path = new ArrayList<>();
        T current = endNode;
        while (current != null && !current.equals(startNode)) {
            path.add(0, current); // Add to the beginning to get correct order
            // current = current.getPredecessor(); // This method would be needed in SearchNode
            break; // Placeholder, as predecessor isn't stored in this conceptual example
        }
        if (current != null && current.equals(startNode)) {
            path.add(0, startNode);
        }
        return path;
    }
}
