import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {

	public static void main(String[] args) throws IOException {
		String input = null;
		Path output_directory = null;
		if(args.length == 0) {
			return;
		}
		for(int i = 0 ; i < args.length ; i++) {
			if(args[i].equals("-path") && (i + 1) != args.length) {
				input = args[i + 1];
			}
			if(args[i].equals("-index")) {
				if(!((i + 1) >= args.length)) {
					if(!(args[i + 1].equals("-path"))) {
						output_directory = Paths.get(args[i + 1]);
					} else {
						output_directory = Paths.get("index.json");
						try(BufferedWriter writer = Files.newBufferedWriter(output_directory, StandardCharsets.UTF_8)) {
							
						}
					}
				} else {
					output_directory = Paths.get("index.json");
					try(BufferedWriter writer = Files.newBufferedWriter(output_directory, StandardCharsets.UTF_8)) {
						
					}
				}
			}
		}
		int found = 0;
		int no_output = 0;
		for(int m = 0 ; m < args.length ; m++) {
			if(args[m].equals("-path")) {
				found = 1;
				if((m + 1) == args.length) {
					return;
				} else {
					if(args[m + 1].equals("-index")) {
						return;
					}
				}
			}
			if(args[m].equals("-index")) {
				no_output = 1;
			}
		}
		if(found == 0) {
			return;
		}
		Path test = Paths.get(input);
		boolean path_notexists = Files.notExists(test, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
		if(path_notexists == true) {
			return;
		}
		the_index index = new the_index();
		if(!(input.toLowerCase().endsWith(".html") || input.toLowerCase().endsWith(".htm"))) {
			ArrayList<String> filenames = new ArrayList();
			getFileNames(filenames, test);
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
	    	try(BufferedReader reader = Files.newBufferedReader(test, StandardCharsets.UTF_8)) {
	    		for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
	    			html.append(x);
	    			html.append("\n");
	    		}
	    		String no_html = HTMLCleaner.stripHTML(html.toString());
	    		String[] the_words = WordParser.parseWords(no_html);
	    		build_index.buildIndex(test.toString(), index, the_words);
	    	}
		}
	    the_index.clean_index();
	    if(no_output == 1) {
	    	the_index.writeJSON(output_directory);
	    }
	
	}
	
	private static ArrayList<String> getFileNames(ArrayList<String> fileNames, Path dir) {
	    try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	        for (Path path : stream) {
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
