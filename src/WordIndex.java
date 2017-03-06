import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class is obviously used to structure
 * my WordIndex.  I create a private final
 * TreeMap that maps a String to another TreeMap,
 * which maps another String to a TreeSet of
 * Integers.
 * @author mitchellmcpartland
 */
public class WordIndex {
	
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index; 
	
	public WordIndex() {
		this.index = new TreeMap<>();
	}
	
	/**
	 * This add function takes in the word to be added,
	 * the position to be added, and the file to be
	 * added.  I do two checks to see if the word was
	 * already added or if the path was already added.
	 * @param String, int, String
	 */
	
	public void add(String word, int position, String path) {
		if(this.index.containsKey(word)) {
			TreeMap<String, TreeSet<Integer>> map = this.index.get(word);
			if(map.containsKey(path)) {
				TreeSet<Integer> set = map.get(path);
				set.add(position);
				map.put(path, set);
				this.index.put(word, map);
			} else {
				TreeSet<Integer> set = new TreeSet<>();
				set.add(position);
				map.put(path, set);
				this.index.put(word, map);
			}
		} else {
			TreeMap<String, TreeSet<Integer>> map = new TreeMap<>();
			TreeSet<Integer> set = new TreeSet<>();
			set.add(position);
			map.put(path, set);
			this.index.put(word, map);
		}
	}
	
	/**
	 * This function is used to return the iterator needed
	 * for my writeJSON function.  I need this getter
	 * because my WordIndex is private.
	 * @param none
	 */
	
	//Sophie helppppp
	public Iterator getIterator() {
		Iterator it = this.index.entrySet().iterator();
		return it;
	}
}
