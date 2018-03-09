import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.lang.Math;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 * 
 * You must add code for the 1 member and 4 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
  private DecTreeNode root;
  //ordered list of class labels
  private List<String> labels; 
  //ordered list of attributes
  private List<String> attributes; 
  //map to ordered discrete values taken by attributes
  private Map<String, List<String>> attributeValues; 
  //map for getting the index
  private HashMap<String,Integer> label_inv;
  private HashMap<String,Integer> attr_inv;
  
  /**
   * Answers static questions about decision trees.
   */
  DecisionTreeImpl() {
    // no code necessary this is void purposefully
  }

  /**
   * Build a decision tree given only a training set.
   * 
   * @param train: the training set
   */
  DecisionTreeImpl(DataSet train) {

    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    // TODO: Homework requirement, learn the decision tree here
    // Get the list of instances via train.instances
    // You should write a recursive helper function to build the tree
    //
    // this.labels contains the possible labels for an instance
    // this.attributes contains the whole set of attribute names
    // train.instances contains the list of instances
  }

  boolean sameLabel(List<Instance> instances){
      // Suggested helper function
      // returns if all the instances have the same label
      // labels are in instances.get(i).label
      // TODO
      return false;
  }
  String majorityLabel(List<Instance> instances){
      // Suggested helper function
      // returns the majority label of a list of examples
      // TODO
      return "";
  }
  double entropy(List<Instance> instances){
      // Suggested helper function
      // returns the Entropy of a list of examples
      // TODO
      return 0.0;
  }
  double conditionalEntropy(List<Instance> instances, String attr){
      // Suggested helper function
      // returns the conditional entropy of a list of examples, given the attribute attr
      // TODO
      return 0.0;
  }
  double InfoGain(List<Instance> instances, String attr){
      // Suggested helper function
      // returns the info gain of a list of examples, given the attribute attr
      return entropy(instances) - conditionalEntropy(instances,attr);
  }
  @Override
  public String classify(Instance instance) {
      // TODO: Homework requirement
      // The tree is already built, when this function is called
      // this.root will contain the learnt decision tree.
      // write a recusive helper function, to return the predicted label of instance
    return "";
  }
  @Override
  public void rootInfoGain(DataSet train) {
    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    // TODO: Homework requirement
    // Print the Info Gain for using each attribute at the root node
    // The decision tree may not exist when this funcion is called.
    // But you just need to calculate the info gain with each attribute,
    // on the entire training set.
  }
  @Override
  public void printAccuracy(DataSet test) {
    // TODO: Homework requirement
    // Print the accuracy on the test set.
    // The tree is already built, when this function is called
    // You need to call function classify, and compare the predicted labels.
    // List of instances: test.instances 
    // getting the real label: test.instances.get(i).label
    return;
  }
  
  @Override
  /**
   * Print the decision tree in the specified format
   * Do not modify
   */
  public void print() {

    printTreeNode(root, null, 0);
  }

  /**
   * Prints the subtree of the node with each line prefixed by 4 * k spaces.
   * Do not modify
   */
  public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < k; i++) {
      sb.append("    ");
    }
    String value;
    if (parent == null) {
      value = "ROOT";
    } else {
      int attributeValueIndex = this.getAttributeValueIndex(parent.attribute, p.parentAttributeValue);
      value = attributeValues.get(parent.attribute).get(attributeValueIndex);
    }
    sb.append(value);
    if (p.terminal) {
      sb.append(" (" + p.label + ")");
      System.out.println(sb.toString());
    } else {
      sb.append(" {" + p.attribute + "?}");
      System.out.println(sb.toString());
      for (DecTreeNode child : p.children) {
        printTreeNode(child, p, k + 1);
      }
    }
  }

  /**
   * Helper function to get the index of the label in labels list
   */
  private int getLabelIndex(String label) {
    if(label_inv == null){
        this.label_inv = new HashMap<String,Integer>();
        for(int i=0; i < labels.size();i++)
        {
            label_inv.put(labels.get(i),i);
        }
    }
    return label_inv.get(label);
  }
 
  /**
   * Helper function to get the index of the attribute in attributes list
   */
  private int getAttributeIndex(String attr) {
    if(attr_inv == null)
    {
        this.attr_inv = new HashMap<String,Integer>();
        for(int i=0; i < attributes.size();i++)
        {
            attr_inv.put(attributes.get(i),i);
        }
    }
    return attr_inv.get(attr);
  }

  /**
   * Helper function to get the index of the attributeValue in the list for the attribute key in the attributeValues map
   */
  private int getAttributeValueIndex(String attr, String value) {
    for (int i = 0; i < attributeValues.get(attr).size(); i++) {
      if (value.equals(attributeValues.get(attr).get(i))) {
        return i;
      }
    }
    return -1;
  }
}
