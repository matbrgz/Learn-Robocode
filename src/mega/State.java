package mega;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import robocode.*;
import robocode.util.Utils; // Explicitly use robocode.util.Utils to avoid confusion with mega.Util

/**
 * The {@code State} class is a central repository for all critical information
 * about the robot's current battle environment. It maintains a consistent
 * view of the battlefield, including other robots' positions and states,
 * and tracks events like bullet hits and robot deaths. This centralized
 * state allows various robot components (Movement, Gun, Radar) to access
 * up-to-date information for their decision-making processes.
 * 
 * It acts as the "state space" for the robot's AI, enabling components to
 * react to and predict changes in the battle.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/State_space">Wikipedia: State space</a>
 * 
 * @author VisualMelon
 * @author thomasleese
 * @author Cyanogenoid
 * @author Gates (for improvements)
 */
public class State {

    /**
     * A reference to the owning {@link AdvancedRobot} instance.
     * This allows the state to query the robot for its current status (e.g., position, heading, time).
     */
    AdvancedRobot owner;

    /**
     * A map storing information about all other robots currently known to be in the battle.
     * The key is the robot's name ({@code String}), and the value is an {@link OtherRobot} object.
     */
    Map<String, OtherRobot> otherRobots;
    /**
     * A reference to the most recently scanned {@link OtherRobot}.
     * This is typically set by the {@code onScannedRobot} event handler.
     */
    OtherRobot latestRobot;
    /**
     * A reference to the {@link OtherRobot} that the main robot is currently
     * focused on tracking or targeting. This can be {@code null}.
     */
    OtherRobot trackingRobot;
    /**
     * A list of {@link BulletTracked} objects representing bullets fired by
     * our robot that are still active on the battlefield.
     */
    ArrayList<BulletTracked> ourBullets;

    /**
     * A buffer to store {@link BulletHitEvent}s that occurred in the current turn.
     * Cleared at the beginning of each turn.
     */
    java.util.Vector<BulletHitEvent> bulletHitEvents = new java.util.Vector<BulletHitEvent>();
    /**
     * A buffer to store {@link HitRobotEvent}s that occurred in the current turn.
     * Cleared at the beginning of each turn.
     */
    java.util.Vector<HitRobotEvent> hitRobotEvents = new java.util.Vector<HitRobotEvent>();

    /**
     * The width of the battlefield in pixels.
     */
    double battleWidth;
    /**
     * The height of the battlefield in pixels.
     */
    double battleHeight;

    /**
     * Constructs a new {@code State} object for a given {@link AdvancedRobot}.
     * Initializes collections for tracking other robots and bullets.
     * @param robot The {@link AdvancedRobot} instance that owns this state.
     */
    public State(AdvancedRobot robot) {
        this.owner = robot;
        this.otherRobots = new HashMap<String, OtherRobot>();
        this.latestRobot = null;
        this.ourBullets = new ArrayList<BulletTracked>();
    }

    /**
     * Advances the state of all tracked {@link OtherRobot} objects by one turn.
     * Also clears the event buffers ({@code bulletHitEvents}, {@code hitRobotEvents})
     * at the start of a new turn. This method should be called once per turn
     * by the main robot's {@code run()} loop.
     */
    public void advance() {
        for (OtherRobot robot : this.otherRobots.values()) {
            robot.advance();
        }
        this.bulletHitEvents.clear();
        this.hitRobotEvents.clear();
    }

    /**
     * Handles a {@link ScannedRobotEvent} to update information about a scanned enemy robot.
     * If the robot is new, a new {@link OtherRobot} object is created.
     * The robot's history is updated with the latest scan data, including its position,
     * distance, energy, and calculated velocity and turn rate.
     * @param e The {@link ScannedRobotEvent} received from Robocode.
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        OtherRobot robot = this.otherRobots.get(e.getName());
        if (robot == null) {
            robot = new OtherRobot(e.getName());
            this.otherRobots.put(e.getName(), robot);
        }

        OtherRobot.Tick tick = new OtherRobot.Tick(this.owner.getTime());
        tick.bearing = e.getBearing();
        tick.distance = e.getDistance();
        tick.energy = e.getEnergy();
        tick.position = this.calculatePosition(tick.bearing, tick.distance);
        robot.pushHistory(tick);

        // Calculate velocity and turn rate based on historical ticks.
        // This relies on having at least two ticks in history.
        OtherRobot.Tick oldTick = robot.getHistory(-2); 
        long timeDisc = tick.time - oldTick.time;
        if (timeDisc <= 3 && timeDisc > 0) /* A small leniency for time difference */ {
            tick.velocity = State.calculateVelocity(oldTick.position, tick.position).div((double)timeDisc);
        } else {
            tick.velocity = new Vector(0, 0); // Default to zero if insufficient history or time difference.
        }

        if (timeDisc <= 3 && timeDisc > 0) {
            tick.turnRate = State.calculateTurnRate(oldTick.velocity, tick.velocity) / (double)timeDisc;
        } else {
            tick.turnRate = 0; // Default to zero if insufficient history or time difference.
        }
        this.latestRobot = robot; // Update the reference to the latest scanned robot.

        robot.predictBulletShot(this.owner.getTime(), this);
    }

    /**
     * Handles a {@link RobotDeathEvent} to remove a dead robot from the tracked
     * {@code otherRobots} map. Also clears any references to the dead robot
     * if it was the {@code latestRobot} or {@code trackingRobot}.
     * @param e The {@link RobotDeathEvent} received from Robocode.
     */
    public void onRobotDeath(RobotDeathEvent e) {
        System.out.println("Someone else has died: " + e);

        // Remove dead robot from otherRobots map.
        String name = e.getName();
        this.otherRobots.remove(name);

        // Clear latest robot reference if it died.
        if (this.latestRobot != null && this.latestRobot.getName().equals(name)) {
            this.latestRobot = null;
        }

        // Clear tracking robot reference if it died.
        if (this.trackingRobot != null && this.trackingRobot.getName().equals(name)) {
            this.trackingRobot = null;
        }
    }

    /**
     * Calculates the absolute position of a scanned robot on the battlefield
     * based on our robot's current position and the scanned event's bearing and distance.
     * @param bearing The bearing to the scanned robot relative to our robot's heading (in degrees).
     * @param distance The distance to the scanned robot.
     * @return A {@link Vector} representing the absolute (x, y) coordinates of the scanned robot.
     */
    private Vector calculatePosition(double bearing, double distance) {
        // Convert bearing to absolute angle relative to battlefield (0 degrees is up).
        double angleR = Math.toRadians(bearing + this.owner.getHeading());
        // Calculate X and Y components based on trigonometric functions.
        double x = this.owner.getX() + Math.sin(angleR) * distance;
        double y = this.owner.getY() + Math.cos(angleR) * distance;
        return new Vector(x, y);
    }

    /**
     * Calculates the velocity vector of a robot based on its change in position
     * between two consecutive ticks.
     * @param oldPosition The {@link Vector} representing the robot's previous position.
     * @param newPosition The {@link Vector} representing the robot's current position.
     * @return A {@link Vector} representing the robot's velocity (change in x, change in y per tick).
     */
    private static Vector calculateVelocity(Vector oldPosition, Vector newPosition) {
        return new Vector(newPosition.getX() - oldPosition.getX(),
                          newPosition.getY() - oldPosition.getY());
    }

    /**
     * Calculates the turn rate of a robot (in degrees per tick) based on the
     * change in direction of its velocity vector between two consecutive ticks.
     * @param oldVelocity The {@link Vector} representing the robot's previous velocity.
     * @param newVelocity The {@link Vector} representing the robot's current velocity.
     * @return The turn rate in degrees per tick.
     */
    private static double calculateTurnRate(Vector oldVelocity, Vector newVelocity) {
        // Robocode's normalRelativeAngleDegrees handles angle normalization.
        return Utils.normalRelativeAngleDegrees(newVelocity.getAngle() - oldVelocity.getAngle());
    }

}
