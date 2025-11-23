package mega;

/**
 * A robot that uses the Anti-Gravity movement strategy, ideal for melee.
 * It also uses a simple gun and radar for basic functionality.
 */
public class AntiGravityBot extends Boilerplate {
    public AntiGravityBot() {
        super();
        // This robot will use Anti-Gravity movement, a simple gun, and a simple radar.
        updateStrategy(Strategy.RADAR_DYNAMIC_CLUSTERING);
        updateStrategy(Strategy.GUN_SIMPLE);
        updateStrategy(Strategy.MOVEMENT_ANTI_GRAVITY);
    }
}
