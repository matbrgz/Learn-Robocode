package mega;

/**
 * Interface for activation functions used in a Neural Network.
 * An activation function transforms the weighted sum of inputs and bias
 * of a neuron into its output signal. Different activation functions
 * introduce non-linearity, allowing neural networks to learn complex patterns.
 * 
 * Common activation functions include Sigmoid, ReLU, Tanh, etc.
 * 
 * @author Gates
 */
public interface ActivationFunction {

    /**
     * Applies the activation function to the given input value.
     * @param input The weighted sum of inputs and bias of a neuron.
     * @return The output of the activation function.
     */
    double activate(double input);

    /**
     * Calculates the derivative of the activation function with respect to its input.
     * This is crucial for training algorithms like backpropagation.
     * @param input The input value (before activation).
     * @return The derivative of the activation function.
     */
    double derivative(double input);
}
