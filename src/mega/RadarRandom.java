package mega;

import robocode.*;
import robocode.util.Utils; // Ensure robocode.util.Utils is imported if needed for angle normalization

/**
 * Implements a "Random" radar strategy.
 * This strategy causes the radar to rotate by a random amount in each turn.
 * It's a simple, non-targeted approach useful for initial enemy discovery
 * in the absence of specific target information, or as a fallback behavior.
 * While not optimal for tracking, it ensures continuous scanning of the
 * battlefield.
 * 
 * The rotation amount is a random value between -180 and +180 degrees
 * (converted to radians for the {@code rotation} field).
 * 
 * @author Gates
 */
public class RadarRandom extends Radar {

	/**
	 * Constructs a new RadarRandom component with the given battle {@link State}.
	 * @param state The shared {@link State} object for the robot.
	 */
    public RadarRandom(State state) {
        super(state);
    }

    /**
     * Executes the Random radar strategy for one turn.
     * Sets the radar's rotation to a random value between -180 and +180 degrees
     * (converted to radians). This causes the radar to continuously sweep
     * in unpredictable directions, helping to discover or re-acquire enemies
     * across the battlefield.
     */
    @Override
    public void execute() {
        // Generate a random rotation value between -180 and +180 degrees, then convert to radians.
        // Math.random() generates a double between 0.0 (inclusive) and 1.0 (exclusive).
        // (Math.random() * 360 - 180) yields a value between -180 and 180 degrees.
        this.rotation = Math.toRadians(Math.random() * 360 - 180);
    }

}
