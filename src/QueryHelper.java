import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

// TODO @Override

/**
 * Stores a TreeMap that matches queries to an ArrayList
 * of SearchResults. This class reads queries, calls
 * appropriate search methods, and contains the toJSON
 * method to output the search results.
 * 
 * @author mitchellmcpartland
 */
public class QueryHelper implements QueryHelperInterface {
	
	private final TreeMap<String, ArrayList<SearchResult>> results;
	private final InvertedIndex index;
	
	/**
	 * Initializes the QueryHelper.
	 */
	public QueryHelper(InvertedIndex index) {
		this.results = new TreeMap<>();
		this.index = index;
	}
	
	/**
	 * Reads the query file and then calls the appropriate
	 * search method in my InvertedIndex class.
	 * 
	 * @param the path that contains the queries
	 * @param boolean indicating whether a partial
	 * 		  or exact search is to be performed
	 */
	public void parseQueries(Path path, boolean exact) throws IOException {
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			for(String line = reader.readLine(); line != null ; line = reader.readLine()) {
				String[] words = WordParser.parseWords(line);
				Arrays.sort(words);
				line = String.join(" ", words);
				if(line.length() >= 1) {
					this.results.put(line, index.search(words, exact));
				}
			}
			
		}
	}
	
	/**
	 * Outputs the search results to the provided
	 * output Path. 
	 * 
	 * @param the path that will be output to
	 */
	public void toJSON(Path path) {
		try {
			JSONWriter.writeResults(this.results, path);
		} catch (IOException e) {
			System.out.println("Unable to write the JSON file to the output path");
		}
	
	}
}