import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
	 * This function was provided by Sophie
	 * from the JSONWriter homework assignment.
	 * It simply returns a String representation
	 * of a certain number of indents.
	 * 
	 * @param number of indents that will be
	 *        returned in a String
	 */
	
	public static String indent(int times) {
		char[] tabs = new char[times];
		Arrays.fill(tabs, '\t');
		return String.valueOf(tabs);
	}
}
