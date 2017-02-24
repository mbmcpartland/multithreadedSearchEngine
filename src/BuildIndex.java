public class BuildIndex {

//-----------------------------------------------------------------------------------------------//
// Method: buildIndex	
// Description: This method takes in a String representation of the path of
//				file that is being analyzed.  It also takes in the index
//				and a String array of the words of the file.  Using a simple
//				enhanced for-loop, along with a counter, I call the add
//				function for my index.
//-----------------------------------------------------------------------------------------------//
	public static void buildIndex(String path, WordIndex index, String[] words) {
		int m = 1;
		for(String word : words) {
			index.add(word, m, path);
			m++;
		}
	}
}
