package test.fr.ufrt.bi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.primefaces.event.SlideEndEvent;

import fr.ufrt.bi.config.Config;
import fr.ufrt.bi.controllers.SearchBean;
import fr.ufrt.bi.evaluators.Evaluation;
import fr.ufrt.bi.sampling.HashMapRKeys;
import fr.ufrt.bi.sampling.Sampling;
import fr.ufrt.bi.sampling.SamplingType;

public class Eval {

	private SearchBean sb;
	public Eval(SamplingType type) {
		sb = new SearchBean();
		sb.setSamplingType(type);
	}

	public void evaluate(ArrayList<Integer> startingPattern, ArrayList<Integer> endPattern, double smilarity) {
			sb.setPattern(String.valueOf(startingPattern.get(0)));
			sb.search();
			LinkedList<LinkedList<Integer>> l=sb.getPatterns();
			
			for (int i = 0; i < 50; i++) {
				double sim=getSimilarity(new ArrayList<Integer>(l.get(0)), endPattern);
				System.out.println(sim);
				if(sim > smilarity){
					sb.userEvaluation(0, true);
				}
				else
					sb.userEvaluation(0, false);
				
				l=sb.getPatterns();
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


}
