package mega;

import robocode.*;
import robocode.util.Utils;
import java.util.ArrayList;
import java.util.Collection; // Use Collection for iteration
import java.util.Iterator; // For safe removal if needed

/**
 * Implements a "Priority Scanning" radar strategy.
 * This strategy aims to maintain optimal situational awareness by prioritizing
 * scanning of enemy robots based on how recently they were observed.
 * Specifically, it attempts to rotate the radar to find the enemy that has
 * gone the longest without being scanned. This helps ensure that all enemies
 * are periodically re-acquired, rather than getting "locked on" to a single
 * target while others disappear.
 * 
 * In a multi-robot battle, this approach helps to keep a fresh view of the
 * entire battlefield. If no specific robot is being tracked, it will
 * randomly select one from the known enemies.
 * 
 * @author Gates
 */
public class RadarPriority extends Radar {

    /**
     * Stores the {@link OtherRobot.Tick} data of the least recently seen
     * enemy robot. Used to determine the next priority target for scanning.
     * Initialized to {@code Long.MAX_VALUE} to ensure the first scanned
     * robot becomes the initial {@code lastSeenTick}.
     */
    private OtherRobot.Tick lastSeenTick;

    /**
     * Constructs a new RadarPriority component with the given battle {@link State}.
     * Initializes the {@code lastSeenTick} with a dummy tick having the maximum
     * possible time to ensure any scanned robot is considered "more recent".
     * @param state The shared {@link State} object for the robot.
     */
    public RadarPriority(State state) {
        super(state);
        this.lastSeenTick = new OtherRobot.Tick(Long.MAX_VALUE);
    }

    /**
     * Executes the Priority Scanning radar strategy for one turn.
     * The logic dynamically selects a robot to track. If no robot is
     * currently being tracked, it randomly picks one. Otherwise, it
     * scans for the robot that was least recently seen and adjusts the
     * radar rotation to sweep towards that robot. The radar continuously
     * sweeps in a direction determined by the least recently seen enemy.
     */
    @Override
    public void execute() {
        // If there's no tracking robot selected, and there are other robots available, pick one randomly.
        // This ensures we always have a primary focus, even if not the absolute least seen.
        if (this.state.trackingRobot == null && !this.state.otherRobots.isEmpty()) {
            // Retrieve all known enemy robots.
            Collection<OtherRobot> enemies = this.state.otherRobots.values();
            // A safer way to get a random element from a Map's values.
            int randomIndex = Util.RANDOM.nextInt(enemies.size());
            this.state.trackingRobot = (OtherRobot) enemies.toArray()[randomIndex];
        }

        // --- Logic to find the least recently seen robot ---
        OtherRobot.Tick currentLeastSeenTick = new OtherRobot.Tick(Long.MAX_VALUE); // Temporary holder for the oldest tick found this turn
        OtherRobot leastRecentlySeenRobot = null;

        for (OtherRobot r : this.state.otherRobots.values()) {
            if (r.getHistory(-1) != null && r.getHistory(-1).time < currentLeastSeenTick.time) {
                currentLeastSeenTick = r.getHistory(-1);
                leastRecentlySeenRobot = r;
            }
        }
        
        // If we found a least recently seen robot, make it the tracking target if it's not already.
        // This ensures the radar eventually sweeps to re-scan all enemies.
        if (leastRecentlySeenRobot != null && (this.state.trackingRobot == null || !this.state.trackingRobot.getName().equals(leastRecentlySeenRobot.getName()))) {
            this.state.trackingRobot = leastRecentlySeenRobot;
        }

        // --- Determine radar rotation based on trackingRobot ---
        if (this.state.trackingRobot != null && this.state.trackingRobot.getHistory(-1) != null) {
            // Target the least recently seen robot for radar sweep.
            // Calculate the absolute bearing to the least recently seen robot's last known position.
            double absoluteBearing = Utils.normalAbsoluteAngle(
                Math.atan2(this.state.trackingRobot.getHistory(-1).position.x - this.state.owner.getX(),
                           this.state.trackingRobot.getHistory(-1).position.y - this.state.owner.getY())
            );
            
            // Calculate the shortest turn needed to point the radar at it.
            this.rotation = Utils.normalRelativeAngle(absoluteBearing - this.state.owner.getRadarHeadingRadians());
            
            // If the radar is pointing approximately at the target, sweep a bit past it
            // to ensure discovery or to look for other robots.
            // For continuous scanning, often a full sweep is done.
            // The current approach uses positive/negative infinity, implying continuous spin.
            // Let's refine this to make it more intentional for priority scanning.
            
            // A continuous sweep is achieved by setting a large positive or negative value.
            // The direction can be adjusted based on if we just passed the target.
            if (Math.abs(this.rotation) < Math.toRadians(5)) { // If we are close to pointing at target
                // If we've just scanned it, make a wide sweep to look for other enemies or pass it.
                // Or, if we haven't seen it for a while, ensure a full scan over its area.
                // For a "priority" radar, if we're on target, make a wide sweep to find others.
                this.rotation = (this.rotation >= 0) ? Math.toRadians(45) : Math.toRadians(-45); // Sweep wider
            } else {
                // If we are far from target, turn towards it.
                // The owner's turn rate will cap this.
                // Keep the value high to ensure continuous turning.
                this.rotation = (this.rotation >= 0) ? Math.toRadians(45) : Math.toRadians(-45);
            }

        } else {
            // If no robots are known, spin the radar continuously to find one.
            this.rotation = Math.toRadians(360); // Full spin to cover all directions.
        }
    }

}
