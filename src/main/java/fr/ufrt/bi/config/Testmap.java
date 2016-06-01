package fr.ufrt.bi.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import fr.ufrt.bi.sampling.HashMapRKeys;

public class Testmap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

BufferedReader br = null;
		
		String fileName = "D:\\notes\\ADW\\retail.dat.txt";

		HashMapRKeys hashMapRKeys = new HashMapRKeys();
        try {
        	br = new BufferedReader(new FileReader(fileName));
			
			String line = "";
			String datSplitBy = " ";
			
			int transaction = 1;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(datSplitBy);
				LinkedList<Integer> lines = new LinkedList<Integer>();
				
				for (String value : values) {
					Integer integ = Integer.parseInt(value);
					hashMapRKeys.addValue(integ, transaction);
				}
				transaction++;
				//System.out.println(transaction);
			}
			
			br.close();
			int[] data = {36,38,39};
			//System.out.println(hashMapRKeys.getItemTransactions(36));
			//System.out.println(hashMapRKeys.getItemTransactions(38));
			//System.out.println(hashMapRKeys.getItemTransactions(39));
			System.out.println(hashMapRKeys.getTransactionsItems(data));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
