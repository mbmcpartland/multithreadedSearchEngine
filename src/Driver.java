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

// TODO Always address your warnings

// TODO You can avoid having import warnings by configuring Eclipse "Save Actions" to always "Organize Imports" on save.

// TODO Add Javadoc comments to all classes and methods

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
	
	// TODO You can throw exceptions everywhere EXCEPT main
	
	// TODO Driver should have project-specific argument handling
	// TODO All other classes must be generalized
	
	/**
	 * TODO Add description here
	 * @param args describe parameter here
	 * @throws IOException
	 */
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
		
		/* TODO Try this
		ArgumentMap map = new ArgumentMap(args);
		WordIndex index = new WordIndex();
		
		if (map.hasFlag("-path")) {
			trigger the directory traversal and reading of files here
		}
		
		if (map.hasFlag("-index")) {
			trigger writing your word index to file here
		}
		*/
	}
}