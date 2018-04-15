import java.util.*;

/**
 * Class for internal organization of a Neural Network.
 * There are 5 types of nodes. Check the type attribute of the node for details.
 * Feel free to modify the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; //0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
    public ArrayList<NodeWeightPair> parents = null; //Array List that will contain the parents (including the bias node) with weights if applicable
//    public ArrayList<NodeWeightPair> children = null;
    private double inputValue = 0.0;
    private double outputValue = 0.0;
    private double outputGradient = 0.0;
    private double delta = 0.0; //input gradient

    //Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);

        } else {
            this.type = type;
        }

        if (type == 2 || type == 4) {
            parents = new ArrayList<>();
        }
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInput(double inputValue) {
        if (type == 0) {    //If input node
            this.inputValue = inputValue;
        }
    }

    /**
     * Calculate the output of a node.
     * You can get this value by using getOutput()
     */
    public void calculateOutput(ArrayList<Node> outputNodes) {
    	if (type == 0) {
    		outputValue = inputValue;
    	}
        if (type == 2 || type == 4) {   //Not an input or bias node
            // TODO: add code here
        	double g = g(outputNodes);
        	if(g!=-1) {
        		outputValue = g;
        	}
        	else {
        		System.out.println("calculate output wrong type 1 "+type);
        	}
        }
    }

    //Gets the output value
    public double getOutput() {

        if (type == 0) {    //Input node
            return inputValue;
        } else if (type == 1 || type == 3) {    //Bias node
            return 1.00;
        } else {
            return outputValue;
        }

    }

//    public double getDelta() {
//    	if(type==2||type==4) {
//    		return delta;
//    	}
//    	else {
//    		System.out.println("wrong type");
//    	}
//    	return -9999999;
//    }
    //Calculate the delta value of a node.
    public void calculateDelta(double targetValue, ArrayList<Node> outputNodes, int nodeIndex) {
        if (type == 2 || type == 4)  {
        	// TODO: add code here
        	double delta = 0;
        	if(type == 2) {
        		delta = gPrimeReLU() * calcWeightedOutputDelta(outputNodes,nodeIndex);
        	}
        	else if(type == 4){
        		delta = targetValue - g(outputNodes); 
        	}
        	this.delta = delta;
        }
    }


    //Update the weights between parents node and current node
    public void updateWeight(double learningRate) {
        if (type == 2 || type == 4) {
            // TODO: add code here
        	for(NodeWeightPair parentPair: this.parents) {
        		double deltaW = learningRate * parentPair.node.outputValue * delta;
        		parentPair.weight+=deltaW;
        		//System.out.println(learningRate+" "+parentPair.node.outputValue+" "+delta);
        	}
        }
    }
    
    private double g(ArrayList<Node> outputNodes) {
    	if(type == 2) {
    		return calcReLU(); 
    	}
    	else if(type == 4){
    		return calcSoftMax(outputNodes);
    	}
    	return -1;
    }

    public double gPrimeReLU() {
    	if(type == 2) {
    		if(calcWeightedInputSum(this)<0.0000001) {
    			return 0;
    		}
    		else {
    			return 1;
    		}
    	}
    	return -1;
    }
    
    public double calcReLU() {
    	double value = calcWeightedInputSum(this);
    	value = Math.max(0, value);
    	//System.out.println(value);
		return value;
    }
    
    public double calcSoftMax(ArrayList<Node> outputNodes) {
    	double sum = 0;
    	double z = calcWeightedInputSum(this);
		for(Node node : outputNodes) {
			double nodeValue = calcWeightedInputSum(node);
			sum+=Math.pow(Math.E, nodeValue);
		}
		return Math.pow(Math.E,z)/sum;
    }
    //weighted sum from parents
    public double calcWeightedInputSum(Node node) {
    	double value = 0;
    	for(NodeWeightPair in: node.parents) {
    		value+= in.node.outputValue * in.weight;
    		//System.out.println(in.node.outputValue);
    	}
    	return value;
    }

    //weighted sum from children
    public double calcWeightedOutputDelta(ArrayList<Node> outputNodes,int nodeIndex) {
    	double value = 0;
    	for(Node node: outputNodes) {
    		value+= node.parents.get(nodeIndex).weight*delta;
    	}
    	return value;
    }
}


