import java.util.*;

public class Main {
    static int inputNodes = 8;
    static int outputNodes = 8;
    static int generations = 10000;
    // In order to add layers, just add a number in the middle and add a comma
    static int[] layers = new int[] {inputNodes, 10, outputNodes};
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        NeuralNetwork net = null;
        for (int i = 0; i < generations; i++)
            net = train(net, 100);
        System.out.println("done");

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
            System.out.println("output of array: " + Arrays.toString(out));
        }
    }
    // Training is supposed to:
    // 1. create a duplicate network with a little change
    // 2. check if it's better
    // 3. return the best one
    static NeuralNetwork train(NeuralNetwork net, int popSize) {

        // Make an input as long as there are input nodes
        float[] input = new float[inputNodes];

        // Out
        float[] out;

        // Random Number stuff
        float randomNumber;

        // Best network stuff
        int bestNetwork = 0;

        // New list of networks
        List<NeuralNetwork> nets = new ArrayList<>();

        // Check if the neuralNetwork doesn't exist, if not, make one
        if (net == null) {
            net = new NeuralNetwork(layers);
        }
        for (int i = 0; i < popSize; i++) {
            // Add parent network to list, and mutate
            nets.add(i, new NeuralNetwork(net));
            nets.get(i).mutate();

            for (int k = 0; k < Math.pow(2, input.length); k++) {

                boolean[] boolArray;
                boolArray = toBin(k, input.length);
                for (int j = 0; j < input.length; j++) {
                    input[j] = (float)(boolArray[j] ? 1 : 0);
                }
                // Get output
                out = nets.get(i).feedForward(input);


                // Turn out into 1s or 0s
                for (int j = 0; j < out.length; j++) {
                    if(out[j] <= 0){
                        out[j] = 0;
                    }else{
                        out[j] = 1;
                    }
                }

                // Check if it does what I want it to do
                for (int j = 0; j < out.length; j++) {
                    if(out[j] == input[j]){
                        nets.get(i).addFitness(1);
                    }
                }
            }

            // Check if this network is best
            if(nets.get(i).getFitness() > nets.get(bestNetwork).getFitness()){
                bestNetwork = i;
            }
        }
        return nets.get(bestNetwork);
    }
    private static boolean[] toBin(int number, int length) {
        final boolean[] boolArr = new boolean[length];
        for (int i = 0; i < length; i++) {
            // boolArr [length - 1] is bitwise flip IDK what this is
            boolArr[length - 1 - i] = (1 << i & number) != 0;
        }
        return boolArr;
    }
    static float[] toDec(){
            return null;
    }
}