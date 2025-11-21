package mega;

/**
 * Represents a node in a k-dimensional tree (KD-Tree).
 * Each node stores a {@link KDPoint} and references to its left and right children,
 * along with the dimension (axis) by which the space was split at this node.
 * 
 * @param <T> The type of {@link KDPoint} stored in this node.
 * 
 * @author Gates
 */
public class KDNode<T extends KDPoint> {

    private T point;
    private int axis; // The dimension (0 for x, 1 for y, etc.) used to split the space at this node
    private KDNode<T> leftChild;
    private KDNode<T> rightChild;

    /**
     * Constructs a new KDNode.
     * @param point The {@link KDPoint} stored at this node.
     * @param axis The splitting dimension for this node.
     */
    public KDNode(T point, int axis) {
        this.point = point;
        this.axis = axis;
    }

    /**
     * Gets the {@link KDPoint} stored at this node.
     * @return The point.
     */
    public T getPoint() {
        return point;
    }

    /**
     * Gets the splitting dimension (axis) for this node.
     * @return The axis (0 for x, 1 for y, etc.).
     */
    public int getAxis() {
        return axis;
    }

    /**
     * Gets the left child of this node. Points in the left subtree have a
     * coordinate less than or equal to this node's point along the splitting axis.
     * @return The left child {@link KDNode}.
     */
    public KDNode<T> getLeftChild() {
        return leftChild;
    }

    /**
     * Sets the left child of this node.
     * @param leftChild The {@link KDNode} to set as the left child.
     */
    public void setLeftChild(KDNode<T> leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Gets the right child of this node. Points in the right subtree have a
     * coordinate greater than this node's point along the splitting axis.
     * @return The right child {@link KDNode}.
     */
    public KDNode<T> getRightChild() {
        return rightChild;
    }

    /**
     * Sets the right child of this node.
     * @param rightChild The {@link KDNode} to set as the right child.
     */
    public void setRightChild(KDNode<T> rightChild) {
        this.rightChild = rightChild;
    }
}
