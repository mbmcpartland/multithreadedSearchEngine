import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * This class is used to write to the JSON
 * file. I also put Sophie's indent function
 * in this class.
 * @author mitchellmcpartland
 * Proj2
 */
public class JSONWriter {

	/**
	 * This function takes in my WordIndex and
	 * the output path to write to. I use three
	 * iterators for the two TreeMaps and the
	 * nested TreeSet. This function doesn't
	 * return anything.
	 * @param WordIndex, Path
	 */

	public static void writeJSON(InvertedIndex index, Path path) throws IOException {
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write("{");
			writer.write(System.lineSeparator());
			@SuppressWarnings("unchecked")
			Iterator<TreeMap<String, TreeMap<String, TreeSet<Integer>>>> it1 = index.getIterator();
			while(it1.hasNext()) {
				@SuppressWarnings("unchecked")
				Entry<String, TreeMap<String, TreeSet<Integer>>> entry = (Entry<String, TreeMap<String, TreeSet<Integer>>>) it1.next();
				String word = (String) entry.getKey();
				writer.write(indent(1));
				writer.write("\"" + word + "\": {");
				writer.write(System.lineSeparator());
				TreeMap<String, TreeSet<Integer>> nestedMap = (TreeMap<String, TreeSet<Integer>>) entry.getValue();
				Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
				while(it2.hasNext()) {
					Entry<String, TreeSet<Integer>> entry2 = it2.next();
					String file = entry2.getKey().toString();
					TreeSet<Integer> positions = entry2.getValue();
					writer.write(indent(2));
					writer.write("\"" + file + "\": [");
					writer.write(System.lineSeparator());
					writer.write(indent(3));
					Iterator<Integer> it3 = positions.iterator();
					while(it3.hasNext()) {
						writer.write(it3.next().toString());
						if(it3.hasNext()) {
							writer.write(",");
							writer.write(System.lineSeparator());
						}
						writer.write(indent(3));
					}
					writer.write(System.lineSeparator());
					writer.write(indent(2));
					writer.write("]");
					if(it2.hasNext()) {
						writer.write(",");
						writer.write(System.lineSeparator());
					}
				}
				writer.write(System.lineSeparator());
				writer.write(indent(1));
				writer.write("}");
				if(it1.hasNext()) {
					writer.write(",");
				}
				writer.write(System.lineSeparator());
			}
			writer.write("}");
			writer.flush();
		}
	}
	
	public static void writeResults(SearchResults results, Path path) throws IOException {
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write("[");
			writer.write(System.lineSeparator());
			Iterator<TreeMap<String, HashMap<String, TreeSet<Integer>>>> it1 = results.getIterator();
			while(it1.hasNext()) {
				writer.write(indent(1));
				writer.write("{");
				writer.write(System.lineSeparator());
				writer.write(indent(2));
				@SuppressWarnings("unchecked")
				Entry<String, HashMap<String, TreeSet<Integer>>> entry = (Entry<String, HashMap<String, TreeSet<Integer>>>) it1.next();
				String query = entry.getKey().toString();
				writer.write("\"queries\": " + "\"" + query + "\",");
				writer.write(System.lineSeparator());
				writer.write(indent(2));
				writer.write("\"results\": [");
				writer.write(System.lineSeparator());
				ArrayList<WriteObject> resultObjects = SearchResults.getWriteObj(entry);
				Collections.sort(resultObjects, WriteObject.COUNT_COMPARATOR);
				int i = 0;
				for(WriteObject obj : resultObjects) {
					i++;
					writer.write(indent(3));
					writer.write("{");
					writer.write(System.lineSeparator());
					writer.write(indent(4));
					writer.write("\"where\": \"" + obj.getQuery() + "\",");
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
				writer.write(indent(2));
				writer.write("]");
				writer.write(System.lineSeparator());
				writer.write(indent(1));
				writer.write("}");
				if(it1.hasNext()) {
					writer.write(",");
				}
				writer.write(System.lineSeparator());
			}	
			writer.write("]");
			writer.flush();
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
