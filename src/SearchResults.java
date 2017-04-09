import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * This class is used to store the
 * results of searching for words
 * in the query.
 * 
 * @author mitchellmcpartland
 */
public class SearchResults {
	
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> results;
	
	/**
	 * Constructor for the SearchResults object
	 * 
	 */
	public SearchResults() {
		this.results = new TreeMap<String, TreeMap<String, TreeSet<Integer>>>();
	}
	
	/**
	 * Used to add a word from the query if there
	 * were no instances found in the InvertedIndex.
	 * 
	 * @param query word to be added
	 */
	public void add(String word) {
		this.results.put(word, new TreeMap<String, TreeSet<Integer>>());
	}
	
	/**
	 * Used to add a word from the query, along
	 * with the position and path from which
	 * the word was found.
	 * 
	 * @param query word to be added
	 * @param the position to be added
	 * @param the path to be added
	 */
	public void add(String word, Integer position, String path) {
		if(this.results.containsKey(word)) {
			TreeMap<String, TreeSet<Integer>> map = this.results.get(word);
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
			TreeMap<String, TreeSet<Integer>> map = new TreeMap<>();
			TreeSet<Integer> set = new TreeSet<>();
			set.add(position);
			map.put(path, set);
			this.results.put(word, map);
		}
	}
	
	/**
	 * Used to iterate through the files and positions
	 * of a specific word. Then calls add() to add
	 * the necessary information.
	 * 
	 * @param query word to be added
	 * @param iterator that goes through the files
	 *        and positions for a certain word
	 */
	public void addResult(String searchWord, Iterator<Entry<String, TreeSet<Integer>>> it) {
		while(it.hasNext()) {
			Entry<String, TreeSet<Integer>> entry = it.next();
			String file = entry.getKey().toString();
			TreeSet<Integer> positions = entry.getValue();
			Iterator<Integer> it3 = positions.iterator();
			while(it3.hasNext()) {
				Integer position = it3.next();
				this.add(searchWord, position, file);
			}
		}
	}
	
	/**
	 * Used to put a found word, its files, and its
	 * positions into an ArrayList of WriteObjects.
	 * This is done so that, when I have to write
	 * the results to the output file, the files and
	 * positions are sorted properly.
	 * 
	 * @param an entry that contains the found word,
	 *        along with its associated files and
	 *        positions
	 */
	public static ArrayList<WriteObject> getWriteObj(Entry<String, TreeMap<String, TreeSet<Integer>>> entry) {
		ArrayList<WriteObject> list = new ArrayList<WriteObject>();
		TreeMap<String, TreeSet<Integer>> nestedMap = (TreeMap<String, TreeSet<Integer>>) entry.getValue();
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
	
	/**
	 * Used to call the writeResults method
	 * from within my JSONWriter class.
	 * 
	 * @param the output path to be written to
	 */
	public void toJSON(Path path) {
		try {
			JSONWriter.writeResults(this.results, path);
		} catch (IOException e) {
			System.out.println("Unable to write the JSON file to the output path");
		}
	}
}
