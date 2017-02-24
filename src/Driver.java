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

//-----------------------------------------------------------------------------------------------//
// Method: main	
// Description: This method takes in a String representation of the path of
//				In this Driver class, my main method first calls handleArgs
//				and checkArgs, which are two methods in my Helper class. To
//				read more about handleArgs and checkArgs, you can go to my
//				Helper class to read my notes about what they both do. After
//				doing my necessary checks in the arguments passed in through
//				the command line, I call my fileExists method from my Helper
//				class.  If the input file exists, then I call my most important
//				function for this whole project, which is my readAndBuild
//				method in my Helper class.
//-----------------------------------------------------------------------------------------------//
	
	public static void main(String[] args) throws IOException {
		
		String[] inputOutput = Helper.handleArgs(args);
		if(inputOutput[0] == null && inputOutput[1] == null) {
			return;
		}
		
		int[] threeThings = Helper.checkArgs(args);
		if(threeThings[2] == 6) {
			return;
		}
		
		Path inputPath = Paths.get(inputOutput[0]);
		if(Helper.fileExists(inputPath) == true) {
			return;
		}
		
		Helper.readAndBuild(inputOutput, threeThings[1], inputPath);
	
	}
}