import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This class simply traverses directories and it
 * gets the file names for html files.
 * 
 */
public class DirectoryTraverser {
	
	/**
	 * Recursive method that traverses a directory and returns
	 * an ArrayList of html files.
	 * 
	 * @param empty ArrayList of Paths that will be filled and returned
	 * @param directory that will be traversed
	 */	
	public static void getFileNames(ArrayList<Path> files, Path directory) {
		if(Files.isDirectory(directory)) {
			try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
				for(Path path : stream) {
					getFileNames(files, path);
				}
			} catch(IOException e) {
				System.out.println("The directory is invalid");
			}
		}
		else if(isHTML(directory)) {
			files.add(directory);
		}
	}
	
	/**
	 * This simple function is used to check if a path
	 * file is an html file or an htm file.
	 * @param Path of the file to be checked
	 */		
	public static boolean isHTML(Path path) {
		String filename = path.toString().toLowerCase();
		return filename.endsWith(".html") || filename.endsWith(".htm");
	}
}