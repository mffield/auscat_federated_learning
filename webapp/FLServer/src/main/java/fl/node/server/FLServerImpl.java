package fl.node.server;

/*
 * This class implements the methods in FLServer web service definition
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.ServletContext;

import javax.xml.ws.WebServiceContext;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@WebService(endpointInterface="fl.node.server.FLServer",portName="FLServerPort", serviceName="FLServerService")
public class FLServerImpl implements FLServer {

	private int number_of_participating_clients=0;
	private int clientPointer=0;
	private boolean MasterOn=true;
	private String propertyHome  =  System.getProperty("property.home");
	private Properties prop = new Properties();
	private Logger logger;
	//private String ws_password="";
	private List<ProgramPackage> clients = new ArrayList<ProgramPackage>();
		
	private String WebAppName;
	
	private List<String> projectClients;
	private String client_pw;
	
	@Resource
    private WebServiceContext wsCtx;
	
	@Override
	public int initClientInfo() 
	{
		// this fn initial the list of clients
		this.clients.clear();
    	for (int i=0; i< this.number_of_participating_clients;i++) 	{ 
    		clients.add(new ProgramPackage());
    	}
    	this.clientPointer=0;
    	
    	return 0;
	}
	
	@Override
	   public int AddClient(String ClientName, String password) {
		
		
		int index = -1;
		/*// Use this section to retrieve the client name from the certificate instead of explicit message
		MessageContext msgCtx = wsCtx.getMessageContext();
		HttpServletRequest httpSR = (HttpServletRequest)msgCtx.get(MessageContext. SERVLET_REQUEST );
		String x500Prin = ""; 
		try { 
				X509Certificate[] x509Cert = (X509Certificate[])httpSR.getAttribute( "javax.servlet.request.X509Certificate"); 
				x500Prin = x509Cert[0].getSubjectX500Principal().getName();
			} 
		catch (Exception e) {
			System.out.println("Error =======> " + e.getMessage()); 
			} 
		System.out.println("x500Prin: " + x500Prin); 
		*/
		
		ServletContext c = ListenerName.context;
		this.WebAppName = c.getContextPath().substring(1);
		
		this.logger = LogManager.getLogger(this.WebAppName);
		
		try {	
			this.prop.load(new FileInputStream(new File(this.propertyHome + "\\" + this.WebAppName + ".properties")));
			
			this.projectClients = Arrays.asList(prop.getProperty("projectsClients").split(","));
			if (this.projectClients.stream().anyMatch(ClientName::equalsIgnoreCase)) {
				this.client_pw = prop.getProperty(ClientName.toLowerCase()+"_pw");
				System.out.println(this.client_pw);
				System.out.println(ClientName.toLowerCase()+"_pw");
				System.out.println(password);
				if (this.client_pw.compareTo(password) == 0)	{
					
					
					this.logger.info("Password Checked Correct");
					if (! this.ClientExists(ClientName))
					{
						
						this.logger.info("Client name checked and does not exist");
						 ProgramPackage temp=new ProgramPackage();
						  
						   temp.setClient_name(ClientName.toString());
						   
						   
						   this.logger.info(this.clientPointer);
						   this.clients.add(temp);
						   this.clients.set(this.clientPointer, temp);
						   this.clientPointer++;
						   return this.clientPointer-1;
					}
					else
					{ 
						index=this.getClientIndex(ClientName);
						//this.clients.get(index).Activate();						
						return index;
					}
				}
				else {
					this.logger.info("Password incorrect for this client connection.");
					return index;
				}
				
			}
			else {
				this.logger.info("Client name was not on the project list.");
				return index;
			}
			
		} catch (IOException e) {
			//logger.error("Could not load config.properties file", e);
			e.printStackTrace();
			return -1;
		}

		
	   			
	   }
	@Override
		public boolean ClientExists(String ClientName)
		{
		
			ServletContext c = ListenerName.context;
			this.WebAppName = c.getContextPath().substring(1);
		
			this.logger = LogManager.getLogger(this.WebAppName);
		   boolean found=false;
		   for(int i=0; i<this.clients.size();i++)
		   {
			   if( this.clients.get(i).getClient_name()==null || (this.clients.get(i).getClient_name().compareTo("")==0))
			   {continue;}
			   else
			   {
				   if(this.clients.get(i).getClient_name().toString().compareTo(ClientName.toString()) == 0)
				   {
					   found=true;
					   break;
				   }
			   }
		   }
		   return found;
		}
		
	@Override
	public int getClientIndex(String ClientName)
	{
	   int index=-1;
	   for(int i=0; i<this.clients.size();i++)
	   {
		   if( this.clients.get(i).getClient_name()==null || (this.clients.get(i).getClient_name().compareTo("")==0))
		   {continue;}
		   else
		   {
			   if(this.clients.get(i).getClient_name().toString().compareTo(ClientName.toString()) == 0)
			   {
				   index=i;
				   return index;
			   }
		   }
	   }
	   return index;
	}
	@Override
    public void DeActivateClient(int index)
    {
	    this.clients.get(index).DeActivate();

    }
	@Override
	public void ActivateClient(int index)
	{
		this.clients.get(index).Activate();
	}
	@Override
	public boolean isActivatedClient(int index)
	{
		if (index==-1 || index > this.clientPointer) {
			return false;
		}
		else {
			return !this.clients.get(index).isInActive();
		}	
		
	}
	
	@Override
   public void uploadProgramPackageToClient(int index, String config_name, String config_contents, String executable_name, byte[] executable_content)
   {
	    ProgramPackage temp=new ProgramPackage();
	    temp=this.clients.get(index);
   		temp.setExecutable_file_name(executable_name);
   		temp.setBinary_content(executable_content);
   		temp.setConfig_file_name(config_name);
   		temp.setConfig_file_content(config_contents);
   		temp.setPackage_complete(true);
   		this.clients.set(index, temp);
   		temp=null; 

   }
   
	@Override
   public void uploadProgramBinaryPackageToClient(int index, String executable_name, byte[] executable_content)
   {
	   ProgramPackage temp=new ProgramPackage();
	    temp=this.clients.get(index);
   		temp.setExecutable_file_name(executable_name);
   		temp.setBinary_content(executable_content);
   		temp.setPackage_complete(true);
   		this.clients.set(index, temp);
   		temp=null;
   		

   }
   
	@Override
   public String downloadPackageConfigName(int index)
   {
	   return this.clients.get(index).getConfig_file_name();
   }
	@Override
   public String downloadPackageConfigContent(int index)
   {
	   return this.clients.get(index).getConfig_file_content();
   }
   
	@Override
   public String downloadPackageExecutableName(int index)
   {
	   return this.clients.get(index).getExecutable_file_name();
   }
	@Override
   public byte[] downloadPackageExecutableContent(int index)
   {
	   //return this.clients.get(index).getExecutable_file_content();
	   return this.clients.get(index).getBinary_content();
	   //return this.download(this.filePath);
   }
	@Override
   public byte[] downloadBinaryPackageExecutableContent(int index)
   {
	   return this.clients.get(index).getBinary_content();
   }
	@Override
   public boolean isPackageComplete(int index) 
   {
	   return this.clients.get(index).isPackage_complete();
   }
	@Override
   public void clearPackage(int index)
   {
	   this.clients.get(index).clearPackage();

   }
   
	@Override
    public int getNumber_of_participating_clients() {
    	// gets the number of clients allowed to log on before starting the learning
		return number_of_participating_clients;
	}
	
	@Override
	public void setNumber_of_participating_clients(int number_of_participating_clients) {
		// sets the number of clients allowed to log on before starting the learning
		this.number_of_participating_clients = number_of_participating_clients;

	}
    
	@Override
    public int getNumberofCurrentAddedClients()
    {
    	// gets the number of clients currently logged on for the learning
    	return this.clientPointer;
    }
   
	@Override
   public int getNumberOfActiveClients()
   {
	   int active_counter=0;
	   for(int i=0; i< this.clients.size();i++)
	   {
		   if(this.clients.get(i).isInActive()==false)
		   {
			   active_counter++;
		   }
	   }
	   return active_counter;
   }
	@Override
   public String[] getActiveClientsList()
   {
	   int active_list_counter=0;
	   String[] active_list=new String[this.getNumberOfActiveClients()];
	   for (int i=0; i< this.clients.size();i++)
	   {
		   if(this.clients.get(i).isInActive()==false)
		   {
			   active_list[active_list_counter]=this.clients.get(i).getClient_name();
			   active_list_counter++;
		   }
	   }
	   return active_list;
   }
	@Override
	   public String[] getClientsList()
	   {
		   int list_counter=0;
		   String[] client_name_list=new String[this.getNumberofCurrentAddedClients()];
		   for (int i=0; i< this.clients.size();i++)
		   {
				   client_name_list[list_counter]=this.clients.get(i).getClient_name();
				   list_counter++;
		   }
		   return client_name_list;
	   } 
	
   @Override
   public int[] getActiveClientsListIndexes()
   {
	   int active_list_counter=0;
	   int[] active_list=new int[this.getNumberOfActiveClients()];
	   for (int i=0; i< this.clients.size();i++)
	   {
		   if(this.clients.get(i).isInActive()==false)
		   {
			   active_list[active_list_counter]=i;
			   active_list_counter++;
		   }
	   }
	   return active_list;
   }
   @Override
   public boolean isMasterOn() {
		return MasterOn;
	}
   @Override
	public void setMasterOn(boolean masterOn) {
		MasterOn = masterOn;

	}
	
   @Override
    public void publishEXEToClients(String file_name, byte[] file_content) 
	{
    	// Send Master message to all clients
    	for(int i=0;i< this.clients.size();i++)
		{
			if (isActivatedClient(i))
			{
				this.uploadProgramBinaryPackageToClient(i, file_name, file_content);
			}
		}
    }
	
   @Override
    public void publishEXEandConfigToClients(String exefile_name, byte[] exefile_content, String configFile_name, String configFile_content ) 
	{
    	
    	for(int i=0;i< this.clients.size();i++)
		{
			if (isActivatedClient(i))
			{
				this.uploadProgramPackageToClient(i, configFile_name, configFile_content, exefile_name, exefile_content);
			}
		}
    }
	
   @Override
	public boolean allClientsAdded()
	{
	   	System.out.println("The number of active clients = "+this.getNumberOfActiveClients());
	   	System.out.println("number of particpating = "+ this.number_of_participating_clients);
	   	
		return this.getNumberOfActiveClients()==this.number_of_participating_clients;
	}
	
   @Override
	public void ClearAllClientsPackages()
	{
		for (int i=0; i< this.clients.size();i++)
		{
			//this.clients.get(i).DeActivate();
			this.clients.get(i).clearPackage();
		}
	}
	
	@Override
	public boolean AllPackagesReceived()
	{
		/*
		int[] active_clients=this.getActiveClientsListIndexes();
		int clients_received_counter=0;
		for(int i=0;i<active_clients.length;i++)
		{
			if(this.clients.get(active_clients[i]).isPackage_received()==true)
			{
				clients_received_counter++;
			}
		}
		//return clients_received_counter==active_clients.length;
		*/
		for(int i=0;i< this.clients.size();i++)
		{
			if (isActivatedClient(i))
			{
				if(this.clients.get(i).isPackage_received()==false)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean getPackageReceived(int index) {
		return this.clients.get(index).isPackage_received();
	}
	
	@Override
	public void setPackageReceived(int index)
	{
		this.clients.get(index).setPackage_received(true);
	}

	@Override
	public void setClientStatus(String clientName, String password, String status)
	{
		int index;
		
		this.client_pw = prop.getProperty(clientName.toLowerCase()+"_pw");
		
		if(this.client_pw.compareTo(password.toString()) == 0)
		{
			index = this.getClientIndex(clientName);
			if (index==-1) {
				this.clients.get(index).setClientStatus("null");
			}
			else {
				this.clients.get(index).setClientStatus(status);
			}
		}
	}
	
	@Override
	public String getClientStatus(int clientIndex)
	{
		if (this.clients.size() >= clientIndex+1 ) {
			return this.clients.get(clientIndex).getClientStatus();	
		}
		else {
			return "null";
		}
	}
	
	@Override
	public void setAlgorithmOutput(String clientName, String password, String status)
	{
		int index;
		this.client_pw = prop.getProperty(clientName.toLowerCase()+"_pw");
		if(this.client_pw.compareTo(password.toString()) == 0)
		{
			index = this.getClientIndex(clientName);
			this.clients.get(index).setAlgorithmOutput(status);	
		}
	}
	
	@Override
	public String getAlgorithmOutput(int clientIndex)
	{
		if (this.clients.size() >= clientIndex+1 ) {
			return this.clients.get(clientIndex).getAlgorithmOutput();	
		}
		else {
			return "null";
		}
	}
	
	@Override
	public String getClientName(int clientIndex)
	{
		if (this.clients.size() >= clientIndex+1 ) {
			return this.clients.get(clientIndex).getClient_name();	
		}
		else {
			return "null";
		}
	}
	
	@Override
	public void setStartJob()
	{
		for (int i=0; i < this.clients.size(); i++)
		{
			this.clients.get(i).startJob();
		}
	}
	
	@Override
	public void setFinishJob()
	{
		for (int i=0; i < this.clients.size(); i++)
		{
			this.clients.get(i).finishJob();
		}
	}
	
	@Override
	public boolean getStartJob(int clientIndex)
	{
		return this.clients.get(clientIndex).isStartingJob();
	}
	
}
