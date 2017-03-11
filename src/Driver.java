import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO Address warnings

/**
 * This is my Driver class that does argument-handling
 * and calls all appropriate methods to create the
 * inverted index and write to the JSON file.
 * @author mitchellmcpartland
 */
public class Driver { 	
	
	/**
	 * I use ArgumentMap to make argument-handling easier. I create
	 * a new WordIndex object, and then I do a couple of checks
	 * for the input path.  If the input path passes the tests,
	 * then I use my DirectoryTraverser class to check if the
	 * input path is an html file or a directory.  If it is a
	 * directory, then I call my getFileNames function from
	 * DirectoryTraverser to get an ArrayList of paths. I then
	 * call my readAndBuildIndex function from my readAndBuild
	 * class to build the index.  Once the index has been created,
	 * I use my ArgumentMap again to see if the "-index" flag was
	 * entered.  If it was, then I check if a output path was
	 * provided.  I then simply call my writeJSON function from
	 * my JSONWriter class to write to the output file.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO No stack traces or throwing in main, always output user-friendly error messages
		
		ArgumentMap map = new ArgumentMap(args);
		WordIndex index = new WordIndex();
		
		if(map.hasFlag("-path")) {
			String path = map.getString("-path");
			/*
			try {
				ArrayList<Path> htmlFiles = ..
				readandBuildIndex()
			}
			catch () {
				Unable to build the index from path + path
			}
			*/
			
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
					readAndBuild.readAndBuildIndex(htmlFiles, index);
				} catch (IOException e) {
					e.printStackTrace();
					throw new IllegalArgumentException("Read Error");
				}
			} else {
				try {
					readAndBuild.readAndBuildIndex(inputPath, index);
				} catch (IOException e) {
					e.printStackTrace();
					throw new IllegalArgumentException("Read Error");
				}
			}
		}
		
		if(map.hasFlag("-index")) {
			String outputPath = map.getString("-index");
			if(outputPath == null) {
				try {
					JSONWriter.writeJSON(index, Paths.get("index.json"));
				} catch (IOException e) {
					e.printStackTrace();
					throw new IllegalArgumentException("Write Error");
				}
			} else {
				try {
					JSONWriter.writeJSON(index, Paths.get(outputPath));
				} catch (IOException e) {
					e.printStackTrace();
					throw new IllegalArgumentException("Write Error");
				}
			}
		}
	}
}