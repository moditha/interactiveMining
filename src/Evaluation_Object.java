import java.util.LinkedList;

public class Evaluation_Object {
	
	int search;
	LinkedList<Integer> sample_evaluated;
	int eval;
	public Evaluation_Object(int search, LinkedList<Integer> sample_evaluated, int eval){
		this.search=search;
		this.sample_evaluated =sample_evaluated;
		this.eval= eval;
	}
	
	public int getSearch(){
		return search;
	}
	
	public LinkedList<Integer> getSample(){
		return sample_evaluated;
	}
	
	public int getEval(){
		return eval;
	}

}
