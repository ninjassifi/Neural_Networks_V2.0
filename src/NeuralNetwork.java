import java.util.Arrays;
import java.util.Collections;

public class NeuralNetwork{
    // Middle layers not including start and end nodes
    private final int[] layers;
    private float[][] neurons;
    private float[][][] weights;
    private float fitness;
    public NeuralNetwork(int[] layers) {
        //deep copy of layers of this network
        this.layers = new int[layers.length];
        System.arraycopy(layers, 0, this.layers, 0, layers.length);


        //generate matrix
        initNeurons();
        initWeights();
    }
    public NeuralNetwork(NeuralNetwork copyNetwork){
        this.layers = new int[copyNetwork.layers.length];
        System.arraycopy(copyNetwork.layers, 0, this.layers, 0, copyNetwork.layers.length);
        initNeurons();
        initWeights();
        copyWeights(copyNetwork.weights);
    }
    private void copyWeights(float[][][] copyWeights){
        for(int i = 0; i < weights.length; i++){
            // Loop over all neurons
            for(int j = 0; j < weights[i].length; j++){
                // Multiply the value of the previous neuron by the weights of this neuron
                System.arraycopy(copyWeights[i][j], 0, weights[i][j], 0, weights[i][j].length);
            }
        }
    }

    private void initNeurons(){
        neurons = new float[layers.length][];
        // For every line of (neurons), do one loop and initialize it to (layers) length
        for(int i = 0; i < neurons.length; i++){
            neurons[i] = new float[layers[i]];
        }
        //System.out.println(Arrays.deepToString(neurons));
    }
    private void initWeights() {
        weights = new float[layers.length - 1][][];
        // For every neuron inside the neuron array (except the input layer) make weighted connections
        for(int i = 0; i < neurons.length - 1; i++){
            weights[i] = new float[layers[i]][];
            for(int j = 0; j < layers[i]; j++){
                // [ [ [f][f][f] ][ [[]][] ][ [][][] ] ]
                // Make an array of floats inside weights, size of layers[i + 1]

                weights[i][j] = new float[layers[i + 1]];

                for(int k = 0; k < layers[i + 1]; k++){
                    // Initialize them to a value of -.5 to .5
                    weights[i][j][k] = (float) ((Math.random() - .5) * 2);
                }
            }
        }
        // Reverse it because for some reason it is reversed
        Collections.reverse(Arrays.asList(weights));
        //System.out.println(Arrays.deepToString(weights));
    }
    public float[] feedForward(float[] inputs){
        // Go over the input layer and initialize the inputs to what we put
        System.arraycopy(inputs, 0, neurons[0], 0, inputs.length);
        // Loop over all layers except input
        for(int i = 1; i < layers.length; i++){
            // Loop over all neurons
            for(int j = 0; j < neurons[i].length; j++){
                // Multiply the value of the previous neuron by the weights of this neuron
                float value = 0.25f;
                for(int k = 0; k < neurons[i - 1].length; k++){
                    value += weights[i - 1][j][k] * neurons[i - 1][k];
                }
                // IDK what tanh does
                neurons[i][j] = (float)Math.tanh(value);
            }
        }
        // Return output layer
        return neurons[neurons.length - 1];
    }
    public void mutate(){
        // For each column of neurons
        for (int i = 0; i < weights.length; i++){
            // For each row of neurons
            for (int j = 0; j < weights[i].length; j++){
                // For each weight
                for (int k = 0; k < weights.length; k++) {
                    // Mutate
                    float weight = weights[i][j][k];
                    int randomNumber = (int) (Math.random() * 100);
                    if (randomNumber <= 5) {
                        weight *= -1;
                    } else if (randomNumber <= 10) {
                        weight = (float) (Math.random() - .5);
                    }
                    weights[i][j][k] = weight;
                }
            }
        }
    }
    public void AddFitness(float fit) {
        fitness += fit;
    }

    public void SetFitness(float fit) {
        fitness = fit;
    }

    public float GetFitness() {
        return fitness;
    }
}
