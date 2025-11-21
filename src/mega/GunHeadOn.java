package mega;

import robocode.*;
import robocode.util.Utils;
import java.awt.geom.Point2D;

/**
 * Implements a "Head-On" gun targeting strategy.
 * This strategy is a simple approach where the gun attempts to aim directly
 * at the last known position of the currently targeted enemy robot.
 * It does not account for enemy movement, bullet travel time, or prediction.
 * It is effective against stationary or very slow-moving targets, or as
 * a basic fallback.
 * 
 * The gun will fire when it is aimed directly at the enemy and has cooled down.
 * 
 * @author Gates
 */
public class GunHeadOn extends Gun {

	/**
	 * Constructs a new GunHeadOn component with the given battle {@link State}.
	 * Initializes the gun with a default bullet power.
	 * @param state The shared {@link State} object for the robot.
	 */
	public GunHeadOn(State state) {
		super(state);
		this.bulletPower = 1.0; // Default bullet power for a simple gun.
	}

	/**
	 * Executes the Head-On gun targeting strategy for one turn.
	 * This method calculates the bearing to the {@code targetEnemy} (if available)
	 * and adjusts the gun's rotation to aim directly at it.
	 * If the gun is aligned with the target and has cooled down, it will attempt to fire.
	 * If no target enemy is available, the gun remains stationary and will not fire.
	 */
	@Override
	public void execute() {
		OtherRobot target = this.state.targetEnemy;

		if (target != null && target.getHistory(-1) != null) {
			// Get the last known position of the target.
			Point2D.Double targetPosition = target.getHistory(-1).position;

			// Calculate the absolute bearing from our robot to the target's position.
			            double absoluteBearing = Utils.normalAbsoluteAngle(
			                Math.atan2(targetPosition.getX() - this.state.owner.getX(),
			                           targetPosition.getY() - this.state.owner.getY()) 
			            );
			// Calculate the shortest turn needed for the gun to reach this absolute bearing.
			// Robocode's getGunHeadingRadians is in radians.
			double gunTurnRadians = Utils.normalRelativeAngle(absoluteBearing - this.state.owner.getGunHeadingRadians());
			
			this.rotation = gunTurnRadians; // Set the desired gun rotation.

			// Check if the gun is approximately aimed at the target.
			// A small tolerance is used due to floating-point precision and turn rates.
			if (Math.abs(gunTurnRadians) < Math.toRadians(5)) { // If gun is pointed within 5 degrees
				this.shouldFire = true; // Signal to fire.
			} else {
				this.shouldFire = false; // Not aimed, do not fire.
			}
			
			// Set bullet power, can be dynamic later.
			this.bulletPower = 1.0; 

		} else {
			// No target or no recent data, so don't rotate the gun and don't fire.
			this.rotation = 0;
			this.shouldFire = false;
		}
	}

}