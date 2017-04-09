import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * Stores an inverted index of words to paths to positions within those paths
 * the words were found.
 * 
 * @author mitchellmcpartland
 */
public class InvertedIndex {

	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	
	/**
	 * Initializes the inverted index.
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
	}

	/**
	 * Adds the word, position, and path to the inverted index.
	 * 
	 * @param word the word to add
	 * @param position the position at the path the word was found
	 * @param path the path the word was found
	 */
	public void add(String word, int position, String path) {
		if(!this.index.containsKey(word)) {
			this.index.put(word, new TreeMap<>());
		}
		if(!this.index.get(word).containsKey(path)) {
			this.index.get(word).put(path, new TreeSet<>());
		}
		this.index.get(word).get(path).add(position);
	}
	
	/**
	 * Overridden toString method.
	 * 
	 */
	@Override
	public String toString() {
		return this.toString();
	}
	
	/**
	 * Checks to see if the inverted index has the word.
	 * 
	 * @param the word that the user wants to know if the index contains
	 */
	public boolean contains(String word) {
		return this.index.containsKey(word);
	}
	
	/**
	 * Checks to see if the inverted index has the path associated
	 * with the word.
	 * 
	 * @param the word that may have the path
	 * @param path that the user wants to know if the index contains
	 */
	public boolean contains(String word, String path) {
		if(this.contains(word) == true) {
			return this.index.get(word).containsKey(path);
		} else {
			return false;
		}
	}
	
	/**
	 * Checks to see if the inverted index has the position associated
	 * with the word and the path.
	 * 
	 * @param the word that may have the path
	 * @param the path that may have the position
	 * @param position that the user wants to know if the index contains
	 */
	public boolean contains(String word, String path, int position) {
		if(this.contains(word, path) == true) {
			TreeMap<String, TreeSet<Integer>> nested = this.index.get(word);
			TreeSet<Integer> set = nested.get(path);
			return set.contains(position);
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the number of words in the inverted index.
	 * 
	 */
	public int count() {
		return this.index.size();
	}
	
	/**
	 * Returns the number of paths associated with the word.
	 * 
	 * @param the word in which the number of paths is returned
	 */
	public int count(String word) {
		if(this.contains(word) == true) {
			TreeMap<String, TreeSet<Integer>> nested = this.index.get(word);
			return nested.size();
		} else {
			return 0;
		}
	}
	
	/**
	 * Returns the number of positions for a word and its
	 * associated path.
	 * 
	 * @param the word that has the path
	 * @param the path in which the number of positions is returned
	 */
	public int count(String word, String path) {
		if(this.contains(word, path) == true) {
			TreeMap<String, TreeSet<Integer>> nested = this.index.get(word);
			TreeSet<Integer> set = nested.get(path);
			return set.size();
		} else {
			return 0;
		}
	}
	
	/**
	 * Used to write to the output JSON file.
	 * 
	 * @param the output path that will be written to
	 */
	public void toJSON(Path path) {
		try {
			JSONWriter.writeJSON(this.index, path);
		} catch (IOException e) {
			System.out.println("Unable to write the JSON file to the output path");
		}
	}
	
	/**
	 * Used to add each word, along with the associated
	 * path and position, to the InvertedIndex.
	 * 
	 * @param the words that will be iterated through
	 *        and added to the index
	 * @param the path that contained the words
	 */
	public void addAll(String[] words, String path) { 
		int position = 1;
		for(String word : words) {
			this.add(word, position, path);
			position++;
		}
	}
	
	/**
	 * Used to iterate through the InvertedIndex, searching
	 * for the word(s) in the query.
	 * 
	 * @param SearchResults object that is used to store
	 *        the found word(s), positions, and files.
	 * @param Array of words from the query
	 * @param Boolean signifying whether this is a
	 *        partial search or an exact search
	 */
	public void QueryFinder(SearchResults results, String[] words, boolean partial) {
		Iterator<Entry<String, TreeMap<String, TreeSet<Integer>>>> it1 = this.index.entrySet().iterator();
		String proper = getProperName(words);
		int found = 0;
		while(it1.hasNext()) {
			Entry<String, TreeMap<String, TreeSet<Integer>>> entry = (Entry<String, TreeMap<String, TreeSet<Integer>>>) it1.next();
			String word = entry.getKey();
			for(int i = 0 ; i < words.length ; i++) {
				if(word.startsWith(words[i])) {
					if(partial == false) {
						found = 1;
						TreeMap<String, TreeSet<Integer>> nestedMap = entry.getValue();
						Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
						results.addResult(proper, it2);
					} else {
						if(word.equals(words[i])) {
							found = 1;
							TreeMap<String, TreeSet<Integer>> nestedMap = entry.getValue();
							Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
							results.addResult(proper, it2);
						}
					}
				}
			}
		}
		if(found == 0 && proper.length() > 0) {
			results.add(proper);
		}
	}
	
	/**
	 * Simple method to remove white space at end of
	 * a provided query.
	 * 
	 * @param the words in the query
	 */
	public static String getProperName(String[] words) {
		StringBuilder proper = new StringBuilder();
		for(int i = 0 ; i < words.length ; i++) {
			proper.append(words[i]);
			proper.append(" ");
		}
		if(proper.length() != 0) {
			proper.deleteCharAt(proper.length() - 1);
		}
		return proper.toString();
	}
}