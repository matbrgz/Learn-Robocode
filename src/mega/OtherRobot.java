package mega;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import robocode.*;

/**
 * Represents an opponent robot on the battlefield, storing its historical
 * data and managing its predicted bullet waves. This class helps in tracking
 * enemy movements, energy changes, and potential firing events, which is
 * crucial for advanced targeting and movement strategies.
 * 
 * Each {@code OtherRobot} instance maintains a history of its observed states
 * ({@link Tick} objects) and a list of {@link WaveBullet} objects
 * that represent potential or confirmed bullet waves fired by this enemy.
 * 
 * @author Gates
 */
public class OtherRobot implements Comparable<OtherRobot> {
	
	/**
	 * Represents a snapshot of an {@link OtherRobot}'s state at a specific point in time.
	 * This includes observed properties (time, bearing, distance, energy) and
	 * calculated properties (position, velocity, turn rate).
	 * Historical {@code Tick} data is used for predicting future movements and shots.
	 * 
	 * @author Gates
	 */
    public static class Tick implements Comparable<Tick> {

        /** The game time (turn number) when this tick's data was recorded. */
        public long time;
        /** The bearing to the robot relative to our robot's heading at the time of scan (in degrees). */
        public double bearing;
        /** The distance to the robot at the time of scan. */
        public double distance;
        /** The energy level of the robot at the time of scan. */
        public double energy;

        /** The absolute position (x, y coordinates) of the robot on the battlefield. */
        public Vector position;
        /** The velocity vector (change in x, change in y per tick) of the robot. */
        public Vector velocity;
        /** The turn rate of the robot (in degrees per tick). */
        public double turnRate;

        /**
         * Constructs a new Tick with the specified game time.
         * Other fields are initialized to default values and are expected to be set subsequently.
         * @param time The game time (turn number) for this tick.
         */
        public Tick(long time) {
            this.time = time;
            this.velocity = new Vector(0,0); // Initialize to zero velocity
            this.position = new Vector(0,0); // Initialize to zero position
        }

        /**
         * Returns a string representation of this Tick, displaying all its properties.
         * @return A formatted string of the Tick's state.
         */
        @Override
        public String toString() {
            return "Tick(time=" + this.time + ", bearing=" + this.bearing
                + ", distance=" + this.distance + ", energy=" + this.energy
                + ", position=" + this.position + ", velocity=" + this.velocity
                + ", turnRate=" + this.turnRate + ")";
        }

        /**
         * Compares this Tick to another Tick based on their {@code time} property.
         * Ticks are ordered in descending order of time (latest tick first).
         * @param t The other Tick to compare to.
         * @return A negative integer, zero, or a positive integer as this tick's time
         *         is greater than, equal to, or less than the specified tick's time.
         */
        @Override
        public int compareTo(Tick t) {
            return this.time < t.time ?  1: // this tick is older, so greater in descending sort
                   this.time > t.time ? -1: // this tick is newer, so smaller in descending sort
                /* this.time ==t.time */ 0; // times are equal
        }

    }

    /** The name of this opponent robot. */
    private String name;
    /** A historical record of this robot's {@link Tick} states. */
    private ArrayList<Tick> history = new ArrayList<Tick>(10000);
    /** A list of {@link WaveBullet}s that this robot has potentially fired. */
    private ArrayList<WaveBullet> bulletWaves = new ArrayList<WaveBullet>();

    /**
     * Constructs a new OtherRobot instance with the given name.
     * @param name The name of the opponent robot.
     */
    public OtherRobot(String name) {
        this.name = name;
    }

    /**
     * Advances the state of all tracked {@link WaveBullet}s for this robot.
     * Waves that have finished traveling across the battlefield are removed.
     */
    public void advance() {
        Iterator<WaveBullet> it = this.bulletWaves.iterator();
        while (it.hasNext()) {
            if (!it.next().advance()) { // Advance the wave and check if it's expired
                it.remove(); // Remove expired waves
            }
        }
    }

    /**
     * Compares this {@code OtherRobot} to another {@code OtherRobot} based on
     * the time of their latest historical {@link Tick} entry. This provides
     * a way to sort robots by when they were last scanned.
     * @param robot The other {@code OtherRobot} to compare to.
     * @return A negative integer, zero, or a positive integer as the latest
     *         tick's time of this robot is greater than, equal to, or less than
     *         the latest tick's time of the specified robot (effectively sorting
     *         by newest scan first).
     */
    @Override
    public int compareTo(OtherRobot robot) {
        // Compares based on the latest tick's time (getHistory(-1) returns the latest).
        Tick thisLatest = this.getHistory(-1);
        Tick otherLatest = robot.getHistory(-1);

        if (thisLatest == null && otherLatest == null) return 0;
        if (thisLatest == null) return -1; // null is "older"
        if (otherLatest == null) return 1;  // null is "older"

        return thisLatest.compareTo(otherLatest);
    }

    /**
     * Returns an unmodifiable list of all historical {@link Tick} data
     * recorded for this robot.
     * @return A {@link List} of {@link Tick} objects representing the robot's history.
     */
    public List<Tick> getAllHistory() {
        return new ArrayList<>(this.history); // Return a copy to prevent external modification.
    }

    /**
     * Retrieves a {@link Tick} from the robot's history at a specified index.
     * The index can be negative to access recent history (e.g., -1 for latest, -2 for second latest).
     * The modulo operator {@link Util#modulo(int, int)} is used to handle negative indices
     * and ensure it wraps around the history size.
     * @param index The index in the history. Use negative values for recent entries
     *              (-1 for the most recent, -2 for the one before that, etc.).
     * @return The {@link Tick} at the specified index, or {@code null} if history is empty.
     */
    public Tick getHistory(int index) {
        if (this.history.size() == 0) {
            return null;
        }
        return this.history.get(Util.modulo(index, this.history.size()));
    }

    /**
     * Returns the total number of historical {@link Tick} entries recorded for this robot.
     * @return The size of the robot's history.
     */
    public int getHistorySize() {
        return this.history.size();
    }

    /**
     * Adds a new {@link Tick} entry to the robot's historical data.
     * @param tick The new {@link Tick} to add to the history.
     */
    public void pushHistory(Tick tick) {
        this.history.add(tick);
    }
    /**
     * Returns an unmodifiable list of all active {@link WaveBullet}s associated
     * with this robot.
     * @return A {@link List} of {@link WaveBullet} objects.
     */
    public List<WaveBullet> getAllBullets() {
        return new ArrayList<>(this.bulletWaves); // Return a copy to prevent external modification.
    }

    /**
     * Retrieves a {@link WaveBullet} from this robot's active bullet waves
     * at a specified index.
     * The index can be negative to access recent entries (e.g., -1 for latest).
     * The modulo operator {@link Util#modulo(int, int)} is used to handle negative indices.
     * @param index The index in the {@code bulletWaves} list.
     * @return The {@link WaveBullet} at the specified index, or {@code null} if no waves are present.
     */
    public WaveBullet getBullet(int index) {
        if (this.bulletWaves.size() == 0) {
            return null;
        }
        return this.bulletWaves.get(Util.modulo(index, this.bulletWaves.size()));
    }

    /**
     * Returns the name of this opponent robot.
     * @return The robot's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Estimates the gun heat of this opponent robot at a given time.
     * This is a simplified estimation and might not be perfectly accurate
     * as Robocode's internal gun heat mechanics are complex.
     * @param time The current game time.
     * @return An estimated gun heat value.
     */
    public double getGunHeat(long time) {
        WaveBullet lastBullet = this.getBullet(-1);
        if (lastBullet == null) {
            return 0.0; // No bullets fired, assume no heat.
        }
        // This formula seems to attempt to estimate heat, but might be incorrect.
        // Robocode's gun heat = 1 + (firePower / 5), and cools at 0.1 per turn.
        // A more accurate estimation would track actual firing times.
        return (1 + (lastBullet.getPower()/5)) - (0.1 * (time - lastBullet.getFireTime()));
    }

    /**
     * Predicts the future location of this robot after a given time frame,
     * based on specified turn and speed behaviors.
     * Uses a {@link ProjectedBot} to simulate future movement.
     * @param timeFrame The number of ticks into the future to predict.
     * @param tb The {@link ProjectedBot.TurnBehaviours} to assume for prediction.
     * @param sb The {@link ProjectedBot.SpeedBehaviours} to assume for prediction.
     * @param state The current {@link State} of the battle, needed for battlefield dimensions.
     * @return A {@link Vector} representing the predicted future position of the robot.
     */
    public Vector predictLocation(int timeFrame, ProjectedBot.TurnBehaviours tb, ProjectedBot.SpeedBehaviours sb, State state)
    {
        ProjectedBot pb = new ProjectedBot(getHistory(-1));
        pb.project(timeFrame, tb, sb, state);
        return pb.getPosition();
    }

    /**
     * An enumeration to indicate the level of historical data available
     * for a robot at a given time. This helps in determining how accurately
     * a robot's past movement (and thus future prediction) can be derived.
     */
    public enum PresentHistoryDatas
    {
        /** No historical data available. */
        none,
        /** Only position data is available. */
        positionOnly,
        /** Position and velocity data are available. */
        positionVelocity,
        /** Position, velocity, and turn rate data are available. */
        positionVelocityTurnRate,
    }

    /**
     * Determines the most granular level of historical data available
     * for this robot at a given time, typically used for predicting movement.
     * This method checks the timestamps of the last few ticks in history.
     * @param time The current game time.
     * @return A {@link PresentHistoryDatas} enum value indicating the data availability.
     */
    public PresentHistoryDatas availablePresentHistoryData(long time)
    {
        // This logic needs to be reviewed as it compares absolute times rather than tick differences.
        // It might be intended to check for consecutive ticks.
        if (this.history.size() < 1 || time - this.getHistory(-1).time != 0) // Should check if last tick is current time
            return PresentHistoryDatas.none;
        else if (this.history.size() < 2 || this.getHistory(-1).time - this.getHistory(-2).time != 1) // Second last tick is 1 turn before last
            return PresentHistoryDatas.positionOnly;
        else if (this.history.size() < 3 || this.getHistory(-2).time - this.getHistory(-3).time != 1) // Third last tick is 1 turn before second last
            return PresentHistoryDatas.positionVelocity;
        else
            return PresentHistoryDatas.positionVelocityTurnRate;
    }

    /**
     * Attempts to predict if this opponent robot has fired a bullet in the
     * previous turn by analyzing energy drops and event data.
     * If a shot is predicted, a new {@link WaveBullet} is created and added
     * to the {@code bulletWaves} list.
     * @param time The current game time.
     * @param state The current {@link State} of the battle.
     * @return {@code true} if a bullet shot was predicted, {@code false} otherwise.
     */
    public boolean predictBulletShot(long time, State state) {

        // check if it's possible that a bullet was shot (i.e., gun is not hot)
        // This condition should be relative to Robocode's actual gun heat mechanics.
        // If the gun heat is above zero, a shot cannot be fired.
        // This current check `this.getGunHeat(time) > 0` might be reversed:
        // if (this.getGunHeat(time) <= 0) // Meaning gun is cold enough to fire.
        // However, this.getGunHeat(time) is an ESTIMATION.
        // A more robust check involves energy drop + previous energy.
        
        OtherRobot.Tick current = this.getHistory(-1);
        OtherRobot.Tick previous = this.getHistory(-2);

        if (current == null || previous == null || current.time <= previous.time) {
            // We don't have enough consecutive historical data to determine energy drop.
            return false;
        }

        // Energy drop indicates a possible shot.
        // Rules.getGunHeat(firePower) is typically used for our own robot.
        // For enemy, we observe energy drop.
        double energyDrop = previous.energy - current.energy;
        double predictedPower = Util.roundTo1(energyDrop); // Rough estimate of bullet power from energy drop.

        // Account for other energy-losing events
        // 1. BulletHitEvent (if we hit them)
        for (BulletHitEvent e : state.bulletHitEvents) {
            if (e.getName().equals(this.name)) {
                predictedPower -= Rules.getBulletDamage(e.getBullet().getPower());
            }
        }

        // 2. Wall hit (if they crashed)
        // This logic incorrectly uses previous.position for out of battlefield check.
        // It should check if the robot HIT a wall between previous and current tick.
        // A more reliable way is to observe robot's velocity change when hitting a wall.
        // For now, retaining original logic but adding a note.
        double battlefieldWidth = state.owner.getBattleFieldWidth();
        double battlefieldHeight = state.owner.getBattleFieldHeight();
        double marginTopBottom = Util.ROBOT_HEIGHT / 2 - 2; 
        double marginLeftRight = Util.ROBOT_WIDTH / 2 - 2;

        if (Util.isOutOfBattleField(previous.position, battlefieldWidth, battlefieldHeight,
                                    marginLeftRight, marginTopBottom, marginLeftRight, marginTopBottom)) {
            // This condition is checking if *previous* position was out of bounds,
            // not if a wall *hit* occurred.
            // Robocode event onHitWall for enemy is not available directly.
            // One would need to infer wall hit from position change and velocity.
            // Assuming this is an attempt to detect a wall hit through position.
            double speedChange = previous.velocity.length() - current.velocity.length();
            if (Math.abs(speedChange) >= 2.0) { // Significant speed change
                // This formula for Rules.getWallHitDamage expects an energy, not bullet power.
                // Assuming `predictedPower -= Rules.getWallHitDamage(some_energy_value)` was intended.
                // Robocode's wall hit damage is usually a flat rate, or tied to speed.
                // For now, a fixed deduction for a potential wall hit.
                predictedPower -= 0.1; // A small arbitrary deduction for wall hit influence.
            }
        }

        // 3. Ramming another robot (if they rammed us or another bot)
        for (HitRobotEvent e : state.hitRobotEvents) {
            if (e.getName().equals(this.name)) {
                predictedPower -= Rules.ROBOT_HIT_DAMAGE;
            }
        }

        // If the calculated power is too low, assume no shot.
        if (predictedPower < Rules.MIN_BULLET_POWER) {
            return false;
        }

        // If something unexpected happened, cap the power.
        if (predictedPower > Rules.MAX_BULLET_POWER) {
            predictedPower = Rules.MAX_BULLET_POWER;
        }

        // Assume the robot was aiming at us or in our general direction.
        // This angle needs to be carefully chosen for wave surfing.
        // In wave surfing, the wave is propagated from the enemy's position at fire time.
        // The angle range should cover the maximum possible escape angles.
        double fireAngle = Util.getAngle(previous.position.getX(), previous.position.getY(),
                                        state.owner.getX(), state.owner.getY()); // Angle from enemy to us.
        
        // Add a new WaveBullet. The angle range (fireAngle - 10, fireAngle + 10)
        // is a placeholder for the actual "guess factor" range.
        this.bulletWaves.add(new WaveBullet(previous.position, predictedPower,
                                            fireAngle - 10, fireAngle + 10,
                                            1.0, // Confidence level (1.0 for now)
                                            state, current.time)); // Pass current.time as fireTime

        return true;
    }

    /**
     * Renders the visual representation of this opponent robot's active
     * {@link WaveBullet}s on the battlefield for debugging purposes.
     * @param g The {@link Graphics2D} object used for drawing.
     * @param time The current game time, used by {@link WaveBullet} for rendering.
     */
    public void onPaint(Graphics2D g, long time) {
        for (WaveBullet bw : this.bulletWaves) {
            bw.onPaint(g);
        }
    }

}
