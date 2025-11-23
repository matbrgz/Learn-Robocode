package mega;

/**
 * The base abstract class for all Robocode gun components.
 * This class extends {@link Component} and provides common properties
 * and methods related to gun aiming, firing, and bullet tracking.
 * Concrete gun strategies will extend this class to implement specific
 * targeting and firing patterns.
 * 
 * @author Gates
 */
public abstract class Gun extends Component {

    /**
     * The power at which the gun intends to fire a bullet.
     * Higher power means faster and more damaging bullets, but also
     * increases gun heat and cooldown time.
     */
    protected double bulletPower;
    /**
     * The desired rotation rate for the gun in radians/turn.
     * Positive for clockwise, negative for counter-clockwise.
     */
    protected double rotation;
    /**
     * A flag indicating whether the gun component intends to fire a bullet
     * in the current turn.
     */
    protected boolean shouldFire = true;

    /**
     * Constructs a new Gun component with the given battle {@link State}.
     * @param state The shared {@link State} object for the robot.
     */
    public Gun(State state) {
        super(state);
    }

    /**
     * Returns the desired power for the bullet to be fired.
     * @return The bullet power, typically between 0.1 and 3.0.
     */
    public double getBulletPower() {
        return this.bulletPower;
    }

    /**
     * Returns the desired rotation rate for the gun.
     * @return The rotation rate, in radians per turn.
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * Returns whether the gun component intends to fire a bullet in this turn.
     * @return {@code true} if the gun should fire, {@code false} otherwise.
     */
    public boolean getShouldFire() {
        return this.shouldFire;
    }

    /**
     * Called when our robot successfully fires a bullet.
     * Subclasses can override this method to perform actions or record data
     * immediately after a bullet is fired.
     * @param tb The {@link BulletTracked} object representing the fired bullet.
     */
    public void firedBullet(BulletTracked tb) {
        // No default behavior. Subclasses should override if needed.
    }

    /**
     * Called when one of our bullets hits another robot.
     * Subclasses can override this method to update targeting logic,
     * adjust bullet power, or record hit statistics.
     * @param tb The {@link BulletTracked} object that hit an enemy.
     */
    public void bulletHit(BulletTracked tb) {
        // No default behavior. Subclasses should override if needed.
    }

    /**
     * Called when one of our bullets misses its target.
     * Subclasses can override this method to refine targeting algorithms
     * or adjust future firing decisions.
     * @param tb The {@link BulletTracked} object that missed its target.
     */
    public void bulletMissed(BulletTracked tb) {
        // No default behavior. Subclasses should override if needed.
    }

}
