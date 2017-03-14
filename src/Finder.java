import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class Finder {

	public static void FinderP(InvertedIndex index, SearchResults results, Queries queries) {
		Iterator<String> it = queries.getIterator();
		while(it.hasNext()) {
			String[] words = WordParser.parseWords(it.next());
			if(words.length == 1) {
				FinderPartial(index, results, words[0]);
			} else {
				FinderPartial(index, results, words);
			}	
		}
	}
	
	public static void FinderE(InvertedIndex index, SearchResults results, Queries queries) {
		Iterator<String> it = queries.getIterator();
		while(it.hasNext()) {
			String[] words = WordParser.parseWords(it.next());
			if(words.length == 1) {
				FinderExact(index, results, words[0]);
			} else {
				FinderExact(index, results, words);
			}	
		}
	}
	
	public static void FinderPartial(InvertedIndex index, SearchResults results, String searchWord) {
		Iterator<TreeMap<String, TreeMap<String, TreeSet<Integer>>>> it1 = index.getIterator();
		int found = 0;
		while(it1.hasNext()) {
			Entry<String, TreeMap<String, TreeSet<Integer>>> entry = (Entry<String, TreeMap<String, TreeSet<Integer>>>) it1.next();
			String word = entry.getKey();
			if(word.startsWith(searchWord)) {
				found = 1;
				TreeMap<String, TreeSet<Integer>> nestedMap = entry.getValue();
				Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
				addResult(results, searchWord, it2);
			}
		}
		if(found == 0) {
			results.add(searchWord);
		}
	}
	
	public static void FinderPartial(InvertedIndex index, SearchResults results, String[] words) {
		@SuppressWarnings("unchecked")
		Iterator<TreeMap<String, TreeMap<String, TreeSet<Integer>>>> it1 = index.getIterator();
		String proper = getProperName(words);
		int found = 0;
		while(it1.hasNext()) {
			@SuppressWarnings("unchecked")
			Entry<String, TreeMap<String, TreeSet<Integer>>> entry = (Entry<String, TreeMap<String, TreeSet<Integer>>>) it1.next();
			String word = entry.getKey();
			for(int i = 0 ; i < words.length ; i++) {
				if(word.startsWith(words[i])) {
					found = 1;
					TreeMap<String, TreeSet<Integer>> nestedMap = entry.getValue();
					Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
					addResult(results, proper, it2);
				}
			}
		}
		if(found == 0 && proper.length() > 0) {
			results.add(proper);
		}
	}
	
	public static void FinderExact(InvertedIndex index, SearchResults results, String searchWord) {
		Iterator<TreeMap<String, TreeMap<String, TreeSet<Integer>>>> it1 = index.getIterator();
		int found = 0;
		while(it1.hasNext()) {
			Entry<String, TreeMap<String, TreeSet<Integer>>> entry = (Entry<String, TreeMap<String, TreeSet<Integer>>>) it1.next();
			String word = entry.getKey();
			if(word.equals(searchWord)) {
				found = 1;
				TreeMap<String, TreeSet<Integer>> nestedMap = entry.getValue();
				Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
				addResult(results, searchWord, it2);
			}
		}
		if(found == 0) {
			results.add(searchWord);
		}
	}
	
	public static void FinderExact(InvertedIndex index, SearchResults results, String[] words) {
		@SuppressWarnings("unchecked")
		Iterator<TreeMap<String, TreeMap<String, TreeSet<Integer>>>> it1 = index.getIterator();
		String proper = getProperName(words);
		int found = 0;
		while(it1.hasNext()) {
			@SuppressWarnings("unchecked")
			Entry<String, TreeMap<String, TreeSet<Integer>>> entry = (Entry<String, TreeMap<String, TreeSet<Integer>>>) it1.next();
			String word = entry.getKey();
			for(int i = 0 ; i < words.length ; i++) {
				if(word.equals(words[i])) {
					found = 1;
					TreeMap<String, TreeSet<Integer>> nestedMap = entry.getValue();
					Iterator<Entry<String, TreeSet<Integer>>> it2 = nestedMap.entrySet().iterator();
					addResult(results, proper, it2);
				}
			}
		}
		if(found == 0 && proper.length() > 0) {
			results.add(proper);
		}
	}
	
	public static void addResult(SearchResults result, String searchWord, Iterator<Entry<String, TreeSet<Integer>>> it) {
		while(it.hasNext()) {
			Entry<String, TreeSet<Integer>> entry2 = it.next();
			String file = entry2.getKey().toString();
			TreeSet<Integer> positions = entry2.getValue();
			Iterator<Integer> it3 = positions.iterator();
			while(it3.hasNext()) {
				Integer position = it3.next();
				result.add(searchWord, position, file);
			}
		}
	}
	
	public static String getProperName(String[] words) {
		StringBuilder proper = new StringBuilder();
		for(int i = 0 ; i < words.length ; i++) {
			proper.append(words[i]);
			proper.append(" ");
		}
		if(proper.length() != 0) {
			proper.deleteCharAt(proper.length() - 1);
		}
		return proper.toString();
	}
}
