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
		
		String[] input_output = helper.handle_args(args);
		if(input_output[0] == null && input_output[1] == null) {
			return;
		}
		
		int[] three_things = helper.check_args(args);
		if(three_things[2] == 6) {
			return;
		}
		
		Path input_path = Paths.get(input_output[0]);
		if(helper.file_exists(input_path) == true) {
			return;
		}
		
		helper.read_and_build(input_output, three_things[1], input_path);
	
	}
}