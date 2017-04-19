import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

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
	
		// TODO for (String word : words)
		for(int i = 0 ; i < words.length ; i++) {
			if(this.index.containsKey(words[i])) {
				// TODO This code should be the same for both exact and partial search
				// TODO Move this into a private void searchHelper(String key, HashMap<String, SearchResult> resultMap, ArrayList<SearchResult> resultList)
				// TODO And call the searchHelper in both search methods
				for(String path : index.get(words[i]).keySet()) {
					// TODO create and set count and lowestIndex here 
					
					if(!(resultMap.containsKey(path))) {
						// TODO A little more efficient to do this: int count = index.get(words[i]).get(path).size();
						// TODO int lowestIndex = index.get(words[i]).get(path).first();
						int count = this.count(words[i], path);
						int lowestIndex = this.getLowestIndex(words[i], path);
						SearchResult result = new SearchResult(path, count, lowestIndex);
						resultList.add(result);
						resultMap.put(path, result);
					} else {
						SearchResult result = resultMap.get(path);
						// TODO Same comment for count/index
						int count = this.count(words[i], path);
						int lowestIndex = this.getLowestIndex(words[i], path);
						result.addCount(count);
						result.updateIndex(lowestIndex);
						resultMap.put(path, result); // TODO Don't need because your results are mutable
					}
				}
			}
		}
		
		Collections.sort(resultList);
		return resultList;	
	}
	
	
	// TODO Either remove, or keep  but don't use
	/**
	 * Contains method that checks the InvertedIndex
	 * to see if the index contains words that would
	 * count as a "partial match".
	 * 
	 * @param the word being matched against
	 */
	public boolean containsPartial(String word) {
		Set<String> words = this.index.keySet();
		for(String theWord : words) {
			if(theWord.startsWith(word)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns an ArrayList of words in the InvertedIndex
	 * that start with the "partial search word".
	 * 
	 * @param the word being matched against
	 */
	public ArrayList<String> partialWords(String word) {
		ArrayList<String> partials = new ArrayList<>();
		Set<String> words = this.index.keySet();
		for(String theWord : words) {
			if(theWord.startsWith(word)) {
				partials.add(theWord);
			}
		}
		return partials;
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
		
		/*
		for word in words:
		    look at: https://github.com/usf-cs212-2017/lectures/blob/master/Data%20Structures/src/FindDemo.java#L144
		    use instead of tailSet, use tailMap.keySet
			for every key that starts with our query in index
				if (key.startsWith(word)) {
					searchHelper(key, resultMap, resultList);
				}
				else break
		 */
		
		
		
		for(int i = 0 ; i < words.length ; i++) {
			if(this.containsPartial(words[i])) {
				ArrayList<String> partials = partialWords(words[i]);
				for(int x = 0 ; x < partials.size() ; x++) {
					for(String path : index.get(partials.get(x)).keySet()) {
						if(!(resultMap.containsKey(path))) {
							int count = this.count(partials.get(x), path);
							int lowestIndex = this.getLowestIndex(partials.get(x), path);
							SearchResult result = new SearchResult(path, count, lowestIndex);
							resultList.add(result);
							resultMap.put(path, result);
						} else {
							SearchResult result = resultMap.get(path);
							int count = this.count(partials.get(x), path);
							int lowestIndex = this.getLowestIndex(partials.get(x), path);
							result.addCount(count);
							result.updateIndex(lowestIndex);
							resultMap.put(path, result);
						}
					}
				}
			}
		}
		Collections.sort(resultList);
		return resultList;	
	}
}