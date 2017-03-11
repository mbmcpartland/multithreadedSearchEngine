import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO Rethink Javadoc

/**
 * This class is obviously used to structure my WordIndex. I create a private
 * final TreeMap that maps a String to another TreeMap, which maps another
 * String to a TreeSet of Integers.
 * 
 * Stores an inverted index of words to paths to positions within those paths
 * the words were found.
 * 
 * @author mitchellmcpartland
 */
public class WordIndex { // TODO Refactor to InvertedIndex

	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	public WordIndex() {
		this.index = new TreeMap<>();
	}

	/**
	 * This add function takes in the word to be added, the position to be
	 * added, and the file to be added. I do two checks to see if the word was
	 * already added or if the path was already added.
	 * 
	 * @param String,
	 *            int, String
	 */

	/**
	 * Adds the word, position, and path to the inverted index.
	 * 
	 * @param word the word to add
	 * @param position the position at the path the word was found
	 * @param path the path the word was found
	 */
	public void add(String word, int position, String path) {
		if (this.index.containsKey(word)) {
			TreeMap<String, TreeSet<Integer>> map = this.index.get(word);
			if (map.containsKey(path)) {
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
	 * This function is used to return the iterator needed for my writeJSON
	 * function. I need this getter because my WordIndex is private.
	 * 
	 * @param none
	 */

	// TODO 	Iterator<Entry<String, TreeMap<String, TreeSet<Integer>>>> it = this.index.entrySet().iterator();
	// TODO Returning the iterator still breaks encapsulation
	
	// Sophie helppppp
	public Iterator getIterator() {
		Iterator it = this.index.entrySet().iterator();
		return it;
	}
	
	// TODO Always implement toString()
	
	// TODO More generally useful to have a handful more methods (think about other data structures)
	// TODO contains(String word), contains(String word, String path), contains(String word, String path, int position)
	// TODO count() number of words, count(String word) returns number of files for that word, count(String word, String path), etc.
	// TODO BE CAREFUL about null pointers... should not throw any null pointer exceptions
	
	/* TODO
	public void toJSON(Path path) {
		JSONWriter.writeJSON(this.index, path);
	}
	*/
}
