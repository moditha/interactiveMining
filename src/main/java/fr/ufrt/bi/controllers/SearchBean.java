package fr.ufrt.bi.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import fr.ufrt.bi.config.Config;
import fr.ufrt.bi.evaluators.Evaluation;
import fr.ufrt.bi.sampling.FrequencySampling;
import fr.ufrt.bi.sampling.HashMapRKeys;
import fr.ufrt.bi.sampling.Sampling;
import fr.ufrt.bi.sampling.SamplingType;

@ManagedBean(name = "searchBean")
@ViewScoped
public class SearchBean {
	
	private String pattern;

	private ArrayList<ArrayList<Integer>> dataset;
	private HashMapRKeys invertedMatrix;
	private ArrayList<ArrayList<Integer>> searchResults;
	private ArrayList<ArrayList<Integer>> patterns;
	
	private HashMap<Integer, Integer> transactionIndexMap;
	
	private ArrayList<Evaluation> evaluations;
	private ArrayList<Integer> transactions = new ArrayList<>();
	public Sampling sampling;
	private SamplingType samplingType;
	
	public SearchBean() {
		dataset = Config.loadDataset();
		
		createInvertedMatrix();
		
		searchResults = new ArrayList<ArrayList<Integer>>();
		patterns = new ArrayList<ArrayList<Integer>>();
		
		samplingType = SamplingType.FREQUENCY;
		
		transactionIndexMap = new HashMap<Integer, Integer>();
		
		evaluations = new ArrayList<Evaluation>();
	}

	private void createInvertedMatrix() {
		this.invertedMatrix = new HashMapRKeys();
		
		this.invertedMatrix = new HashMapRKeys();
		BufferedReader br = null;
	
	String fileName = "D:\\notes\\BIS\\test.txt";

    try {
    	br = new BufferedReader(new FileReader(fileName));
		
		String line = "";
		String datSplitBy = " ";
		
		int transaction = 0;
		System.out.println("...Creating the inverted index...");
		while ((line = br.readLine()) != null ) {
			String[] values = line.split(datSplitBy);
			LinkedList<Integer> lines = new LinkedList<Integer>();
			
			for (String value : values) {
				Integer integ = Integer.parseInt(value);
				invertedMatrix.addValue(integ, transaction);
			}
			transactions.add(transaction);
			transaction++;

			//System.out.println(transaction);
		}
		
		br.close();
		//int[] data = {39};
		//System.out.println(hashMapRKeys.getItemTransactions(36));
		//System.out.println(hashMapRKeys.getItemTransactions(38));
		//System.out.println(hashMapRKeys.getItemTransactions(39));
		//System.out.println("INVERTED MATRIXXXXXXXXXXXXXXXXX:"+invertedMatrix.getTransactionsItems(data));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (NumberFormatException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		/**
		for (int i=0; i<dataset.size(); i++) {
			for (int j=0; j<dataset.get(i).size(); j++) {
				invertedMatrix.addValue(dataset.get(i).get(j), i);
			}
		}
		**/
	}
	
	
	
	
	public void search() {
		
		
		//ArrayList<Integer> transactions = invertedMatrix.getTransactionsItems(null);
		//System.out.println("Itemsets that contain : "+ transactions);
		
		if (transactions != null) {
			for(int i=0; i<transactions.size();i++){
				addtoSearchResults(transactions.get(i));
				transactionIndexMap.put(transactions.get(i), i);
			}
			
			if(samplingType == SamplingType.FREQUENCY) {
				this.sampling = new FrequencySampling(transactionIndexMap, searchResults, evaluations);
		   	} else if(samplingType == SamplingType.AREA){
			//	this.sampling = new AreaSampling(transactionIndexMap, searchResults, evaluations, searchedPattern);
			}
			
			getPatternsFromSample();
		}
	}

	private void getPatternsFromSample() {
		this.sampling.clearPatterns();
		for (int i = 0; i < 5; i++) {
			this.sampling.calculateSample();
		this.sampling.calculateOutputPatterns();
		}
		
		
		this.setPatterns(sampling.getPatterns());
		if (this.getPatterns().size() == 0) {
			this.getPatternsFromSample();
		}
	}
	
	public void userEvaluation(int patternIndex, boolean feedback) {
		
		//Integer searchedPattern = value;
		
		//System.out.println("Pattern "+patternIndex + "   feedback "+feedback);
		
		//como poderíamos usar a lista de evaluations, alem de permitir multiplas buscas simultaneas?
		//Evaluation evaluation = new Evaluation(searchedPattern, patterns.get(patternIndex), feedback);
		//evaluations.add(evaluation);
		
		int[] items = new int[patterns.get(patternIndex).size()];
		for (int i=0; i<patterns.get(patternIndex).size(); i++) {
			items[i] = patterns.get(patternIndex).get(i);
			//System.out.println("GGGGG->>"+patterns.get(patternIndex).get(i));
		}
		
		ArrayList<Integer> transactionsItems = invertedMatrix.getTransactionsItems(items);
		//System.out.println("Trans for " + patterns.get(patternIndex)+" -- >" + transactionsItems);
		if (feedback) {
			this.sampling.updatePositives(transactionsItems);
		} else {
			this.sampling.updateNegatives(transactionsItems);
		}
		
		//IMPORTANT REMEMBER TO UNCOMMENT
		//this.sampling.updateWeights();
		
		//after feedback is given, remove from the list of patterns
//this.patterns.remove(patternIndex);
//		
//		getPatternsFromSample();
	}
	
	public void calc(){
this.patterns.clear();
		this.sampling.updateWeights();
		getPatternsFromSample();
	}
	
	private void addtoSearchResults(int i) {
		ArrayList<Integer> tuple = new ArrayList<>();
		tuple.addAll(dataset.get(i));
//		for(int j=0; j<dataset.get(i).size(); j++){
//			tuple.add(dataset.get(i).get(j));
//		}
		searchResults.add(tuple);
	}

	public SamplingType[] getSamplingTypes() {
		return SamplingType.values();
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public SamplingType getSamplingType() {
		return samplingType;
	}

	public void setSamplingType(SamplingType samplingType) {
		this.samplingType = samplingType;
	}

	public ArrayList<ArrayList<Integer>> getPatterns() {
		return patterns;
	}

	public void setPatterns(ArrayList<ArrayList<Integer>> pattern) {
		this.patterns = pattern;
	}
	
	public double[] getWeights() {
		return this.sampling.calcWeight;
	}
}
