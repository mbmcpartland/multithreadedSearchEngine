import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SearchServlet extends HttpServlet {

		/** Title of web page. */
		private static final String TITLE = "Mitchell's Search Engine";
		private static final String SEARCH = "Enter a query:";
		private static final String CRAWL = "Enter a new URL to crawl:";

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
	    }
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			out.printf("<html>%n");
			out.printf("<head><title>%s</title></head>%n", TITLE);
			out.printf("<body>%n");
			out.printf("<p><big><strong>%s</big></strong></p>", TITLE);
			
			HttpSession session = request.getSession(true);
		    Date lastAccessTime = new Date(session.getLastAccessedTime());
		    SimpleDateFormat theFormat = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		    if(session.isNew()){
		    	out.printf("<p> This is your first time using my search engine :) </p>");
		    } else {
		    	out.println("<p> The last time you visited my search engine was: " + theFormat.format(lastAccessTime));
		    }
			
			String param = request.getParameter("param");
			String URL = null;
			if(param != null) {
				if(param.equals("crawl")) {
					URL = request.getParameter("crawl");
					SearchServer.crawler.newCrawl(new URL(URL), 100);
				}
				if(param.equals("partial")) {
					int partial = ResultsServlet.incrementPartialToggle();
					if((partial % 2) == 0) {
						out.printf("<p> Partial Search turned on </p>");
					} else {
						out.printf("<p> Partial Search turned off </p>");
					}
				}
				if(param.equals("private")) {
					int private1 = CookieResultServlet.incrementPrivateToggle();
					int private2 = ResultsServlet.incrementPrivateToggle();
					if( (private1 % 2 == 0) && (private2 % 2 == 0) ) {
						out.printf("<p> Private Search turned off </p>");
					} else {
						out.printf("<p> Private Search turned on </p>");
					}
				}
			}

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
			
			
			if(param != null && param.equals("crawl")) {
				out.printf("<p>The following URL has been crawled: %s</p>", URL);
			}

			out.printf("</body>%n");
			out.printf("</html>%n");

			response.setStatus(HttpServletResponse.SC_OK);
		}
}
