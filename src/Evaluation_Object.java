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
	
	/**
	 * Returns a item (integer) that was the search pon which the subset was retrieved
	 * @return the searched item
	 */
	public int getSearch(){
		return search;
	}
	
	/**
	 * For a given searched item, there is a sample(there can be more, but then other objects of this will be created) that is shown to the user
	 * @return the sample retrieved
	 */
	public LinkedList<Integer> getSample(){
		return sample_evaluated;
	}
	
	/**
	 * For each subset outputed to the user, we evaluates it and this evaluation is associated to the subset retrieved and the original search
	 * @return user feedback
	 */
	public int getEval(){
		return eval;
	}

}
