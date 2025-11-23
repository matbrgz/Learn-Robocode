package mega;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a simple feedforward Artificial Neural Network (ANN).
 * This network is composed of layers of {@link Neuron}s, connected
 * in a feedforward manner. It supports basic operations like
 * feedforward propagation for output calculation.
 * 
 * Neural Networks can be used in Robocode for various tasks, such as:
 * <ul>
 *     <li>Enemy movement prediction.</li>
 *     <li>Targeting decisions.</li>
 *     <li>Adaptive movement patterns.</li>
 *     <li>Behavior selection.</li>
 * </ul>
 * 
 * This is a foundational structure. Training algorithms (e.g., backpropagation)
 * and more complex network architectures (e.g., recurrent networks) would need
 * to be added for a fully functional, learning ANN.
 * 
 * @author Gates
 */
public class NeuralNetwork implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization if needed

    private List<Layer> layers;
    private Random random;

    /**
     * Constructs a new NeuralNetwork with a specified architecture.
     * The `layerSizes` array defines the number of neurons in each layer,
     * starting from the input layer. An {@link ActivationFunction} is applied
     * to all neurons in hidden and output layers.
     * @param layerSizes An array where each element specifies the number of neurons in that layer.
     * @param activationFunction The {@link ActivationFunction} to use for neurons (except input layer).
     */
    public NeuralNetwork(int[] layerSizes, ActivationFunction activationFunction) {
        this.layers = new ArrayList<>();
        this.random = new Random();

        // Create layers
        for (int i = 0; i < layerSizes.length; i++) {
            Layer layer = new Layer(layerSizes[i]);
            this.layers.add(layer);

            // Populate layers with neurons
            for (int j = 0; j < layerSizes[i]; j++) {
                Neuron neuron = new Neuron(activationFunction); // Pass activation function to neuron
                layer.addNeuron(neuron);
            }
        }

        // Connect layers (weights and biases initialization)
        for (int i = 0; i < layers.size() - 1; i++) {
            Layer currentLayer = layers.get(i);
            Layer nextLayer = layers.get(i + 1);

            for (Neuron currentNeuron : currentLayer.getNeurons()) {
                for (Neuron nextNeuron : nextLayer.getNeurons()) {
                    // Initialize weights randomly
                    double weight = random.nextDouble() * 2 - 1; // Between -1 and 1
                    currentNeuron.addOutputConnection(nextNeuron, weight);
                }
            }
            // Initialize biases for neurons in the next layer
            for (Neuron nextNeuron : nextLayer.getNeurons()) {
                 nextNeuron.setBias(random.nextDouble() * 2 - 1);
            }
        }
    }

    /**
     * Performs a feedforward pass through the network with the given input values.
     * This calculates the output of the network based on the inputs and current
     * weights and biases.
     * @param inputs An array of input values for the input layer.
     * @return An array of output values from the output layer.
     * @throws IllegalArgumentException if the number of inputs does not match the input layer size.
     */
    public double[] feedForward(double[] inputs) {
        if (inputs.length != layers.get(0).size()) {
            throw new IllegalArgumentException("Number of inputs does not match input layer size.");
        }

        // Set inputs to the input layer neurons
        Layer inputLayer = layers.get(0);
        for (int i = 0; i < inputs.length; i++) {
            inputLayer.getNeuron(i).setOutput(inputs[i]);
        }

        // Propagate outputs through hidden layers to the output layer
        for (int i = 1; i < layers.size(); i++) {
            Layer currentLayer = layers.get(i);
            Layer previousLayer = layers.get(i - 1);
            
            for (Neuron neuron : currentLayer.getNeurons()) {
                neuron.calculateOutput();
            }
        }

        // Get outputs from the output layer
        Layer outputLayer = layers.get(layers.size() - 1);
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputLayer.size(); i++) {
            outputs[i] = outputLayer.getNeuron(i).getOutput();
        }
        return outputs;
    }

    /**
     * Retrieves a list of all layers in this neural network.
     * @return A list of {@link Layer} objects.
     */
    public List<Layer> getLayers() {
        return layers;
    }

    // --- Conceptual methods for training (to be implemented) ---

    /**
     * (Conceptual) Trains the neural network using a specified training algorithm,
     * such as backpropagation. This method would adjust the weights and biases
     * of the neurons based on a set of training data and desired outputs.
     * @param trainingData A dataset of input-output pairs used for training.
     * @param learningRate The learning rate for the training algorithm.
     * @param epochs The number of training iterations.
     */
    public void train(List<double[]> trainingData, double learningRate, int epochs) {
        // Implementation for training, e.g., backpropagation, gradient descent.
        // This would involve:
        // 1. Iterating through epochs.
        // 2. For each epoch, iterating through training data.
        // 3. Performing feedforward.
        // 4. Calculating error.
        // 5. Performing backpropagation to adjust weights and biases.
        System.out.println("Conceptual: Training the Neural Network.");
    }

    /**
     * (Conceptual) Adjusts the weights and biases of the network based on
     * calculated gradients from a training step.
     */
    public void applyGradientDescent() {
        // Implementation for updating weights and biases.
    }
}
