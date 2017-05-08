import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to read the input files
 * and then to build the MultithreadedIndex.
 * @author mitchellmcpartland
 */
public class MultithreadedBuilder {
	
	public static final Logger log = LogManager.getLogger();
	
	/**
	 * Iterates through the ArrayList of input files
	 * and calls queue.execute, passing in the path and
	 * the index in each iteration.
	 * 
	 * @param the ArrayList of htmlFiles to iterate through
	 * @param the MultithreadedInvertedIndex that is being added to
	 */
	public static void buildFromHTML(ArrayList<Path> htmlFiles, MultithreadedInvertedIndex index, int threads) throws IOException {
		WorkQueue queue = new WorkQueue(threads);
		
		for(Path path : htmlFiles) {
			queue.execute(new BuildWorker(path, index));
		}
		
		queue.finish();
		queue.shutdown();
	}
	
	/**
	 * Private BuildWorker class that implements the
	 * Runnable interface. Each BuildWorker
	 * simply calls buildFromHTML with the path and
	 * the MultithreadedInvertedIndex.
	 */
	private static class BuildWorker implements Runnable {
		
		private MultithreadedInvertedIndex index;
		private Path path;
		
		/**
		 * Constructor for the BuildWorker,
		 * or should I say.....minion!
		 */
		public BuildWorker(Path path, MultithreadedInvertedIndex index) {
			this.path = path;
			this.index = index;
		}
		
		@Override
		public void run() {
			 try {
				InvertedIndex local = new InvertedIndex();
				InvertedIndexBuilder.buildFromHTML(this.path, local);
				this.index.addAll(local);
			} catch (IOException e) {
				System.out.println("Error building the index from worker thread");
			}
		}	
	}
}