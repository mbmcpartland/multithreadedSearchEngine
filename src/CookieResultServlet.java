import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieResultServlet extends HttpServlet {

		private static int privateToggle = 0;
		private static final String TITLE = "Mitchell's Search Engine";
		
		public static int incrementPrivateToggle() {
			CookieResultServlet.privateToggle++;
			return CookieResultServlet.privateToggle;
		}

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
	    }
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			
			response.setContentType("text/html");
			
			String link = request.getParameter("param"); 
			PrintWriter out = response.getWriter();
			
			out.printf("<html>%n");
			out.printf("<head><title>%s</title></head>%n", TITLE);
			out.printf("<body>%n");
			
			out.printf("<p><big><strong>%s</big></strong></p>", TITLE);

			if((privateToggle % 2) == 0) {
				Cookie linkCookie = new Cookie("ktxxzz" + URLEncoder.encode(link, "UTF-8"), URLEncoder.encode(link, "UTF-8"));
				linkCookie.setMaxAge(60*60*24); 
				response.addCookie(linkCookie);
			
				out.printf("<p>Add to favorites?<p/>");
				out.printf("<form name='viewSeenResults' method='post' action='CookieServlet?param=addfave&paramLink=%s'>%n", link);
				out.printf("<input type='submit' value='Yes' />%n");
				out.printf("</form>%n");
			
				out.printf("<form name='viewSeenResults' method='post' action='CookieServlet?param=justGo&paramLink=%s'>%n", link);
				out.printf("<input type='submit' value='No' />%n");
				out.printf("</form>%n");
			
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendRedirect(link);
			}
		}
}
