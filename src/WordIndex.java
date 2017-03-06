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

public class WordIndex {
	
	// TODO Avoid making members static, instead make this final
	private static Map<String, ArrayList<Map<String, ArrayList<Integer>>>> index;
	
	// TODO private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index; 
	
	public WordIndex() {
		this.index = new TreeMap(); // TODO TreeMap<>();
	}
//-----------------------------------------------------------------------------------------------//
// Method: add
// Description: This method is used to either a) add a new word to my index,
//				b) if the word already exists, then add a new file mapping,
//				or c) if the file mapping already exists, then add a new
//				position to the index for the corresponding word and file.
//-----------------------------------------------------------------------------------------------//	
	public void add(String word, int position, String path) {
		/* TODO Try this
		if (word exists in the  map) {
			if (file exists in the inner map) {
				add the new position
			}
			else {
				intialize the inner set
			}
		}
		else {
			initialize the inner map and the inner set
		}
		*/
		
		
		if(!(this.index.containsKey(word))) {
			ArrayList<Map<String, ArrayList<Integer>>> theList = new ArrayList();
			ArrayList<Integer> posList = new ArrayList();
			posList.add(position);
			LinkedHashMap<String, ArrayList<Integer>> theMap = new LinkedHashMap();
			theMap.put(path, posList);
			theList.add(theMap);
			this.index.put(word, theList);
		} else {
			ArrayList<Map<String, ArrayList<Integer>>> theList = this.index.get(word);
			int exists = 0;
			for(Map<String, ArrayList<Integer>> map : theList) {
				if(map.containsKey(path)) {
					exists = 1;
					ArrayList<Integer> thePosList = map.get(path);
					thePosList.add(position);
					map.put(path, thePosList);
					int index = theList.indexOf(map);
					theList.add(index, map);
					this.index.put(word, theList);
					break;
				}
			}
			if(exists == 0) {
				Map<String, ArrayList<Integer>> newMap = new LinkedHashMap();
				ArrayList<Integer> posList = new ArrayList();
				posList.add(position);
				newMap.put(path, posList);
				theList.add(newMap);
				this.index.put(word, theList);
			}
		}
	}
	
//-----------------------------------------------------------------------------------------------//
// Method: clean_index
// Description: This method is used to remove duplicates from my
//				index structure, while preserving the original
//				order in which the files were read.
//-----------------------------------------------------------------------------------------------//	
	public static void cleanIndex() {
		Set<Map<String, ArrayList<Integer>>> theSet = new LinkedHashSet();
		for(Map.Entry<String, ArrayList<Map<String, ArrayList<Integer>>>> entry : index.entrySet()) {
			ArrayList<Map<String, ArrayList<Integer>>> theList = entry.getValue();
			theSet.addAll(theList);
			theList.clear();
			theList.addAll(theSet);
			theSet.clear();
		}
	}

	// TODO Keep this method here... but to make your code more general add back JSONWRiter with this method:
//	public static void asDoubleNestedObject(TreeMap<String, TreeMap<String, TreeSet<Integer>>> index, Path path) {
//		basically the code you already have now
//	}
	
	// keep this method here
//	public static void writeJSON(Path path) {
//		JSONWriter.asDoubleNestedObject(index, path);
//	}
	
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
				Entry firstEntry = (Entry) iterator.next();
				String word = (String) firstEntry.getKey();
				writer.write(indent(1));
				writer.write("\"" + word + "\": {");
				writer.write(System.lineSeparator());
				ArrayList<Map<String, ArrayList<Integer>>> theList = (ArrayList<Map<String, ArrayList<Integer>>>) firstEntry.getValue();
				java.util.Iterator<Map<String, ArrayList<Integer>>> itr = theList.iterator();
				while(itr.hasNext()) {
					Map<String, ArrayList<Integer>> theMap = itr.next();
					Set entrySet = theMap.entrySet();
					java.util.Iterator itr2 = entrySet.iterator();
					while(itr2.hasNext()) {
						Entry theEntry = (Entry) itr2.next();
						Object file = theEntry.getKey();
						ArrayList<Integer> value = (ArrayList<Integer>) theEntry.getValue();
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
