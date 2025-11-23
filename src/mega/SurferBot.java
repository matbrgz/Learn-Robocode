package mega;

/**
 * A robot that combines Wave Surfing movement with Guess Factor targeting.
 * A classic combination for 1v1 battles.
 */
public class SurferBot extends Boilerplate {
    public SurferBot() {
        super();
        updateStrategy(Strategy.RADAR_HEAD_ON);
        updateStrategy(Strategy.GUN_GUESS_FACTOR);
        updateStrategy(Strategy.MOVEMENT_WAVE_SURFING);
    }
}
