import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This is my Driver class that does argument-handling
 * and calls all appropriate methods to create the
 * inverted index and write to the JSON file.
 * @author mitchellmcpartland
 */
public class Driver {
	
	/**
	 * Main method that handles arguments, creates the
	 * InvertedIndex, and writes to the JSON file.
	 * @param args
	 */
	public static void main(String[] args) { 
		ArgumentMap map = new ArgumentMap(args);
		InvertedIndex index = null;
		MultithreadedInvertedIndex multIndex = null;
		QueryHelperInterface query = null;
		int numThreads = 0;
		
		if(map.hasFlag("-threads")) {
			multIndex = new MultithreadedInvertedIndex();
			index = multIndex;
			
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
			query = new ThreadedQueryHelper(multIndex, numThreads);
		} else {
			index = new InvertedIndex();
			query = new QueryHelper(index);
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
			if(multIndex == null) {
				try {
					InvertedIndexBuilder.buildFromHTML(htmlFiles, index);
				} catch (IOException e) {
					System.out.println("Unable to build the index from the provided path");
				}
			} else {
				try {
					MultithreadedBuilder.buildFromHTML(htmlFiles, multIndex, numThreads);
				} catch (IOException e) {
					System.out.println("Unable to build the index from the provided path");
				}
			}
		}
		
		if(map.hasFlag("-index")) {
			index.toJSON(Paths.get(map.getString("-index", "index.json")));
		}
		
		if(map.hasFlag("-query")) {
			if(map.getString("-query") != null) {
				try {
					query.parseQueries(Paths.get(map.getString("-query")), map.hasFlag("-exact"));
				} catch (IOException e) {
					System.out.println("Unable to read the Query file");
				}
			}
		}
		
		if(map.hasFlag("-results")) {
			query.toJSON(Paths.get(map.getString("-results", "results.json")));
		}
	}
}