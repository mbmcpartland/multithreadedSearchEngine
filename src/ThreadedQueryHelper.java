import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Stores a TreeMap that matches queries to an ArrayList
 * of SearchResults. This class reads queries, calls
 * appropriate search methods, and contains the toJSON
 * method to output the search results.
 * 
 * @author mitchellmcpartland
 */
public class ThreadedQueryHelper implements QueryHelperInterface {
	
	private final TreeMap<String, ArrayList<SearchResult>> results;
	private final MultithreadedInvertedIndex index;
	private final int numThreads;
	
	/**
	 * Initializes the QueryHelper.
	 */
	public ThreadedQueryHelper(MultithreadedInvertedIndex index, int threads) {
		this.results = new TreeMap<>();
		this.index = index;
		this.numThreads = threads;
	}
	
	@Override
	public void parseQueries(Path path, boolean exact) throws IOException {
		WorkQueue queue = new WorkQueue(this.numThreads);
		
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			for(String line = reader.readLine(); line != null ; line = reader.readLine()) {
				queue.execute(new SearchWorker(line, exact));	
			}	
		}
		queue.finish();
		queue.shutdown();
	}
	
	public void searchOneQuery(String query, boolean exact) throws IOException {
		String[] words = WordParser.parseWords(query);
		Arrays.sort(words);
		query = String.join(" ", words);
		
		if(query.length() >= 1) {
			ArrayList<SearchResult> local = index.search(words, exact);
			results.put(query, local);
		}
	}
	
	public ArrayList<String> returnLinks() {
		ArrayList<String> links = new ArrayList<String>();
		Iterator<Entry<String, ArrayList<SearchResult>>> iterator = this.results.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, ArrayList<SearchResult>> entry = iterator.next();
			ArrayList<SearchResult> resultObjects = entry.getValue();
			for(SearchResult obj : resultObjects) {
				links.add(obj.getPath());
			}
		}
		return links;
	}
	
	@Override
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
		private boolean exact;
		
		/**
		 * Constructor for the SearchWorker,
		 * or should I say.....minion!
		 */
		public SearchWorker(String query, boolean exact) {
			this.query = query;
			this.exact = exact;
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
			String[] words = WordParser.parseWords(this.query);
			Arrays.sort(words);
			this.query = String.join(" ", words);
			
			if(this.query.length() >= 1) {
				ArrayList<SearchResult> local = index.search(words, exact);

				synchronized(results) {
					results.put(this.query, local);
				}
			}
		}
	}
}