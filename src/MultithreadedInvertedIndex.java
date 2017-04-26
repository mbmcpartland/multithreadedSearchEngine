import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedInvertedIndex extends InvertedIndex {
	
	public static final Logger log = LogManager.getLogger();
	private CustomLock lock;
	
	/**
	 * Initializes the inverted index.
	 */
	public MultithreadedInvertedIndex() {
		super();
		lock = new CustomLock();
	}
	
	/**
	 * Overridden toString method.
	 * 
	 */
	@Override
	public String toString() {
		lock.lockReadOnly();
		try {
			return super.toString();
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	/**
	 * Checks to see if the inverted index has the word.
	 * 
	 * @param the word that the user wants to know if the index contains
	 */
	public boolean contains(String word) {
		lock.lockReadOnly();
		try {
			return super.contains(word);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	/**
	 * Checks to see if the inverted index has the path associated
	 * with the word.
	 * 
	 * @param the word that may have the path
	 * @param path that the user wants to know if the index contains
	 */
	public boolean contains(String word, String path) {
		lock.lockReadOnly();
		try {
			return super.contains(word, path);
		}
		finally {
			lock.unlockReadOnly();
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
		lock.lockReadOnly();
		try {
			return super.contains(word, path, position);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	/**
	 * Returns the number of words in the inverted index.
	 * 
	 */
	public int count() {
		lock.lockReadOnly();
		try {
			return super.count();
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	/**
	 * Returns the number of paths associated with the word.
	 * 
	 * @param the word in which the number of paths is returned
	 */
	public int count(String word) {
		lock.lockReadOnly();
		try {
			return super.count(word);
		}
		finally {
			lock.unlockReadOnly();
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
		lock.lockReadOnly();
		try {
			return super.count(word, path);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	/**
	 * Used to write to the output JSON file.
	 * 
	 * @param the output path that will be written to
	 */
	public void toJSON(Path path) {
		lock.lockReadOnly();
		try {
			super.toJSON(path);
		}
		finally {
			lock.unlockReadOnly();
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
		lock.lockReadWrite();
		try {
			super.addAll(words, path);
		}
		finally {
			lock.unlockReadWrite();
		}
	}
	
	public ArrayList<SearchResult> search(String[] words, boolean exact) {
		lock.lockReadOnly();
		try {
			return super.search(words, exact);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	public ArrayList<SearchResult> exactSearch(String[] words) {
		lock.lockReadOnly();
		try {
			return super.exactSearch(words);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	public ArrayList<SearchResult> partialSearch(String[] words) {
		lock.lockReadOnly();
		try {
			return super.partialSearch(words);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
}
