package fl.node.server.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import fl.node.server.jaxws.FLServer;

/**
 * Servlet to handle File upload request
 * @author Matthew Field
 */
@WebServlet("/FileUploadHandler/*")
public class FileUploadHandler extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
    	String UPLOAD_DIRECTORY = "/uploads";
    	ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getRealPath(File.separator);
		
        //process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
              
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        String name = new File(item.getName()).getName();
                        String fullpathname = contextPath + UPLOAD_DIRECTORY + File.separator + name;
                        item.write( new File(fullpathname));
                        
                        // Now call the server methods to read the file and deploy to clients
                        FLWeb WebClientServer = new FLWeb();
                        WebClientServer.initWebClient();
                        FLServer server = WebClientServer.getServerObject();
                        byte[] input_file_content = null;
                        input_file_content = WebClientServer.readBinaryFile(contextPath + UPLOAD_DIRECTORY + File.separator + name);
                        server.publishEXEToClients(Paths.get(fullpathname).getFileName().toString(), input_file_content);
                        
                        // TO DO: add timeout for this loop
                        while(server.allPackagesReceived()==false) 
                		{
                        	System.out.println("Waiting for all to receive");
                		}
                		System.out.println("All programs received");
                		
                    }
                }
           
               //File uploaded successfully
               request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
               request.setAttribute("message", "File Upload Failed due to " + ex);
            }          
         
        }else{
            request.setAttribute("message", "Sorry this Servlet only handles file upload request");
        }
    
        
	    
        request.getRequestDispatcher("/manager.jsp").forward(request, response);
     
    }
}

