import java.nio.file.Path;
import java.util.ArrayList;

public class MultithreadedInvertedIndex extends InvertedIndex {
	
	private final CustomLock lock;
	
	/**
	 * Initializes the inverted index.
	 */
	public MultithreadedInvertedIndex() {
		super();
		lock = new CustomLock();
	}
	
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
	
	@Override
	public boolean contains(String word) {
		lock.lockReadOnly();
		try {
			return super.contains(word);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public boolean contains(String word, String path) {
		lock.lockReadOnly();
		try {
			return super.contains(word, path);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public boolean contains(String word, String path, int position) {
		lock.lockReadOnly();
		try {
			return super.contains(word, path, position);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public int count() {
		lock.lockReadOnly();
		try {
			return super.count();
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public int count(String word) {
		lock.lockReadOnly();
		try {
			return super.count(word);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public int count(String word, String path) {
		lock.lockReadOnly();
		try {
			return super.count(word, path);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public int getLowestIndex(String word, String path) {
		lock.lockReadOnly();
		try {
			return super.getLowestIndex(word, path);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public void toJSON(Path path) {
		lock.lockReadOnly();
		try {
			super.toJSON(path);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
	public void addAll(String[] words, String path) { 
		lock.lockReadWrite();
		try {
			super.addAll(words, path);
		}
		finally {
			lock.unlockReadWrite();
		}
	}
	
	@Override
	public void addAll(InvertedIndex other) { 
		lock.lockReadWrite();
		try {
			super.addAll(other);
		}
		finally {
			lock.unlockReadWrite();
		}
	}
	
	@Override
	public void add(String word, String path, int position) {
		lock.lockReadWrite();
		try {
			super.add(word, path, position);
		}
		finally {
			lock.unlockReadWrite();
		}
	}
	
	@Override
	public ArrayList<SearchResult> exactSearch(String[] words) {
		lock.lockReadOnly();
		try {
			return super.exactSearch(words);
		}
		finally {
			lock.unlockReadOnly();
		}
	}
	
	@Override
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