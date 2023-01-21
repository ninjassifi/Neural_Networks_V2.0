package com.ninjasifi.neuralnetwork;

import java.util.*;

public class Main {
    static int threads = 8;
    static int inputNodes = 4;
    static int outputNodes = 4;
    static int generations = 10;
    // In order to add layers, just add a number in the middle and add a comma
    static int[] layers = new int[] {inputNodes ,outputNodes};
    static NeuralNetwork net = new NeuralNetwork(layers);

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws InterruptedException {
        float percent;
        for (int i = 0; i < generations; i++){
            net = train(net, 1000);
            percent = i;
            percent = (percent / generations) * 100;
            System.out.println((int)percent + "%");
        }
        System.out.println("100%");
        System.out.println(net.getFitness());
        // test the neural networks
        while(true) {

            // Get user number
            int num = scanner.nextInt();

            // Initialize input array
            float[] input = new float[inputNodes];

            // This is temp boolean array to change into 1s and 0s
            boolean[] boolArray;
            boolArray = toBin(num, inputNodes);
            for(int i = 0; i < inputNodes; i++) {
                input[i] = (float)(boolArray[i] ? 1 : 0);
            }

            System.out.println(Arrays.toString(input));
            float[] out = net.feedForward(input);
            for (int i = 0; i < out.length; i++) {
                if(out[i] <= 0){
                    out[i] = 0;
                }else{
                    out[i] = 1;
                }
            }
            System.out.println("output of AI: " + toDec(out));
        }
    }

    // Training is supposed to:
    // 1. create a duplicate network with a little change
    // 2. check if it's better
    // 3. return the best one
    static NeuralNetwork train(NeuralNetwork net, int popSize) throws InterruptedException {
        int bestNet = 0;
        ArrayList<Train> threads = new ArrayList<>(Main.threads);
        for (int i = 0; i < Main.threads; i++) {
            threads.add(new Train(net, (i * popSize) / Main.threads, (i + 1) * popSize / Main.threads));
            threads.get(i).start();
        }
        for (int i = 0; i < Main.threads; i++) {
            threads.get(i).join();
            float currentFitness = threads.get(i).getBestNet().getFitness();
            float bestFitness = threads.get(bestNet).getBestNet().getFitness();
            if(currentFitness > bestFitness){
            //System.out.println(trains.get(i).getBestNet().getFitness());
                bestNet = i;
            }
        }
        System.out.println(threads.get(bestNet).getBestNet().getFitness());
        return threads.get(bestNet).getBestNet();
    }

    public static boolean[] toBin(int number, int length) {
        final boolean[] boolArr = new boolean[length];
        for (int i = 0; i < length; i++) {
            // boolArr [length - 1] is bitwise flip IDK what this is
            boolArr[length - 1 - i] = (1 << i & number) != 0;
        }
        return boolArr;
    }
    static int toDec(float[] binArray){
        int out = 0;
        for (int i = 0; i < binArray.length; i++) {
            if (binArray[i] == 1){
                out += (int) Math.pow(2, binArray.length - i - 1);
            }
        }
        return out;
    }
}