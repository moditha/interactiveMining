
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.sound.sampled.Port;

public class Area_Sampling extends Sampling{
	
	


	
	public Area_Sampling(LinkedList<LinkedList> matrix) {
		super(matrix);
	
	}

	/**
	 * Given the number of the tuple (recorded in the sample matrix)it gets that tuple from the dataset
	 * With that tuple, calculates the probabilities of each size of subset to be generated and saves it in the int[] probabilityList
	 * After, given the probabilities, calculates which sizes of subsets to generate
	 * Given that selection, generates ONE random subset of the size selected for generation
	 * @param sample_nb_itemset - the location of the tuple in the input matrix (original data -all itemsets)
	 */
	public LinkedList<Integer> calculateSubset(Integer sample_nb_itemset) {
		LinkedList<Integer> itemset = matrixinput.get(sample_nb_itemset);
		int nb_items = (int)itemset.size();
		//the list of the probabilities for each subset size
		//each cell corresponds to its index size. example probabilityList[5] will be the probability of generating a subset of size 5
		//probabilityList will have the range for which the size will be chosen given a random generated number
		//probabilityList has as many lines as items on the tuple, all the possible sizes of a subset
		int[][] probabilityList = new int[nb_items][2];
		int denominator =0;
		int size=itemset.size();
		for (int i=0;i<itemset.size();i++){
			denominator=denominator + size;
			size--;
		}
		int marker=0;
		System.out.println("Winner intervals for itemset " + sample_nb_itemset + ":" );
		//creates the intervals for which each size will be the "winner"
		for(int i=0; i<nb_items;i++){
			
				probabilityList[i][0]=marker;
				probabilityList[i][1]=(marker+i) ;
				marker = marker+i+1;
			
			
			System.out.println((i+1) + ": [" + probabilityList[i][0] + ", " +probabilityList[i][1]  + "]");
		}
		Random r = new Random();
		int winner_number =r.nextInt(denominator);
		System.out.println("The Random is: " + winner_number);
		System.out.println("The denominator for the subset generator is: " + denominator);
		
		int subsetsize=0;
		for(int i=0; i<nb_items; i++){
			if(winner_number>=probabilityList[i][0] && winner_number<=probabilityList[i][1]){
				subsetsize=i;
			}
		}
		
		//creates a list with the number of items of the input itemset
		//shuffles (randomly) and takes the first n indexes
		System.out.println("The chosen size is: " + (subsetsize+1));
		System.out.println();
		Integer[] arr = new Integer[nb_items];
		    for (int i = 0; i < arr.length; i++) {
		        arr[i] = i;
		    }
		    Collections.shuffle(Arrays.asList(arr));
		    System.out.println(Arrays.toString(arr));
		    
		 //Copy the first n indexes to an arraylist to order them
		    Integer[] arr_list = new Integer[subsetsize+1];
		    for (int i = 0; i < subsetsize+1; i++) {
		    	arr_list[i]=arr[i];
		    }
		   Arrays.sort(arr_list);
		   
		//We go to the original itemset to ge the items on the first n indexes of the arr
		 LinkedList <Integer> pattern = new LinkedList<Integer>();
		 for(int i=0; i<arr_list.length;i++){
			 pattern.add(itemset.get(arr_list[i]));
		 }


		return pattern;
		
		/**
		//How to get the number of digits of an integer
		Integer nb = itemset.get(0);
		int length = (int) Math.log10(nb) + 1;
		System.out.println(length);
		**/
	}


	


	
}
