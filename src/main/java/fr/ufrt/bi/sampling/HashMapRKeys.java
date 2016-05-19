package fr.ufrt.bi.sampling;

import java.util.ArrayList;
import java.util.HashMap;

public class HashMapRKeys{
	
	private HashMap<Integer, ArrayList<Integer>> hashMap = new HashMap <Integer, ArrayList<Integer>>();
	
	protected void addValue(Integer object, int value) {
		   ArrayList<Integer> tempList = null;
		   
		   if (hashMap.containsKey(object)) {
		      tempList = hashMap.get(object);
		      if(tempList == null)
		         tempList = new ArrayList<Integer>();
		      tempList.add(value);  
		   } else {
		      tempList = new ArrayList<Integer>();
		      tempList.add(value);               
		   }
		   hashMap.put(object, tempList);
		}
	
	public ArrayList<Integer> getItemTransactions(int key){
		return (ArrayList<Integer>) hashMap.get(key);
	}
	
	protected int size(){
		return hashMap.size();
	}
	protected HashMap<Integer, ArrayList<Integer>> getMap(){
		return hashMap;
	}

}