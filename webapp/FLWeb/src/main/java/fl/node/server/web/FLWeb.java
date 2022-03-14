package fl.node.server.web;

/*
 * Main class for web server, connects to federated learning server and reports status on web page
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;


import fl.node.server.jaxws.FLServer;


public class FLWeb {

	//private String configFilepath = "C:\\DistributedLearning\\WebFramework\\";
	private String propertyHome  =  System.getProperty("property.home");
	private String WebAppName;
	private FLServer server = null;
	
	public void initWebClient() {
		
		String wsdlURL = "";
		Properties prop = new Properties();
		try {
			
			ServletContext c = ListenerName.context;
	    	this.WebAppName = c.getContextPath().substring(1);
	    	prop.load(preprocessPropertiesFile(this.propertyHome + "\\" + this.WebAppName + ".properties"));
			//prop.load(new FileInputStream(new File(this.propertyHome + "\\" + this.WebAppName + ".properties")));
			wsdlURL = prop.getProperty("wsdlURL");
			System.setProperty("javax.net.ssl.trustStore", prop.getProperty("ts_path")); 
	 	    System.setProperty("javax.net.ssl.trustStorePassword", prop.getProperty("ts_pass"));
	 	    System.setProperty("javax.net.ssl.keyStore", prop.getProperty("ks_path"));
	 	    System.setProperty("javax.net.ssl.keyStorePassword", prop.getProperty("ks_pass"));
		} catch (IOException e) {
			//logger.error("Could not load config.properties file", e);
			return;
		}
		
		try {
			URL url = new URL(wsdlURL);
			
			QName qName = new QName("http://server.node.dl/","DLServerNodeService");
			
			try {
				HostnameVerifier hv = new HostnameVerifier() {
		            public boolean verify(String arg0, SSLSession arg1) {
		                return true;
		            }
		        };
		        
		        HttpsURLConnection.setDefaultHostnameVerifier(hv);
		        
				Service service = Service.create(url,qName);
						
				FLServer serverNode = service.getPort(FLServer.class);
				
				this.server = serverNode;
		
		
			} catch (WebServiceException ws_err) {
				
				ws_err.printStackTrace();
			}
		
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public InputStream preprocessPropertiesFile(String myFile) throws IOException{
	    Scanner in = new Scanner(new FileReader(myFile));
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    while(in.hasNext()) {
	        out.write(in.nextLine().replace("\\","\\\\").getBytes());
	        out.write("\n".getBytes());
	    }
	    in.close();
	    return new ByteArrayInputStream(out.toByteArray());
	}
	
	public FLServer getServerObject() {
		
		return this.server;
	}
	
	 public byte[] readBinaryFile(String filePath) {
	 		File file = new File(filePath);
	 		FileInputStream fis;
	 		byte[] imageBytes = new byte[(int) file.length()];
	 		try {
	 				fis = new FileInputStream(file);
		
	 				BufferedInputStream inputStream = new BufferedInputStream(fis);
		
	 				inputStream.read(imageBytes);
	 				inputStream.close();
		 
	 			} catch (IOException e) {
	 				e.printStackTrace();
	 			}
		
		return imageBytes;
	 }
	
	
}

