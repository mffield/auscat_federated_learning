package fl.node.server.web;

/**
 * Servlet to refresh distributed learning infrastructure
 * @author Matthew Field
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fl.node.server.jaxws.FLServer;


@WebServlet("/FLServletRefresh/*")
public class FLServletRefresh extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    FLWeb WebClientServer = new FLWeb();
	    WebClientServer.initWebClient();
	    FLServer server = WebClientServer.getServerObject();

	    server.clearAllClientsPackages();
	    server.initClientInfo();
	    
	    
	    response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); 
	    response.getWriter().write("Done.");       // Write response body.
	    
	}
	
}
