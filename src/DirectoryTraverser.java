import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;

public class DirectoryTraverser {
	
	/**
	 * This function returns an ArrayList of Path
	 * files that are also html files.
	 * @param ArrayList<Path>, Path
	 * Proj2
	 */
	
	public static ArrayList<Path> getFileNames(ArrayList<Path> files, Path directory) {
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for(Path path : stream) {
				if(path.toFile().isDirectory()) {
					getFileNames(files, path);
				} else {
					if(isHTML(path) == true) {
						files.add(path);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("The directory is invalid");
		}
		return files;
	}
	
	/**
	 * This simple function is used to check if a path
	 * file is an html file or an htm file.
	 * @param Path
	 */
		
	public static boolean isHTML(Path path) {
		if(path.toString().toLowerCase().endsWith(".html") || path.toString().toLowerCase().endsWith(".htm")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This is another simple function that is used
	 * to check if a file exists or not.
	 * @param Path
	 */
	
	public static boolean pathExists(Path inputPath) {
		boolean pathNotExists = Files.notExists(inputPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
		if(pathNotExists == true) {
			return true;
		} else {
			return false;
		}
	}
}
