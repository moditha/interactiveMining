import java.util.LinkedList;
import java.util.Scanner;

public class Search {

		//the matrix will be the full dataset to do the search on
		LinkedList<LinkedList> matrix = new LinkedList<LinkedList>();
	Integer searching =0;
	LinkedList<LinkedList> searchResult = new LinkedList<LinkedList>();
	
	
	public Search(LinkedList<LinkedList> matrix2) {
		this.matrix=matrix2;
		System.out.println("MATRIX " + matrix.size());
	}

	/**
	 * Asks for a item to the user, to do the search and for each itemset with tah item calls addtosearchresults in order to add that tuple in the results
	 * Given that item it adds to the Searchresults all the tuples that contain that item
	 */
	public void doSearch(){
		searchResult = new LinkedList<>();
		System.out.println("Which item are you looking for?");
		Scanner s = new Scanner(System.in);
		searching=s.nextInt();
		System.out.println("searching for " +searching);
		for(int i=0; i<matrix.size();i++){
			for(int j=0; j<matrix.get(i).size();j++){
				if(matrix.get(i).get(j)==searching){
					addtoSearchResults(i);
					System.out.println("SEARCH RESULTS SIZE " + searchResult.size());
				}
			}
		}
	
	}
	
	

	/**
	 * GIven an index, it goes to the dataset and copies it to the search results
	 * @param i - the index of the tuple to be retrieved, in given dataset
	 */
	private void addtoSearchResults(int i) {
		LinkedList<Integer> tuple = new LinkedList<>();
		System.out.println("Tuple added to search results:");
		int line = matrix.size();
		for(int j=0; j<matrix.get(i).size(); j++){
			tuple.add((Integer) matrix.get(i).get(j));
			System.out.print(matrix.get(i).get(j) + " ");
		}
		searchResult.add(tuple);
		System.out.println();
		
	}
	
	/**
	 * Retrieves a linkedlist with all the tuples that contain the searched item
	 * @return the search result
	 */
	public LinkedList<LinkedList> getResult(){
		return searchResult;
	}
	
	/**
	 * Retrieves the item the user inputed as search item
	 * @return the item searched
	 */
	public Integer getSearchItem(){
		return searching;
	}
}
