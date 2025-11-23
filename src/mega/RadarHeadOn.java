package mega;

import robocode.*;
import robocode.util.Utils;
import java.awt.geom.Point2D;

/**
 * Implements a "Head-On" radar strategy.
 * This strategy aims to keep the radar locked directly onto the currently
 * targeted enemy robot. It calculates the bearing to the enemy and rotates
 * the radar to point precisely at them, sweeping just enough to maintain
 * a continuous scan. This is a straightforward and effective radar approach
 * in one-on-one combat or when a single primary target needs constant tracking.
 * 
 * @author Gates
 */
public class RadarHeadOn extends Radar { // Corrected: extends Radar

	/**
	 * Constructs a new RadarHeadOn component with the given battle {@link State}.
	 * @param state The shared {@link State} object for the robot.
	 */
	public RadarHeadOn(State state) {
		super(state);
		// No specific initialization needed for this basic placeholder.
	}

	/**
	 * Executes the Head-On radar strategy for one turn.
	 * This method continuously points the radar at the {@code targetEnemy}.
	 * If no target enemy is available, the radar will spin in place
	 * to try and find one.
	 */
	@Override
	public void execute() {
		OtherRobot target = this.state.trackingRobot;

		if (target != null && target.getHistory(-1) != null) {
			// Calculate the absolute bearing to the target enemy.
			double absoluteBearing = Utils.normalAbsoluteAngle(
				Math.atan2(target.getHistory(-1).position.getX() - this.state.owner.getX(), target.getHistory(-1).position.getY() - this.state.owner.getY())
			);
			// Calculate the shortest turn needed to point the radar at the enemy.
			// The rotation field expects radians.
			this.rotation = Utils.normalRelativeAngle(absoluteBearing - this.state.owner.getRadarHeadingRadians());
		} else {
			// If no target is currently available, slowly spin the radar to find one.
			this.rotation = Math.toRadians(10); // Spin 10 degrees clockwise (in radians)
		}
	}
	
}
