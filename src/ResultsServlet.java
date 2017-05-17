import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResultsServlet extends HttpServlet {
		
		private static final String RESULTS = "Search Results";
		private static final String TITLE = "Mitchell's Search Engine";
		private static final String SEARCH = "Enter another query:";
		private static final String NORESULTS = "No results found for your query";
		private static final String CRAWL = "Enter a new URL to crawl:";
		private static int partialToggle = 0;
		private static int privateToggle = 0;
		
		public static int incrementPartialToggle() {
			ResultsServlet.partialToggle++;
			return ResultsServlet.partialToggle;
		}
		
		public static int incrementPrivateToggle() {
			ResultsServlet.privateToggle++;
			return ResultsServlet.privateToggle;
		}
		
		@Override
		protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
			
			Long startTime;
			startTime = System.nanoTime();
			
			String query = request.getParameter("query"); 
			if((privateToggle % 2) == 0) {
				if(query != null) {
					Cookie searchCookie = new Cookie(URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(query, "UTF-8"));
					searchCookie.setMaxAge(60*60*24); 
					response.addCookie(searchCookie);
				}
			}
			
			ThreadedQueryHelper search = new ThreadedQueryHelper(SearchServer.index, 0);
			if((ResultsServlet.partialToggle % 2) == 0) {
				search.searchOneQuery(query, false);
			} else {
				search.searchOneQuery(query, true);
			}
			ArrayList<String> links = search.returnLinks();
			
			PrintWriter out = response.getWriter();
			out.printf("<html>%n");
			out.printf("<head><title>%s</title></head>%n", TITLE);
			out.printf("<body>%n");
			out.printf("<p><big><strong>%s</big></strong></p>", TITLE);

			out.printf("<form name='searchForm' method='post' action='ResultsServlet'>%n");
			out.printf("%s <input type='text' name='query'/> <br/>%n", SEARCH);
			out.printf("<input type='submit' value='Search' />%n");
			out.printf("</form>%n");
			
			out.printf("<p></p>%n");
			out.printf("<form name='newURLform' method='post' action='SearchServlet?param=crawl'>%n");
			out.printf("%s <input type='text' name='crawl'/> <br/>%n", CRAWL);
			out.printf("<input type='submit' value='Crawl' />%n");
			out.printf("</form>%n");
			
			out.printf("<p></p>%n");
			out.printf("<form name='viewHistoryform' method='post' action='CookieServlet?param=search'>%n");
			out.printf("<input type='submit' value='View Search History' />%n");
			out.printf("</form>%n");
			
			out.printf("<p></p>%n");
			out.printf("<form name='viewHistoryform' method='post' action='ClearHistoryServlet?param=search'>%n");
			out.printf("<input type='submit' value='Clear Search History' />%n");
			out.printf("</form>%n");
			
			out.printf("<form name='viewSeenResults' method='post' action='CookieServlet?param=visited'>%n");
			out.printf("<input type='submit' value='View Visited Results' />%n");
			out.printf("</form>%n");
			
		    out.printf("<form name='searchForm' method='post' action='ClearHistoryServlet?param=visited'>%n");
			out.printf("<input type='submit' value='Clear Visited Results' />%n");
			out.printf("</form>%n");
			
			out.printf("<form name='viewSeenResults' method='post' action='CookieServlet?param=favorite'>%n");
			out.printf("<input type='submit' value='View Favorite Results' />%n");
			out.printf("</form>%n");
			
		    out.printf("<form name='searchForm' method='post' action='ClearHistoryServlet?param=favorite'>%n");
			out.printf("<input type='submit' value='Clear Favorite Results' />%n");
			out.printf("</form>%n");
			
		    out.printf("<form name='searchForm' method='post' action='SearchServlet?param=partial'>%n");
			out.printf("<input type='submit' value='Toggle Partial Search' />%n");
			out.printf("</form>%n");
			
			out.printf("<form name='searchForm' method='post' action='SearchServlet?param=private'>%n");
			out.printf("<input type='submit' value='Toggle Private Search' />%n");
			out.printf("</form>%n");

			out.printf("</body>%n");
			out.printf("</html>%n");
			
			Long diffTime = System.nanoTime() - startTime;
			double seconds = (double)diffTime / 1000000000.0;
			
			out.printf("<p> This search took %d nanoseconds (%f seconds) and %d results were found! </p>", diffTime, seconds, links.size());
			
			out.printf("<p><big><strong>%s</big></strong></p>", RESULTS);
			for(String link : links) {
				out.printf("<p><a href='/CookieResultServlet?param=%s'>%s</a></p>", link, link);
			}
			if(links.size() == 0) {
				out.printf("<p> %s </p>", NORESULTS);
			}
	        out.printf("</html>%n");

	    }
	}