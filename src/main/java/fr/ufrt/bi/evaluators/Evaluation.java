package fr.ufrt.bi.evaluators;
import java.util.ArrayList;

public class Evaluation {
	
	private int search;
	private ArrayList<Integer> pattern;
	private boolean eval;
	
	public Evaluation(int search, ArrayList<Integer> pattern, boolean eval){
		this.search = search;
		this.pattern = pattern;
		this.eval = eval;
	}
	
	public int getSearch(){
		return search;
	}
	
	public ArrayList<Integer> getPattern(){
		return pattern;
	}
	
	public boolean getEval(){
		return eval;
	}

}
