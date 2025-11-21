package mega;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Implements a k-dimensional tree (KD-Tree) data structure.
 * A KD-Tree is a space-partitioning data structure for organizing points in a
 * k-dimensional space. It is particularly useful for nearest neighbor searches
 * and range searches.
 * 
 * In Robocode, a KD-Tree could be used to:
 * <ul>
 *     <li>Efficiently find the nearest enemy robot or bullet wave.</li>
 *     <li>Optimize spatial queries for grouping enemies (clustering).</li>
 *     <li>Speed up collision detection or threat assessment by quickly identifying
 *         nearby objects.</li>
 * </ul>
 * 
 * This implementation builds a static KD-Tree. For dynamic environments where
 * points are frequently added or removed, a re-balancing strategy or a different
 * data structure might be more appropriate.
 * 
 * @param <T> The type of {@link KDPoint} that the KD-Tree will store.
 * 
 * @author Gates
 */
public class KDTree<T extends KDPoint> {

    private KDNode<T> root;
    private int k; // Number of dimensions

    /**
     * Constructs a new KD-Tree from a list of points.
     * The tree is built recursively by splitting the points along alternating
     * dimensions based on the median value.
     * @param points The list of {@link KDPoint}s to build the tree from.
     * @param k The number of dimensions for the points.
     */
    public KDTree(List<T> points, int k) {
        this.k = k;
        this.root = buildTree(points, 0);
    }

    /**
     * Recursively builds the KD-Tree.
     * @param points The current list of points for this subtree.
     * @param depth The current depth of the tree, used to determine the splitting dimension.
     * @return The root {@link KDNode} of the built subtree.
     */
    private KDNode<T> buildTree(List<T> points, int depth) {
        if (points == null || points.isEmpty()) {
            return null;
        }

        // Determine the splitting dimension (axis)
        int axis = depth % k;

        // Sort points along the current axis and find the median
        Collections.sort(points, (p1, p2) -> Double.compare(p1.getCoordinate(axis), p2.getCoordinate(axis)));
        
        int medianIndex = points.size() / 2;
        T medianPoint = points.get(medianIndex);

        // Create a new node with the median point
        KDNode<T> node = new KDNode<>(medianPoint, axis);

        // Recursively build left and right subtrees
        node.setLeftChild(buildTree(points.subList(0, medianIndex), depth + 1));
        node.setRightChild(buildTree(points.subList(medianIndex + 1, points.size()), depth + 1));

        return node;
    }

    /**
     * Finds the nearest neighbor to a query point in the KD-Tree.
     * @param queryPoint The {@link KDPoint} for which to find the nearest neighbor.
     * @return The nearest {@link KDPoint} found in the tree, or {@code null} if the tree is empty.
     */
    public T findNearestNeighbor(T queryPoint) {
        if (root == null) {
            return null;
        }
        // Call recursive helper function
        return findNearestNeighbor(root, queryPoint, root.getPoint(), 0);
    }

    /**
     * Recursive helper for finding the nearest neighbor.
     * @param node The current node being visited.
     * @param queryPoint The point to find the nearest neighbor for.
     * @param bestPoint The best point found so far.
     * @param depth The current depth of the tree.
     * @return The nearest {@link KDPoint} found.
     */
    private T findNearestNeighbor(KDNode<T> node, T queryPoint, T bestPoint, int depth) {
        if (node == null) {
            return bestPoint;
        }

        T currentPoint = node.getPoint();
        double currentDistance = distance(queryPoint, currentPoint);
        double bestDistance = distance(queryPoint, bestPoint);

        // Update bestPoint if currentPoint is closer
        if (currentDistance < bestDistance) {
            bestPoint = currentPoint;
            bestDistance = currentDistance;
        }

        int axis = node.getAxis();
        double queryCoord = queryPoint.getCoordinate(axis);
        double nodeCoord = node.getPoint().getCoordinate(axis);

        // Decide which subtree to search first (closer to query point)
        KDNode<T> firstChild = (queryCoord < nodeCoord) ? node.getLeftChild() : node.getRightChild();
        KDNode<T> secondChild = (queryCoord < nodeCoord) ? node.getRightChild() : node.getLeftChild();

        // Recursively search the first child
        bestPoint = findNearestNeighbor(firstChild, queryPoint, bestPoint, depth + 1);
        bestDistance = distance(queryPoint, bestPoint);

        // Check if there's a possibility of a closer point in the other subtree
        // (i.e., if the splitting plane is closer than the current best distance)
        if (Math.abs(queryCoord - nodeCoord) < bestDistance) {
            bestPoint = findNearestNeighbor(secondChild, queryPoint, bestPoint, depth + 1);
        }

        return bestPoint;
    }

    /**
     * Calculates the Euclidean distance between two {@link KDPoint}s.
     * @param p1 The first point.
     * @param p2 The second point.
     * @return The Euclidean distance between the points.
     */
    private double distance(T p1, T p2) {
        double sumSq = 0;
        for (int i = 0; i < k; i++) {
            sumSq += Math.pow(p1.getCoordinate(i) - p2.getCoordinate(i), 2);
        }
        return Math.sqrt(sumSq);
    }

    /**
     * (Conceptual) Performs a range search, returning all points within a
     * specified k-dimensional bounding box.
     * @param minCoords An array of minimum coordinates for each dimension.
     * @param maxCoords An array of maximum coordinates for each dimension.
     * @return A list of {@link KDPoint}s within the specified range.
     */
    public List<T> rangeSearch(double[] minCoords, double[] maxCoords) {
        // Implementation for range search
        System.out.println("Conceptual: Performing range search.");
        return new ArrayList<>();
    }
}
