package mega;

/**
 * Interface representing a k-dimensional point that can be stored in a {@link KDTree}.
 * Implementations of this interface should provide a way to access their
 * coordinates for each dimension.
 * 
 * @author Gates
 */
public interface KDPoint {

    /**
     * Returns the coordinate value of this point along the specified dimension.
     * @param dimension The dimension index (e.g., 0 for x, 1 for y in a 2D space).
     * @return The coordinate value for the given dimension.
     * @throws IllegalArgumentException if the dimension index is out of bounds.
     */
    double getCoordinate(int dimension);

    /**
     * Returns the number of dimensions this point has.
     * @return The dimensionality of the point.
     */
    int getDimensions();
}
