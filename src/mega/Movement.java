package mega;

import robocode.*;

/**
 * The base abstract class for all Robocode movement components.
 * This class extends {@link Component} and provides common properties
 * and utility methods related to robot movement, such as desired speed
 * and rotation. Concrete movement strategies will extend this class
 * to implement specific movement patterns.
 * 
 * @author Gates
 */
public abstract class Movement extends Component {

    /**
     * The desired speed for the robot's movement. A positive value
     * indicates forward movement, a negative value indicates backward movement.
     */
    protected double speed;
    /**
     * The desired rotation rate for the robot's body in radians/turn.
     * Positive for clockwise, negative for counter-clockwise.
     */
    protected double rotation;

    /**
     * Constructs a new Movement component with the given battle {@link State}.
     * @param state The shared {@link State} object for the robot.
     */
    public Movement(State state) {
        super(state);
    }

    /**
     * Returns the desired speed for the robot's movement.
     * @return The speed, in pixels per turn.
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Returns the desired rotation rate for the robot's body.
     * @return The rotation rate, in radians per turn.
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * Checks if a given coordinate (x, y) is outside the battlefield boundaries,
     * considering a specified margin from the walls.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @param margin The distance from the wall within which the coordinate is considered "out of bounds".
     * @return {@code true} if the coordinate is outside the battlefield boundaries plus margin, {@code false} otherwise.
     */
    public boolean isOutOfBattleField(double x, double y, double margin) {
        Robot owner = this.state.owner;
        return Util.isOutOfBattleField(x, y, owner.getBattleFieldWidth(), owner.getBattleFieldHeight(),
                                       owner.getWidth()/2 + margin, // left boundary
                                       owner.getHeight()/2 + margin, // top boundary
                                       owner.getWidth()/2 + margin, // right boundary
                                       owner.getHeight()/2 + margin); // bottom boundary
    }

}
