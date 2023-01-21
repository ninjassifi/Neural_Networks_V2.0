package com.ninjasifi.neuralnetwork;

import java.util.ArrayList;

public class Train extends Thread {
    int bestNet;
    int min;
    int max;
    float[] input;
    float[] out;
    // The copy of the first net
    NeuralNetwork net;

    // List of all the nets
    ArrayList<NeuralNetwork> nets;


    public Train(NeuralNetwork net, int min, int max) {
        this.bestNet = 0;
        this.min = min;
        this.max = max;
        this.net = net;
        nets = new ArrayList<>(max - min);
        for (int i = 0; i < max; i++) {
            nets.add(i, new NeuralNetwork(net));
        }
    }

    // Put all the code here for training
    public void run() {
        input = new float[Main.layers[0]];

        for (int i = 0; i < max; i++) {

            // Mutate
            nets.get(i).mutate();

            // Actual training

            // Array of binary
            boolean[] boolArray;
            for (int k = 0; k < Math.pow(2, input.length); k++) {
                boolArray = Main.toBin(k, input.length);

                // input = binary
                for (int j = 0; j < input.length; j++) {
                    input[j] = (float) (boolArray[j] ? 1 : 0);
                }
                // Calculate out
                out = nets.get(i).feedForward(input);


                // Turn out into 1s or 0s
                for (int j = 0; j < out.length; j++) {
                    if (out[j] <= 0) {
                        out[j] = 0;
                    } else {
                        out[j] = 1;
                    }
                }

                // Check if it does what I want it to do
                for (int j = 0; j < out.length; j++) {
                    if (out[j] == input[j]) {
                        nets.get(i).addFitness(1);
                    }
                }
            }
            // Check if this network is best
            if (nets.get(i).getFitness() > nets.get(bestNet).getFitness()) {
                bestNet = i;
            }
        }
    }

    public void reset(NeuralNetwork net) {
        this.bestNet = 0;
        this.net = net;
        this.net.setFitness(0);
    }

    public NeuralNetwork getBestNet() {
        NeuralNetwork network = nets.get(bestNet);
        return network;
    }
}