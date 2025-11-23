package mega;

import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

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
    private Map<T, T> cameFrom;

    /**
     * Constructs a new GreedyBestFirstSearch instance with a specified heuristic function.
     * @param heuristic The {@link HeuristicFunction} used to estimate the cost from a node to the goal.
     */
    public GreedyBestFirstSearch(HeuristicFunction<T> heuristic) {
        this.heuristic = heuristic;
        this.cameFrom = new HashMap<>();
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

        System.out.println("Conceptual: Performing Greedy Best-First Search from " + startNode + " to " + goalNode);

        while (!openSet.isEmpty()) {
            T currentNode = openSet.poll();

            System.out.println("  Expanding node: " + currentNode);

            if (currentNode.equals(goalNode)) {
                System.out.println("  Goal reached!");
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode);

            for (SearchNode neighborGeneric : currentNode.getNeighbors()) {
                T neighbor = (T) neighborGeneric;
                if (closedSet.contains(neighbor)) {
                    continue; // Skip already evaluated nodes
                }

                if (!openSet.contains(neighbor)) {
                    cameFrom.put(neighbor, currentNode);
                    openSet.add(neighbor);
                    System.out.println("    Adding neighbor to open set: " + neighbor + " (heuristic: " + heuristic.evaluate(neighbor) + ")");
                }
            }
        }

        System.out.println("No path found.");
        return null; // No path found
    }

    /**
     * Reconstructs the path from the goal node back to the start node.
     * @param endNode The goal node from which to start reconstruction.
     * @return A {@link List} of {@link SearchNode}s representing the path.
     */
    private List<T> reconstructPath(T endNode) {
        List<T> path = new ArrayList<>();
        T current = endNode;
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }
}