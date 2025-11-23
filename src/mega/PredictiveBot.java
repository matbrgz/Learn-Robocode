package mega;

/**
 * A robot that relies on predictive movement and gun control.
 */
public class PredictiveBot extends Boilerplate {
    public PredictiveBot() {
        super();
        updateStrategy(Strategy.RADAR_PRIORITY);
        updateStrategy(Strategy.GUN_PREDICTIVE);
        updateStrategy(Strategy.MOVEMENT_PREDICTIVE);
    }
}
