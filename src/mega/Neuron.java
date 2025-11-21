package mega;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single neuron within a neural network.
 * A neuron receives inputs from previous layers, applies weights and a bias,
 * and then passes the result through an {@link ActivationFunction} to produce
 * an output.
 * 
 * @author Gates
 */
public class Neuron implements Serializable {
    private static final long serialVersionUID = 1L;

    private double bias;
    private double output;
    private ActivationFunction activationFunction;
    private Map<Neuron, Double> inputWeights; // Weights from input neurons to this neuron
    private Map<Neuron, Double> outputWeights; // Weights from this neuron to output neurons (for connecting)

    /**
     * Constructs a new Neuron with a specified activation function.
     * Weights and bias are typically initialized randomly by the network.
     * @param activationFunction The {@link ActivationFunction} to use for this neuron.
     */
    public Neuron(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        this.inputWeights = new HashMap<>();
        this.outputWeights = new HashMap<>(); // Used for internal connection management
    }

    /**
     * Sets the bias for this neuron.
     * @param bias The new bias value.
     */
    public void setBias(double bias) {
        this.bias = bias;
    }

    /**
     * Gets the bias of this neuron.
     * @return The bias value.
     */
    public double getBias() {
        return bias;
    }

    /**
     * Adds an input connection to this neuron from another neuron with a specific weight.
     * (Conceptual - a more robust implementation would manage connections differently).
     * @param inputNeuron The neuron providing input.
     * @param weight The weight of the connection.
     */
    public void addInputConnection(Neuron inputNeuron, double weight) {
        this.inputWeights.put(inputNeuron, weight);
    }

    /**
     * Adds an output connection from this neuron to another neuron with a specific weight.
     * This method is mainly used during network construction to establish connections.
     * @param outputNeuron The neuron receiving output from this neuron.
     * @param weight The weight of the connection.
     */
    public void addOutputConnection(Neuron outputNeuron, double weight) {
        // In a feedforward network, 'this' neuron's output influences 'outputNeuron's input.
        // The weight is stored on the 'input' side of the connection.
        outputNeuron.addInputConnection(this, weight);
    }


    /**
     * Calculates the output of this neuron.
     * This involves summing weighted inputs, adding the bias, and
     * applying the activation function.
     */
    public void calculateOutput() {
        double sum = bias;
        for (Map.Entry<Neuron, Double> entry : inputWeights.entrySet()) {
            Neuron inputNeuron = entry.getKey();
            Double weight = entry.getValue();
            sum += inputNeuron.getOutput() * weight;
        }
        this.output = activationFunction.activate(sum);
    }

    /**
     * Gets the calculated output of this neuron.
     * @return The output value.
     */
    public double getOutput() {
        return output;
    }

    /**
     * Sets the output of this neuron directly.
     * This is primarily used for setting the input values in the input layer.
     * @param output The output value to set.
     */
    public void setOutput(double output) {
        this.output = output;
    }

    /**
     * Returns the weight of the connection from a given input neuron to this neuron.
     * @param inputNeuron The input neuron.
     * @return The weight of the connection, or null if no connection exists.
     */
    public Double getInputWeight(Neuron inputNeuron) {
        return inputWeights.get(inputNeuron);
    }

    /**
     * Sets the weight of the connection from a given input neuron to this neuron.
     * @param inputNeuron The input neuron.
     * @param weight The new weight value.
     */
    public void setInputWeight(Neuron inputNeuron, double weight) {
        inputWeights.put(inputNeuron, weight);
    }
}
