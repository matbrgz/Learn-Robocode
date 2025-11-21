package mega;

import java.util.ArrayList;
import robocode.*;
import robocode.util.Utils;

/**
 * Implements a "Tracking" radar strategy.
 * This strategy is designed to keep the radar locked onto a specific
 * {@link OtherRobot} (the {@code trackingRobot} in the shared {@link State}).
 * It calculates the precise rotation needed to point the radar directly at
 * the target's last known position. If the target is lost (i.e., not scanned
 * recently) or no tracking robot is set, the radar will perform a continuous
 * sweep to re-acquire targets.
 * 
 * The {@code coefficient} allows for fine-tuning the radar's turning behavior.
 * A coefficient of 1.0 will attempt to turn the radar directly to the enemy.
 * 
 * @author Gates
 */
public class RadarTracking extends Radar {

    /**
     * A coefficient used to adjust the radar's turning rate.
     * A value of 1.0 means the radar will attempt to turn directly
     * to the target. Other values can make the radar turn faster or slower
     * towards the target, potentially to compensate for processing delays
     * or to create a wider sweep.
     */
    private double coefficient;

    /**
     * Constructs a new RadarTracking component with the given battle {@link State}
     * and a rotation coefficient.
     * @param state The shared {@link State} object for the robot.
     * @param coefficient A double value to scale the radar's turning rate.
     */
    public RadarTracking(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    /**
     * Executes the Tracking radar strategy for one turn.
     * If a {@code trackingRobot} is set in the shared {@link State} and its
     * last scanned data is recent, the radar calculates the shortest turn
     * to point directly at the target. Otherwise, if the target is lost
     * or no target is selected, the radar performs a continuous sweep
     * to find enemies.
     */
    @Override
    public void execute() {
        if (this.state.trackingRobot != null) {
            OtherRobot.Tick tick = this.state.trackingRobot.getHistory(-1);
            // Check if the last scan of the tracking robot is reasonably recent.
            if (tick == null || this.state.owner.getTime() - tick.time > 2) { // Allow for a small delay
                // If the target is lost or too old, perform a continuous sweep to re-acquire.
                this.rotation = Math.toRadians(360); // Continuous clockwise sweep
            } else {
                // Calculate the absolute bearing to the target enemy.
                // The Robocode getHeading() and getRadarHeading() are in degrees.
                double absoluteBearingToEnemy = tick.bearing + this.state.owner.getHeading();
                
                // Calculate the shortest turn needed for the radar to reach this absolute bearing.
                // Robocode Utils.normalRelativeAngleDegrees works with degrees.
                double radarTurnDegrees = Utils.normalRelativeAngleDegrees(absoluteBearingToEnemy - this.state.owner.getRadarHeading());
                
                // Apply the coefficient and convert to radians for this.rotation.
                this.rotation = Math.toRadians(this.coefficient * radarTurnDegrees);
            }
        } else {
            // If no tracking robot is set, perform a continuous sweep to find one.
            this.rotation = Math.toRadians(360); // Continuous clockwise sweep
        }
    }

}
