package fl.node.data;

/*
 * Class handling functions run in FLData including processing properties/parameter files, connecting to server and stopping processes. 
 */


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import fl.node.server.jaxws.FLServer;

public class FLDataUtil {

	
	public Properties prop = new Properties();
	public String clientName = "default";
	public String password;
	public String wsdlURL;
	public String binaryFilePath; 
	public String exeFileName="";
	public String currentJobName="";
	public String previousJobName="";
	public String MPIClient="";
	public String algorithmConfig="";
	
	boolean setProxy=false;
	
	public Logger logger;
	
	
	public FLDataUtil() {
		
		ServletContext c = ListenerName.context;
        String WebAppName = c.getContextPath().substring(1);
        String propertyHome  =  System.getProperty("property.home");
        
        this.logger = LogManager.getLogger(WebAppName);
		
		try {
			
			
			this.prop.load(preprocessPropertiesFile(propertyHome + "\\" + WebAppName + ".properties"));
			//this.prop.load(new FileInputStream(new File(this.configFilepath + WebAppName + ".properties")));
			this.clientName = this.prop.getProperty("clientName");
			this.wsdlURL = this.prop.getProperty("wsdlURL");
			this.setProxy = Boolean.valueOf(this.prop.getProperty("setProxy"));
			this.binaryFilePath = this.prop.getProperty("binaryFilePath");
			this.MPIClient = this.prop.getProperty("MPIClient");
			this.algorithmConfig = propertyHome + '\\' + this.prop.getProperty("algorithmConfig") + ".properties";
			this.password = this.prop.getProperty("ws_password");
			System.setProperty("javax.net.ssl.trustStore", this.prop.getProperty("ts_path")); 
	 	    System.setProperty("javax.net.ssl.trustStorePassword", this.prop.getProperty("ts_pass"));
	 	    System.setProperty("javax.net.ssl.keyStore", this.prop.getProperty("ks_path"));
	 	    System.setProperty("javax.net.ssl.keyStorePassword", this.prop.getProperty("ks_pass"));
			
	 	    
	 	    System.setProperty("java.net.useSystemProxies","true");
	 	    if (this.setProxy) {
				System.setProperty("https.proxyHost", this.prop.getProperty("proxyHost"));
				System.setProperty("https.proxyPort", this.prop.getProperty("proxyPort"));
				System.out.println(System.getProperty("https.proxyHost"));
				System.out.println(System.getProperty("https.proxyPort"));
			}
			
		} catch (IOException e) {
			this.logger.error("Could not load config.properties file", e);
			return;
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
	
	public FLServer serverConnect() {
		
		FLServer serverNode = null;
		System.setProperty("com.sun.xml.internal.ws.developer.JAXWSProperties.CONNECT_TIMEOUT","2000");
		System.setProperty("sun.net.client.defaultConnectTimeout", "2000");
		try 
		{
			URL url = new URL(this.wsdlURL);
			QName qName = new QName("http://server.node.fl/","FLServerService");
			
			try {
				//SSLContext sc = SSLContext.getInstance("SSL");
				HostnameVerifier hv = new HostnameVerifier() {
		            public boolean verify(String arg0, SSLSession arg1) {
		                return true;
		            }
		        };
		        
		        HttpsURLConnection.setDefaultHostnameVerifier(hv);
				
				Service service = Service.create(url,qName);	
				serverNode = service.getPort(FLServer.class);
							
			} catch (WebServiceException ws_err) {
				ws_err.printStackTrace();
				return null;
				}
				
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		return serverNode;
		
	}
	
	
	
	
	public boolean stopProcessRunning(String applicationFilePath) throws IOException, InterruptedException
    {
        
    	File application = new File(applicationFilePath);
        String applicationName = application.getName();
        String line;
    	ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
        Process p1 = processBuilder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        while ((line = br.readLine()) != null) {
			if (line.contains(applicationName)) {
				String[] linesplit = line.split("\\s+");
				String[] killcommand = {"taskkill","/pid",linesplit[1],"/f"};
				new ProcessBuilder(killcommand).start();
			}
		}
        return true;
    }
	
	
	public void writeBinaryFile(byte[] aBytes, String aFileName)
	 {
		try {
	        FileOutputStream fos = new FileOutputStream(aFileName);
	        BufferedOutputStream outputStream = new BufferedOutputStream(fos);
	        outputStream.write(aBytes);
	        outputStream.close();
	         
	        System.out.println("File downloaded: " + aFileName);
	    } catch (IOException ex) {
	        System.err.println(ex);
	    }
	 }
	
	
		
	
}
