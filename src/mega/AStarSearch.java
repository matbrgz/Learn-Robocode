package mega;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Implements the A* Search algorithm.
 * A* is an informed search algorithm that finds the shortest path between
 * a start node and a goal node in a graph. It is optimal and complete
 * (guaranteed to find the shortest path if one exists) given an admissible
 * and consistent heuristic function.
 * 
 * A* combines features of Dijkstra's algorithm (which guarantees optimality)
 * and Greedy Best-First Search (which aims for efficiency). It evaluates
 * nodes using a cost function `f(n) = g(n) + h(n)`, where `g(n)` is the
 * cost from the start node to node `n`, and `h(n)` is the estimated cost
 * from node `n` to the goal node (heuristic).
 * 
 * In Robocode, A* could be used for optimal pathfinding for movement,
 * especially in complex terrains or when avoiding specific enemy fire zones.
 * 
 * @param <T> The type of {@link SearchNodeWithCost} representing states in the search space.
 * 
 * @author Gates
 */
public class AStarSearch<T extends SearchNodeWithCost> {

    private HeuristicFunction<T> heuristic;

    /**
     * Constructs a new AStarSearch instance with a specified heuristic function.
     * @param heuristic The {@link HeuristicFunction} used to estimate the cost from a node to the goal.
     */
    public AStarSearch(HeuristicFunction<T> heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Finds the shortest path from a start node to a goal node using the A* search algorithm.
     * @param startNode The starting node of the search.
     * @param goalNode The target goal node.
     * @return A {@link List} of {@link T} nodes representing the shortest path from start to goal,
     *         or {@code null} if no path is found.
     */
    public List<T> findPath(T startNode, T goalNode) {
        // The set of nodes to be evaluated. Initially, only the start node is known.
        PriorityQueue<T> openSet = new PriorityQueue<>(Comparator.comparingDouble(node -> node.getGCost() + heuristic.evaluate(node)));
        openSet.add(startNode);
        startNode.setGCost(0.0); // Cost from start along best known path.

        // For each node, which node it can most efficiently be reached from.
        // Used to reconstruct the path.
        Map<T, T> cameFrom = new HashMap<>();

        // For each node, the cost of getting from the start node to that node.
        // This is stored within the SearchNodeWithCost object itself (gCost).

        System.out.println("Conceptual: Performing A* Search from " + startNode + " to " + goalNode);

        while (!openSet.isEmpty()) {
            T current = openSet.poll();

            System.out.println("  Expanding node: " + current + " (fCost: " + (current.getGCost() + heuristic.evaluate(current)) + ")");

            if (current.equals(goalNode)) {
                System.out.println("  Goal reached!");
                return reconstructPath(cameFrom, current);
            }

            for (SearchNode neighborGeneric : current.getNeighbors()) {
                T neighbor = (T) neighborGeneric; // Cast to T

                // Calculate tentative gCost from start to neighbor through current
                double tentativeGCost = current.getGCost() + current.getCostTo(neighbor);

                if (tentativeGCost < neighbor.getGCost()) {
                    cameFrom.put(neighbor, current);
                    neighbor.setGCost(tentativeGCost);

                    // If neighbor is not in openSet, add it. PriorityQueue handles updates implicitly.
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                        System.out.println("    Adding/Updating neighbor: " + neighbor + " (fCost: " + (neighbor.getGCost() + heuristic.evaluate(neighbor)) + ")");
                    }
                }
            }
        }

        System.out.println("No path found.");
        return null; // No path found
    }

    /**
     * Reconstructs the path from the goal node back to the start node
     * using the {@code cameFrom} map.
     * @param cameFrom A map where keys are nodes and values are their predecessors in the path.
     * @param current The goal node from which to start reconstruction.
     * @return A {@link List} of {@link T} nodes representing the path from start to goal.
     */
    private List<T> reconstructPath(Map<T, T> cameFrom, T current) {
        List<T> totalPath = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            totalPath.add(current);
            current = cameFrom.get(current);
        }
        totalPath.add(current); // Add the start node
        Collections.reverse(totalPath); // Reverse to get path from start to goal
        return totalPath;
    }
}
