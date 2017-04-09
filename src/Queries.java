import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to form all of the
 * queries that are in the query file.
 * 
 * @author mitchellmcpartland
 */
public class Queries {
	
	private final ArrayList<String> queries;
	
	/**
	 * Constructor for the Queries object
	 * 
	 */
	public Queries() {
		this.queries = new ArrayList<String>();
	}
	
	/**
	 * Used to properly create and add the
	 * necessary queries to the Queries
	 * object.
	 * 
	 * @param all of the queries that will
	 *        be parsed and added
	 */
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
	
	/**
	 * Helper function used to form a query
	 * in the proper format.
	 * 
	 * @param array of words from the query
	 */
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
	
	/**
	 * Overridden toString method.
	 * 
	 */
	@Override
	public String toString() {
		String test = "";
		for(String word : this.queries) {
			test += word;
			test += '\n';
		}
		return test;
	}
	
	/**
	 * Function used to read the the file that
	 * contains queries and properly form a
	 * StringBuilder. 
	 * 
	 * @param path of the query file that needs
	 *        to be read
	 */
	public void readQueryFile(Path path) throws IOException {
		StringBuilder file = new StringBuilder();
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
				file.append(x);
				file.append("\n");
			}
			this.addQuery(file);
		}
	}
	
	/**
	 * Used to iterate through the queries and
	 * then calls QueryFinder in my InvertedIndex
	 * class.
	 * 
	 * @param the current InvertedIndex to be searched
	 * @param SearchResults object that stores the results
	 * @param boolean declaring whether a partial or exact
	 *        search will be performed
	 */
	public void Finder(InvertedIndex index, SearchResults results, boolean partial) {
		Iterator<String> it = this.queries.iterator();
		while(it.hasNext()) {
			String[] words = WordParser.parseWords(it.next());
			index.QueryFinder(results, words, partial);
		}
	}
}
