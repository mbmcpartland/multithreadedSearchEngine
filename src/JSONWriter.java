import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * This class is used to write to the JSON
 * file. I also put Sophie's indent function
 * in this class.
 * @author mitchellmcpartland
 */
public class JSONWriter {

	/**
	 * Used to write to the output JSON file.
	 * 
	 * @param InvertedIndex to read
	 * @param output Path to write the JSON file to
	 */
	public static void writeJSON(TreeMap<String, TreeMap<String, TreeSet<Integer>>> index, Path path) throws IOException {
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write("{");
			writer.write(System.lineSeparator());
			Iterator<Entry<String, TreeMap<String, TreeSet<Integer>>>> it = index.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, TreeMap<String, TreeSet<Integer>>> entry = (Entry<String, TreeMap<String, TreeSet<Integer>>>) it.next();
				String word = (String) entry.getKey();
				writer.write(indent(1));
				writer.write("\"" + word + "\": {");
				writer.write(System.lineSeparator());
				TreeMap<String, TreeSet<Integer>> nestedMap = (TreeMap<String, TreeSet<Integer>>) entry.getValue();
				nestedMapWriter(nestedMap, writer);
				writer.write(System.lineSeparator());
				writer.write(indent(1));
				writer.write("}");
				if(it.hasNext()) {
					writer.write(",");
				}
				writer.write(System.lineSeparator());
			}
			writer.write("}");
			writer.flush();
		}
	}
	
	/**
	 * Used to write information from the nested map
	 * to the output JSON file.
	 * 
	 * @param TreeMap that maps a String to a TreeSet
	 *        of Integers; the nested map in my
	 *        InvertedIndex
	 * @param BufferedWriter to write to the output file
	 */
	public static void nestedMapWriter(TreeMap<String, TreeSet<Integer>> nestedMap, BufferedWriter writer) throws IOException {
		Iterator<Entry<String, TreeSet<Integer>>> it = nestedMap.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, TreeSet<Integer>> entry2 = it.next();
			String file = entry2.getKey().toString();
			TreeSet<Integer> positions = entry2.getValue();
			writer.write(indent(2));
			writer.write("\"" + file + "\": [");
			writer.write(System.lineSeparator());
			writer.write(indent(3));
			nestedSetWriter(positions, writer);
			writer.write(System.lineSeparator());
			writer.write(indent(2));
			writer.write("]");
			if(it.hasNext()) {
				writer.write(",");
				writer.write(System.lineSeparator());
			}
		}
	}
	
	/**
	 * Used to write the positions for a given path.
	 * 
	 * @param TreeSet of Integers that will be written
	 * @param BufferedWriter to write to the output file
	 */
	public static void nestedSetWriter(TreeSet<Integer> positions, BufferedWriter writer) throws IOException {
		Iterator<Integer> it = positions.iterator();
		while(it.hasNext()) {
			writer.write(it.next().toString());
			if(it.hasNext()) {
				writer.write(",");
				writer.write(System.lineSeparator());
			}
			writer.write(indent(3));
		}
	}
	
	/**
	 * Used to write the search results to the
	 * provided output path.
	 * 
	 * @param QueryHelper object that contains the
	 *        results of searching for the query
	 * @param output path to write to
	 */
	public static void writeResults(TreeMap<String, ArrayList<SearchResult>> results, Path path) throws IOException {
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write("[");
			writer.write(System.lineSeparator());
			Iterator<Entry<String, ArrayList<SearchResult>>> iterator = results.entrySet().iterator();
			while(iterator.hasNext()) {
				writer.write(indent(1));
				writer.write("{");
				writer.write(System.lineSeparator());
				writer.write(indent(2));
				Entry<String, ArrayList<SearchResult>> entry = iterator.next();
				String query = entry.getKey().toString();
				writer.write("\"queries\": " + "\"" + query + "\",");
				writer.write(System.lineSeparator());
				writer.write(indent(2));
				writer.write("\"results\": [");
				writer.write(System.lineSeparator());
				ArrayList<SearchResult> resultObjects = entry.getValue();
				writeAdditional(resultObjects, writer);
				writer.write(indent(2));
				writer.write("]");
				writer.write(System.lineSeparator());
				writer.write(indent(1));
				writer.write("}");
				if(iterator.hasNext()) {
					writer.write(",");
				}
				writer.write(System.lineSeparator());
			}	
			writer.write("]");
			writer.flush();
		}
				
	}
	
	/**
	 * Used to iterate through the ArrayList of
	 * SearchResults, and then outputting them
	 * to the JSON file using the BufferedWriter.
	 * 
	 * @param ArrayList of SearchResults
	 * @param BufferedWriter for writing results
	 */
	public static void writeAdditional(ArrayList<SearchResult> resultObjects, BufferedWriter writer) throws IOException {
		int i = 0;
		for(SearchResult obj : resultObjects) {
			i++;
			writer.write(indent(3));
			writer.write("{");
			writer.write(System.lineSeparator());
			writer.write(indent(4));
			writer.write("\"where\": \"" + obj.getPath() + "\",");
			writer.write(System.lineSeparator());
			writer.write(indent(4));
			writer.write("\"count\": " + obj.getCount() + ",");
			writer.write(System.lineSeparator());
			writer.write(indent(4));
			writer.write("\"index\": " + obj.getIndex());
			writer.write(System.lineSeparator());
			writer.write(indent(3));
			writer.write("}");
			if(i < resultObjects.size()) {
				writer.write(",");
			}
			writer.write(System.lineSeparator());
		}
	}
	
	/**
	 * This function was provided by Sophie
	 * from the JSONWriter homework assignment.
	 * It simply returns a String representation
	 * of a certain number of indents.
	 * @param int (for the number of indents)
	 */
	public static String indent(int times) {
		char[] tabs = new char[times];
		Arrays.fill(tabs, '\t');
		return String.valueOf(tabs);
	}
}