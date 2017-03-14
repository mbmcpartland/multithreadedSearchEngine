import java.util.Comparator;

public class WriteObject implements Comparable<WriteObject> {
	
	private String query;
	private int count;
	private int index;
	
	public WriteObject(String query, int count, int index) {
		this.query = query;
		this.count = count;
		this.index = index;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public int getIndex() {
		return this.index;
	}
	
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
	
	public static final Comparator<WriteObject> INDEX_COMPARATOR = new Comparator<WriteObject>() {
		
		@Override
		public int compare(WriteObject o1, WriteObject o2) {
			Integer index1 = o1.index;
			Integer index2 = o2.index;
			int comp = index1.compareTo(index2);
			return comp;
		}
	};

	@Override
	public int compareTo(WriteObject o) {
		int indexComp = INDEX_COMPARATOR.compare(this, o);
		if(indexComp == 0) {
			return this.query.compareTo(o.query);
		}
		return indexComp;
	}
	
}
