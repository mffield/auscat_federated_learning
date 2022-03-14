package fl.node.server.web;


/**
 * Servlet to report data node status
 * @author Matthew Field
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fl.node.server.jaxws.FLServer;


@WebServlet("/DLTableServlet/*")
public class FLTableServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    FLWeb WebClientServer = new FLWeb();
	    WebClientServer.initWebClient();
	    FLServer server = WebClientServer.getServerObject();
	    
	    String clientName;
	    String algorithmOutput;
	    System.out.println("Printing table!");
	    response.setContentType("text/html");
	    PrintWriter htmlOut = response.getWriter();
	    
	    
	    if (server!=null) {
	    	
	    	
	    	
	    	htmlOut.println("<TABLE class=\"w3-table w3-striped w3-bordered\" style=\"width:80%\">");
	    	htmlOut.println("<TR><TH>Selection</TH><TH>Data Centre</TH><TH>Status</TH><TH>Current Job</TH><TH>Last Job</TH><TH>Algorithm Message</TH></TR>");
	    	System.out.println("Printing table header");
	    	for (int i=0; i < server.getNumberofCurrentAddedClients(); i++) {
	    		
	    		clientName = server.getClientName(i);
	    		algorithmOutput = server.getAlgorithmOutput(i);
	    		
	    		
	    		htmlOut.println("<TR><TD><CENTER>");
	    		htmlOut.println("<INPUT class=\"activateNode\" TYPE=\"CHECKBOX\" NAME=\"node" + Integer.toString(i) + "_selected\" onclick='handleActivationClick()'>");
	    		htmlOut.println("</CENTER></TD>");	    
	    		htmlOut.println("<TD> <div id=\"node" + Integer.toString(i) + "_name\">" + clientName + "</div> </TD>");	    
	    		htmlOut.println("<TD> <div id=\"node" + Integer.toString(i) + "_status\"></div>  </TD>");	    
	    		htmlOut.println("<TD> <div id=\"node" + Integer.toString(i) + "_currjob\"></div>  </TD>");	 
	    		htmlOut.println("<TD> <div id=\"node" + Integer.toString(i) + "_lastjob\"></div>  </TD>");	 
	    		htmlOut.println("<TD><div class=\"row\">");	    
	    		htmlOut.println("<div class=\"column\" onclick=\"openTab('node" + Integer.toString(i) + "_tabmessage');\" style=\"background:green;\">----></div>");	    
	    		htmlOut.println("</div>");
	    		htmlOut.println("<div id=\"node" + Integer.toString(i) + "_tabmessage\" class=\"containerTab\" style=\"display:none;background:green\">");
	    		htmlOut.println("<span onclick=\"this.parentElement.style.display='none'\" class=\"closebtn\">x</span>");
	    		htmlOut.println("<b>" + clientName + " algorithm output</b>");
	    		htmlOut.println("<div id=\"node" + Integer.toString(i) + "_algmessage\">" + algorithmOutput + "</div></div></TD></TR>");
	    		System.out.println("Printing table row " + Integer.toString(i));
	    	}
	    	htmlOut.println("</TABLE>");
	    
	    }
	    
	}
	
}
