import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.text.html.HTMLDocument.Iterator;

public class the_index {
	
	private static Map<String, ArrayList<Map<String, ArrayList<Integer>>>> index;
	
	public the_index() {
		this.index = new TreeMap();
	}
//-----------------------------------------------------------------------------------------------//
// Method: add
// Description: This method is used to either a) add a new word to my index,
//				b) if the word already exists, then add a new file mapping,
//				or c) if the file mapping already exists, then add a new
//				position to the index for the corresponding word and file.
//-----------------------------------------------------------------------------------------------//	
	public void add(String word, int position, String path) {
		if(!(this.index.containsKey(word))) {
			ArrayList<Map<String, ArrayList<Integer>>> the_list = new ArrayList();
			ArrayList<Integer> pos_list = new ArrayList();
			pos_list.add(position);
			LinkedHashMap<String, ArrayList<Integer>> the_map = new LinkedHashMap();
			the_map.put(path, pos_list);
			the_list.add(the_map);
			this.index.put(word, the_list);
		} else {
			ArrayList<Map<String, ArrayList<Integer>>> the_list = this.index.get(word);
			int exists = 0;
			for(Map<String, ArrayList<Integer>> map : the_list) {
				if(map.containsKey(path)) {
					exists = 1;
					ArrayList<Integer> the_pos_list = map.get(path);
					the_pos_list.add(position);
					map.put(path, the_pos_list);
					int index = the_list.indexOf(map);
					the_list.add(index, map);
					this.index.put(word, the_list);
					break;
				}
			}
			if(exists == 0) {
				Map<String, ArrayList<Integer>> new_map = new LinkedHashMap();
				ArrayList<Integer> pos_list = new ArrayList();
				pos_list.add(position);
				new_map.put(path, pos_list);
				the_list.add(new_map);
				this.index.put(word, the_list);
			}
		}
	}
//-----------------------------------------------------------------------------------------------//
// Method: clean_index
// Description: This method is used to remove duplicates from my
//				index structure, while preserving the original
//				order in which the files were read.
//-----------------------------------------------------------------------------------------------//	
	public static void clean_index() {
		Set<Map<String, ArrayList<Integer>>> the_set = new LinkedHashSet();
		for(Map.Entry<String, ArrayList<Map<String, ArrayList<Integer>>>> entry : index.entrySet()) {
			ArrayList<Map<String, ArrayList<Integer>>> the_list = entry.getValue();
			the_set.addAll(the_list);
			the_list.clear();
			the_list.addAll(the_set);
			the_set.clear();
		}
	}

//-----------------------------------------------------------------------------------------------//
// Method: writeJSON	
// Description: This complex method makes use of multiple iterators
//				so that I can iterate through my complex index
//				structure.  I had to use iterators so that I know
//				when to write a comma.
//-----------------------------------------------------------------------------------------------//	
	public static void writeJSON(Path path) throws IOException {
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write("{");
			writer.write(System.lineSeparator());
			Set entrySet1 = index.entrySet();
			java.util.Iterator iterator = entrySet1.iterator();
			while(iterator.hasNext()) {
				Entry first_entry = (Entry) iterator.next();
				String word = (String) first_entry.getKey();
				writer.write(indent(1));
				writer.write("\"" + word + "\": {");
				writer.write(System.lineSeparator());
				ArrayList<Map<String, ArrayList<Integer>>> the_list = (ArrayList<Map<String, ArrayList<Integer>>>) first_entry.getValue();
				java.util.Iterator<Map<String, ArrayList<Integer>>> itr = the_list.iterator();
				while(itr.hasNext()) {
					Map<String, ArrayList<Integer>> the_map = itr.next();
					Set entrySet = the_map.entrySet();
					java.util.Iterator itr2 = entrySet.iterator();
					while(itr2.hasNext()) {
						Entry the_entry = (Entry) itr2.next();
						Object file = the_entry.getKey();
						ArrayList<Integer> value = (ArrayList<Integer>) the_entry.getValue();
						writer.write(indent(2));
						writer.write("\"" + file + "\": [");
						writer.write(System.lineSeparator());
						writer.write(indent(3));
						java.util.Iterator<Integer> itr3 = value.iterator();
						while(itr3.hasNext()) {
							writer.write(itr3.next().toString());
							if(itr3.hasNext()) {
								writer.write(",");
								writer.write(System.lineSeparator());
							}
							writer.write(indent(3));
						}
						writer.write(System.lineSeparator());
						writer.write(indent(2));
						writer.write("]");
						if(itr.hasNext()) {
							writer.write(",");
							writer.write(System.lineSeparator());
						}
					}
				}
				writer.write(System.lineSeparator());
				writer.write(indent(1));
				writer.write("}");
				if(iterator.hasNext()) {
					writer.write(",");
				}
				writer.write(System.lineSeparator());
			}
			writer.write("}");
			writer.flush();
		}
	}
//-----------------------------------------------------------------------------------------------//
// Method: indent	
// Description: This method was provided by Sophie and it just
//				returns a String of indents (or just one indent).
//-----------------------------------------------------------------------------------------------//	
	public static String indent(int times) {
		char[] tabs = new char[times];
		Arrays.fill(tabs, '\t');
		return String.valueOf(tabs);
	}
}
