package Not_trashing_but_not_using_now;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PowerSet {

	public static void main(String[] args) {
		

		Set<String> mySet = new HashSet<String>();
		 mySet.add("a");
		 mySet.add("b");
		 mySet.add("c");
		 mySet.add("d");
		 mySet.add("e");

		 for (Set<String> s : PowerSet.powerSet_v1(mySet)) {
		     System.out.println(s);
		 }

		/**
		 String[] set_v2 = new String[20];
		 
		 for(int i=0; i<20; i++){
			 set_v2[i]=i+"";
		 }
		 LinkedHashSet result_v2 = new LinkedHashSet<>();
		 result_v2 = powerset_v2(set_v2);

			 System.out.println(result_v2);
		**/
	}
	/**
	 * FIrst implementation of the powerset - http://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
	 * @param originalSet
	 * @return
	 */
	public static <T> Set<Set<T>> powerSet_v1(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet_v1(rest)) {
	    	Set<T> newSet = new HashSet<T>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }		
	    return sets;
	}
	
	
	
	/**
	 * Second implementation of the powerset - http://jvalentino.blogspot.fr/2007/02/shortcut-to-calculating-power-set-using.html
	 * @param set
	 * @return
	 */
	private static LinkedHashSet powerset_v2(String[] set) {
	     
	       //create the empty power set
	       LinkedHashSet power = new LinkedHashSet();
	     
	       //get the number of elements in the set
	       int elements = set.length;
	     
	       //the number of members of a power set is 2^n
	       int powerElements = (int) Math.pow(2,elements);
	     
	       //run a binary counter for the number of power elements
	       for (int i = 0; i < powerElements; i++) {
	         
	           //convert the binary number to a string containing n digits
	           String binary = intToBinary(i, elements);
	         
	           //create a new set
	           LinkedHashSet innerSet = new LinkedHashSet();
	         
	           //convert each digit in the current binary number to the corresponding element
	            //in the given set
	           for (int j = 0; j < binary.length(); j++) {
	               if (binary.charAt(j) == '1')
	                   innerSet.add(set[j]);
	           }
	         
	           //add the new set to the power set
	           power.add(innerSet);
	         
	       }
	     
	       return power;
	   }
	
	  /**
     * Converts the given integer to a String representing a binary number
     * with the specified number of digits
     * For example when using 4 digits the binary 1 is 0001
     * @param binary int
     * @param digits int
     * @return String
     */
   private static String intToBinary(int binary, int digits) {
     
       String temp = Integer.toBinaryString(binary);
       int foundDigits = temp.length();
       String returner = temp;
       for (int i = foundDigits; i < digits; i++) {
           returner = "0" + returner;
       }
     
       return returner;
   } 

}
