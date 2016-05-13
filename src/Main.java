import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) throws IOException {
   
		LinkedList<LinkedList> matrix = new LinkedList<LinkedList>();
		LinkedList<LinkedList> SearchResult = new LinkedList<LinkedList>();
		
		LinkedList<LinkedList> sample = new LinkedList<LinkedList>();
		LinkedList<Evaluation_Object> evaluations = new LinkedList<Evaluation_Object>();		

		Sampling sampling;
		
		String fileName = "D:\\Faculdade\\IT4BI master\\1st year\\2nd semester - UFRT\\BI seminar\\some smaller data sets\\retail.dat\\retail.dat";

        DataInputStream input = new DataInputStream(new FileInputStream(
        		fileName));
        
        
        //THRESHOLD is to test for a small number of tuples - 10 is set - remove conditon to get all
        String line = "";
		String datSplitBy = " ";
		int threshold =0;
      //List<Integer> timeInMachines = new LinkedList<Integer>();
		while ((line = input.readLine()) != null 
				&& threshold<10
				) {
			String[] values = line.split(datSplitBy);
			LinkedList<Integer> lines = new LinkedList<Integer>();
			for (String value : values) {
				Integer integ = Integer.parseInt(value);
				lines.add(integ);
			}
			threshold++;
		matrix.add(lines);
		System.out.println(lines);

		System.out.println();
		}
		
//Check nb of lines and cols 	
//for retail.dta the nb of lines is 88162
		System.out.println();
		System.out.println("Number of imported tuples: " + matrix.size());
//Too long to check the nb of columns; for retail.dat the maximum is 76
/**		int maxcol=0;
		for(int i=0;i < matrix.size(); i++){
			int col = matrix.get(i).size();
			if(col>maxcol){
				maxcol=col;
			}
		}
		System.out.println("Max columns: " + maxcol);
**/		
		
		//Create the vertical matrix - the indexes of each item
		HashMapRKeys invertedM  = new HashMapRKeys();
		for(int i=0;i < matrix.size(); i++){
			for(int j=0;j<matrix.get(i).size();j++){
				invertedM.addValue(matrix.get(i).get(j), i);
			}
		}
		System.out.println("HASHMAP nb of keys: " + invertedM.size());
		System.out.println("HASHMAP complete: " + invertedM.getMap());
		
		
		Search search = new Search(matrix, invertedM);
		
		while(true){
			//Do the search method that will ask the user the item he wants to search
			//Retrieves a linkedList with the tuples found in the search
			search.doSearch();
			SearchResult=search.getResult();
			
			
			//Searched the evaluation LinkedList to check which evaluations are relevant for this new pattern sampling
			//The ones which the searched item is the same are added to a LikedList of EvaluationObjects
			int searchItem = search.getSearchItem();
			LinkedList<Evaluation_Object> relevantFeedback = new LinkedList<Evaluation_Object>();
			for(int i=0;i<evaluations.size();i++){
				if(evaluations.get(i).getSearch()==searchItem){
					relevantFeedback.add(evaluations.get(i));
				}
			}
			
			//Choose algorithm
				System.out.println("Do you want to use Frequency based Sampling or Area based Sampling?");
				System.out.println("Type 1 for Frenquency based Sampling");
				System.out.println("Type 2 for Area based Sampling");
				Scanner s1 = new Scanner(System.in);
				int answer = -1 ;
				while (answer > 2 || answer < 1) {			
				   while(!s1.hasNextInt())
				   {
				       s1.next() ;
				   }
				   	answer= s1.nextInt();
				   	if(answer==1){
				   		//System.out.println("search result to sampling"+ SearchResult.size() + " " + SearchResult.get(0).size());
				   		sampling= new Freq_Sampling(SearchResult,relevantFeedback, searchItem);
						sample=sampling.getSample();
				   	}
					if(answer==2){
						sampling = new Area_Sampling(SearchResult,relevantFeedback, searchItem);
						sample=sampling.getSample();
					}
	
				}
				
				//for each subset retrieved ask the user for feedback
				UserEvaluation userEval= new UserEvaluation(sample, search.getSearchItem());
				for (int i=0; i<userEval.getUserEvalList().size();i++){
					evaluations.add(userEval.getUserEvalList().get(i));
				}
				System.out.println("User eval list size " + evaluations.size());
				
			}
	}

	
	


}