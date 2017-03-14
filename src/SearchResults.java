import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class SearchResults {
	
	private final TreeMap<String, HashMap<String, TreeSet<Integer>>> results;
	
	public SearchResults() {
		this.results = new TreeMap<String, HashMap<String, TreeSet<Integer>>>();
	}
	
	public void add(String word) {
		this.results.put(word, new HashMap<String, TreeSet<Integer>>());
	}
	
	public void add(String word, Integer position, String path) {
		if(this.results.containsKey(word)) {
			HashMap<String, TreeSet<Integer>> map = this.results.get(word);
			if(map.containsKey(path)) {
				TreeSet<Integer> set = map.get(path);
				set.add(position);
				map.put(path, set);
				this.results.put(word, map);
			} else {
				TreeSet<Integer> set = new TreeSet<>();
				set.add(position);
				map.put(path, set);
				this.results.put(word, map);
			}
		} else {
			HashMap<String, TreeSet<Integer>> map = new HashMap<>();
			TreeSet<Integer> set = new TreeSet<>();
			set.add(position);
			map.put(path, set);
			this.results.put(word, map);
		}
	}
	
	public Iterator getIterator() {
		Iterator it = this.results.entrySet().iterator();
		return it;
	}
	
	public static ArrayList<WriteObject> getWriteObj(Entry<String, HashMap<String, TreeSet<Integer>>> entry) {
		ArrayList<WriteObject> list = new ArrayList<WriteObject>();
		HashMap<String, TreeSet<Integer>> nestedMap = (HashMap<String, TreeSet<Integer>>) entry.getValue();
		Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
		while(it2.hasNext()) {
			Entry<String, TreeSet<Integer>> entry2 = it2.next();
			String file = entry2.getKey().toString();
			TreeSet<Integer> positions = entry2.getValue();
			WriteObject obj = new WriteObject(file, positions.size(), positions.first());
			list.add(obj);
		}
		return list;
	}
}
