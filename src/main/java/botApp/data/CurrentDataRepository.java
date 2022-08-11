package botApp.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class CurrentDataRepository {
	
	private HashMap<String, ArrayList<String>> dataMap = new HashMap<String, ArrayList<String>>();
	
	public void put(String key, ArrayList<String> value) {
		dataMap.put(key, value);
	}
	
	public void addValueToKey(String key, String value) {
		if (dataMap.containsKey(key)) {
			dataMap.get(key).add(value);
		} else {
			ArrayList<String> list = new ArrayList<String>();
			list.add(value);
			dataMap.put(key, list);
		}
	}
	
	public Double getValue(String key) {
		return Double.valueOf(dataMap.get(key).get(0));
	}
	
	public ArrayList<String> get(String key){
		return dataMap.get(key);
	}
	public void deleteKey(String key) {
		dataMap.remove(key);
	}
}
