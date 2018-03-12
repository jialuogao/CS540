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
    this.root = buildTreeRec(train.instances,this.attributes,null,majorityLabel(train.instances));
  }
  int x=0;
  private DecTreeNode buildTreeRec(List<Instance> instances, List<String> attributes, String parentAttrValue, String defaultLabel) {
	  if(instances.isEmpty()) {
		  return new DecTreeNode(defaultLabel,null,parentAttrValue,true);
	  }
	  if(sameLabel(instances)) {
		  return new DecTreeNode(instances.get(0).label,null,parentAttrValue,true);
	  }
	  if(attributes.isEmpty()) {
		  return new DecTreeNode(majorityLabel(instances),null,parentAttrValue,true);
	  }
	  String bestAttr = "";
	  double maxInfo = Double.NEGATIVE_INFINITY;
	  for (String attr : attributes) {
		  double info = InfoGain(instances, attr);
		  if(info>maxInfo) {
			  maxInfo = info;
			  bestAttr = attr;
		  }
	  }
	  DecTreeNode tree = new DecTreeNode(majorityLabel(instances),bestAttr,parentAttrValue,false);
	  List<String> attrNames = attributeValues.get(bestAttr); 
	  int attrIndex =  getAttributeIndex(bestAttr);
	  for(String attrName: attrNames) {
		  List<Instance> partialInst = new ArrayList<Instance>();
		  for(Instance inst : instances) {
			  if(inst.attributes.get(attrIndex).equals(attrName)) {
				  partialInst.add(inst);
			  }
		  }
		  List<String> subAttributes = new ArrayList<String>();
		  for(String attr: attributes) {
			  if(!attr.equals(bestAttr)) {
				  subAttributes.add(attr);
			  }
		  }
		  DecTreeNode subTree = buildTreeRec(partialInst,subAttributes,attrName,majorityLabel(instances));
		  tree.addChild(subTree);
	  }
	  return tree;
  }
  
  boolean sameLabel(List<Instance> instances){
      // Suggested helper function
      // returns if all the instances have the same label
      // labels are in instances.get(i).label
      // TODO
	  boolean sameLabel = true;
	  for(int i=0;i<instances.size()-1;i++) {
		  if(!instances.get(i).label.equals(instances.get(i+1).label)){
			  sameLabel = false;
		  }
	  }
      return sameLabel;
  }

//  String majorityLabel(List<Instance> instances){
//      // Suggested helper function
//      // returns the majority label of a list of examples
//      // TODO
//	  String majorityLabel = "";
//	  int majorityCount = 0;
//	  for(Instance inst: instances) {
//		  if(!inst.label.equals(majorityLabel)) {
//			  int labelCount = 0;
//			  for(Instance inst2: instances) {
//				  if(inst.label.equals(inst2.label)) {
//					  labelCount++;
//				  }
//			  }
//			  if(labelCount > majorityCount) {
//				  majorityLabel = inst.label;
//				  majorityCount = labelCount;
//			  }
//		  }
//	  }
//      return majorityLabel;
//  }

  String majorityLabel(List<Instance> instances){
      // Suggested helper function
      // returns the majority label of a list of examples
      // TODO
	  int[] labelCount = new int[labels.size()];
	  for(Instance inst: instances) {
		  labelCount[getLabelIndex(inst.label)]++;
	  }
	  int maxCount = -1;
	  int maxIndex = -1;
	  for(int i=0;i<labelCount.length;i++) {
		  if(labelCount[i]>maxCount) {
			  maxCount = labelCount[i];
			  maxIndex = i;
		  }
	  }
	  String majorityLabel = labels.get(maxIndex);
      return majorityLabel;
  }
  
  double entropy(List<Instance> instances){
      // Suggested helper function
      // returns the Entropy of a list of examples
      // TODO
		int numLabel1 = 0;
		int numLabel2 = 0;
		for (Instance inst : instances) {
			if (inst.label.equals(this.labels.get(0))) {
				numLabel1++;
			} else if (inst.label.equals(this.labels.get(1))) {
				numLabel2++;
			}
		}
		double entropy;
		double p1 = (double) numLabel1 / instances.size();
		double p2 = (double) numLabel2 / instances.size();
		if(p1 <= 0.0000001) {
			p1 = 1;
		}
		if(p2 <= 0.0000001) {
			p2 = 1;
		}
		entropy = -p1 * (Math.log(p1) / Math.log(2)) - p2 * (Math.log(p2) / Math.log(2));
		return entropy;
  }

  double conditionalEntropy(List<Instance> instances, String attr){
      // Suggested helper function
      // returns the conditional entropy of a list of examples, given the attribute attr
      // TODO
	  //this.attributeValues
	  int attrIndex = getAttributeIndex(attr);
	  List<String> attrNames = attributeValues.get(attr);
	  int[] attrNameNum = new int[attrNames.size()];
	  for(Instance inst : instances) {
		  String attrValue = inst.attributes.get(attrIndex);
		  for(String attrName : attrNames) {
	    	if(attrName.equals(attrValue)) {
	    		int attrNameIndex = getAttributeValueIndex(attr, attrName);
	    		attrNameNum[attrNameIndex]++;
	    	}
		  }
	  }
	  double[] attrNameProb = new double[attrNameNum.length];
	  for(int i=0;i<attrNameNum.length;i++) {
		  attrNameProb[i] = (double)attrNameNum[i]/instances.size();
	  }
	  double[] probTimesEntropyArray = new double[attrNameProb.length];
	  for(int i=0;i<attrNames.size();i++) {
		  String attrName = attrNames.get(i);
		  List<Instance> partialInst = new ArrayList<Instance>();
		  for(Instance inst : instances) {
			  if(inst.attributes.get(attrIndex).equals(attrName)) {
				  partialInst.add(inst);
			  }
		  }
		  if(!partialInst.isEmpty()) {
			  double entropy = entropy(partialInst);
			  probTimesEntropyArray[i]=attrNameProb[i]*entropy;			  
		  }
	  }
	  double conditionalEntropy = 0;
	  for(double probTimesEntropy : probTimesEntropyArray) {
		  conditionalEntropy += probTimesEntropy;
	  }
      return conditionalEntropy;
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
	  DecTreeNode node = this.root;
	  while(!node.terminal) {
		  List<DecTreeNode> children = node.children;
		  String divideAttr = node.attribute;
		  int divideAttrIndex = getAttributeIndex(divideAttr);
		  String attrValue = instance.attributes.get(divideAttrIndex);
		  int childIndex = getAttributeValueIndex(divideAttr, attrValue);
		  node = children.get(childIndex);
	  }
	  String classLabel=node.label;
	  return classLabel;
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
    for (String attr : this.attributes) {
    	double info = InfoGain(train.instances, attr);
    	System.out.print(attr + " ");
    	System.out.format("%.5f\n", info);
    }
  }

  @Override
  public void printAccuracy(DataSet test) {
    // TODO: Homework requirement
    // Print the accuracy on the test set.
    // The tree is already built, when this function is called
    // You need to call function classify, and compare the predicted labels.
    // List of instances: test.instances 
    // getting the real label: test.instances.get(i).label
	int correctNum = 0;
	for(Instance inst: test.instances) {
		String predict = classify(inst);
		if(predict.equals(inst.label)) {
			correctNum++;
		}
	}
	double accuracy = (double)correctNum/test.instances.size();
	System.out.format("%.5f\n", accuracy);
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
