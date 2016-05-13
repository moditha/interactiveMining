import java.util.LinkedList;
import java.util.Random;

public class Sampling {
	
	protected LinkedList<LinkedList> matrixinput;
	
	protected LinkedList<LinkedList> interestingPatterns;
	
	
	protected int n_tuples=0;
	protected int[] weights;
	protected int powerSetSum=0;
	protected LinkedList<Integer> sample;
	protected LinkedList<Evaluation_Object> relevantFeedback;
	

	
	/**
	 * Create the weights for each tuple - create_weights()
	 * According to the probability given by the weights creates a vector with the indexes of the tuples to be sampled - getSample()
	 * Retrieves the tuples for sampling, creates a set for each and the respective powerset for each
	 * @param matrix
	 */
	public Sampling (LinkedList<LinkedList> matrix, LinkedList<Evaluation_Object> relevantEvals) {
		System.out.println("Entered Frequency sampling");
		this.matrixinput = matrix;
		this.relevantFeedback=relevantEvals;
		create_weights();
		calculateSample();
		calculateOutputPatterns();
	}

	/**
	 * Frequency based
	 * 
	 * Creates a vector with the weight of each tuple
	 * the weight is given by w = 2^(nb of items) - 1
	 */
	public void create_weights(){
		calculateNTuples();
		this.weights = new int[n_tuples];
		for (int i=0; i<n_tuples;i++){
			int w =matrixinput.get(i).size();
			int we =(int) (Math.pow(2, w)-1);
				
			weights[i] =we;
			powerSetSum = powerSetSum + we;
		}
		System.out.println("Weights matrix created " + weights.length + " powerset sum: " +powerSetSum);
	}
 
	
	/**
	 * Counts the number of tuples (lines) in the dataset
	 */
	public void calculateNTuples(){
		this.n_tuples=matrixinput.size();
	}
	
	
	/**
	 * Gets a sample approximately of the size defined in sampleSize variable
	 * Each tuple gets a probability of being chosen proportional to the size of its powerSet (given by the weight)
	 */
	public void calculateSample(){
		Random r = new Random();
		int desiredSampleSize =2;
		int dist =0;
		int samplesize=0;
		this.sample = new LinkedList<Integer>();
		for(int i =0; i< n_tuples;i++){
			dist= r.nextInt(powerSetSum);
			if (dist<weights[i]*desiredSampleSize){
				sample.add(i);
				samplesize++;
				System.out.println("Samples has itemset number: " + i);
			}
		}
		System.out.println("Sample size: " + samplesize);
		System.out.println();
		
		
	}

	/**
	 * For each itemset on the sample, calls a method that generates the output subset
	 */
	private void calculateOutputPatterns() {
		interestingPatterns = new LinkedList<LinkedList>();
		for (int i=0; i<sample.size();i++){
			interestingPatterns.add(calculateSubset(sample.get(i)));
		}	
		System.out.println();
		System.out.println("Interesting Patterns found:");
		for (int i =0; i<interestingPatterns.size();i++){
			for(int j =0; j<interestingPatterns.get(i).size();j++){
				System.out.print(interestingPatterns.get(i).get(j)+ " ");
			}
			System.out.println();
		}
	}
	
	/**
	 *Defined for each of the sampling algorithms (frequency based and area based)
	 * @param sample_nb_itemset - the location of the tuple in the input matrix (original data -all itemsets)
	 * @return the subset calculated for the given itemset
	 */
	public LinkedList<Integer> calculateSubset(Integer sample_nb_itemset) {
		return null;
	}
	
	public LinkedList<LinkedList> getSample(){
		return interestingPatterns;
	}
	

}
