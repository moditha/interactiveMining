import java.util.LinkedList;
import java.util.Scanner;

public class UserEvaluation {

	LinkedList<LinkedList> sample;
	int search;
	LinkedList<Evaluation_Object> evaluations = new LinkedList<Evaluation_Object>();
	
	public UserEvaluation(LinkedList<LinkedList> sample, int search) {
		this.sample=sample;
		this.search=search;
		askUserforEval();
	}

	/**
	 * Given the sample retrieved ad showed to the user and the item he searched, it asks for each subset retreived the user's opionion
	 * For each subset retrieved in the search it adds one object of Evaluation_object to a LinkedList that saves the user's input
	 */
	private void askUserforEval() {
		
		//needs better interface
		Scanner s = new Scanner(System.in);
		for (int i =0; i<sample.size();i++){
			System.out.println("How relevant did you find this itemset for your search?");
			for(int j=0; j<sample.get(i).size(); j++){
				System.out.print(sample.get(i).get(j)+" ");
			}
			int e = s.nextInt();
			evaluations.add(new Evaluation_Object(search,sample.get(i), e));
		}
		
	}
	
	/**
	 * Gets the LinkedList with all the user feedbacks for the subsets he saw for a given search
	 * @return the user feedback
	 */
	public LinkedList<Evaluation_Object> getUserEvalList(){
		return evaluations;
	}

}
