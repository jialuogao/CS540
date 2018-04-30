import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier {
	private Instance[] m_trainingData;
	private int m_v;
	private double m_delta;
	public int m_sports_count, m_business_count;
	public int m_sports_word_count, m_business_word_count;
	private HashMap<String,Integer> m_map[] = new HashMap[2];

  /**
   * Trains the classifier with the provided training data and vocabulary size
   */
  @Override
  public void train(Instance[] trainingData, int v) {
    // TODO : Implement
    // For all the words in the documents, count the number of occurrences. Save in HashMap
    // e.g.
    // m_map[0].get("catch") should return the number of "catch" es, in the documents labeled sports
    // Hint: m_map[0].get("asdasd") would return null, when the word has not appeared before.
    // Use m_map[0].put(word,1) to put the first count in.
    // Use m_map[0].replace(word, count+1) to update the value
  	  m_trainingData = trainingData;
  	  m_v = v;
  	  m_map[0] = new HashMap<>();//Sports
  	  m_map[1] = new HashMap<>();//Business
  	  for(Instance inst : trainingData)
  	  {
  		  int map = -1;
  		  if(inst.label==Label.BUSINESS) {
  			  map = 1;
  		  }
  		  else if(inst.label==Label.SPORTS) {
  			  map = 0;
  		  }
  		  for(String word : inst.words) {
  			  // Label label = word.label;
  			  int count;
  			  if(m_map[map].get(word) != null) {
  				 count = m_map[map].get(word);
  				 m_map[map].put(word, count+1);
  			  }
  			  else {
  				  m_map[map].put(word,1);  				  
  			  }
  		  }
  	  }
  	  documents_per_label_count(trainingData);
  	  words_per_label_count(trainingData);
  	  
  }

  /*
   * Counts the number of documents for each label
   */
  public void documents_per_label_count(Instance[] trainingData){
    // TODO : Implement
    m_sports_count = 0;
    m_business_count = 0;
    for(Instance inst : trainingData) {
    	if(inst.label==Label.BUSINESS) {
    		m_business_count ++;
    	}
    	else if(inst.label==Label.SPORTS) {
    		m_sports_count ++;
    	}
    }
  }

  /*
   * Prints the number of documents for each label
   */
  public void print_documents_per_label_count(){
  	  System.out.println("SPORTS=" + m_sports_count);
  	  System.out.println("BUSINESS=" + m_business_count);
  }


  /*
   * Counts the total number of words for each label
   */
  public void words_per_label_count(Instance[] trainingData){
    // TODO : Implement
    m_sports_word_count = 0;
    m_business_word_count = 0;
    for(Instance inst : trainingData) {
    	if(inst.label==Label.BUSINESS) {
			m_business_word_count+=inst.words.length;
    	}
    	else if(inst.label==Label.SPORTS) {
			m_sports_word_count+=inst.words.length;
    	}
    }
  }

  /*
   * Prints out the number of words for each label
   */
  public void print_words_per_label_count(){
  	  System.out.println("SPORTS=" + m_sports_word_count);
  	  System.out.println("BUSINESS=" + m_business_word_count);
  }

  /**
   * Returns the prior probability of the label parameter, i.e. P(SPORTS) or P(BUSINESS)
   */
  @Override
  public double p_l(Label label) {
    // TODO : Implement
    // Calculate the probability for the label. No smoothing here.
    // Just the number of label counts divided by the number of documents.
    double ret = 0;
    if(label==Label.BUSINESS) {
    	ret = (double)m_business_count/(double)m_trainingData.length;
    }
    else if(label==Label.SPORTS) {
    	ret = (double)m_sports_count/(double)m_trainingData.length;
    }
    return ret;
  }

  /**
   * Returns the smoothed conditional probability of the word given the label, i.e. P(word|SPORTS) or
   * P(word|BUSINESS)
   */
  @Override
  public double p_w_given_l(String word, Label label) {
    // TODO : Implement
    // Calculate the probability with Laplace smoothing for word in class(label)
    double ret = 0;
    m_delta = 0.00001;
    if(label==Label.BUSINESS) {
    	double prob = 0;
    	if(m_map[1].get(word)!=null) {
    		prob = m_map[1].get(word);    		
    	}
    	ret = (prob+m_delta)/(m_business_word_count+m_delta*m_v);
    }
    else if(label==Label.SPORTS) {
    	double prob = 0;
    	if(m_map[0].get(word)!=null) {
    		prob = m_map[0].get(word);    		
    	}
    	ret = (prob+m_delta)/(m_sports_word_count+m_delta*m_v);
    }
    return ret;
  }

  /**
   * Classifies an array of words as either SPORTS or BUSINESS.
   */
  @Override
  public ClassifyResult classify(String[] words) {
    // TODO : Implement
    // Sum up the log probabilities for each word in the input data, and the probability of the label
    // Set the label to the class with larger log probability
    ClassifyResult ret = new ClassifyResult();
    ret.label = Label.SPORTS;
    ret.log_prob_sports = Math.log(p_l(Label.SPORTS));
    ret.log_prob_business = Math.log(p_l(Label.BUSINESS));
    for(String word:words) {
    	ret.log_prob_sports+=Math.log(p_w_given_l(word,Label.SPORTS));
    	ret.log_prob_business+=Math.log(p_w_given_l(word,Label.BUSINESS));
    }
    if(ret.log_prob_business>ret.log_prob_sports) {
    	ret.label = Label.BUSINESS;
    }
    return ret; 
  }
  
  /*
   * Constructs the confusion matrix
   */
  @Override
  public ConfusionMatrix calculate_confusion_matrix(Instance[] testData){
    // TODO : Implement
    // Count the true positives, true negatives, false positives, false negatives
    int TP, FP, FN, TN;
    TP = 0;
    FP = 0;
    FN = 0;
    TN = 0;
    for(Instance inst: testData) {
    	ClassifyResult result = classify(inst.words);
    	if(inst.label==result.label) {
    		if(inst.label==Label.BUSINESS) {
    			TN++;
    		}
    		else {
    			TP++;
    		}
    	}
    	else {
    		if(inst.label==Label.BUSINESS) {
    			FP++;
    		}
    		else {
    			FN++;
    		}
    	}
    }
    return new ConfusionMatrix(TP,FP,FN,TN);
  }
  
}
