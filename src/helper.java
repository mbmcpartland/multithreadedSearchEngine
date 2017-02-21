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

public class helper {
	
//-----------------------------------------------------------------------------------------------//
// Method: handle_args	
// Description: This method is used to handle the command-line arguments.
//				It returns the "input_output" array, where input_output[0]
//				corresponds to the input file, and input_output[1] corresponds
//				to the output file.
//-----------------------------------------------------------------------------------------------//	
	public static String[] handle_args(String[] args_arr) throws IOException {
		String[] input_output = new String[2];
		input_output[0] = null;
		input_output[1] = null;
		Path output_directory = null;
		if(args_arr.length == 0) {
			return input_output;
		}
		for(int i = 0 ; i < args_arr.length ; i++) {
			if(args_arr[i].equals("-path") && (i + 1) != args_arr.length) {
				input_output[0] = args_arr[i + 1];
			}
			if(args_arr[i].equals("-index")) {
				if(!((i + 1) >= args_arr.length)) {
					if(!(args_arr[i + 1].equals("-path"))) {
						output_directory = Paths.get(args_arr[i + 1]);
						input_output[1] = output_directory.toString();
					} else {
						output_directory = Paths.get("index.json");
						input_output[1] = output_directory.toString();
						try(BufferedWriter writer = Files.newBufferedWriter(output_directory, StandardCharsets.UTF_8)) {
							//do nothing
						}
					}
				} else {
					output_directory = Paths.get("index.json");
					input_output[1] = output_directory.toString();
					try(BufferedWriter writer = Files.newBufferedWriter(output_directory, StandardCharsets.UTF_8)) {
						//do nothing
					}
				}
			}
		}
		return input_output;
	}
//-----------------------------------------------------------------------------------------------//
// Method: check_args	
// Description: This method is used to do error checking on the command-
//				line arguments.  If the -path flag is not found, then
//				the "found" variable stays set to 0, and three_things[2]
//				is set to 6, meaning that main should return.  The variable,
//				no_output, is used to see if I need to write output. So,
//				three_things[0] corresponds to the "found" variable,
//				three_things[1] corresponds to the "no_output" variable,
//				and three_things[2] is only set to 6 if main must return
//				after this method is called.
//-----------------------------------------------------------------------------------------------//
	public static int[] check_args(String[] args_arr) {
		int found = 0;
		int no_output = 0;
		int[] three_things = new int[3];
			
		for(int m = 0 ; m < args_arr.length ; m++) {
			if(args_arr[m].equals("-path")) {
				found = 1;
				three_things[0] = found;
				if((m + 1) == args_arr.length) {
					three_things[2] = 6;
				} else {
					if(args_arr[m + 1].equals("-index")) {
						three_things[2] = 6;
					}
				}
			}
			if(args_arr[m].equals("-index")) {
				no_output = 1;
				three_things[1] = no_output;
			}
		}
		if(found == 0) {
			three_things[2] = 6;
		}
		return three_things;
	}
//-----------------------------------------------------------------------------------------------//
// Method: file_exists	
// Description: This method is called to make sure that the user did not
//				provide a bogus path for the file that needs to be read.
//-----------------------------------------------------------------------------------------------//
	public static boolean file_exists(Path input_path) {
		boolean path_notexists = Files.notExists(input_path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
		if(path_notexists == true) {
			return true;
		} else {
			return false;
		}
	}
//-----------------------------------------------------------------------------------------------//
// Method: read_and_build	
// Description: This method takes in the input_output array, which contains
//				String representations of the input and output paths. It
//				also takes in the no_output int, which is used to decide
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
	public static void read_and_build(String[] input_output, int no_output, Path input_path) throws IOException {
		the_index index = new the_index();
		if(!(input_output[0].toLowerCase().endsWith(".html") || input_output[0].toLowerCase().endsWith(".htm"))) {
			ArrayList<String> filenames = new ArrayList();
			get_file_names(filenames, input_path);
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
					build_index.buildIndex(path.toString(), index, the_words);
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
				build_index.buildIndex(input_path.toString(), index, the_words);
			}
		}
		the_index.clean_index();
		if(no_output == 1) {
			Path output_directory = Paths.get(input_output[1]);
			the_index.writeJSON(output_directory);
		}
	}
//-----------------------------------------------------------------------------------------------//
// Method: get_file_names	
// Description: This method is called if the input file provided does 
//				not end with ".html" or ".htm", meaning that it is a 
//				directory.  This method recursively traverses the
//				directory and returns an ArrayList of the files that 
//				need to be read.
//-----------------------------------------------------------------------------------------------//
	public static ArrayList<String> get_file_names(ArrayList<String> fileNames, Path directory) {
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for(Path path : stream) {
				if(path.toFile().isDirectory()) {
					get_file_names(fileNames, path);
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
