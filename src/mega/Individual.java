package mega;

/**
 * Interface representing an individual in a Genetic Algorithm population.
 * An individual encapsulates a potential solution to the problem being optimized.
 * Concrete implementations of this interface will define how a solution is represented
 * (e.g., a set of parameters for a Robocode bot) and provide methods for genetic
 * operations like crossover and mutation.
 * 
 * @author Gates
 */
public interface Individual {

    /**
     * Gets the fitness score of this individual.
     * The fitness score quantifies how "good" this individual's solution is.
     * Higher fitness typically means a better solution.
     * @return The fitness score of the individual.
     */
    double getFitness();

    /**
     * Sets the fitness score for this individual. This method is typically
     * called by the {@link GeneticAlgorithm} after evaluation by a
     * {@link FitnessFunction}.
     * @param fitness The calculated fitness score.
     */
    void setFitness(double fitness);

    /**
     * Performs a crossover operation with another individual (the parent).
     * This method combines the genetic material of two parents to produce
     * a new offspring individual. The specific implementation of crossover
     * depends on the representation of the individual's "genes".
     * @param parent The other individual to cross over with.
     * @return A new {@link Individual} representing the offspring.
     */
    Individual crossover(Individual parent);

    /**
     * Mutates this individual's genetic material with a given probability.
     * Mutation introduces random changes to the individual's genes,
     * which helps maintain genetic diversity in the population and
     * explore new areas of the solution space.
     * @param mutationRate The probability that a gene will be mutated.
     */
    void mutate(double mutationRate);

    /**
     * (Optional) Initializes the individual with random "genes".
     * This method is typically used when creating the initial population
     * of individuals.
     */
    void initializeRandomly();
}
