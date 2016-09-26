package fr.ufrt.bi.sampling;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import fr.ufrt.bi.evaluators.Evaluation;

public class FrequencySampling extends Sampling {

	public FrequencySampling(HashMap<Integer, Integer> transactionIndexMap, ArrayList<ArrayList<Integer>> dataset,
			ArrayList<Evaluation> relevantEvals) {
		super(transactionIndexMap, dataset, relevantEvals);
	}

	/**
	 * Frequency based
	 * 
	 * Creates a vector with the weight of each tuple the weight is given by w =
	 * 2^(nb of items)-1
	 */
	public void createWeights() {
		calculateNumberOfTuples();

		this.weights = new double[searchResultsSize];

		this.positives = new int[searchResultsSize];
		this.negatives = new int[searchResultsSize];
		this.calcWeight = new double[searchResultsSize];
		this.denominators = new double[searchResultsSize];
		this.numerators = new double[searchResultsSize];
		this.feedbacks = new int[searchResultsSize];
		this.cumltive = new double[searchResultsSize];
		this.positiveFeedbacks=new int[searchResultsSize];

		for (int i = 0; i < searchResultsSize; i++) {
			int itemsetSize = transactions.get(i).size();
			double weight = (double) ((Math.pow(2, itemsetSize)));

			weights[i] = weight;
			// powerSetSum = powerSetSum.add(weight);
			positives[i] = 0;
			negatives[i] = 0;
			denominators[i] = 0.0;
			numerators[i] = 0.0;
			feedbacks[i] = 0;
			positiveFeedbacks[i]=0;

			ErrorCorrectWeight(i);
			// System.out.println(weights[i]+ "----"+ calcWeight[i]);
			cumltive[i] = (double) weights[i] * calcWeight[i];
			if (i == 0) {
				max = cumltive[i];
				sumCalcWeight = calcWeight[i];
			} else {
				max = max + cumltive[i];
				sumCalcWeight = sumCalcWeight + calcWeight[i];
			}
		}

		// System.out.println("PS Sum"+powerSetSum);
		// BigDecimal sum=new BigDecimal("0.0000");
		// for (int i = 0; i < searchResultsSize; i++) {
		// //System.out.println(weights[i]);
		// weights[i] = weights[i].divide(powerSetSum, MathContext.DECIMAL128);
		// if(i==0){
		// sum= weights[i];
		// //System.out.println(weights[i]);
		// }
		// else
		// sum=sum.add(weights[i]);
		// }
		//
		// powerSetSum=sum;

		// System.out.println("Weights matrix created " + weights.length + "
		// powerset sum: " + powerSetSum);
	}

	/**
	 * Given the number of the tuple (recorded in the sample matrix) it gets
	 * that tuple from the dataset Randomly calculates a binary matrix with each
	 * item of the tuple and a 50-50 chance of it being in the chosen subset
	 * This way, all the subsets of the powerset have the same probability of
	 * being chosen, regardless of its size
	 * 
	 * @param sampleIndex
	 *            - the location of the tuple in the input matrix (original data
	 *            -all itemsets)
	 * @return the subset calculated for the given itemset
	 */
	public ArrayList<Integer> calculateSubset(Integer sampleIndex) {
		ArrayList<Integer> itemset = transactions.get(sampleIndex);
		int[] outputListBinary = new int[(int) itemset.size()];

		Random r = new Random();

		// System.out.println("Sample itemset to generate a subset: ");
		
		for (int i = 0; i < itemset.size(); i++) {
			outputListBinary[i] = r.nextInt(2);
		
			// Makes sure the searched item is retrieved in the subset found
			// if (searchedItem.contains(itemset.get(i))) {
			// outputListBinary[i] = 1;
			// }
			// System.out.print(itemset.get(i) + " ");
		}

		// System.out.println();
		// System.out.println("Generated subset: ");

		ArrayList<Integer> pattern = new ArrayList<Integer>();
		for (int i = 0; i < outputListBinary.length; i++) {
			if (outputListBinary[i] == 1) {
				pattern.add(itemset.get(i));
				// System.out.print(itemset.get(i) + " ");
			}
		}
		// System.out.println();
		return pattern;
	}
}
