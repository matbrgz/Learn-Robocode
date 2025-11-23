package mega;

/**
 * A robot optimized for melee battles using Anti-Gravity and Dynamic Clustering.
 */
public class MeleeBot extends Boilerplate {
    public MeleeBot() {
        super();
        updateStrategy(Strategy.RADAR_DYNAMIC_CLUSTERING);
        updateStrategy(Strategy.GUN_SIMPLE); // A simple gun is often effective in chaotic melee
        updateStrategy(Strategy.MOVEMENT_ANTI_GRAVITY);
    }
}
