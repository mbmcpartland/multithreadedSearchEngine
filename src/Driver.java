import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO Clean up old TODO comments in your code! There are at least 3.

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
		InvertedIndex index = new InvertedIndex();
		
		if(map.hasFlag("-path")) {
			String path = map.getString("-path");
			
			if(path == null) {
				System.out.println("No path was provided");
				return;
			}
			
			Path inputPath = Paths.get(path);
			ArrayList<Path> htmlFiles = new ArrayList<Path>();
			DirectoryTraverser.getFileNames(htmlFiles, inputPath); // Start from here
			
			// TODO Fix TODOs in Builder, update here. Not expecting two different buildFromHTML calls here.
			try {
				InvertedIndexBuilder.buildFromHTML(htmlFiles, index);
			} catch (IOException e) {
				System.out.println("Unable to build the index from the provided path");
			}
			try {
				InvertedIndexBuilder.buildFromHTML(inputPath, index);
			} catch (IOException e) {
				System.out.println("Unable to build the index from the provided path");
			}
		}
		
		if(map.hasFlag("-index")) {
			String outputPath = map.getString("-index", "index.json");
			index.toJSON(Paths.get(outputPath));
	  } // TODO Fix indentation
	}
}
