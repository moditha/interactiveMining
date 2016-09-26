package fr.ufrt.bi.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import fr.ufrt.bi.evaluators.Evaluation;
import fr.ufrt.bi.sampling.HashMapRKeys;
import fr.ufrt.bi.sampling.SamplingType;
import test.fr.ufrt.bi.Eval;

public class Testmap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

BufferedReader br = null;
		
		String fileName = "D:\\notes\\BIS\\retail10.txt";

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
			int[] data = {38, 39, 110, 124, 270};
			//System.out.println(hashMapRKeys.getItemTransactions(36));
			//System.out.println(hashMapRKeys.getItemTransactions(38));
			//System.out.println(hashMapRKeys.getItemTransactions(39));
			//System.out.println(hashMapRKeys.getTransactionsItems(data));
			
			System.out.println(hashMapRKeys.getTransactionsItems(data).size());
			
//			Eval e= new Eval(SamplingType.FREQUENCY);
//			ArrayList<Integer> l= new ArrayList<>();
//			l.add(34);
//			ArrayList<Integer> l2= new ArrayList<Integer>(Arrays.asList(34,39,48,107,399,475));
//			e.evaluate(l, l2, 0.25);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
