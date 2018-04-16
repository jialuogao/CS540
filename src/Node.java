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
        if (type != 1 && type != 3) {
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
    	else if (type == 2 || type == 4) {   //Not an input or bias node
            // TODO: add code here
        	double g = 0;
        	if(type == 2) {
        		g = calcReLU(); 
        	}
        	else if(type == 4){
        		g = calcSoftMax(outputNodes);
        	}
    		outputValue = g;
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

    public double getDelta() {
    	if(type==2||type==4) {
    		return delta;
    	}
    	return -9999999;
    }
    //Calculate the delta value of a node.
    public void calculateDelta(double targetValue, ArrayList<Node> outputNodes, int nodeIndex) {
        if (type == 2 || type == 4)  {
        	// TODO: add code here
        	double delta = 0;
        	if(type == 2) {
        		delta = gPrimeReLU() * calcWeightedOutputDelta(outputNodes,nodeIndex);
        		//System.out.println(gPrimeReLU()+"  "+calcWeightedOutputDelta(outputNodes,nodeIndex));
        	}
        	else if(type == 4){
        		delta = targetValue - outputValue; 
        	}
        	this.delta = delta;
        }
    }


    //Update the weights between parents node and current node
    public void updateWeight(double learningRate) {
        if (type == 2 || type == 4) {
            // TODO: add code here
        	for(NodeWeightPair parentPair: this.parents) {
        		double deltaW = learningRate * outputValue * delta;
        		parentPair.weight+=deltaW;
        		//System.out.println(learningRate+" "+parentPair.node.outputValue+" "+delta);
        	}
        }
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
    	double value = Math.max(0, inputValue);
    	//System.out.println(inputValue +"   "+value);
    	//System.out.println(value);
		return value;
    }
    
    public double getInput() {
    	return inputValue;
    }
    public double calcSoftMax(ArrayList<Node> outputNodes) {
    	double sum = 0;
    	double z = Math.exp(inputValue);
//    	System.out.println(inputValue);
//    	System.out.println(inputValue);
		for(Node node : outputNodes) {
			sum+=Math.exp(node.getInput());
//			System.out.println(node.inputValue);
		}
//		System.out.println("a      "  +z);
//		System.out.println("b       "+sum);
		return z/sum;
    }
    
    //weighted sum from parents
    public double calcWeightedInputSum(Node node) {
    	double value = 0;
    	for(NodeWeightPair in: node.parents) {
    		value+= in.node.outputValue * in.weight;
    	}
    	return value;
    }

    //weighted sum from children
    public double calcWeightedOutputDelta(ArrayList<Node> outputNodes,int nodeIndex) {
    	double value = 0;
    	for(Node node: outputNodes) {
    		value+= node.parents.get(nodeIndex).weight*node.delta;
    		//System.out.println(node.parents.get(nodeIndex).weight+"  "+delta);
    	}
    	return value;
    }
}


