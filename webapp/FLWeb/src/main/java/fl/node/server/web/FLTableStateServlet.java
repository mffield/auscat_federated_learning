package fl.node.server.web;

/**
 * Servlet to report data node status in table format
 * @author Matthew Field
 * 
 */
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import fl.node.server.jaxws.FLServer;

@WebServlet("/FLTableStateServlet/*")
public class FLTableStateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    FLWeb WebClientServer = new FLWeb();
	    WebClientServer.initWebClient();
	    FLServer server = WebClientServer.getServerObject();
	    
	    ArrayList<String> list = new ArrayList<String>();
	    String json="";
	    
	    if (server!=null) {
	    	for (int i=0; i < server.getNumberofCurrentAddedClients(); i++) {
			    list.add(server.getClientName(i));
			    list.add(server.getClientStatus(i));
			    list.add(server.getAlgorithmOutput(i));
			    
	    	}
	    	json = new Gson().toJson(list);
	    }
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json); 
	    
	}
	
}
