package mega;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Implements a simple K-Nearest Neighbors (KNN) classifier.
 * KNN is a non-parametric, lazy learning algorithm used for classification
 * and regression. In classification, the output is a class membership. An
 * object is classified by a majority vote of its neighbors, with the object
 * being assigned to the class most common among its K nearest neighbors.
 * 
 * This implementation assumes that {@link DataPoint}s have numerical features
 * and a categorical label. It uses Euclidean distance to find neighbors.
 * 
 * @param <T> The type of {@link DataPoint} that this classifier will operate on.
 * 
 * @author Gates
 */
public class SimpleKNearestNeighbors<T extends DataPoint> {

    private List<T> trainingData;
    private int k; // Number of neighbors to consider

    /**
     * Constructs a new SimpleKNearestNeighbors classifier.
     * @param trainingData The list of {@link DataPoint}s to use for training.
     * @param k The number of nearest neighbors to consider for classification.
     *          Must be a positive integer.
     * @throws IllegalArgumentException if k is not positive.
     */
    public SimpleKNearestNeighbors(List<T> trainingData, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("K must be a positive integer.");
        }
        this.trainingData = trainingData;
        this.k = k;
    }

    /**
     * Classifies a new {@link DataPoint} by finding its K nearest neighbors
     * in the training data and determining the majority class among them.
     * @param newPoint The {@link DataPoint} to classify.
     * @return The predicted label (Object) for the new data point.
     * @throws IllegalStateException if no training data is provided or if
     *         no clear majority class can be determined (e.g., tie).
     */
    public Object classify(T newPoint) {
        if (trainingData.isEmpty()) {
            throw new IllegalStateException("No training data provided.");
        }

        // Calculate distances to all training data points
        List<NeighborDistance<T>> neighborDistances = new ArrayList<>();
        for (T data : trainingData) {
            double distance = euclideanDistance(newPoint.getFeatures(), data.getFeatures());
            neighborDistances.add(new NeighborDistance<>(data, distance));
        }

        // Sort by distance and get the K nearest neighbors
        Collections.sort(neighborDistances, Comparator.comparingDouble(nd -> nd.distance));
        List<T> kNearestNeighbors = new ArrayList<>();
        for (int i = 0; i < Math.min(k, neighborDistances.size()); i++) {
            kNearestNeighbors.add(neighborDistances.get(i).neighbor);
        }

        // Determine the majority class among the K nearest neighbors
        Map<Object, Integer> classCounts = new HashMap<>();
        for (T neighbor : kNearestNeighbors) {
            Object label = neighbor.getLabel();
            if (label != null) {
                classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
            }
        }

        // Find the class with the most votes
        Object majorityClass = null;
        int maxCount = -1;
        for (Map.Entry<Object, Integer> entry : classCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                majorityClass = entry.getKey();
            }
        }

        if (majorityClass == null) {
             throw new IllegalStateException("Could not determine a majority class (e.g., no labels, or a tie without tie-breaking logic).");
        }

        return majorityClass;
    }

    /**
     * Calculates the Euclidean distance between two feature vectors.
     * @param features1 The first feature vector.
     * @param features2 The second feature vector.
     * @return The Euclidean distance.
     * @throws IllegalArgumentException if feature vectors have different lengths.
     */
    private double euclideanDistance(double[] features1, double[] features2) {
        if (features1.length != features2.length) {
            throw new IllegalArgumentException("Feature vectors must have the same length.");
        }
        double sum = 0;
        for (int i = 0; i < features1.length; i++) {
            sum += Math.pow(features1[i] - features2[i], 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * Helper class to store a neighbor and its distance.
     */
    private static class NeighborDistance<T extends DataPoint> {
        T neighbor;
        double distance;

        NeighborDistance(T neighbor, double distance) {
            this.neighbor = neighbor;
            this.distance = distance;
        }
    }
}
