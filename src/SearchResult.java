/**
 * Stores the path, total count, and first index for
 * each query.
 * 
 * @author mitchellmcpartland
 */
public class SearchResult implements Comparable<SearchResult> {
	
	private String path;
	private int count;
	private int index;
	
	/**
	 * Constructor for the SearchResult object
	 * 
	 * @param path
	 * @param the count of the query
	 * @param the first index of the query
	 */
	public SearchResult(String path, int count, int index) {
		this.path = path;
		this.count = count;
		this.index = index;
	}
	
	/**
	 * Returns the path
	 * 
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Returns the count
	 * 
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * Returns the first index
	 * 
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Increments the count
	 * 
	 */
	public void addCount(int count) {
		this.count += count;
	}
	
	/**
	 * Updates the first index
	 * 
	 */
	public void updateIndex(int index) {
		if(index < this.index) {
			this.index = index;
		}
	}

	/**
	 * Compares first by total count, then by first index,
	 * and then by path name.
	 * 
	 * @param the SearchResult object to be compared with
	 */
	@Override
	public int compareTo(SearchResult o) {
		Integer count1 = this.count;
		Integer count2 = o.count;
		int countComp = count2.compareTo(count1);
		if(countComp != 0) {
			return countComp;
		}
		Integer index1 = this.index;
		Integer index2 = o.index;
		int indexComp = index1.compareTo(index2);
		if(indexComp == 0) {
			return this.path.compareTo(o.path);
		}
		return indexComp;
	}	
}
