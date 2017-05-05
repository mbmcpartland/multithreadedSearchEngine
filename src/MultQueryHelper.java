import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Stores a TreeMap that matches queries to an ArrayList
 * of SearchResults. This class reads queries, calls
 * appropriate search methods, and contains the toJSON
 * method to output the search results.
 * 
 * @author mitchellmcpartland
 */
public class MultQueryHelper implements QueryHelperInterface {
	
	public static final Logger log = LogManager.getLogger();
	
	private final TreeMap<String, ArrayList<SearchResult>> results;
	private final MultithreadedInvertedIndex index;
	private final int numThreads;
	
	/**
	 * Initializes the QueryHelper.
	 */
	public MultQueryHelper(MultithreadedInvertedIndex index, int threads) {
		this.results = new TreeMap<>();
		this.index = index;
		this.numThreads = threads;
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
		WorkQueue queue = new WorkQueue(this.numThreads);
		
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			for(String line = reader.readLine(); line != null ; line = reader.readLine()) {
				String[] words = WordParser.parseWords(line);
				Arrays.sort(words);
				line = String.join(" ", words);
				queue.execute(new SearchWorker(line, words, exact));	
			}	
		}
		queue.finish();
		queue.shutdown();
	}
	
	/**
	 * Outputs the search results to the provided
	 * output Path. 
	 * 
	 * @param the path that will be output to
	 */
	public void toJSON(Path path) {
		try {
			synchronized(this.results) {
				JSONWriter.writeResults(this.results, path);
			}
		} catch (IOException e) {
			System.out.println("Unable to write the JSON file to the output path");
		}
	}
	
	/**
	 * Private SearchWorker class that implements the
	 * Runnable interface. Each SearchWorker
	 * performs a search for a query.
	 */
	private class SearchWorker implements Runnable {
		private String query;
		private String[] words;
		private boolean exact;
		
		/**
		 * Constructor for the SearchWorker,
		 * or should I say.....minion!
		 */
		public SearchWorker(String query, String[] words, boolean exact) {
			this.query = query;
			this.exact = exact;
			this.words = words;
		}
		
		/**
		 * Run method that first makes sure that
		 * the length of the query is longer than
		 * 0, then it performs a search that is
		 * NOT synchronized because a search is
		 * just a read. Then puts the results in
		 * the SearchResults in the synchronized block.
		 */
		public void run() {
			if(this.query.length() >= 1) {
				ArrayList<SearchResult> local = index.search(words, exact);

				synchronized(results) {
					results.put(this.query, local);
				}
			}
		}
	}
}