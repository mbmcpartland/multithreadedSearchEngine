import java.util.Comparator;

// TODO Refactor to SearchResult

/**
 * The purpose of this class is to make
 * sure that the files and positions are
 * properly sorted when being written to
 * the output JSON results file.
 * 
 * @author mitchellmcpartland
 */
public class WriteObject implements Comparable<WriteObject> {
	
	private String query;
	private int count;
	private int index;
	
	/**
	 * Constructor for the WriteObject
	 * 
	 * @param query
	 * @param the count of the query
	 * @param the first index of the query
	 */
	public WriteObject(String query, int count, int index) {
		this.query = query;
		this.count = count;
		this.index = index;
	}
	
	/**
	 * Returns the query
	 * 
	 */
	public String getQuery() {
		return this.query;
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
	
	// TODO Add some setters here so we can update this object's values
	// addCount(int count) where we add this count plus a new count
	// updateIndex(int index) changes to the min of this index and the new index
	
	
	// TODO Remove
	/**
	 * Comparator object that is used to compare
	 * the counts for two WriteObjects.
	 * 
	 */
	public static final Comparator<WriteObject> COUNT_COMPARATOR = new Comparator<WriteObject>() {
		
		@Override
		public int compare(WriteObject o1, WriteObject o2) {
			Integer count1 = o1.count;
			Integer count2 = o2.count;
			int comp = count2.compareTo(count1);
			if(comp == 0) {
				return o1.compareTo(o2);
			}
			return comp;
		}
	};
	
	// TODO Remove
	/**
	 * Comparator object that is used to compare
	 * the first index of two WriteObjects.
	 * 
	 */
	public static final Comparator<WriteObject> INDEX_COMPARATOR = new Comparator<WriteObject>() {
		
		@Override
		public int compare(WriteObject o1, WriteObject o2) {
			Integer index1 = o1.index;
			Integer index2 = o2.index;
			int comp = index1.compareTo(index2);
			return comp;
		}
	};

	// TODO Have a single compareTo method that does all of the comparison necessary
	/**
	 * Keeps the natural ordering if the first
	 * index is the same for two WriteObjects.
	 * 
	 * @param the WriteObject to be compared with
	 */
	@Override
	public int compareTo(WriteObject o) {
		int indexComp = INDEX_COMPARATOR.compare(this, o);
		if(indexComp == 0) {
			return this.query.compareTo(o.query);
		}
		return indexComp;
	}	
}
