package fr.ufrt.bi.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Config {
	
	@SuppressWarnings("resource")
	public static LinkedList<LinkedList<Integer>> loadDataset() {
		LinkedList<LinkedList<Integer>> dataset = new LinkedList<LinkedList<Integer>>();
		
		BufferedReader br = null;
		
		String fileName = "/Users/larissaleite/Downloads/retail10.txt";

        try {
        	br = new BufferedReader(new FileReader(fileName));
			
			String line = "";
			String datSplitBy = " ";
			
			while ((line = br.readLine()) != null) {
				String[] values = line.split(datSplitBy);
				LinkedList<Integer> lines = new LinkedList<Integer>();
				
				for (String value : values) {
					Integer integ = Integer.parseInt(value);
					lines.add(integ);
				}
				
				dataset.add(lines);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return dataset;
	}

}
