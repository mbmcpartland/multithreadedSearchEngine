import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class SearchServer {
	
	public int port;
	public static MultithreadedInvertedIndex index;
	public static WebCrawler crawler;
	
	public SearchServer(int portNumber, MultithreadedInvertedIndex index, WebCrawler crawler) {
		this.port = portNumber;
		SearchServer.index = index;
		SearchServer.crawler = crawler;
	}
	
	public void runServer() throws Exception {

			// type of handler that supports sessions
			ServletContextHandler servletContext = null;

			// turn on sessions and set context
			servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
			servletContext.setContextPath("/");
			servletContext.addServlet(SearchServlet.class, "/");
			servletContext.addServlet(ResultsServlet.class, "/ResultsServlet");
			servletContext.addServlet(CookieServlet.class, "/CookieServlet");
			servletContext.addServlet(ClearHistoryServlet.class, "/ClearHistoryServlet");
			servletContext.addServlet(CookieResultServlet.class, "/CookieResultServlet");

			// default handler for favicon.ico requests
			DefaultHandler defaultHandler = new DefaultHandler();
			defaultHandler.setServeIcon(true);

			ContextHandler defaultContext = new ContextHandler("/favicon.ico");
			defaultContext.setHandler(defaultHandler);

			// setup handler order
			HandlerList handlers = new HandlerList();
			handlers.setHandlers(new Handler[] { defaultContext, servletContext });

			// setup jetty server
			Server server = new Server(this.port);
			server.setHandler(handlers);
			server.start();
			server.join();
	}
}
