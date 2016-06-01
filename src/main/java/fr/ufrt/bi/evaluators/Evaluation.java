package fr.ufrt.bi.evaluators;
import java.util.LinkedList;

public class Evaluation {
	
	private int search;
	private LinkedList<Integer> pattern;
	private boolean eval;
	
	public Evaluation(int search, LinkedList<Integer> pattern, boolean eval){
		this.search = search;
		this.pattern = pattern;
		this.eval = eval;
	}
	
	public int getSearch(){
		return search;
	}
	
	public LinkedList<Integer> getPattern(){
		return pattern;
	}
	
	public boolean getEval(){
		return eval;
	}

}
