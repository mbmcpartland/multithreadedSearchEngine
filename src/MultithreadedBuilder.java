import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedBuilder {
	
	public static final Logger log = LogManager.getLogger();
	
	public static void buildFromHTML(ArrayList<Path> htmlFiles, MultithreadedInvertedIndex index, int threads) throws IOException {
		WorkQueue queue = new WorkQueue(threads);
		
		for(Path path : htmlFiles) {
			queue.execute(new BuildWorker(path, index));
		}
		
		queue.finish();
		queue.shutdown();
	}
	
	private static class BuildWorker implements Runnable {
		
		private MultithreadedInvertedIndex index;
		private Path path;
		
		public BuildWorker(Path path, MultithreadedInvertedIndex index) {
			this.path = path;
			this.index = index;
		}
		
		@Override
		public void run() {
			synchronized(index) {
				StringBuilder html = new StringBuilder();
				try(BufferedReader reader = Files.newBufferedReader(this.path, StandardCharsets.UTF_8)) {
					for(String x = reader.readLine(); x != null ; x = reader.readLine()) {
						html.append(x);
						html.append("\n");
					}
					String no_html = HTMLCleaner.stripHTML(html.toString());
					String[] the_words = WordParser.parseWords(no_html);
					index.addAll(the_words, this.path.toString());
				} catch (IOException e) {
					System.out.println("error building index");
				}
			}
		}	
	}
}
