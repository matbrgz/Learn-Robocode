package mega;

/**
 * The base abstract class for all Robocode radar components.
 * This class extends {@link Component} and provides common properties
 * and methods related to radar scanning and rotation.
 * Concrete radar strategies will extend this class to implement specific
 * scanning patterns and enemy detection logic.
 * 
 * @author Gates
 */
public abstract class Radar extends Component {

    /**
     * The desired rotation rate for the radar in radians/turn.
     * Positive for clockwise, negative for counter-clockwise.
     */
    protected double rotation;

    /**
     * Constructs a new Radar component with the given battle {@link State}.
     * @param state The shared {@link State} object for the robot.
     */
    public Radar(State state) {
        super(state);
    }

    /**
     * Returns the desired rotation rate for the radar.
     * @return The rotation rate, in radians per turn.
     */
    public double getRotation() {
        return this.rotation;
    }

}
