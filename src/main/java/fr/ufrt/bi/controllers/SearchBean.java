package fr.ufrt.bi.controllers;

import fr.ufrt.bi.sampling.HashMapRKeys;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import fr.ufrt.bi.config.Config;
import fr.ufrt.bi.evaluators.Evaluation;
import fr.ufrt.bi.sampling.AreaSampling;
import fr.ufrt.bi.sampling.FrequencySampling;
import fr.ufrt.bi.sampling.Sampling;
import fr.ufrt.bi.sampling.SamplingType;

@ManagedBean(name = "searchBean")
@ViewScoped
public class SearchBean {
	
	private String pattern;

	private LinkedList<LinkedList<Integer>> dataset;
	private HashMapRKeys invertedMatrix;
	private LinkedList<LinkedList<Integer>> searchResult;
	private LinkedList<LinkedList<Integer>> sample;
	
	private LinkedList<Evaluation> evaluations;
	
	private SamplingType samplingType;
	
	public SearchBean() {
		dataset = Config.loadDataset();
		searchResult = new LinkedList<LinkedList<Integer>>();
		sample = new LinkedList<LinkedList<Integer>>();
		
		samplingType = SamplingType.FREQUENCY;
		
		evaluations = new LinkedList<Evaluation>();
	}
	
	public void search() {
		Integer searchedPattern = Integer.parseInt(pattern);
		
		ArrayList<Integer> transactions = invertedMatrix.getItemTransactions(searchedPattern);
		System.out.println("Itemsets that contain " + pattern + ": "+ transactions);
		
		for(int i=0; i<transactions.size();i++){
			addtoSearchResults(transactions.get(i));
		}
		
		Sampling sampling = null;
		
		if(samplingType == SamplingType.FREQUENCY){
			sampling = new FrequencySampling(searchResult, evaluations, searchedPattern);
	   	} else if(samplingType == SamplingType.AREA){
			sampling = new AreaSampling(searchResult, evaluations, searchedPattern);
		}
		
		this.setSample(sampling.getSample());
	}
	
	public void userEvaluation(int itemsetIndex, boolean feedback) {
		Integer searchedPattern = Integer.parseInt(pattern);
		
		System.out.println("Itemset "+itemsetIndex + "   feedback "+feedback);
		
		Evaluation evaluation = new Evaluation(searchedPattern, sample.get(itemsetIndex), feedback);
		evaluations.add(evaluation);
	}
	
	private void addtoSearchResults(int i) {
		LinkedList<Integer> tuple = new LinkedList<>();
		
		for(int j=0; j<dataset.get(i).size(); j++){
			tuple.add(dataset.get(i).get(j));
		}
		searchResult.add(tuple);
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

	public LinkedList<LinkedList<Integer>> getSample() {
		return sample;
	}

	public void setSample(LinkedList<LinkedList<Integer>> sample) {
		this.sample = sample;
	}
	
}
