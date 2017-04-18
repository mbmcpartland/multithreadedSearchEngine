import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedInvertedIndex extends InvertedIndex {
	
	public static final Logger log = LogManager.getLogger();
	
	/**
	 * Initializes the inverted index.
	 */
	public MultithreadedInvertedIndex() {
		super();
	}

	/**
	 * Adds the word, position, and path to the inverted index.
	 * 
	 * @param word the word to add
	 * @param position the position at the path the word was found
	 * @param path the path the word was found
	 */
	public synchronized void add(String word, int position, String path) {
		super.add(word, position, path);
	}
	
	/**
	 * Overridden toString method.
	 * 
	 */
	@Override
	public synchronized String toString() {
		return super.toString();
	}
	
	/**
	 * Checks to see if the inverted index has the word.
	 * 
	 * @param the word that the user wants to know if the index contains
	 */
	public synchronized boolean contains(String word) {
		return super.contains(word);
	}
	
	/**
	 * Checks to see if the inverted index has the path associated
	 * with the word.
	 * 
	 * @param the word that may have the path
	 * @param path that the user wants to know if the index contains
	 */
	public synchronized boolean contains(String word, String path) {
		return super.contains(word, path);
	}
	
	/**
	 * Checks to see if the inverted index has the position associated
	 * with the word and the path.
	 * 
	 * @param the word that may have the path
	 * @param the path that may have the position
	 * @param position that the user wants to know if the index contains
	 */
	public synchronized boolean contains(String word, String path, int position) {
		return super.contains(word, path, position);
	}
	
	/**
	 * Returns the number of words in the inverted index.
	 * 
	 */
	public synchronized int count() {
		return super.count();
	}
	
	/**
	 * Returns the number of paths associated with the word.
	 * 
	 * @param the word in which the number of paths is returned
	 */
	public synchronized int count(String word) {
		return super.count(word);
	}
	
	/**
	 * Returns the number of positions for a word and its
	 * associated path.
	 * 
	 * @param the word that has the path
	 * @param the path in which the number of positions is returned
	 */
	public synchronized int count(String word, String path) {
		return super.count(word, path);
	}
	
	/**
	 * Used to write to the output JSON file.
	 * 
	 * @param the output path that will be written to
	 */
	public synchronized void toJSON(Path path) {
		super.toJSON(path);
	}
	
	/**
	 * Used to add each word, along with the associated
	 * path and position, to the InvertedIndex.
	 * 
	 * @param the words that will be iterated through
	 *        and added to the index
	 * @param the path that contained the words
	 */
	public synchronized void addAll(String[] words, String path) { 
		super.addAll(words, path);
	}
	
	public synchronized ArrayList<SearchResult> search(String[] words, boolean exact) {
		return super.search(words, exact);
	}
	
	public synchronized ArrayList<SearchResult> exactSearch(String[] words) {
		return super.exactSearch(words);
	}
	
	public synchronized boolean containsPartial(String word) {
		return super.containsPartial(word);
	}
	
	public synchronized ArrayList<String> partialWords(String word) {
		return super.partialWords(word);
	}
	
	public synchronized ArrayList<SearchResult> partialSearch(String[] words) {
		return super.partialSearch(words);
	}
}
