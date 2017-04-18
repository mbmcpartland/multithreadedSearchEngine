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
public class MultQueryHelper {
	
	public static final Logger log = LogManager.getLogger();
	
	private TreeMap<String, ArrayList<SearchResult>> results;
	private final MultithreadedInvertedIndex index;
	
	/**
	 * Initializes the QueryHelper.
	 */
	public MultQueryHelper(MultithreadedInvertedIndex index) {
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
	public void parseQueries(Path path, boolean exact, int threads) throws IOException {
		WorkQueue queue = new WorkQueue(threads);
		
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			for(String line = reader.readLine(); line != null ; line = reader.readLine()) {
				String[] words = WordParser.parseWords(line);
				Arrays.sort(words);
				line = String.join(" ", words);
				if(line.length() >= 1) {
					queue.execute(new SearchWorker(line, this.results, this.index, words, exact));	
				}
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
			JSONWriter.writeResults(this.results, path);
		} catch (IOException e) {
			System.out.println("Unable to write the JSON file to the output path");
		}
	}
	
	private static class SearchWorker implements Runnable {
		private String query;
		private TreeMap<String, ArrayList<SearchResult>> results;
		private MultithreadedInvertedIndex index;
		private String[] words;
		private boolean exact;
		
		public SearchWorker(String query, TreeMap<String, 
				ArrayList<SearchResult>> results, MultithreadedInvertedIndex index, 
				String[] words, boolean exact) {
			this.query = query;
			this.results = results;
			this.index = index;
			this.exact = exact;
			this.words = words;
		}
		
		@Override
		public void run() {
			synchronized(results) {
				results.put(this.query, index.search(words, exact));
			}
		}
	}
}
