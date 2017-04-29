import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO Merge project 3 into your master branch.

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is my Driver class that does argument-handling
 * and calls all appropriate methods to create the
 * inverted index and write to the JSON file.
 * @author mitchellmcpartland
 */
public class Driver { 	
	
	public static final Logger log = LogManager.getLogger();
	
	/**
	 * Main method that handles arguments, creates the
	 * InvertedIndex, and writes to the JSON file.
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * TODO
		 * 
		 * ArgumentMap map = ...
		 * 
		 * InvertedIndex index = null;
		 * QueryHelperInterface query = null;
		 * 
		 * 
		 * if (-threads) {
		 * 		MultithreadedInvertedIndex threaded = new Multi...
		 * 		index = threaded;
		 * 		query = new MultithreadedQueryHelper(threaded);
		 * 
		 * }
		 * else {
		 * 		index = new InvertedIndex();
		 * 		query = new QueryHelper(index);
		 * }
		 * 
		 * if (-results) {
		 * 	query.toJSON();
		 * }
		 * 
		 * if (-index) {
		 * 	index.toJSON();
		 * }
		 * 
		 */
		
		
		ArgumentMap map = new ArgumentMap(args);
		InvertedIndex index = new InvertedIndex();
		MultithreadedInvertedIndex multIndex = new MultithreadedInvertedIndex();
		
		int numThreads = 0;
		
		if(map.hasFlag("-threads")) {
			String numTStr = map.getString("-threads");
			try {
				numThreads = Integer.parseInt(numTStr);
				if(numThreads <= 0) {
					return;
				}
			} catch (NumberFormatException e) {
				System.out.println("Enter a real number greater than 1 please");
				return;
			}
		}
		
		if(map.hasFlag("-path")) {
			String path = map.getString("-path");
			
			if(path == null) {
				System.out.println("No path was provided");
				return;
			}
			
			Path inputPath = Paths.get(path);
			ArrayList<Path> htmlFiles = new ArrayList<Path>();
			DirectoryTraverser.getFileNames(htmlFiles, inputPath);
			
			if(!(map.hasFlag("-threads"))) {
				try {
					InvertedIndexBuilder.buildFromHTML(htmlFiles, index);
				} catch (IOException e) {
					System.out.println("Unable to build the index from the provided path");
				}
			} else {
				MultithreadedBuilder.buildFromHTML(htmlFiles, multIndex, numThreads);
			}
		}
		
		if(map.hasFlag("-index")) {
			if(map.hasFlag("-threads")) {
				multIndex.toJSON(Paths.get(map.getString("-index", "index.json")));
			} else {
				index.toJSON(Paths.get(map.getString("-index", "index.json")));
			}
		}
		
		QueryHelper theQueries = null;
		MultQueryHelper multQueries = null;
		if(map.hasFlag("-threads")) {
			multQueries = new MultQueryHelper(multIndex);
		} else {
			theQueries = new QueryHelper(index);
		}
		
		if(map.hasFlag("-query")) {
			if(map.getString("-query") != null) {
				try {
					if(map.hasFlag("-threads")) { 
						multQueries.parseQueries(Paths.get(map.getString("-query")), map.hasFlag("-exact"), numThreads);
					} else {
						theQueries.parseQueries(Paths.get(map.getString("-query")), map.hasFlag("-exact"));
					}
				} catch (IOException e) {
					System.out.println("Unable to read the Query file");
				}
			}
		}
		
		if(map.hasFlag("-results")) {
			String resultsPath = map.getString("-results", "results.json");
			if(map.hasFlag("-threads")) { 
				multQueries.toJSON(Paths.get(resultsPath));
			} else {
				theQueries.toJSON(Paths.get(resultsPath));
			}
		}
	}
}