import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

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
	 * Overridden toString method.
	 * 
	 */
	@Override
	public String toString() {
		return this.toString(); // TODO this.index.toString();
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
	 * Returns the lowest position for a given word and path.
	 * 
	 * @param the word that has the path
	 * @param the path in which the lowest index is returned
	 */
	public int getLowestIndex(String word, String path) {
		if(this.contains(word, path) == true) {
			TreeMap<String, TreeSet<Integer>> nested = this.index.get(word);
			TreeSet<Integer> set = nested.get(path);
			return set.first();
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
	 * Main add function that actually adds the word,
	 * the associated path, and the associated position
	 * to the InvertedIndex.
	 * 
	 * @param the words that will be iterated through
	 *        and added to the index
	 * @param the path that contained the words
	 */
	private void addHelper(String word, String path, int position) {
		if(!this.index.containsKey(word)) {
			this.index.put(word, new TreeMap<>());
		}
		if(!this.index.get(word).containsKey(path)) {
			this.index.get(word).put(path, new TreeSet<>());
		}
		this.index.get(word).get(path).add(position);
	}
	
	/**
	 * Used to add a single word, along with the associated
	 * path and position, to the InvertedIndex.
	 * 
	 * @param the word that will be added to the index
	 * @param the path that contained the words
	 */
	public void add(String word, String path, int position) {
		addHelper(word, path, position);
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
			addHelper(word, path, position);
			position++;
		}
	}
	
	/**
	 * Search method that calls partialSearch or exactSearch.
	 * 
	 * @param the words in the query
	 * @param boolean indicating a partial or exact search
	 */
	public ArrayList<SearchResult> search(String[] words, boolean exact) {
		if(exact == false) {
			return partialSearch(words);
		} else {
			return exactSearch(words);
		}
	}
	
	/**
	 * Search method used to do "exact searching" in the
	 * InvertedIndex. An ArrayList of SearchResults is
	 * returned, which contains the exact search results.
	 * 
	 * @param the words in the query
	 */
	public ArrayList<SearchResult> exactSearch(String[] words) {
		ArrayList<SearchResult> resultList = new ArrayList<>();
		HashMap<String, SearchResult> resultMap = new HashMap<>();
		
		for(String word : words) {
			if(this.index.containsKey(word)) {
				searchHelper(word, resultMap, resultList);
			}
		}
		
		Collections.sort(resultList);
		return resultList;	
	}
	
	/**
	 * Search method used to do "exact searching" in the
	 * InvertedIndex. An ArrayList of SearchResults is
	 * returned, which contains the partial search results.
	 * 
	 * @param the words in the query
	 */
	public ArrayList<SearchResult> partialSearch(String[] words) {
		ArrayList<SearchResult> resultList = new ArrayList<>();
		HashMap<String, SearchResult> resultMap = new HashMap<>();
		
		for(String word : words) {
			for(String key : this.index.tailMap(word).keySet()) {
				if(key.startsWith(word)) {
					searchHelper(key, resultMap, resultList);
				}
			}
		}
		
		Collections.sort(resultList);
		return resultList;	
	}
	
	/**
	 * Search method used to do "exact searching" in the
	 * InvertedIndex. An ArrayList of SearchResults is
	 * returned, which contains the partial search results.
	 * 
	 * @param the word being searched for
	 * @param the map which maps the search word with its
	 * 		  associated SearchResult object
	 * @param the ArrayList of SearchResult objects
	 */
	private void searchHelper(String key, HashMap<String, SearchResult> resultMap, ArrayList<SearchResult> resultList) {
		for(String path : index.get(key).keySet()) {
			int count = this.index.get(key).get(path).size();
			int lowestIndex = this.index.get(key).get(path).first();
			if(!(resultMap.containsKey(path))) {
				SearchResult result = new SearchResult(path, count, lowestIndex);
				resultList.add(result);
				resultMap.put(path, result);
			} else {
				SearchResult result = resultMap.get(path);
				result.addCount(count);
				result.updateIndex(lowestIndex);
			}
		}
	}
}