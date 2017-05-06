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
	public static void main(String[] args) throws IOException { // TODO Remove throws IOException 
		ArgumentMap map = new ArgumentMap(args);
		InvertedIndex index = null;
		MultithreadedInvertedIndex multIndex = null;
		QueryHelperInterface query = null;
		int numThreads = 0;
		
		if(map.hasFlag("-threads")) {
			multIndex = new MultithreadedInvertedIndex();
			// TODO index = multIndex;
			
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
			query = new MultQueryHelper(multIndex, numThreads);
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
			
			if(!(map.hasFlag("-threads"))) { // TODO Test if multIndex != null;
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
				index.toJSON(Paths.get(map.getString("-index", "index.json"))); // TODO Keep this, remove the rest
			}
		}
		
		if(map.hasFlag("-query")) {
			if(map.getString("-query") != null) {
				try {
					// TODO Do not need the if/else anymore, they are doing the same thing!
					if(map.hasFlag("-threads")) { 
						query.parseQueries(Paths.get(map.getString("-query")), map.hasFlag("-exact"));
					} else {
						query.parseQueries(Paths.get(map.getString("-query")), map.hasFlag("-exact"));
					}
				} catch (IOException e) {
					System.out.println("Unable to read the Query file");
				}
			}
		}
		
		if(map.hasFlag("-results")) {
			String resultsPath = map.getString("-results", "results.json");
			if(map.hasFlag("-threads")) {  // TODO Remove
				query.toJSON(Paths.get(resultsPath)); // TODO Remove
			} else { // TODO Remove
				query.toJSON(Paths.get(resultsPath)); // TODO Keep
			}
		}
	}
}