import java.util.*;

public class Main {
    static int inputNodes = 2;
    static int outputNodes = 2;
    static int generations = 10000;
    static int[] layers = new int[] {inputNodes, 10, 10, outputNodes};
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        NeuralNetwork net = null;
        for(int i = 0; i < generations; i++)
            net = train(net, 100, 0, 3);
        System.out.println("done");
        // test the neural networks
        while(true) {

            int num = scanner.nextInt();
            float[] input = new float[inputNodes];
            boolean[] boolArray = new boolean[input.length];
            boolArray = toBin(num, inputNodes);
            for(int i = 0; i < inputNodes; i++) {
                input[i] = (float)(boolArray[i] ? 1 : 0);
            }
            float[] out = net.feedForward(input);
            System.out.println("output of array: " + Arrays.toString(out));
        }
    }
    static NeuralNetwork train(NeuralNetwork net, int populationSize, int min, int max) {
        // Random number var
        int randomNumber;
        boolean[] boolArray;
        float[] binArray;
        int lastDeviation = 10000;
        int deviation = 0;
        int bestNetwork = 0;
        float[] out;
        // Generate network
        if (net == null) {
            net = new NeuralNetwork(layers);
        }
        List<NeuralNetwork> nets = new ArrayList<NeuralNetwork>();
        for (int i = 0; i < populationSize; i++) {
            // Add neuralNetwork to nets
            nets.add(new NeuralNetwork(net));

            // Mutate
            //nets.get(i).mutate();

            // Reset deviation
            deviation = 0;


            // Randomly choose x numbers and see the deviation of them, x being 10
            for (int k = 0; k < 10; k++) {
                // Generate binary for decimal
                randomNumber = (int) Math.floor((Math.random() * max) + min);
                boolArray = toBin(randomNumber, inputNodes);
                binArray = new float[inputNodes];
                for (int j = 0; j < inputNodes; j++) {
                    binArray[j] = (float) (boolArray[j] ? 1 : 0);
                }

                // Convert the answers to negative or positive
                for (int j = 0; j < max - 1; j++) {
                    binArray[j] -= .5;
                }

                // Run through network
                out = nets.get(i).feedForward(binArray);

                // Basically turn out into negative or positive, then figure out the deviation
                for (int j = 0; j < binArray.length; j++) {
                    if (out[j] < 0) {
                        out[j] = (float) -0.5;
                    } else {
                        out[j] = (float) 0.5;
                    }
                    // Formula is absoluteValueOf((firstDigitInBinArray - firstDigitInOut) * 2^theFirstDigitBinary)
                    // Meaning if the bit of binArray[0] = 1, and out[0] = 0, it would be (1 - 0) * what place it is in binary, (this case is 2)
                    // the way I store it is read from right to left, 10 = 2, and 01 = 1
                    deviation += Math.abs((binArray[j] - out[j]) * Math.pow(2, binArray.length - 1 - j));
                }
            }

            // Pick the next best algorithm
            if (deviation < lastDeviation) {
                bestNetwork = i;
            }
            // Remember the last deviation to see if this ai is better than the last one
            lastDeviation = deviation;
        }
        return nets.get(bestNetwork);
    }
    static float[] toDec(){
            return null;
    }
    private static boolean[] toBin(int number, int length) {
        final boolean[] boolArr = new boolean[length];
        for (int i = 0; i < length; i++) {
            // boolArr [length - 1] is bitwise flip IDK what this is
            boolArr[length - 1 - i] = (1 << i & number) != 0;
        }
        return boolArr;
    }
}