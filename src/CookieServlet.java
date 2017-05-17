import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieServlet extends HttpServlet {
		
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
			
		    
		    boolean found = false;
		    
		    if(request.getParameter("param").equals("search")) {
		    	Cookie[] cookies = null;
			    cookies = request.getCookies();
			    out.printf("<p>Here is your search history:</p>");
		    	if(cookies != null) {
		    		for(int i = 0 ; i < cookies.length ; i++) {
		    			if(!(cookies[i].getName().startsWith("JSESSI")) && !(cookies[i].getName().startsWith("ktxxzz"))) {
		    				found = true;
		    				out.printf("<p>%s</p>", URLDecoder.decode(cookies[i].getValue(), "UTF-8"));
		    			}	
		    		}
		    	}
		    	if(found == false) {	
		    		out.printf("<p>No search history found</p>");
		    	}
		    }
		    if(request.getParameter("param").equals("visited")) {
		    	Cookie[] cookies = null;
			    cookies = request.getCookies();
			    out.printf("<p>Here are the results that you have visited:</p>");
		    	if(cookies != null) {
			    	for(int i = 0 ; i < cookies.length ; i++) {
			    		if(!(cookies[i].getName().startsWith("JSESSI"))) {
			    			if(cookies[i].getName().startsWith("ktxxzz")) {
			    				found = true;
			    				String link = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
			    				out.printf("<p><a href='/CookieResultServlet?param=%s'>%s</a></p>", link, link);
			    			}
			    		}	
			    	}
			    } 
		    	if(found == false) {
		    		out.printf("<p>No visited results found</p>");
		    	}
		    }
		    if(request.getParameter("param").equals("addfave")) {
		    	String link = request.getParameter("paramLink");
				Cookie linkCookie = new Cookie("favezzzx" + URLEncoder.encode(link, "UTF-8"), URLEncoder.encode(link, "UTF-8"));
				linkCookie.setMaxAge(60*60*24); 
				response.addCookie(linkCookie);
				response.sendRedirect(link);
		    }
		    if(request.getParameter("param").equals("justGo")) {
		    	String link = request.getParameter("paramLink");
				response.sendRedirect(link);
		    }
		    if(request.getParameter("param").equals("favorite")) {
		    	Cookie[] cookies = null;
			    cookies = request.getCookies();
			    out.printf("<p>Here are your favorite results:</p>");
		    	if(cookies != null) {
			    	for(int i = 0 ; i < cookies.length ; i++) {
			    		if(!(cookies[i].getName().startsWith("JSESSI"))) {
			    			if(cookies[i].getName().startsWith("favezzzx")) {
			    				found = true;
			    				String link = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
			    				out.printf("<p><a href='/CookieResultServlet?param=%s'>%s</a></p>", link, link);
			    			}
			    		}	
			    	}
			    } 
		    	if(found == false) {
		    		out.printf("<p>No favorite results found</p>");
		    	}
		    }
		    out.printf("<p> </p>");
		    
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