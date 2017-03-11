import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

// TODO Capitalize properly, and think of class names as job titles
// TODO InvertedIndexBuilder

/**
 * This class is used to read the input files
 * and then to build the WordIndex. My first
 * readAndBuildIndex function is used when the
 * input path is a directory of html files. The
 * second one is used for just one html file.
 * @author mitchellmcpartland
 */
public class readAndBuild {
	
	/**
	 * This function first iterates through the Paths
	 * of the html files, and it uses a StringBuilder
	 * object to add the whole file.  I then call
	 * my stripHTML function from HTMLCleaner to
	 * remove all of the HTML code. I then use Sophie's
	 * parseWords function from WordParser to get a
	 * String array of all of the words in the files.
	 * I then call my buildIndex function, which is in
	 * this class.
	 * @param ArrayList<Path>, WordIndex
	 */
	// TODO buildFromHTML()
	public static void readAndBuildIndex(ArrayList<Path> htmlFiles, WordIndex index) throws IOException {
		for(Path path : htmlFiles) {
			// TODO reduce repeated code and call your readAndBuildIndex()
			StringBuilder html = new StringBuilder();
			try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
					html.append(x);
					html.append("\n");
				}
				String no_html = HTMLCleaner.stripHTML(html.toString());
				String[] the_words = WordParser.parseWords(no_html);
				buildIndex(path.toString(), index, the_words);
			}
		}
	}
	
	/**
	 * This function uses a StringBuilder object to
	 * add all of the text in the input file.  I then call
	 * my stripHTML function from HTMLCleaner to
	 * remove all of the HTML code. I then use Sophie's
	 * parseWords function from WordParser to get a
	 * String array of all of the words in the file.
	 * I then call my buildIndex function, which is in
	 * this class.
	 * @param Path, WordIndex
	 */
	
	public static void readAndBuildIndex(Path inputFile, WordIndex index) throws IOException {
		StringBuilder html = new StringBuilder();
		try(BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
			for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
				html.append(x);
				html.append("\n");
			}
			String no_html = HTMLCleaner.stripHTML(html.toString());
			String[] the_words = WordParser.parseWords(no_html);
			buildIndex(inputFile.toString(), index, the_words);
		}
	}
	
	// TODO Make this an addAll(String[] words, String path) in your inverted index class
	/**
	 * This function takes in a String representation of
	 * the path. It also takes in the WordIndex that needs
	 * to be added to, and it takes in the String array of
	 * words that need to be added to the index.  I use a
	 * very simple enhanced for-loop to iterate over the
	 * words, and I call my index.add() function to add to
	 * the index. 
	 * @param String, WordIndex, String[]
	 */
	public static void buildIndex(String path, WordIndex index, String[] words) {
		int m = 1;
		for(String word : words) {
			index.add(word, m, path);
			m++;
		}
	}
}
