import java.util.*;

/**
 * The main class that handles the entire network
 * Has multiple attributes each with its own use
 */

public class NNImpl {
    private ArrayList<Node> inputNodes; //list of the output layer nodes.
    private ArrayList<Node> hiddenNodes;    //list of the hidden layer nodes
    private ArrayList<Node> outputNodes;    // list of the output layer nodes

    private ArrayList<Instance> trainingSet;    //the training set

    private double learningRate;    // variable to store the learning rate
    private int maxEpoch;   // variable to store the maximum number of epochs
    private Random random;  // random number generator to shuffle the training set

    /**
     * This constructor creates the nodes necessary for the neural network
     * Also connects the nodes of different layers
     * After calling the constructor the last node of both inputNodes and
     * hiddenNodes will be bias nodes.
     */

    NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Random random, Double[][] hiddenWeights, Double[][] outputWeights) {
        this.trainingSet = trainingSet;
        this.learningRate = learningRate;
        this.maxEpoch = maxEpoch;
        this.random = random;

        //input layer nodes
        inputNodes = new ArrayList<>();
        int inputNodeCount = trainingSet.get(0).attributes.size();
        int outputNodeCount = trainingSet.get(0).classValues.size();
        for (int i = 0; i < inputNodeCount; i++) {
            Node node = new Node(0);
            inputNodes.add(node);
        }

        //bias node from input layer to hidden
        Node biasToHidden = new Node(1);
        inputNodes.add(biasToHidden);

        //hidden layer nodes
        hiddenNodes = new ArrayList<>();
        for (int i = 0; i < hiddenNodeCount; i++) {
            Node node = new Node(2);
            //Connecting hidden layer nodes with input layer nodes
            for (int j = 0; j < inputNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
                node.parents.add(nwp);
                
                //add children
                NodeWeightPair nwp2 = new NodeWeightPair(node, outputWeights[i][j]);//
            	inputNodes.get(j).children.add(nwp2);//
            }
            hiddenNodes.add(node);
        }

        //bias node from hidden layer to output
        Node biasToOutput = new Node(3);
        hiddenNodes.add(biasToOutput);

        //Output node layer
        outputNodes = new ArrayList<>();
        for (int i = 0; i < outputNodeCount; i++) {
            Node node = new Node(4);
            //Connecting output layer nodes with hidden layer nodes
            for (int j = 0; j < hiddenNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);                
            	node.parents.add(nwp);
            	
            	//add children
            	NodeWeightPair nwp2 = new NodeWeightPair(node, outputWeights[i][j]);//
            	hiddenNodes.get(j).children.add(nwp2);//
            }
            outputNodes.add(node);
        }
    }

    /**
     * Get the prediction from the neural network for a single instance
     * Return the idx with highest output values. For example if the outputs
     * of the outputNodes are [0.1, 0.5, 0.2], it should return 1.
     * The parameter is a single instance
     */

    public int predict(Instance instance) {
        // TODO: add code here
    	useNN(instance);
    	int predict = 0;
    	double max = 0;
    	for(int i=0; i<outputNodes.size(); i++) {
    		double out = outputNodes.get(i).getOutput();
    		if(out > max) {
    			max = out;
    			predict = i;
    		}
    	}
        return predict;
    }


    /**
     * Train the neural networks with the given parameters
     * <p>
     * The parameters are stored as attributes of this class
     */

    public void train() {
        // TODO: add code here
    	double totleE = 0;
    	for(Instance instance: trainingSet) {
    		double loss = loss(instance);
    		totleE += loss;
    		double dldz = 
    	}
    	totleE/=trainingSet.size();
    	
//    	for each epoch
//
//        shuffle training data
//
//        for each training instance in trainingset
//
//                  do training
//
//                  update weights
//
//        end of training set loop
//
//       for each training instance 
//
//               calculate total loss
//
//       end for
//
//      print average loss
//
//		end epoch loop
    }

    /**
     * Calculate the cross entropy loss from the neural network for
     * a single instance.
     * The parameter is a single instance
     */
    private double loss(Instance instance) {
        // TODO: add code here
    	useNN(instance);
    	double ce = 0;
    	for(int i =0 ; i<outputNodes.size();i++) {
    		double g = outputNodes.get(i).getOutput();
    		ce -= instance.classValues.get(i)*Math.log(g);
    	}
        return ce;
    }
    
    private void useNN(Instance instance) {
    	for(int i =0; i<inputNodes.size();i++) {
    		inputNodes.get(i).setInput(instance.attributes.get(i));
    	}
    	for(Node node: hiddenNodes) {
    		node.calculateOutput(null);
    	}
    	for(Node node: outputNodes) {
    		node.calculateOutput(outputNodes);
    	}
    }
}
