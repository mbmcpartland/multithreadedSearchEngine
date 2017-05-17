import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClearHistoryServlet extends HttpServlet {
		
		private static final String TITLE = "Mitchell's Search Engine";
		private static final String SEARCH = "Enter another query:";
		private static final String CRAWL = "Enter a new URL to crawl:";
		
		@Override
		protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException { 
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			out.printf("<html>%n");
			out.printf("<head><title>%s</title></head>%n", TITLE);
			out.printf("<body>%n");
			
			out.printf("<p><big><strong>%s</big></strong></p>", TITLE);
			
			Cookie[] cookies = null;
		    cookies = request.getCookies();
		    
		    if(request.getParameter("param").equals("search")) {
		    	if(cookies != null) {
		    		for(int i = 0 ; i < cookies.length ; i++) {
		    			if(!(cookies[i].getName().startsWith("ktxxzz"))) {
		    				cookies[i].setMaxAge(0);
		    				response.addCookie(cookies[i]);
		    			}
		    		}
		    	}
		    	out.printf("<p>Your search history has been cleared successfully</p>");
		    } 
		    if(request.getParameter("param").equals("visited")) {
		    	if(cookies != null) {
		    		for(int i = 0 ; i < cookies.length ; i++) {
		    			if(cookies[i].getName().startsWith("ktxxzz")) {
		    				cookies[i].setMaxAge(0);
		    				response.addCookie(cookies[i]);
		    			}
		    		}
		    	}
		    	out.printf("<p>Your visited results has been cleared successfully</p>");
		    }
		    if(request.getParameter("param").equals("favorite")) {
		    	if(cookies != null) {
		    		for(int i = 0 ; i < cookies.length ; i++) {
		    			if(cookies[i].getName().startsWith("favezzzx")) {
		    				cookies[i].setMaxAge(0);
		    				response.addCookie(cookies[i]);
		    			}
		    		}
		    	}
		    	out.printf("<p>Your favorited results has been cleared successfully</p>");
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

			out.printf("</body>%n");
			out.printf("</html>%n");

			response.setStatus(HttpServletResponse.SC_OK);
		}
	}