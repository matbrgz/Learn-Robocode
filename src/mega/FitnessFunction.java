package mega;

/**
 * Interface for a fitness function used in a Genetic Algorithm.
 * A fitness function evaluates how "good" an {@link Individual} is
 * by assigning it a numerical fitness score. The Genetic Algorithm
 * uses this score to guide the evolutionary process, favoring individuals
 * with higher fitness.
 * 
 * In Robocode, a fitness function might measure a robot's survival time,
 * damage dealt, accuracy, or a combination of these factors.
 * 
 * @param <T> The type of {@link Individual} to evaluate.
 * 
 * @author Gates
 */
public interface FitnessFunction<T extends Individual> {

    /**
     * Evaluates the fitness of a given {@link Individual}.
     * @param individual The individual to evaluate.
     * @return A double representing the fitness score of the individual.
     */
    double evaluate(T individual);
}
