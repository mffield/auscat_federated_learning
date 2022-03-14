package fl.mpi.client;


/*
 * The class implements the client-side of the message passing interface. Two MPIs are defined, one for the data centre and one for the central server.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import javax.jws.WebService;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fl.mpi.server.jaxws.MessagePassingInterface;

@WebService(endpointInterface="fl.mpi.client.ClientMPI",portName="MPIPort", serviceName="MPIService")
public class ClientMPIImpl implements ClientMPI {
	
	private String ws_password;
	
	private Properties prop = new Properties();
	private String clientName = "default";
	private String MPIwsdlURL;
	public int clientIndex = -1;
	private boolean setProxy=false;
	private String WebAppName;
	//Interface for client/data centre messages
	public MessagePassingInterface MPI=null;
	//Interface for master/central algorithm messages
	public MessagePassingInterface CentralMPI=null;
	
	// create a log for all messages sent over interfaces
	private Logger logger;
	
    
	public boolean loadConfig() {
		
		ServletContext c = ListenerName.context;
        this.WebAppName = c.getContextPath().substring(1);
        this.logger = LogManager.getLogger(this.WebAppName);
        String propertyHome  =  System.getProperty("property.home");
		
        try {	
        	this.prop.load(preprocessPropertiesFile(propertyHome + "\\" + this.WebAppName + ".properties"));
			this.ws_password = this.prop.getProperty("ws_password");
			
			this.clientName = this.prop.getProperty("clientName");
			this.MPIwsdlURL = this.prop.getProperty("MPIwsdlURL");
			
			System.setProperty("javax.net.ssl.trustStore", this.prop.getProperty("ts_path")); 
	 	    System.setProperty("javax.net.ssl.trustStorePassword", this.prop.getProperty("ts_pass"));
	 	    System.setProperty("javax.net.ssl.keyStore", this.prop.getProperty("ks_path"));
	 	    System.setProperty("javax.net.ssl.keyStorePassword", this.prop.getProperty("ks_pass"));
	 	   
	 	    if (setProxy) {
				System.setProperty("http.proxyHost", this.prop.getProperty("proxyHost"));
				System.setProperty("http.proxyPort", this.prop.getProperty("proxyPort"));
			}
			return true;
			
        } catch (IOException e) {
			this.logger.error("Could not load config.properties file", e);
			e.printStackTrace();
			return false;
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
	
	
	// Methods used by clients 
	@Override
	public boolean initialiseClientMPI() {
		
		if (loadConfig()) {
		this.clientIndex = -1;
		
			try {
				this.logger.info("MPI connection attempt to " + MPIwsdlURL);
				URL url = new URL(MPIwsdlURL);
				QName qName = new QName("http://server.mpi.fl/","MessagePassingInterfaceService");
				
				try {
					
					HostnameVerifier hv = new HostnameVerifier() {
			            public boolean verify(String arg0, SSLSession arg1) {
			                return true;
			            }
			        };
			        HttpsURLConnection.setDefaultHostnameVerifier(hv);
					Service service = Service.create(url,qName);	
					this.MPI = service.getPort(MessagePassingInterface.class);
					
					this.logger.info("MPI connection initialised");
					
				} catch (WebServiceException e) {
					e.printStackTrace();
					return false;
					}
					
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		else {
			return false;
		}
    	
    }	
			
    
	@Override
	public int addClient() {
		this.clientIndex = this.MPI.addClient(this.clientName, this.ws_password);
		this.logger.info("Client " + this.clientName + " added on MPI");
		return this.clientIndex;
	}
	   
	@Override
	public int getClientIndex()
	{
	   return this.clientIndex;
	}
   
	@Override
	public String readMasterMessage() {
		return this.MPI.readMasterMessage(this.clientIndex);
	}
    
	@Override
	public int readMasterItr() {
		return this.MPI.readMasterItr(this.clientIndex);
	}
	
   
	@Override
	public int sendReplyToMaster(int itr, String reply) {
		this.MPI.sendReplyToMaster(this.clientIndex, itr, reply);
		this.logger.info("Reply to server: " + reply);
		return 0;
	}
	
	@Override
	public boolean isMasterOn()	{
		return this.MPI.isMasterOn();
	}
   
  /////////////////////////////////////////////////////////////////
    //Methods used by central algorithm
   
	
	@Override
	public boolean initialiseCentralMPI() {
		
		
		if (loadConfig()) {
		
			try {
				URL url = new URL(MPIwsdlURL);
				QName qName = new QName("http://server.mpi.fl/","MessagePassingInterfaceService");
				
				try {
					HostnameVerifier hv = new HostnameVerifier() {
			            public boolean verify(String arg0, SSLSession arg1) {
			                return true;
			            }
			        };
			        HttpsURLConnection.setDefaultHostnameVerifier(hv);
					Service service = Service.create(url,qName);	
					this.CentralMPI = service.getPort(MessagePassingInterface.class);
					
				} catch (WebServiceException e) {
					e.printStackTrace();
					return false;
				}
					
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}
			
	    	return true;
		}
		else {
			return false;
		}
		
    }	

	
	@Override
	public int initClientInfo() {
		this.CentralMPI.initClientInfo();
		this.logger.info("Central MPI is initialised");
		return 0;
	}
	
	@Override
	public int setAllowedClientsList(String allowed_clients_list) {
		this.CentralMPI.setAllowedClientsList(allowed_clients_list);
		return 0;
	}
	
	@Override
	public int getNumberofCurrentClients() {
		return this.CentralMPI.getNumberofCurrentClients();
	}
    
	@Override
	public int publishMessageToClients(String message, int itr) {
		this.CentralMPI.publishMessageToClients(message, itr);
		this.logger.info("Broadcast to clients: " + message);
		return 0;
	}
	
	@Override
	public boolean didAllClientsReply(int itr) {
		return this.CentralMPI.didAllClientsReply(itr);
	}
	
	@Override
	public String getClientReply(int index) {
		return this.CentralMPI.getClientReply(index);
	}
	
	@Override
	public int setMasterOn()	{
		return this.CentralMPI.setMasterOn(this.ws_password);

	}
	@Override
	public int setMasterOff() {
		return this.CentralMPI.setMasterOff();

	}
	@Override
	public int allClientsAdded()
	{
	   
		return 0;
	}
	
    

}