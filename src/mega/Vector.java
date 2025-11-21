package mega;

import java.awt.geom.Point2D;

/**
 * Represents a 2D vector, extending {@link Point2D.Double} to provide
 * vector-specific mathematical operations such as length, addition,
 * scalar multiplication/division, rotation, and utility methods for
 * clamping and rounding. This class is useful for handling positions,
 * velocities, and forces in a more intuitive vector-based manner.
 * 
 * @author Gates
 */
public class Vector extends Point2D.Double {

    /**
     * A constant {@link Vector} representing the zero vector (0, 0).
     */
    public static final Vector ZERO = new Vector(0, 0);

    /**
     * Constructs a new Vector with the specified x and y coordinates.
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     */
    public Vector(double x, double y) {
        super(x, y);
    }

    /**
     * Constructs a new Vector as a copy of an existing {@link Vector}.
     * @param copy The Vector to copy.
     */
    public Vector(Vector copy) {
        super(copy.getX(), copy.getY());
    }

    /**
     * Calculates the Euclidean length (magnitude) of this vector.
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    /**
     * Calculates the squared Euclidean length (magnitude) of this vector.
     * This can be more efficient than {@code length()} if only comparison
     * of lengths is needed, as it avoids a square root operation.
     * @return The squared length of the vector.
     */
    public double lengthSq() {
        return this.x*this.x + this.y*this.y;
    }

    /**
     * Adds another vector, scaled by a coefficient, to this vector.
     * Returns a new Vector representing the result. This vector remains unchanged.
     * @param v The vector to add.
     * @param coefficient The scalar coefficient to multiply {@code v} by.
     * @return A new Vector representing the sum.
     */
    public Vector add(Vector v, double coefficient) {
        return new Vector(this.getX() + coefficient * v.getX(),
                          this.getY() + coefficient * v.getY());
    }

    /**
     * Multiplies this vector by a scalar coefficient.
     * Returns a new Vector representing the scaled result. This vector remains unchanged.
     * @param coefficient The scalar coefficient to multiply by.
     * @return A new Vector representing the scaled vector.
     */
    public Vector mul(double coefficient) {
        return new Vector(coefficient * this.getX(),
                          coefficient * this.getY());
    }

    /**
     * Divides this vector by a scalar coefficient.
     * Returns a new Vector representing the divided result. This vector remains unchanged.
     * @param coefficient The scalar coefficient to divide by. Must not be zero.
     * @return A new Vector representing the divided vector.
     * @throws ArithmeticException if {@code coefficient} is zero.
     */
    public Vector div(double coefficient) {
        if (coefficient == 0) {
            throw new ArithmeticException("Division by zero in Vector.div()");
        }
        return new Vector(this.getX() / coefficient,
                          this.getY() / coefficient);
    }

    /**
     * Rotates this vector (point) around a specified origin by a given angle.
     * The angle is expected in degrees. Returns a new Vector representing the
     * rotated point. This vector remains unchanged.
     * @param angle The angle of rotation in degrees.
     * @param origin The {@link Vector} representing the point of rotation.
     * @return A new Vector representing the rotated point.
     */
    public Vector rotate(double angle, Vector origin)
    {
        double originX = origin.getX();
        double originY = origin.getY();
        double pointX = getX();
        double pointY = getY();
        double angleRadians = Math.toRadians(angle); // Convert degrees to radians
        double sinVar = Math.sin(angleRadians);
        double cosVar = Math.cos(angleRadians);
        double resX = originX + (pointX - originX) * cosVar - (pointY - originY) * sinVar;
        double resY = originY + (pointY - originY) * cosVar + (pointX - originX) * sinVar;
        return new Vector(resX, resY);
    }

    /**
     * Adds another vector to this vector with a coefficient of 1.0.
     * Equivalent to {@code add(v, 1.0)}. Returns a new Vector.
     * @param v The vector to add.
     * @return A new Vector representing the sum.
     */
    public Vector add(Vector v) {
        return this.add(v, 1.0);
    }

    /**
     * Returns a new Vector where each component is squared.
     * @return A new Vector (x*x, y*y).
     */
    public Vector square() {
        return new Vector(this.getX() * this.getX(), this.getY() * this.getY());
    }

    /**
     * Calculates the angle (in degrees, Robocode convention where 0 is up)
     * of this vector, assuming it originates from (0,0).
     * Delegates to {@link Util#getAngle(double, double)} for calculation.
     * @return The angle of the vector in degrees.
     */
    public double getAngle() {
        // Note: Util.getAngle uses negative x and negative y for the Robocode angle convention.
        // If this vector represents a direction *from* (0,0), then this is correct.
        return Util.getAngle(-this.getX(), -this.getY());
    }

    /**
     * Returns a string representation of this Vector in the format "Vector(x, y)".
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "Vector(" + this.getX() + ", " + this.getY() +")";
    }

    /**
     * Compares this Vector with another Vector for equality based on their x and y components.
     * This method performs a direct double comparison, which might be subject to
     * floating-point precision issues. For robust comparisons, consider a small epsilon.
     * @param v The other Vector to compare with.
     * @return {@code true} if both vectors have the same x and y coordinates, {@code false} otherwise.
     */
    public boolean equals(Vector v) {
        return this.getX() == v.getX() && this.getY() == v.getY();
    }

    /**
     * Rounds the x and y components of this vector to a specified number of decimal places.
     * Returns a new Vector with the rounded components. This vector remains unchanged.
     * Delegates to {@link Util#round(double, int)} for component rounding.
     * @param n The number of decimal places to round to.
     * @return A new Vector with rounded x and y components.
     */
    public Vector round(int n) {
        return new Vector(Util.round(this.getX(), n), Util.round(this.getY(), n));
    }

    /**
     * Clamps the x and y components of this vector within specified bounding boxes.
     * Returns a new Vector with the clamped components. This vector remains unchanged.
     * Delegates to {@link Util#clamp(double, double, double)} for component clamping.
     * @param xMin The minimum allowed x-coordinate.
     * @param xMax The maximum allowed x-coordinate.
     * @param yMin The minimum allowed y-coordinate.
     * @param yMax The maximum allowed y-coordinate.
     * @return A new Vector with x and y components clamped within the specified bounds.
     */
    public Vector bound(double xMin, double xMax, double yMin, double yMax) {
        return new Vector(Util.clamp(this.getX(), xMin, xMax), Util.clamp(this.getY(), yMin, yMax));
    }
}
