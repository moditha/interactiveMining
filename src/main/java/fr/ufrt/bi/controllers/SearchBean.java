package fr.ufrt.bi.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import fr.ufrt.bi.config.Config;
import fr.ufrt.bi.evaluators.Evaluation;
import fr.ufrt.bi.sampling.AreaSampling;
import fr.ufrt.bi.sampling.FrequencySampling;
import fr.ufrt.bi.sampling.HashMapRKeys;
import fr.ufrt.bi.sampling.Sampling;
import fr.ufrt.bi.sampling.SamplingType;

@ManagedBean(name = "searchBean")
@ViewScoped
public class SearchBean {
	
	private String pattern;

	private LinkedList<LinkedList<Integer>> dataset;
	private HashMapRKeys invertedMatrix;
	private LinkedList<LinkedList<Integer>> searchResults;
	private LinkedList<LinkedList<Integer>> patterns;
	
	private HashMap<Integer, Integer> transactionIndexMap;
	
	private LinkedList<Evaluation> evaluations;
	
	private Sampling sampling;
	private SamplingType samplingType;
	
	public SearchBean() {
		dataset = Config.loadDataset();
		
		createInvertedMatrix();
		
		searchResults = new LinkedList<LinkedList<Integer>>();
		patterns = new LinkedList<LinkedList<Integer>>();
		
		samplingType = SamplingType.FREQUENCY;
		
		transactionIndexMap = new HashMap<Integer, Integer>();
		
		evaluations = new LinkedList<Evaluation>();
	}

	private void createInvertedMatrix() {
		this.invertedMatrix = new HashMapRKeys();
		
		for (int i=0; i<dataset.size(); i++) {
			for (int j=0; j<dataset.get(i).size(); j++) {
				invertedMatrix.addValue(dataset.get(i).get(j), i);
			}
		}
	}
	
	public void search() {
		Integer searchedPattern = Integer.parseInt(pattern);
		
		ArrayList<Integer> transactions = invertedMatrix.getItemTransactions(searchedPattern);
		System.out.println("Itemsets that contain " + pattern + ": "+ transactions);
		
		if (transactions != null) {
			for(int i=0; i<transactions.size();i++){
				addtoSearchResults(transactions.get(i));
				transactionIndexMap.put(transactions.get(i), i);
			}
			
			if(samplingType == SamplingType.FREQUENCY) {
				this.sampling = new FrequencySampling(transactionIndexMap, searchResults, evaluations, searchedPattern);
		   	} else if(samplingType == SamplingType.AREA){
				this.sampling = new AreaSampling(transactionIndexMap, searchResults, evaluations, searchedPattern);
			}
			
			getPatternsFromSample();
		}
	}

	private void getPatternsFromSample() {
		this.sampling.calculateSample();
		this.sampling.calculateOutputPatterns();
		
		this.setPatterns(sampling.getPatterns());
	}
	
	public void userEvaluation(int patternIndex, boolean feedback) {
		Integer searchedPattern = Integer.parseInt(pattern);
		
		System.out.println("Pattern "+patternIndex + "   feedback "+feedback);
		
		//como poderíamos usar a lista de evaluations, alem de permitir multiplas buscas simultaneas?
		Evaluation evaluation = new Evaluation(searchedPattern, patterns.get(patternIndex), feedback);
		evaluations.add(evaluation);
		
		int[] items = new int[patterns.get(patternIndex).size()];
		for (int i=0; i<patterns.get(patternIndex).size(); i++) {
			items[i] = patterns.get(patternIndex).get(i);
		}
		
		ArrayList<Integer> transactionsItems = invertedMatrix.getTransactionsItems(items);
		
		if (feedback) {
			this.sampling.updatePositives(transactionsItems);
		} else {
			this.sampling.updateNegatives(transactionsItems);
		}
		
		this.sampling.updateWeights();
		
		//after feedback is given, remove from the list of patterns
		this.patterns.remove(patternIndex);
		
		getPatternsFromSample();
	}
	
	private void addtoSearchResults(int i) {
		LinkedList<Integer> tuple = new LinkedList<>();
		
		for(int j=0; j<dataset.get(i).size(); j++){
			tuple.add(dataset.get(i).get(j));
		}
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

	public LinkedList<LinkedList<Integer>> getPatterns() {
		return patterns;
	}

	public void setPatterns(LinkedList<LinkedList<Integer>> pattern) {
		this.patterns = pattern;
	}
	
}
