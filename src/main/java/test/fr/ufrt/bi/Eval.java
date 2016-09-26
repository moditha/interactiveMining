package test.fr.ufrt.bi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.DoubleStream;

import org.primefaces.event.SlideEndEvent;

import fr.ufrt.bi.config.Config;
import fr.ufrt.bi.controllers.SearchBean;
import fr.ufrt.bi.evaluators.Evaluation;
import fr.ufrt.bi.sampling.HashMapRKeys;
import fr.ufrt.bi.sampling.PatternEvaluation;
import fr.ufrt.bi.sampling.Sampling;
import fr.ufrt.bi.sampling.SamplingType;

public class Eval {

	private SearchBean sb;
	private int alpha = 1;
	private HashMap<Integer, Double> map = new HashMap<>();
	private ArrayList<double[]> map2 = new ArrayList<>();
	private ArrayList<Integer> D0 = new ArrayList<>();
	private ArrayList<Integer> D1 = new ArrayList<>();
	ArrayList<Integer> a = new ArrayList<>();
	ArrayList<Double> error = new ArrayList<>();
	ArrayList<IterationResult> result = new ArrayList<>();
	ArrayList<ArrayList<IterationResult>> results = new ArrayList<>();

	ArrayList<double[]> weights = new ArrayList<>();
	ArrayList<ArrayList<Double>> errors = new ArrayList<>();
	double[] optimalWeights;
	int transaction = 0;
	private String typ;
	private String sim;
	private HashMapRKeys hashMapRKeys = new HashMapRKeys();
	private HashMapRKeys KeysD0 = new HashMapRKeys();
	private HashMapRKeys KeysD1 = new HashMapRKeys();

	public Eval(SamplingType type) {
		sb = new SearchBean();
		sb.setSamplingType(type);
		typ = type.name();
		// create();
	}

	public void reset(SamplingType type) {
		sb = new SearchBean();
		sb.setSamplingType(type);
	}

	private void initialize(boolean fb) {
		BufferedReader br = null;

		if (fb)
			hashMapRKeys = new HashMapRKeys();
		String fileName = "D:\\notes\\BIS\\test.txt";

		try {
			br = new BufferedReader(new FileReader(fileName));

			String line = "";
			String datSplitBy = " ";

			transaction = 0;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(datSplitBy);
				LinkedList<Integer> lines = new LinkedList<Integer>();

				for (String value : values) {
					Integer integ = Integer.parseInt(value);
					hashMapRKeys.addValue(integ, transaction);
					if (fb) {
						if (D1.contains(transaction)) {
							KeysD1.addValue(integ, transaction);
							optimalWeights[transaction] = 1.0;
							// System.out.println("Adding "+ integ+ " to "+
							// transaction +" D1");
						} else {
							KeysD0.addValue(integ, transaction);
							optimalWeights[transaction] = 0.0;
							// System.out.println("Adding "+ integ+ " to "+
							// transaction +" D0");
						}

					}
				}
				transaction++;
			}

			System.out.println("Exit initialize");
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDs(ArrayList<Integer> pattern) {

		int[] array = new int[pattern.size()];
		int x = 0;
		for (int i : pattern) {
			array[x] = i;
			x++;
		}

		D1 = hashMapRKeys.getTransactionsItems(array);
		D0 = hashMapRKeys.getTransactionsItems(null);
		D0.removeAll(D1);
		// System.out.println("Size of D1 is " + D1.size());
		// System.out.println("Size of D0 is " + D0.size());
		// System.out.println(D1);
		// System.out.println(D0);
		initialize(true);
	}

	private boolean voteYes(ArrayList<Integer> ptrn) {
		boolean bool = true;
		int[] array = new int[ptrn.size()];
		int x = 0;
		for (int i : ptrn) {
			array[x] = i;
			// System.out.print(i+",");
			x++;
		}

		// if (KeysD0 == null) {
		// System.out.println("KD0 is null");
		// }
		// if (KeysD1 == null) {
		// System.out.println("KD1 is null");
		// }
		// if (array == null) {
		// System.out.println("array is null");
		// }
		// if (array != null) {
		// System.out.println("array is ot");
		// }
		// if (KeysD0.getTransactionsItems(array) == null) {
		// System.out.println("D0 is null");
		// }
		// if (KeysD1.getTransactionsItems(array) == null) {
		// System.out.println("D1 is null");
		// }

		// System.out.println(KeysD0.getTransactionsItems(array));
		// System.out.println(KeysD1.getTransactionsItems(array));
		// System.out.println("----D0---");

		int countD0 = KeysD0.getTransactionsItems(array) != null ? KeysD0.getTransactionsItems(array).size() : 0;
		// System.out.println("----D1---");

		int countD1 = KeysD1.getTransactionsItems(array) != null ? KeysD1.getTransactionsItems(array).size() : 0;
		// System.out.println("Count in D1 is" + countD1 + " D0 is" + countD0);
		// System.out.println("Evaluation is" + (countD1 > countD0));

		// return countD1 > countD0;
		return (double) countD1 / (double) D1.size() > (double) alpha * ((double) countD0 / (double) D0.size());
	}

	private void initOptimal() {
		optimalWeights = new double[transaction + 1];
	}

	public void evaluate(ArrayList<Integer> endPattern, int index) {
		initialize(false);
		initOptimal();
		createDs(endPattern);

		sb.search();
		ArrayList<ArrayList<Integer>> l = sb.getPatterns();

		for (int i = 1; i < 600; i++) {
			int y = 0;
			for (ArrayList<Integer> linkedList : l) {
				// System.out.println("Pattern to vote is" + linkedList);
				sb.userEvaluation(y, voteYes(linkedList));
				y++;
			}
			sb.calc();
			// if (i == 1 || i % 10 == 0)
			addIterationResult();
		}
		errors.add((ArrayList<Double>) error.clone());
		error.clear();
		// writeFile3(index);
		// writeD0(index);
		// writeD1(index);
		// writeStats(index);
		writeIterationResult(index);
		writeFinalWeights(index);
		writePatternSupport(index);
	}

	private void nextItResult() {

	}

	private double calcError() {
		double err = 0.0;
		double[] curWeights = sb.getWeights();
		for (int i = 0; i < optimalWeights.length - 1; i++) {
			err = err + Math.pow((curWeights[i] - optimalWeights[i]), 2);
		}
		return Math.sqrt(err);
	}

	private void writeFile() {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\test" + sim + "_" + typ + ".csv")) {
			for (Map.Entry<Integer, Double> entry : map.entrySet()) {
				writer.append(entry.getKey().toString()).append(',').append(entry.getValue().toString()).append(eol);
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	private void writeFile3(int index) {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\test_" + index + ".csv")) {
			for (int i = 0; i < D0.size() + D1.size(); i++) {
				writer.append(String.valueOf(i)).append(',').append(String.valueOf(sb.getWeights()[i])).append(eol);
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	private void writeFile2() {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\weights" + sim + "_" + typ + ".csv")) {
			for (int i = 0; i < map2.size(); i++) {
				for (int j = 0; j < map2.get(i).length; j++) {

					// writer.append((map2.get(i)[j]).toString()).append(",");

				}
				writer.append(eol);
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void writeD0(int index) {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\test_D0_" + index + ".csv")) {
			for (int i = 0; i < D0.size(); i++) {
				writer.append(D0.get(i).toString()).append(eol);
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void writeD1(int index) {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\test_D1_" + index + ".csv")) {
			for (int i = 0; i < D1.size(); i++) {
				writer.append(D1.get(i).toString()).append(eol);
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void writeWeights() {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\weights.csv")) {
			for (int i = 0; i < weights.size(); i++) {
				for (int j = 0; j < weights.get(i).length; j++) {
					writer.append(String.valueOf(weights.get(i)[j])).append(",");
				}
				writer.append(eol);
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void writeStats(int index) {
		String eol = System.getProperty("line.separator");

		ArrayList<Integer> actualD1 = new ArrayList<>();

		for (int i = 0; i < sb.getWeights().length; i++) {
			if (sb.getWeights()[i] >= 0.5)
				actualD1.add(i);
		}
		int originalD1size = D1.size();
		Set<Integer> copyD1 = new HashSet((ArrayList<Integer>) D1.clone());
		D1.retainAll(actualD1);
		copyD1.addAll(actualD1);
		int union = copyD1.size();

		int intersection = D1.size();
		try (Writer writer = new FileWriter("D:\\notes\\BIS\\test_Stats" + index + ".txt")) {
			writer.append("original size is " + originalD1size).append(eol);
			writer.append("intersection is " + intersection).append(eol);
			writer.append("union is " + union).append(eol);
			;
			writer.append("Our D1 is is " + actualD1.size());
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public double getSimilarity(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		Set<Integer> intersection = new HashSet<>(list1);
		Set<Integer> union = new HashSet<>(list1);
		Set<Integer> s2 = new HashSet<>(list2);
		union.addAll(s2);
		intersection.retainAll(list2);
		return (double) intersection.size() / (double) union.size();
	}

	private void addIterationResult() {
		ArrayList<Integer> actualD1 = new ArrayList<>();
		ArrayList<Integer> actualD0 = new ArrayList<>();

		// Commented code to save memory
		// weights.add(sb.getWeights().clone());
		error.add(calcError());

		for (int i = 0; i < sb.getWeights().length; i++) {
			if (sb.getWeights()[i] > 0.5) {
				actualD1.add(i);
			} else {
				actualD0.add(i);
			}
		}

		int originalD1size = D1.size();
		int originalD0size = D0.size();
		Set<Integer> copyD1 = new HashSet((ArrayList<Integer>) D1.clone());
		ArrayList<Integer> copy2D1 = (ArrayList<Integer>) D1.clone();
		copy2D1.retainAll(actualD1);
		copyD1.addAll(actualD1);
		int union = copyD1.size();
		int intersection = copy2D1.size();

		Set<Integer> copyD0 = new HashSet((ArrayList<Integer>) D0.clone());
		ArrayList<Integer> copy2D0 = (ArrayList<Integer>) D0.clone();
		copy2D0.retainAll(actualD0);
		copyD0.addAll(actualD0);
		int unionD0 = copyD0.size();
		int intersectionD0 = copy2D0.size();

		IterationResult r = new IterationResult();
		r.D1Size = D1.size();
		r.D0Size = D0.size();
		r.CurrentD1Size = actualD1.size();
		r.D1Intersection = intersection;
		r.D1Union = union;
		r.CurrentD0Size = actualD0.size();
		r.D0Intersection = intersectionD0;
		r.D0Union = unionD0;

		result.add(r);

	}

	private void writeIterationResult(int index) {
		String eol = System.getProperty("line.separator");
		results.add((ArrayList<IterationResult>) result.clone());
		try (Writer writer = new FileWriter("D:\\notes\\BIS\\iteation_pattern_" + index + ".csv")) {
			writer.append("D1,CurrentD1,IntersectionD1,UnionD1,D0,CurrentD0,IntersectionD0,UnionD0").append(eol);
			for (IterationResult entry : result) {
				writer.append(String.valueOf(entry.D1Size)).append(',').append(String.valueOf(entry.CurrentD1Size))
						.append(',').append(String.valueOf(entry.D1Intersection)).append(',')
						.append(String.valueOf(entry.D1Union)).append(',').append(String.valueOf(entry.D0Size))
						.append(',').append(String.valueOf(entry.CurrentD0Size)).append(',')
						.append(String.valueOf(entry.D0Intersection)).append(',').append(String.valueOf(entry.D0Union))
						.append(',').append(eol);
			}
			result.clear();
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void calculateAverages() {
		ArrayList<IterationResult> totals = new ArrayList();
		ArrayList<Double> stdDivPrecision = new ArrayList();
		ArrayList<Double> stdDivRecall = new ArrayList();
		ArrayList<Double> stdDivSpec = new ArrayList();

		for (int i = 0; i < results.get(0).size(); i++) {
			IterationResult r = new IterationResult();
			for (int j = 0; j < results.size(); j++) {
				r.CurrentD1Size = r.CurrentD1Size + results.get(j).get(i).CurrentD1Size;
				r.D1Intersection = r.D1Intersection + results.get(j).get(i).D1Intersection;
				r.D1Size = r.D1Size + results.get(j).get(i).D1Size;
				r.D1Union = r.D1Union + results.get(j).get(i).D1Union;
				r.CurrentD0Size = r.CurrentD0Size + results.get(j).get(i).CurrentD0Size;
				r.D0Intersection = r.D0Intersection + results.get(j).get(i).D0Intersection;
				r.D0Size = r.D0Size + results.get(j).get(i).D0Size;
				r.D0Union = r.D0Union + results.get(j).get(i).D0Union;

			}

			double averagePrecision = ((double) r.D1Intersection / (double) r.CurrentD1Size)
					/ (double) results.get(0).size();
			double averageRecall = ((double) r.D1Intersection / (double) r.D1Size) / (double) results.get(0).size();
			double averageSpec = ((double) r.D0Intersection / (double) r.D0Size)/ (double) results.get(0).size();

			double tmpPrecision = 0.0;
			double tmpRecall = 0.0;
			double tmpSpec = 0.0;
			for (int j = 0; j < results.size(); j++) {
				tmpPrecision = tmpPrecision + Math.pow(
						((double) results.get(j).get(i).D1Intersection / (double) results.get(j).get(i).CurrentD1Size)
								- averagePrecision,
						2);
				tmpRecall = tmpRecall + Math
						.pow(((double) results.get(j).get(i).D1Intersection / (double) results.get(j).get(i).D1Size)
								- averageRecall, 2);
				tmpSpec = tmpSpec + Math
						.pow(((double) results.get(j).get(i).D0Intersection / (double)results.get(j).get(i).D0Size)
								- averageSpec, 2);
			}

			stdDivPrecision.add(Math.sqrt(tmpPrecision / (double) results.get(0).size()));
			stdDivRecall.add(Math.sqrt(tmpRecall / (double) results.get(0).size()));
			stdDivSpec.add(Math.sqrt(tmpSpec / (double) results.get(0).size()));

			// r.CurrentD1Size = r.CurrentD1Size / results.size();
			// r.D1Intersection = r.D1Intersection / results.size();
			// r.D1Size = r.D1Size / results.size();
			// r.D1Union = r.D1Union / results.size();
			totals.add(r);
		}
		String eol = System.getProperty("line.separator");

		int i = 0;
		try (Writer writer = new FileWriter("D:\\notes\\BIS\\averages.csv")) {
			writer.append("recall,presicion,stdDivRecall,stdDivPrecision,specificity,stdDivSpec").append(eol);
			for (IterationResult entry : totals) {
				writer.append(String.valueOf(((double) entry.D1Intersection / (double) results.size())
						/ ((double) entry.D1Size / (double) results.size())))
						.append(',')
						.append(String.valueOf(((double) entry.D1Intersection / (double) results.size())
								/ ((double) entry.CurrentD1Size / (double) results.size())))
						.append(',').append(String.valueOf(stdDivRecall.get(i))).append(',')
						.append(String.valueOf(stdDivPrecision.get(i))).append(",")
						.append(String.valueOf(((double) entry.D0Intersection / (double) results.size())
								/ ((double) entry.D0Size / (double) results.size())))
						.append(",").append(String.valueOf(stdDivSpec.get(i))).append(eol);
				i++;
			}
			result.clear();
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void writeErrors() {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\error.csv")) {

			for (int i = 0; i < errors.size(); i++) {

				for (int j = 0; j < errors.get(i).size(); j++) {
					writer.append(String.valueOf(errors.get(i).get(j))).append(",");
				}
				writer.append(eol);
			}

		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	private void writeFinalWeights(int index) {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\FinalWeights_" + index + ".csv")) {

			writer.append("transaction,expected,actual,feedbacks,positives").append(eol);
			int i = 0;
			for (double wt : sb.getWeights()) {
				writer.append(String.valueOf(i)).append(",").append(String.valueOf(optimalWeights[i])).append(",")
						.append(String.valueOf(wt)).append(",").append(String.valueOf(sb.sampling.feedbacks[i])).append(",").append(String.valueOf(sb.sampling.positiveFeedbacks[i])).append(eol);
				i++;
			}

		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}
	
	private void writePatternSupport(int index) {
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("D:\\notes\\BIS\\pattern_support" + index + ".csv")) {

			writer.append("support,feedback").append(eol);
			int i = 0;
			for (PatternEvaluation e : sb.sampling.patternEvaluations) {
				writer.append(String.valueOf(e.getSupport())).append(",")
						.append(String.valueOf(e.getIsPositive())).append(eol);
				i++;
			}

		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}
}
