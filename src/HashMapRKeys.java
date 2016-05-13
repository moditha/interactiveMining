import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HashMapRKeys{
	
	private HashMap hashMap = new HashMap <Integer, ArrayList<Integer>>();
	
	protected void addValue(Object object, int value) {
		   ArrayList tempList = null;
		   if (hashMap.containsKey(object)) {
		      tempList = (ArrayList) hashMap.get(object);
		      if(tempList == null)
		         tempList = new ArrayList();
		      tempList.add(value);  
		   } else {
		      tempList = new ArrayList();
		      tempList.add(value);               
		   }
		   hashMap.put(object,tempList);
		}
	
	protected ArrayList<Integer> getItemTransactions(int key){
		return (ArrayList<Integer>) hashMap.get(key);
	}
	
	protected int size(){
		return hashMap.size();
	}
	protected HashMap<Integer, ArrayList<Integer>> getMap(){
		return hashMap;
	}

}
