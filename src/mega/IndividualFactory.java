package mega;

/**
 * Interface for a factory that creates {@link Individual} instances.
 * This factory is used by the {@link GeneticAlgorithm} to generate
 * the initial population of random individuals.
 * 
 * @param <T> The type of {@link Individual} that this factory will create.
 * 
 * @author Gates
 */
public interface IndividualFactory<T extends Individual> {

    /**
     * Creates and returns a new {@link Individual} instance, typically
     * initialized with random "genes".
     * @return A new randomly initialized {@link Individual}.
     */
    T createRandomIndividual();
}
