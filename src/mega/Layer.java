package mega;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a layer within a neural network.
 * A layer contains a collection of {@link Neuron}s.
 * 
 * @author Gates
 */
public class Layer implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Neuron> neurons;

    /**
     * Constructs a new Layer with a specified initial capacity for neurons.
     * @param capacity The initial number of neurons this layer can hold.
     */
    public Layer(int capacity) {
        this.neurons = new ArrayList<>(capacity);
    }

    /**
     * Adds a {@link Neuron} to this layer.
     * @param neuron The neuron to add.
     */
    public void addNeuron(Neuron neuron) {
        this.neurons.add(neuron);
    }

    /**
     * Returns the number of neurons in this layer.
     * @return The size of the layer.
     */
    public int size() {
        return this.neurons.size();
    }

    /**
     * Retrieves a {@link Neuron} from this layer by its index.
     * @param index The index of the neuron to retrieve.
     * @return The Neuron at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()}).
     */
    public Neuron getNeuron(int index) {
        return this.neurons.get(index);
    }

    /**
     * Returns an unmodifiable list of all neurons in this layer.
     * @return A {@link List} of {@link Neuron} objects.
     */
    public List<Neuron> getNeurons() {
        return java.util.Collections.unmodifiableList(neurons);
    }
}
