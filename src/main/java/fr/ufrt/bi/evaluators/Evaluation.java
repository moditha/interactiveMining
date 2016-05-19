package fr.ufrt.bi.evaluators;
import java.util.LinkedList;

public class Evaluation {
	
	private int search;
	private LinkedList<Integer> sampleEvaluated;
	private boolean eval;
	
	public Evaluation(int search, LinkedList<Integer> sampleEvaluated, boolean eval){
		this.search = search;
		this.sampleEvaluated = sampleEvaluated;
		this.eval = eval;
	}
	
	public int getSearch(){
		return search;
	}
	
	public LinkedList<Integer> getSample(){
		return sampleEvaluated;
	}
	
	public boolean getEval(){
		return eval;
	}

}
