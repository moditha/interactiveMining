package fr.ufrt.bi.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HashMapRKeys {

	private HashMap<Integer, ArrayList<Integer>> hashMap = new HashMap<Integer, ArrayList<Integer>>();

	public void addValue(Integer item, int value) {
		ArrayList<Integer> tempList = null;

		if (hashMap.containsKey(item)) {
			tempList = hashMap.get(item);
		}
		if (tempList == null)
			tempList = new ArrayList<Integer>();

		tempList.add(value);
		hashMap.put(item, tempList);
	}

	public ArrayList<Integer> getItemTransactions(int key) {
		return hashMap.get(key);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Gets the intersection of transactions that contains all items
	 * 
	 * @param items
	 * @return
	 */
	public ArrayList<Integer> getTransactionsItems(int[] items) {
		ArrayList<Set<Integer>> transactions = new ArrayList<Set<Integer>>();

		if (items == null) {

			System.out.println("Items is null");
			for (int item : hashMap.keySet()) {
				//System.out.println(hashMap.get(item));
				Set<Integer> set = new HashSet<Integer>(hashMap.get(item));
				transactions.add(set);
			}
			
		}

		else {
			for (int item : items) {
				if (hashMap.get(item) != null) {
					Set<Integer> set = new HashSet<Integer>(hashMap.get(item));
					
					transactions.add(set);
				}
				else
					transactions.add(Collections.emptySet());
			}
		}

		Set<Integer> intersectedTransactions = new HashSet<>();
		if (!transactions.isEmpty()) {
		//	System.out.println("FUUUUUCK");
			intersectedTransactions = transactions.get(0);
			for (int i = 1; i < transactions.size(); i++) {
			//	 System.out.println(intersectedTransactions);
				if(items == null){
					intersectedTransactions.addAll(transactions.get(i));
				}
				else{
				intersectedTransactions.retainAll(transactions.get(i));
				}
			}
		}
		//System.out.println("Final is "+ intersectedTransactions);
		return new ArrayList<Integer>(intersectedTransactions);
	}

	protected int size() {
		return hashMap.size();
	}

	protected HashMap<Integer, ArrayList<Integer>> getMap() {
		return hashMap;
	}

}