import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.sound.sampled.Port;

public class Freq_Sampling extends Sampling{
	


	public Freq_Sampling(LinkedList<LinkedList> matrix, LinkedList<Evaluation_Object> relevantEvals) {
		super(matrix, relevantEvals);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Given the number of the tuple (recorded in the sample matrix)it gets that tuple from the dataset
	 * Randomly calculates a binary matrix with each item of the tuple and a 50-50 chance of it being in the choosen subset
	 * This way, all the subsets of the powerset have the same probability of being chosen, regardless of its size
	 * @param sample_nb_itemset - the location of the tuple in the input matrix (original data -all itemsets)
	 * @return the subset calculated for the given itemset
	 */
	public LinkedList<Integer> calculateSubset(Integer sample_nb_itemset) {
		LinkedList<Integer> itemset = matrixinput.get(sample_nb_itemset);
		int[] outputListBinary= new int[(int)itemset.size()];
		Random r = new Random();
		System.out.println("Sample itemset to generate a subset: ");
		for (int i=0;i<itemset.size();i++){
			outputListBinary[i]=r.nextInt(2);
			System.out.print(itemset.get(i) + " ");
		}
		System.out.println();
		System.out.println("Generated subset: ");
		LinkedList <Integer> pattern = new LinkedList<Integer>();
		for (int i=0;i<outputListBinary.length;i++){
			if(outputListBinary[i]==1){
				pattern.add(itemset.get(i));
				System.out.print(itemset.get(i) + " ");
			}
		}
		System.out.println();
		return pattern;
	}


	


	
}
