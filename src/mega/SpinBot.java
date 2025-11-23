package mega;

/**
 * A robot that uses the Spin movement strategy, ideal for melee.
 * It also uses a simple gun and radar for basic functionality.
 */
public class SpinBot extends Boilerplate {
    public SpinBot() {
        super();
        // This robot will use Spin movement, a simple gun, and a simple radar.
        updateStrategy(Strategy.SIMPLE); // Load simple gun/radar
        updateStrategy(Strategy.SPIN);   // Override movement with Spin
    }
}
