import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
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
		InvertedIndex index = new InvertedIndex();
		
		if(map.hasFlag("-path")) {
			String path = map.getString("-path");
			if(path == null) {
				return;
			}
			Path inputPath = Paths.get(path);
			if(Files.notExists(inputPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}) == true) {
				return;
			}
			if(DirectoryTraverser.isHTML(inputPath) == false) {
				ArrayList<Path> htmlFiles = new ArrayList<Path>();
				htmlFiles = DirectoryTraverser.getFileNames(htmlFiles, inputPath);
				try {
					InvertedIndexBuilder.buildFromHTML(htmlFiles, index);
				} catch (IOException e) {
					System.out.println("Unable to build the index from the provided path");
				}
			} else {
				try {
					InvertedIndexBuilder.buildFromHTML(index, inputPath);
				} catch (IOException e) {
					System.out.println("Unable to build the index from the provided path");
				}
			}
		}
		
		if(map.hasFlag("-index")) {
			String outputPath = map.getString("-index");
			if(outputPath == null) {
				index.toJSON(Paths.get("index.json"));
			} else {
				index.toJSON(Paths.get(outputPath));
			}
		}
	}
}