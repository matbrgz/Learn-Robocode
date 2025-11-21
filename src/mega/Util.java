package mega;

import java.util.Random;
import robocode.util.Utils; // Robocode's utility class, not mega.Util.

/**
 * A utility class providing a collection of static helper methods for common
 * mathematical operations, Robocode-specific calculations, and battlefield
 * boundary checks. These methods are designed to simplify robot logic and
 * provide reusable functionalities across different components.
 * 
 * @author VisualMelon
 * @author thomasleese
 * @author Cyanogenoid
 * @author Gates (for improvements)
 */
public class Util {

    /**
     * A shared {@link Random} instance for generating pseudo-random numbers
     * across different parts of the robot's logic.
     */
    public static final Random RANDOM = new Random();
    /**
     * The standard width of a Robocode robot, in pixels.
     */
    public static final int ROBOT_WIDTH = 48;
    /**
     * The standard height of a Robocode robot, in pixels.
     */
    public static final int ROBOT_HEIGHT = 48;

    /**
     * Calculates the mathematical modulo, ensuring the result is always
     * non-negative, even if the input {@code c} is negative.
     * @param c The dividend.
     * @param mod The modulus.
     * @return The non-negative remainder of {@code c} divided by {@code mod}.
     */
    public static int modulo(int c, int mod) {
        int result = c % mod;
        return result < 0 ? mod + result : result;
    }

    /**
     * Clamps a given value {@code val} between a specified lower and upper bound.
     * If {@code val} is less than {@code lower}, {@code lower} is returned.
     * If {@code val} is greater than {@code upper}, {@code upper} is returned.
     * Otherwise, {@code val} itself is returned.
     * @param val The value to clamp.
     * @param lower The lower bound.
     * @param upper The upper bound.
     * @return The clamped value.
     */
    public static double clamp(double val, double lower, double upper) {
        if (val < lower)
            return lower;
        if (val > upper)
            return upper;
        return val;
    }

    /**
     * Rounds a double-precision floating-point number to a specified number of decimal digits.
     * @param d The double value to round.
     * @param digits The number of decimal places to round to.
     * @return The rounded double value.
     */
    public static double round(double d, int digits) {
        double factor = Math.pow(10.0, (double)digits);
        return (double)Math.round(d * factor) / factor;
    }

    /**
     * Rounds a double-precision floating-point number to one decimal place.
     * This is a convenience method equivalent to {@code round(d, 1)}.
     * @param d The double value to round.
     * @return The double value rounded to one decimal place.
     */
    public static double roundTo1(double d) {
        return (double)Math.round(d * 10) / 10;
    }

    /**
     * Calculates the speed of a bullet based on its firepower.
     * Robocode's formula: speed = 20 - (3 * firepower).
     * @param firepower The firepower of the bullet (0.1 to 3.0).
     * @return The speed of the bullet in pixels per turn.
     */
    public static double firepowerToSpeed(double firepower) {
        return 20 - 3 * firepower;
    }

    /**
     * Calculates the firepower required to achieve a certain bullet speed.
     * This is the inverse of the Robocode formula: firepower = (20 - speed) / 3.
     * @param speed The desired speed of the bullet.
     * @return The firepower corresponding to the given speed.
     */
    public static double speedToFirepower(double speed) {
        return (20 - speed) / 3;
    }

    /**
     * Calculates the maximum turn rate of a robot's body in degrees per turn
     * based on its current speed. Robocode's formula: max turn rate = 10 - (0.75 * absolute speed).
     * @param speed The current absolute speed of the robot.
     * @return The maximum turn rate in degrees per turn.
     */
    public static double speedToMaxTurnRate(double speed) {
        return (10 - 0.75 * Math.abs(speed));
    }

    /**
     * Calculates the Euclidean distance between two points given their
     * differences in x and y coordinates.
     * @param dx The difference in x-coordinates.
     * @param dy The difference in y-coordinates.
     * @return The distance between the two points.
     */
    public static double getDistance(double dx, double dy)
    {
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates the angle (in degrees, Robocode convention where 0 is up)
     * from the origin (0,0) to a point (dx, dy).
     * @param dx The x-coordinate difference.
     * @param dy The y-coordinate difference.
     * @return The angle in degrees (-180 to 180).
     */
    public static double getAngle(double dx, double dy)
    {
        // Robocode's angle convention: 0 degrees is facing up, increases clockwise.
        // Math.atan2 returns radians in the range -PI to PI (0 is right, increases counter-clockwise).
        // Conversion: Math.toDegrees(Math.atan2(dy, dx)) gives angle where 0 is right.
        // -90 - ... rotates it so 0 is up and inverts direction to clockwise.
        return -90 - Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * Calculates the Euclidean distance between two points (ax, ay) and (bx, by).
     * @param ax The x-coordinate of the first point.
     * @param ay The y-coordinate of the first point.
     * @param bx The x-coordinate of the second point.
     * @param by The y-coordinate of the second point.
     * @return The distance between the two points.
     */
    public static double getDistance(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return getDistance(x, y);
    }

    /**
     * Calculates the angle (in degrees, Robocode convention where 0 is up)
     * from point A (ax, ay) to point B (bx, by).
     * @param ax The x-coordinate of the origin point A.
     * @param ay The y-coordinate of the origin point A.
     * @param bx The x-coordinate of the target point B.
     * @param by The y-coordinate of the target point B.
     * @return The angle in degrees (-180 to 180).
     */
    public static double getAngle(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return getAngle(x, y);
    }

    /**
     * Checks if a given coordinate (x, y) is outside the battlefield boundaries,
     * considering specific margins for each side.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @param width The width of the battlefield.
     * @param height The height of the battlefield.
     * @param marginLeft The margin from the left edge.
     * @param marginTop The margin from the top edge.
     * @param marginRight The margin from the right edge.
     * @param marginBottom The margin from the bottom edge.
     * @return {@code true} if the coordinate is outside the battlefield boundaries plus margins, {@code false} otherwise.
     */
    public static boolean isOutOfBattleField(double x, double y, double width, double height,
                                             double marginLeft, double marginTop,
                                             double marginRight, double marginBottom) {
        return x + marginRight > width || /* Right edge check */
               x - marginLeft < 0 ||    /* Left edge check */
               y + marginTop > height ||  /* Top edge check */
               y - marginBottom < 0;    /* Bottom edge check */
    }

    /**
     * Checks if a given {@link Vector} position is outside the battlefield boundaries,
     * considering specific margins for each side.
     * @param position The {@link Vector} representing the position to check.
     * @param width The width of the battlefield.
     * @param height The height of the battlefield.
     * @param marginLeft The margin from the left edge.
     * @param marginTop The margin from the top edge.
     * @param marginRight The margin from the right edge.
     * @param marginBottom The margin from the bottom edge.
     * @return {@code true} if the position is outside the battlefield boundaries plus margins, {@code false} otherwise.
     */
    public static boolean isOutOfBattleField(Vector position, double width, double height,
                                             double marginLeft, double marginTop,
                                             double marginRight, double marginBottom) {
        return Util.isOutOfBattleField(position.getX(), position.getY(), width, height,
                                       marginLeft, marginTop, marginRight, marginBottom);
    }

    /**
     * Checks if a given coordinate (x, y) is outside the battlefield boundaries,
     * without any additional margins.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @param width The width of the battlefield.
     * @param height The height of the battlefield.
     * @return {@code true} if the coordinate is outside the battlefield boundaries, {@code false} otherwise.
     */
    public static boolean isOutOfBattleField(double x, double y, double width, double height) {
        return Util.isOutOfBattleField(x, y, width, height, 0, 0, 0, 0);
    }

    /**
     * Checks if a given {@link Vector} position is outside the battlefield boundaries,
     * without any additional margins.
     * @param position The {@link Vector} representing the position to check.
     * @param width The width of the battlefield.
     * @param height The height of the battlefield.
     * @return {@code true} if the position is outside the battlefield boundaries, {@code false} otherwise.
     */
    public static boolean isOutOfBattleField(Vector position, double width, double height) {
        return Util.isOutOfBattleField(position.getX(), position.getY(), width, height);
    }

    /**
     * Calculates the "headingless" angle, which is the shortest relative turn
     * required to face an absolute angle, regardless of the current heading.
     * It returns an angle between -90 and 90 degrees if turning forward is shorter,
     * or between 90 and 270 (or -270 to -90) if turning backward is shorter.
     * This is useful for deciding whether to move ahead or back to face a target.
     * @param angle The target absolute angle in degrees.
     * @return The shortest relative angle to face the target, considering both
     *         forward and backward movement directions.
     */
    public static double headinglessAngle(double angle) {
        double normalAngle = Utils.normalRelativeAngleDegrees(angle);
        double normalAngleReverse = Utils.normalRelativeAngleDegrees(angle + 180);
        if (Math.abs(normalAngle) < Math.abs(normalAngleReverse)) {
            return normalAngle;
        } else {
            return normalAngleReverse;
        }
    }
}
