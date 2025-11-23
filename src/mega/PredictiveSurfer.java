package mega;

/**
 * A 1v1 bot that combines Wave Surfing with a Predictive Gun.
 */
public class PredictiveSurfer extends Boilerplate {
    public PredictiveSurfer() {
        super();
        updateStrategy(Strategy.RADAR_HEAD_ON);
        updateStrategy(Strategy.GUN_PREDICTIVE);
        updateStrategy(Strategy.MOVEMENT_WAVE_SURFING);
    }
}
