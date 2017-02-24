import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Helper {
	
//-----------------------------------------------------------------------------------------------//
// Method: handleArgs	
// Description: This method is used to handle the command-line arguments.
//				It returns the "inputOutput" array, where inputOutput[0]
//				corresponds to the input file, and inputOutput[1] corresponds
//				to the output file.
//-----------------------------------------------------------------------------------------------//	
	public static String[] handleArgs(String[] argsArray) throws IOException {
		String[] inputOutput = new String[2];
		inputOutput[0] = null;
		inputOutput[1] = null;
		Path outputDir = null;
		if(argsArray.length == 0) {
			return inputOutput;
		}
		for(int i = 0 ; i < argsArray.length ; i++) {
			if(argsArray[i].equals("-path") && (i + 1) != argsArray.length) {
				inputOutput[0] = argsArray[i + 1];
			}
			if(argsArray[i].equals("-index")) {
				if(!((i + 1) >= argsArray.length)) {
					if(!(argsArray[i + 1].equals("-path"))) {
						outputDir = Paths.get(argsArray[i + 1]);
						inputOutput[1] = outputDir.toString();
					} else {
						outputDir = Paths.get("index.json");
						inputOutput[1] = outputDir.toString();
						try(BufferedWriter writer = Files.newBufferedWriter(outputDir, StandardCharsets.UTF_8)) {
							//do nothing
						}
					}
				} else {
					outputDir = Paths.get("index.json");
					inputOutput[1] = outputDir.toString();
					try(BufferedWriter writer = Files.newBufferedWriter(outputDir, StandardCharsets.UTF_8)) {
						//do nothing
					}
				}
			}
		}
		return inputOutput;
	}
//-----------------------------------------------------------------------------------------------//
// Method: checkArgs	
// Description: This method is used to do error checking on the command-
//				line arguments.  If the -path flag is not found, then
//				the "found" variable stays set to 0, and threeThings[2]
//				is set to 6, meaning that main should return.  The variable,
//				noOutput, is used to see if I need to write output. So,
//				threeThings[0] corresponds to the "found" variable,
//				threeThings[1] corresponds to the "noOutput" variable,
//				and threeThings[2] is only set to 6 if main must return
//				after this method is called.
//-----------------------------------------------------------------------------------------------//
	public static int[] checkArgs(String[] argsArray) {
		int found = 0;
		int noOutput = 0;
		int[] threeThings = new int[3];
			
		for(int m = 0 ; m < argsArray.length ; m++) {
			if(argsArray[m].equals("-path")) {
				found = 1;
				threeThings[0] = found;
				if((m + 1) == argsArray.length) {
					threeThings[2] = 6;
				} else {
					if(argsArray[m + 1].equals("-index")) {
						threeThings[2] = 6;
					}
				}
			}
			if(argsArray[m].equals("-index")) {
				noOutput = 1;
				threeThings[1] = noOutput;
			}
		}
		if(found == 0) {
			threeThings[2] = 6;
		}
		return threeThings;
	}
//-----------------------------------------------------------------------------------------------//
// Method: fileExists	
// Description: This method is called to make sure that the user did not
//				provide a bogus path for the file that needs to be read.
//-----------------------------------------------------------------------------------------------//
	public static boolean fileExists(Path inputPath) {
		boolean pathNotExists = Files.notExists(inputPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
		if(pathNotExists == true) {
			return true;
		} else {
			return false;
		}
	}
//-----------------------------------------------------------------------------------------------//
// Method: readAndBuild	
// Description: This method takes in the inputOutput array, which contains
//				String representations of the input and output paths. It
//				also takes in the noOutput int, which is used to decide
//				whether or not the JSON file will be written to. The last
//				parameter is simply the Path object, input_path.  First,
//				I create a new index object, and then I check if the input
//				path is just a file or if it is a directory.  If it is a
//				directory, then I call get_file_names to get a list of the
//				html/htm files that I need to read.  I also do a quick
//				check to make sure that the filenames returned are strictly
//				html or htm files.  Then, I use an enhanced_for loop to
//				iterate through each file, and I use StringBuilder and
//				BufferedReader to build the html.  I then call stripHTML,
//				parseWords, and buildIndex to build the index.  If only
//				one html/htm file was provided (not a directory), then
//				this method jumps to the else statement. I then call my
//				writeJSON method to write the final JSON file.
//-----------------------------------------------------------------------------------------------//
	public static void readAndBuild(String[] inputOutput, int noOutput, Path input_path) throws IOException {
		WordIndex index = new WordIndex();
		if(!(inputOutput[0].toLowerCase().endsWith(".html") || inputOutput[0].toLowerCase().endsWith(".htm"))) {
			ArrayList<String> filenames = new ArrayList();
			getFileNames(filenames, input_path);
			ArrayList<Path> files = new ArrayList();
			for(String the_files : filenames) {
				if(the_files.toLowerCase().endsWith(".html") || the_files.toLowerCase().endsWith(".htm")) {
					files.add(Paths.get(the_files));
				}
			}
			for (Path path : files) {
				StringBuilder html = new StringBuilder();
				try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
					for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
						html.append(x);
						html.append("\n");
					}
					String no_html = HTMLCleaner.stripHTML(html.toString());
					String[] the_words = WordParser.parseWords(no_html);
					BuildIndex.buildIndex(path.toString(), index, the_words);
				}
			}
		} else {
			StringBuilder html = new StringBuilder();
			try(BufferedReader reader = Files.newBufferedReader(input_path, StandardCharsets.UTF_8)) {
				for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
					html.append(x);
					html.append("\n");
				}
				String no_html = HTMLCleaner.stripHTML(html.toString());
				String[] the_words = WordParser.parseWords(no_html);
				BuildIndex.buildIndex(input_path.toString(), index, the_words);
			}
		}
		WordIndex.cleanIndex();
		if(noOutput == 1) {
			Path outputDir = Paths.get(inputOutput[1]);
			WordIndex.writeJSON(outputDir);
		}
	}
//-----------------------------------------------------------------------------------------------//
// Method: getFileNames	
// Description: This method is called if the input file provided does 
//				not end with ".html" or ".htm", meaning that it is a 
//				directory.  This method recursively traverses the
//				directory and returns an ArrayList of the files that 
//				need to be read.
//-----------------------------------------------------------------------------------------------//
	public static ArrayList<String> getFileNames(ArrayList<String> fileNames, Path directory) {
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for(Path path : stream) {
				if(path.toFile().isDirectory()) {
					getFileNames(fileNames, path);
				} else {
					fileNames.add(path.toString());
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return fileNames;
	} 
}
