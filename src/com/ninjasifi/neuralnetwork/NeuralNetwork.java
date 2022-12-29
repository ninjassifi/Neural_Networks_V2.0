package com.ninjasifi.neuralnetwork;

import java.util.Arrays;
import java.util.Collections;

public class NeuralNetwork{
    // Middle layers not including start and end nodes
    private final int[] layers;
    private float[][] neurons;
float[][][] weights;
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
            // Loop over all weights
            for(int j = 0; j < weights[i].length; j++){
                // Copy the weights
                System.arraycopy(copyWeights[i][j], 0, weights[i][j], 0, weights[i][j].length);
            }
        }
    }

    private void initNeurons(){
        neurons = new float[layers.length][];
        // For every line of (neurons), do one loop and initialize it to (layers) length

        for(int i = 0; i < neurons.length; i++){

            neurons[i] = new float[layers[i]];
            /*
            // Initialize neurons to -0.5 to 0.5
            for (int j = 0; j < neurons[i].length; j++) {
                neurons[i][j] = (float) (Math.random() - .5);
            }
             */
        }
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
                    //weights[i][j][k] = 1;
                    weights[i][j][k] = (float) (Math.random() - .5);
                }
            }
        }
        // Reverse it because for some reason it is reversed
        Collections.reverse(Arrays.asList(weights));
        //System.out.println(Arrays.deepToString(weights));
    }
    public float[] feedForward(float[] inputs){
        float value;
        // Go over the input layer and initialize the inputs to what we put
        System.arraycopy(inputs, 0, neurons[0], 0, inputs.length);

        // Loop over all layers except input
        for(int i = 1; i < layers.length; i++){

            // Loop over all neurons
            for(int j = 0; j < neurons[i].length; j++){
                value = 0;
                // Multiply the value of the previous neuron by the weights of this neuron
                for(int k = 0; k < neurons[i - 1].length; k++){
                    value += weights[i - 1][j][k] * neurons[i - 1][k];
                }

                // IDK what tanh does, I followed a tutorial
                // "hyperbolic tangent activation" what the heck does that mean
                neurons[i][j] = (float)Math.tanh(value);
            }
        }

        // Return output layer
        return neurons[neurons.length - 1];
    }
    public void mutate(){
        float weight;
        //float neuron;
        float factor;
        int randomNumber;
        // Mutate weights
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    for (int k = 0; k < weights[i][j].length; k++) {
                        weight = weights[i][j][k];

                        // Mutate weight value
                        randomNumber = (int) (Math.random() * 100);

                        if (randomNumber <= 2f) { //if 1
                            //flip sign of weight
                            weight *= -1f;
                        } else if (randomNumber <= 4f) { //if 2
                            //pick random weight between -1 and 1
                            weight = (float) (Math.random() - .5);
                        } else if (randomNumber <= 6f) { //if 3
                            //randomly increase by 0% to 100%
                            factor = (float) (Math.random() + 1);
                            weight *= factor;
                        } else if (randomNumber <= 8f) { //if 4
                            //randomly decrease by 0% to 100%
                            factor = (float)Math.random();
                            weight *= factor;
                        }
                        weights[i][j][k] = weight;
                    }
                }
            }
            /*
            for (int i = 0; i < neurons.length; i++) {
                for (int j = 0; j < neurons[i].length; j++) {
                    neuron = neurons[i][j];

                    randomNumber = (int)(Math.random() * 100);
                    if (randomNumber <= 2f) { //if 1
                        //flip sign of weight
                        neuron *= -1f;
                    } else if (randomNumber <= 4f) { //if 2
                        //pick random weight between -1 and 1
                        neuron = (float) (Math.random() - .5);
                    } else if (randomNumber <= 6f) { //if 3
                        //randomly increase by 0% to 100%
                        factor = (float) (Math.random() + 1);
                        neuron *= factor;
                    } else if (randomNumber <= 8f) { //if 4
                        //randomly decrease by 0% to 100%
                        factor = (float)Math.random();
                        neuron *= factor;
                    }
                    neurons[i][j] = neuron;
                }
            }
             */
        }
    public void addFitness(float fit) {
        fitness += fit;
    }

    public void setFitness(float fit) {
        fitness = fit;
    }

    public float getFitness() {
        return fitness;
    }
    public int compareTo(NeuralNetwork other) {
        if (other == null) return 1;

        if (fitness > other.fitness)
            return 1;
        else if (fitness < other.fitness)
            return -1;
        else
            return 0;
    }
}
