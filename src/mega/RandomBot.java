package mega;

/**
 * A robot that uses completely random behavior for all components.
 */
public class RandomBot extends Boilerplate {
    public RandomBot() {
        super();
        updateStrategy(Strategy.RADAR_RANDOM);
        updateStrategy(Strategy.GUN_RANDOM);
        updateStrategy(Strategy.MOVEMENT_RANDOM);
    }
}
