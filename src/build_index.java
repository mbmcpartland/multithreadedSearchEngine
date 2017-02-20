
public class build_index {
	
	public static void buildIndex(String path, the_index index, String[] words) {
		int m = 1;
		for(String word : words) {
			index.add(word, m, path);
			m++;
		}
	}
}
