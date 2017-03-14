import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Queries {
	
	private final ArrayList<String> queries;
	
	public Queries() {
		this.queries = new ArrayList<String>();
	}
	
	public void addQuery(StringBuilder queries) {
		StringBuilder temp = new StringBuilder();
		for(int i = 0 ; i < queries.length() ; i++) {
			if(queries.charAt(i) != '\n') {
				temp.append(queries.charAt(i));
			} else {
				String query = temp.toString();
				String[] words = WordParser.parseWords(query);
				if(words.length == 1) {
					this.queries.add(words[0]);
				} else {
					String totalQuery = formQuery(words);
					this.queries.add(totalQuery);
				}
				temp.delete(0, temp.length());
			}
		}
	}
	
	public static String formQuery(String[] words) {
		StringBuilder theString = new StringBuilder();
		List<String> list = Arrays.asList(words);
		Collections.sort(list);
		for(String word : list) {
			theString.append(word);
			theString.append(" ");
		}
		String total = theString.toString();
		return total;
	}
	
	public Iterator getIterator() {
		Iterator<String> it = this.queries.iterator();
		return it;
	}
	
	@Override
	public String toString() {
		String test = "";
		for(String word : this.queries) {
			test += word;
			test += '\n';
		}
		return test;
	}
	
}
