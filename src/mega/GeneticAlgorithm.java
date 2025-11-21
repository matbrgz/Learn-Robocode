package mega;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A generic framework for implementing a Genetic Algorithm (GA).
 * This class orchestrates the evolutionary process, including population
 * initialization, selection, crossover, mutation, and fitness evaluation.
 * 
 * Genetic Algorithms are search heuristics inspired by natural selection,
 * often used to find optimized solutions to complex problems. In Robocode,
 * a GA could be used to evolve parameters for gun targeting, movement patterns,
 * or even a combination of strategies.
 * 
 * To use this framework, you need to define:
 * <ul>
 *     <li>An {@link Individual} class that represents a potential solution.</li>
 *     <li>A {@link FitnessFunction} to evaluate how "good" each individual is.</li>
 *     <li>Specific genetic operators (selection, crossover, mutation) tailored
 *         to your {@link Individual} representation.</li>
 * </ul>
 * 
 * @param <T> The type of {@link Individual} that will be evolved by this GA.
 * 
 * @author Gates
 */
public class GeneticAlgorithm<T extends Individual> {

    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount; // Number of best individuals to carry over directly
    private FitnessFunction<T> fitnessFunction;
    private Random random;

    /**
     * Constructs a new GeneticAlgorithm instance.
     * @param populationSize The number of individuals in each generation.
     * @param mutationRate The probability that an individual's gene will be mutated.
     * @param crossoverRate The probability that two parents will mate to produce offspring.
     * @param elitismCount The number of top individuals to preserve for the next generation.
     * @param fitnessFunction The {@link FitnessFunction} used to evaluate individuals.
     */
    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate,
                            int elitismCount, FitnessFunction<T> fitnessFunction) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.fitnessFunction = fitnessFunction;
        this.random = new Random();
    }

    /**
     * Initializes a new random population of individuals.
     * @param individualFactory A factory function or interface to create new random individuals.
     * @return The initial population as a {@link List} of {@link Individual}s.
     */
    public List<T> initializePopulation(IndividualFactory<T> individualFactory) {
        List<T> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(individualFactory.createRandomIndividual());
        }
        return population;
    }

    /**
     * Evaluates the fitness of each individual in the population.
     * @param population The list of individuals in the current population.
     */
    public void evaluatePopulation(List<T> population) {
        for (T individual : population) {
            individual.setFitness(fitnessFunction.evaluate(individual));
        }
    }

    /**
     * Selects individuals from the current population for reproduction.
     * This implementation uses Tournament Selection.
     * @param population The current population.
     * @param tournamentSize The number of individuals to compete in each tournament.
     * @return The selected parent individual.
     */
    public T selectParent(List<T> population, int tournamentSize) {
        // Tournament selection
        List<T> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        // Find the best individual in the tournament
        return Collections.max(tournament, (ind1, ind2) -> Double.compare(ind1.getFitness(), ind2.getFitness()));
    }

    /**
     * Performs crossover (mating) between two parent individuals to produce offspring.
     * The crossover operation is defined within the Individual class.
     * @param parent1 The first parent.
     * @param parent2 The second parent.
     * @return A new individual (offspring) resulting from the crossover.
     */
    public T crossover(T parent1, T parent2) {
        // This assumes the Individual class has a crossover method.
        // It's a conceptual representation. Actual implementation would be in Individual.
        return (T) parent1.crossover(parent2);
    }

    /**
     * Mutates an individual's genes with a certain probability.
     * The mutation operation is defined within the Individual class.
     * @param individual The individual to mutate.
     */
    public void mutate(T individual) {
        // This assumes the Individual class has a mutate method.
        // Actual implementation would be in Individual.
        individual.mutate(mutationRate);
    }

    /**
     * Creates the next generation of individuals.
     * This involves selection, crossover, mutation, and elitism.
     * @param currentPopulation The current generation's population.
     * @return The new generation's population.
     */
    public List<T> evolvePopulation(List<T> currentPopulation) {
        List<T> newPopulation = new ArrayList<>();

        // Sort the current population by fitness (descending)
        Collections.sort(currentPopulation, (ind1, ind2) -> Double.compare(ind2.getFitness(), ind1.getFitness()));

        // Elitism: carry over the best individuals directly
        for (int i = 0; i < elitismCount; i++) {
            newPopulation.add(currentPopulation.get(i));
        }

        // Fill the rest of the new population through selection, crossover, and mutation
        while (newPopulation.size() < populationSize) {
            T parent1 = selectParent(currentPopulation, 3); // Tournament size 3
            T parent2 = selectParent(currentPopulation, 3);

            T offspring = crossover(parent1, parent2);
            mutate(offspring);
            newPopulation.add(offspring);
        }

        return newPopulation;
    }
}
