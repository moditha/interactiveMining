package fr.ufrt.bi.sampling;

import java.util.LinkedList;
import java.util.Random;

import fr.ufrt.bi.evaluators.Evaluation;

public class FrequencySampling extends Sampling {
	
	public FrequencySampling(LinkedList<LinkedList<Integer>> dataset, LinkedList<Evaluation> relevantEvals, int searchItem) {
		super(dataset, relevantEvals, searchItem);
	}
	
	/**
	 * Frequency based
	 * 
	 * Creates a vector with the weight of each tuple
	 * the weight is given by w = 2^(nb of items)
	 */
	public void create_weights(){
		calculateNTuples();
		
		this.weights = new int[datasetSize];
		
		for (int i=0; i<datasetSize;i++){
			int itemsetSize = dataset.get(i).size();
			int weight = (int) (Math.pow(2, itemsetSize));
				
			weights[i] = weight;
			powerSetSum = powerSetSum + weight;
		}
		System.out.println("Weights matrix created " + weights.length + " powerset sum: " +powerSetSum);
	}

	/**
	 * Given the number of the tuple (recorded in the sample matrix) it gets that tuple from the dataset
	 * Randomly calculates a binary matrix with each item of the tuple and a 50-50 chance of it being in the chosen subset
	 * This way, all the subsets of the powerset have the same probability of being chosen, regardless of its size
	 * @param sampleIndex - the location of the tuple in the input matrix (original data -all itemsets)
	 * @return the subset calculated for the given itemset
	 */
	public LinkedList<Integer> calculateSubset(Integer sampleIndex) {
		LinkedList<Integer> itemset = dataset.get(sampleIndex);
		int[] outputListBinary = new int[(int)itemset.size()];
		
		Random r = new Random();
		
		System.out.println("Sample itemset to generate a subset: ");
		
		for (int i=0;i<itemset.size();i++){
			outputListBinary[i]=r.nextInt(2);
			//Makes sure the searched item is retrieved in the subset found
			if(itemset.get(i) == searchedItem){
				outputListBinary[i]=1;
			}
			System.out.print(itemset.get(i) + " ");
		}
		
		System.out.println();
		System.out.println("Generated subset: ");
		
		LinkedList <Integer> pattern = new LinkedList<Integer>();
		for (int i=0;i<outputListBinary.length;i++){
			if(outputListBinary[i]==1){
				pattern.add(itemset.get(i));
				System.out.print(itemset.get(i) + " ");
			}
		}
		System.out.println();
		return pattern;
	}
}
