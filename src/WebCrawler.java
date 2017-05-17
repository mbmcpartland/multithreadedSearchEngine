import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This is my WebCrawler class reads words
 * from the HTML files from URLs and adds
 * the words to a multithreaded inverted
 * index.
 * @author mitchellmcpartland
 */
public class WebCrawler {
	
	private MultithreadedInvertedIndex index;
	private WorkQueue queue;
	private ArrayList<URL> urls; // TODO urls.contains() is very slow on an array list, hashset 
	private int limit;
	
	/**
	 * Constructor for the WebCrawler
	 */
	public WebCrawler(MultithreadedInvertedIndex index, int limit) {
		this.index = index;
		this.limit = limit;
		this.queue = new WorkQueue(limit);
		this.urls = new ArrayList<URL>();
	}
	
	/**
	 * Simply adds the seed URL to the list of URLs and that calls execute
	 * on the queue with a new CrawlWorker.
	 *
	 * @param seed/starting URL from which the rest of the URLs will come from
	 * @param limit on the number of threads building the index
	 */
	public void crawl(URL url, int limit) {
		urls.add(url);
		queue.execute(new CrawlWorker(url.toString()));

		queue.finish();
		queue.shutdown();
	}
	
	public void newCrawl(URL url, int limit) {
		this.limit = limit;
		this.urls = new ArrayList<URL>();
		this.queue = new WorkQueue(limit);
		urls.add(url);
		queue.execute(new CrawlWorker(url.toString()));

		queue.finish();
		queue.shutdown();
	}
	
	/**
	 * Private CrawlWorker class that implements the
	 * Runnable interface. Each CrawlWorker
	 * handles one URL.
	 */
	private class CrawlWorker implements Runnable {
		private String url;
		private ArrayList<URL> newLinks;
		
		/**
		 * Constructor for the CrawlWorker,
		 * or should I say.....minion!
		 */
		public CrawlWorker(String url) {
			this.url = url;
		}
		
		/**
		 * Used to create a new task for the queue when
		 * a worker has found a new, unique link.
		 * 
		 * @param url to count words from
		 * @param limit on number of CrawlWorkers
		 */
		private void innerCrawl(URL url, int limit) {
			synchronized(urls) {
				if(urls.size() <= limit) {
					queue.execute(new CrawlWorker(url.toString()));
				}
			}
		}
		
		/**
		 * Run method for each CrawlWorker. The
		 * HTML get's fetched, then I get the
		 * links in the HTML, and then I count
		 * the words in the HTML and add to the
		 * index.
		 * 
		 */
		public void run() {
			String html = "";
			try {
				html = HTTPFetcher.fetchHTML(this.url);
			} catch (UnknownHostException e) {
				System.out.println("Unknown host");
			} catch (MalformedURLException e) {
				System.out.println("MalformedURL");
			} catch (IOException e) {
				System.out.println("IOException");
			}
			try {
				if(html != null) {
					this.newLinks = LinkParser.listLinks(new URL(this.url), html);
				}
			} catch (MalformedURLException e) {
				System.out.println("MalformedURL");
			}
			if(newLinks != null) {
				for(URL theURL : newLinks) {
					if(!(urls.contains(theURL))) {
						synchronized(urls) {
							urls.add(theURL);
						}
						innerCrawl(theURL, limit);
					}
				}
				InvertedIndex local = new InvertedIndex();
				String no_html = HTMLCleaner.stripHTML(html.toString());
				String[] the_words = WordParser.parseWords(no_html);
				local.addAll(the_words, this.url);
				index.addAll(local);
			}
		}
	}
}
