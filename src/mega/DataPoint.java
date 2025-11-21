package mega;

/**
 * Interface representing a generic data point with a feature vector.
 * This interface is intended to be used with Machine Learning algorithms
 * that operate on numerical features.
 * 
 * Concrete implementations will provide methods to access the features
 * and potentially a label or classification for supervised learning tasks.
 * 
 * @author Gates
 */
public interface DataPoint {

    /**
     * Returns the feature vector of this data point.
     * The array contains numerical values representing different attributes
     * or characteristics of the data point.
     * @return A double array representing the feature vector.
     */
    double[] getFeatures();

    /**
     * (Optional) Returns the label or class associated with this data point.
     * This is typically used in supervised learning contexts.
     * @return An object representing the label, or null if not applicable.
     */
    Object getLabel();
}
