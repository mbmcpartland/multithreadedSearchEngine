import java.io.IOException;
import java.nio.file.Path;

/**
 * Stores a TreeMap that matches queries to an ArrayList
 * of SearchResults. This class reads queries, calls
 * appropriate search methods, and contains the toJSON
 * method to output the search results.
 * 
 * @author mitchellmcpartland
 */
public interface QueryHelperInterface {
	
	/**
	 * Reads the query file and then calls the appropriate
	 * search method.
	 * 
	 * @param the path that contains the queries
	 * @param boolean indicating whether a partial
	 * 		  or exact search is to be performed
	 */
	public void parseQueries(Path path, boolean exact) throws IOException;
	
	/**
	 * Outputs the search results to the provided
	 * output Path. 
	 * 
	 * @param the path that will be output to
	 */
	public void toJSON(Path path);
}
