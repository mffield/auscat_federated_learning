package fl.node.server.web;

/**
 * Servlet to handle data node activation
 * @author Matthew Field
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fl.node.server.jaxws.FLServer;


@WebServlet("/FLServletActivation/*")
public class FLServletActivation extends HttpServlet {


	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
		String message="null";
		
		//String[] clientsActive = {request.getParameter("clientsActive0"), request.getParameter("clientsActive1"), request.getParameter("clientsActive2"), request.getParameter("clientsActive3")};
		String clientsActive = request.getParameter("clientsActive").trim();
		String[] clients = clientsActive.split(",");
		
		String[] nodeStatus = new String[clients.length];
		
	    //System.out.println(clients[0]);
		
	    FLWeb WebClientServer = new FLWeb();
	    WebClientServer.initWebClient();
	    FLServer server = WebClientServer.getServerObject();
	    

		    if (server!=null) {
		    	System.out.println("Number of clients: "+String.valueOf(server.getNumberofCurrentAddedClients()));
		    	for (int i=0; i < server.getNumberofCurrentAddedClients(); i++)
		    	{
		    		System.out.println("Client " + String.valueOf(i) + " : "  + clients[i]);
		    		if (clients[i].equals("true")) {
		    			server.activateClient(i);
		    		}
		    		else { 
		    			server.deActivateClient(i);	
		    		}
		    		nodeStatus[i] = server.getClientStatus(i);
		    	}
		    	message = String.join(",", nodeStatus);
		    }
		    else {
		    	System.out.println("Server was null!");message = "Server was null!";
		    }
		    response.setContentType("text/plain");  
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(message);      
	    //}
	}
	
}
