import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This class is used to read the input files
 * and then to build the WordIndex.
 * @author mitchellmcpartland
 */
public class InvertedIndexBuilder {
	
	/**
	 * Iterates through the ArrayList of input files
	 * and calls buildFromHTML, passing in the path and
	 * the index in each iteration.
	 * 
	 * @param the ArrayList of htmlFiles to iterate through
	 * @param the InvertedIndex that is being added to
	 */
	public static void buildFromHTML(ArrayList<Path> htmlFiles, InvertedIndex index) throws IOException {
		for(Path path : htmlFiles) {
			buildFromHTML(path, index);
		}
	}
	
	/**
	 * Uses Java 8 NIO to read the input file; uses a
	 * StringBuilder to store the input file; and then
	 * calls stripHTML to remove the html code. The
	 * method parseWords is then called, and after,
	 * buildIndex is called to add the words to the index.
	 * 
	 * @param the InvertedIndex that is being added to
	 * @param the input file that will be read and stripped
	 *        of any html code
	 */
	public static void buildFromHTML(Path inputFile, InvertedIndex index) throws IOException {
		StringBuilder html = new StringBuilder();
		try(BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
			for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
				html.append(x);
				html.append("\n");
			}
			String no_html = HTMLCleaner.stripHTML(html.toString());
			String[] the_words = WordParser.parseWords(no_html);
			index.addAll(the_words, inputFile.toString());
		}
	}
}