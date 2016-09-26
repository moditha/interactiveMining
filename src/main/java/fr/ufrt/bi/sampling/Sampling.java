package fr.ufrt.bi.sampling;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import fr.ufrt.bi.evaluators.Evaluation;

public abstract class Sampling {
	protected ArrayList<ArrayList<Integer>> transactions;

	// map the transaction index to its position (index) on the
	// weight/positive/negative vectors
	protected HashMap<Integer, Integer> transactionIndexMap;

	protected ArrayList<ArrayList<Integer>> interestingPatterns = new ArrayList<ArrayList<Integer>>();

	protected int searchResultsSize = 0;
	protected int denominator = 0;
	public double[] weights;
	public double[] denominators;
	public double[] numerators;
	public double[] cumltive;
	public double[] calcWeight;
	public int[] feedbacks;
	public int[] positiveFeedbacks;
	public double max;
	public double sumCalcWeight;
	public double confidence = 0.01;
	protected BigDecimal powerSetSum = new BigDecimal("0");
	protected ArrayList<Integer> samples;
	protected ArrayList<Evaluation> relevantFeedback;
	public ArrayList<PatternEvaluation> patternEvaluations = new ArrayList<>();

	protected int[] positives;
	protected int[] negatives;

	// private final double euler = 0.5772156649;
	private final double euler = 1.6;

	/**
	 * Create the weights for each tuple - createWeights() According to the
	 * probability given by the weights creates a vector with the indexes of the
	 * tuples to be sampled - getSample() Retrieves the tuples for sampling,
	 * creates a set for each and the respective powerset for each
	 * 
	 * @param transactions
	 * @param searchedItem
	 */
	public Sampling(HashMap<Integer, Integer> transactionIndexMap, ArrayList<ArrayList<Integer>> transactions,
			ArrayList<Evaluation> relevantEvals) {
		this.transactions = transactions;

		this.relevantFeedback = relevantEvals;

		this.transactionIndexMap = transactionIndexMap;

		createWeights();
	}

	/**
	 * Creates a vector with the weight of each tuple
	 */
	public abstract void createWeights();

	/**
	 * Given the relevant feedbacks it recalculates the weights Furthermore the
	 * denominator needs to be updated for the probabilities to be normalized
	 * This meaning, the denominator will no longer be the sum of the powersets
	 * but the sum of the updated weights
	 */
	public void updateWeights() {
		for (int i = 0; i < denominators.length; i++) {
			ErrorCorrectWeight(i);
		}
		// for (int i = 0; i < denominators.length; i++) {
		// System.out.println(calcWeight[i] + ",");
		// }
		// System.out.println();
	}

	public void updatePositives(ArrayList<Integer> transactions) {
		for (int index : transactions) {
			// numerators[index] = numerators[index] + ((double) 1 / (double)
			// transactions.size());
			// denominators[index] = denominators[index] + ((double) 1 /
			// (double) transactions.size());
			numerators[index] = numerators[index] + ((double) 1 / getWeightedSupport(transactions));
			denominators[index] = denominators[index] + ((double) 1 / getWeightedSupport(transactions));
			feedbacks[index]++;
			positiveFeedbacks[index]++;

		}
		PatternEvaluation eval = new PatternEvaluation();
		eval.setIsPositive(1);
		eval.setSupport(transactions.size());
		patternEvaluations.add(eval);
	}

	public void updateNegatives(ArrayList<Integer> transactions) {
		for (int index : transactions) {
			// denominators[index] = denominators[index] + ((double) 1 /
			// (double) transactions.size());
			denominators[index] = denominators[index] + ((double) 1 / getWeightedSupport(transactions));
			feedbacks[index]++;

		}
		PatternEvaluation eval = new PatternEvaluation();
		eval.setIsPositive(0);
		eval.setSupport(transactions.size());
		patternEvaluations.add(eval);
	}

	public double getWeightedSupport(ArrayList<Integer> transactions) {
		double total = 0.0;

		for (int index : transactions) {
			total = total + calcWeight[index];
		}

		return total / sumCalcWeight;
	}

	public void calculateNumberOfTuples() {
		this.searchResultsSize = transactions.size();
	}

	/**
	 * Gets a sample approximately of the size defined in sampleSize variable
	 * Each tuple gets a probability of being chosen proportional to the size of
	 * its powerSet (given by the weight)
	 */
	public void calculateSample() {
		boolean sampled=false;
		
		this.samples = new ArrayList<Integer>();
		double random = ThreadLocalRandom.current().nextDouble(0, max);
		while(!sampled){
		for (int i = 0; i < searchResultsSize; i++) {
			// System.out.println("BEFORE");
			if (random < cumltive[i]) {
				//System.out.println("added to sample");
				samples.add(i);
				sampled=true;
				// System.out.println("Samples has itemset number: " + i);
				// System.out.println("Iteration value: " + iterationvalue + "
				// Random: " + dist );
				i = searchResultsSize;
			} else {
				random = random - cumltive[i];
			}
		}}
		// System.out.println();
	}

	private BigDecimal randomBigDecimal(BigDecimal n) {
		// Random rand = new Random();
		// BigInteger result = new BigInteger(n.bitLength(), rand);
		// while (result.compareTo(n) >= 0) {
		// result = new BigInteger(n.bitLength(), rand);
		// }
		// return result;

		BigDecimal max = n;
		BigDecimal min = new BigDecimal("0");
		BigDecimal range = max.subtract(min);
		BigDecimal result = min.add(range.multiply(new BigDecimal(Math.random())));
		// System.out.println(result);
		return result;
	}

	/**
	 * For each itemset on the sample, calls a method that generates the output
	 * subset
	 */
	public void calculateOutputPatterns() {
		for (int i = 0; i < samples.size(); i++) {
			ArrayList<Integer> a= calculateSubset(samples.get(i));
			//System.out.println("aa->" +a.size());
			while (a.size()==0) {
				a= calculateSubset(samples.get(i));
			}
			
			interestingPatterns.add(a);
		}
		//System.out.println("xx ->"+interestingPatterns.size());
	}

	public void clearPatterns() {
		interestingPatterns.clear();
	}

	/**
	 * Defined for each of the sampling algorithms (frequency based and area
	 * based)
	 * 
	 * @param sampleIndex
	 *            - the location of the tuple in the input matrix (original data
	 *            -all itemsets)
	 * @return the subset calculated for the given itemset
	 */
	public abstract ArrayList<Integer> calculateSubset(Integer sampleIndex);

	public ArrayList<ArrayList<Integer>> getPatterns() {
		return interestingPatterns;
	}

	public void ErrorCorrectWeight(int trx) {

		// If the transaction doesn't have any feedbacks related weight = 0.5
		if (feedbacks[trx] == 0) {
			calcWeight[trx] = 0.5;

		}

		else {

			// w = nume
			double w = (double) (numerators[trx] / denominators[trx]);
			double stdev = Math.sqrt(w - Math.pow(w, 2));
			double eps = Math.sqrt((2 * stdev * Math.log(1 / confidence)) / (double) feedbacks[trx])
					+ Math.log(1 / confidence) / (3 * (double) feedbacks[trx]);

			double corrWeight = ((Math.max(0, w - eps)) + (Math.min(1, w + eps))) / 2;

			calcWeight[trx] = corrWeight;

			// System.out.println("Corr weignt is " + corrWeight);
			cumltive[trx] = (double) weights[trx] * calcWeight[trx];
			if (trx == 0) {
				max = cumltive[trx];
				sumCalcWeight = corrWeight;
			} else {
				max = max + cumltive[trx];
				sumCalcWeight = sumCalcWeight + corrWeight;
			}
		}
	}

}
