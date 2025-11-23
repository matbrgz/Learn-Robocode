package mega;

/**
 * A robot that uses the Walls movement strategy.
 * It also uses a simple gun and radar for basic functionality.
 */
public class WallsBot extends Boilerplate {
    public WallsBot() {
        super();
        // This robot will use Walls movement, a simple gun, and a simple radar.
        // We can combine strategies by calling updateStrategy multiple times,
        // as null components in a strategy won't override existing ones.
        updateStrategy(Strategy.RADAR_TRACKING);
        updateStrategy(Strategy.GUN_SIMPLE);
        updateStrategy(Strategy.MOVEMENT_WALLS);
    }
}
