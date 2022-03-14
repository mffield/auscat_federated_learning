package fl.mpi.server;

/*
 * This class implements the methods for the message passing interface.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.jws.WebService;
import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebService(endpointInterface="fl.mpi.server.MessagePassingInterface",portName="MessagePassingInterfacePort", serviceName="MessagePassingInterfaceService")
public class MessagePassingInterfaceImpl implements MessagePassingInterface {
	
	private int number_of_participating_clients=0;
	private int clientPointer=0;
	private boolean MasterOn=false;
	private String ws_password;
	private List<NodeMessages> clients = new ArrayList<NodeMessages>();
	private String[] allowed_clients_list=new String[0];
	
	private Properties prop = new Properties();
	private String WebAppName;
	
	private List<String> projectClients;
	private String client_pw;

	private Logger logger;
	
	
	public boolean loadConfig() {
		
		ServletContext c = ListenerName.context;
        this.WebAppName = c.getContextPath().substring(1);
        this.logger = LogManager.getLogger(this.WebAppName);
        String propertyHome  =  System.getProperty("property.home");
		
        try {	
        	this.prop.load(preprocessPropertiesFile(propertyHome + "\\" + WebAppName + ".properties"));
			this.ws_password = this.prop.getProperty("ws_password");
			
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
	public int initClientInfo() {
	   this.clients.clear();
    	for (int i=0; i< this.number_of_participating_clients;i++)
    	{ 
    		clients.add(new NodeMessages());
    	}   	
    	this.clientPointer=0; 
    	return 0;
    }
    
  
	@Override
	public int AddClient(String ClientName, String password) {
	   
   	
		int index = -1;
		if (loadConfig()) {
			
			this.projectClients = Arrays.asList(this.prop.getProperty("projectsClients").split(","));
			if (this.projectClients.stream().anyMatch(ClientName::equalsIgnoreCase)) {
				this.client_pw = this.prop.getProperty(ClientName.toLowerCase()+"_pw");
				
				if (this.client_pw.compareTo(password.toString()) == 0)	{

		

				System.out.println("Password Checked Correct and client exists on allowed list");
				if (! this.ClientExists(ClientName))
				{
					System.out.println("Client name checked and does not exist");
					NodeMessages temp=new NodeMessages();
					  
					   temp.setClientName(ClientName);
					   this.clients.add(temp);
					   this.clients.set(this.clientPointer, temp);
					   this.clientPointer++; 
					   return this.clientPointer-1;
				}
				else {
					System.out.println("Client already added");
					index=this.getClientIndex(ClientName);
					
					return index;
				}
			}
			else {
				System.out.println("Client name not on allowed list");
				return -1;
			}
		}
		else {
			System.out.println("Password incorrect");
			return -2;
		}
		}
		else {
			System.out.println("Failed to load the configuration file");
			return -3;
		}
		
	}
	
	@Override
   public boolean isClientAllow(String clientName) {

	   boolean allowed_flag=false;
	   for(int i=0;i<this.allowed_clients_list.length;i++)
	   {
		   if(clientName.compareToIgnoreCase(this.allowed_clients_list[i])==0)
		   {
			   allowed_flag=true;
			   break;
		   }
	   }
	return allowed_flag;
	}
	
	@Override
	public boolean ClientExists(String ClientName)	{
	   boolean found=false;
	   for(int i=0; i<this.clients.size();i++)
	   {
		   if( this.clients.get(i).getClientName()==null || (this.clients.get(i).getClientName().compareTo("")==0))
		   {continue;}
		   else
		   {
			   if(this.clients.get(i).getClientName().toString().compareTo(ClientName.toString()) == 0)
			   {
				   found=true;
				   break;
			   }
		   }
	   }
	   
	   System.out.println("The client found flag= "+found);
	   return found;
	}
	
   
	@Override
	public int getClientIndex(String ClientName) {
	   int index=-1;
	   for(int i=0; i<this.clients.size();i++)
	   {
		   if( this.clients.get(i).getClientName()==null || (this.clients.get(i).getClientName().compareTo("")==0))
		   {continue;}
		   else
		   {
			   if(this.clients.get(i).getClientName().toString().compareTo(ClientName.toString()) == 0)
			   {
				   index=i;
				   return index;
			   }
		   }
	   }
	   
	   
	   return index;
	}
   
	@Override
	public String readMasterMessage(int ClientIndex) {
    	return clients.get(ClientIndex).getMasterMessageText().toString();
    }
    
	@Override
	public int readMasterItr(int ClientIndex) {
    	return clients.get(ClientIndex).getMasterMessageIteratio();
    }
   
	@Override
	public int readNextMasterItr(int ClientIndex, int Prev_itr) {
   	while(clients.get(ClientIndex).getMasterMessageIteratio()==Prev_itr)
   	{
   		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			continue;
		}
   	}
   		return clients.get(ClientIndex).getMasterMessageIteratio();
   }
    
	@Override
	public int sendReplyToMaster(int ClientIndex, int itr, String msg) { 
		// This function for the client to send its reply
    	this.clients.get(ClientIndex).putClientMessageText(msg.toString());
    	this.clients.get(ClientIndex).setClientMessageIteration(itr);
    	return 0;
    }
    
   
  /////////////////////////////////////////////////////////////////
    //Methods used by Central algorithm
   
	@Override
	public int setAllowed_Clients_list(String clients_string) {
	   this.allowed_clients_list=clients_string.split(",");
	   this.setNumber_of_participating_clients(this.allowed_clients_list.length);
	   return 0;
   }
	@Override
	public int getNumber_of_participating_clients() {
    	// gets the number of clients allowed to log on before starting the learning
		return number_of_participating_clients;
	}
	@Override
	public int setNumber_of_participating_clients(int number_of_participating_clients) {
		// sets the number of clients allowed to log on before starting the learning
		this.number_of_participating_clients = number_of_participating_clients;
		return 0;
	}
    
	@Override
	public int getNumberofCurrentClients() {
    	// gets the number of clients currently logged on for the learning
    	return this.clientPointer;
    }
    
	@Override
	public int publishMessageToClients(String input, int itr) {
    	// Send Master message to all clients
		NodeMessages temp=new NodeMessages();
    	for (int i=0; i< this.getNumberofCurrentClients();i++)
    	{
    		temp=this.clients.get(i);
    		temp.putMasterMessageText(input.toString());
    		temp.setMasterMessageIteratio(itr);
    		this.clients.set(i, temp);
    		temp=null;
    	}
    	return 0;
    }   
	@Override
   	public boolean didAllClientsReply(int itr) {
    	
    	// return true if all clients replied to the Master message by the same iteration number
    	boolean replied=false;
    	//while (replied==false)
    	//		{
			    	int replies_count=0;
			    	
			    	for (int i=0; i< this.getNumberofCurrentClients();i++) {
			    		System.out.println(this.clients.get(i).getClientName()+"   "+this.clients.get(i).getClientMessageText()+"    "+clients.get(i).getClientMessageIteration()+"--> itr="+itr);
			    		if (this.clients.get(i).getClientMessageIteration() == itr)	    			
			    			{replies_count++;}
			    	}
			    	if (replies_count == this.getNumberofCurrentClients())
			    		{replied=true;}
    	//		}
    	return replied;
    }
	@Override
	public int getClientMessageIteration(int ClientIndex) {
    	return this.clients.get(ClientIndex).getClientMessageIteration();
    }
	@Override
	public String getClientReply(int ClientIndex) {
    	return this.clients.get(ClientIndex).getClientMessageText().toString();
    }
	@Override
	public boolean isMasterOn() {
	   
	   return this.MasterOn;	
	}
	@Override
	public int setMasterOn(String password) {
		
		if (loadConfig()) {
			if (this.ws_password.compareTo(password)==0)  {
				this.MasterOn = true;
				return 0;
			}
			else {
				this.MasterOn = false; // password was incorrect
				return 1;
			}
			
		}
		else {
			return 2; // failed to load configuration
		}
	}
	@Override
	public int setMasterOff() {
		this.MasterOn = false;
		return 0;
	}
	@Override
	public boolean allClientsAdded() {
		return this.clientPointer==this.number_of_participating_clients;

	}
	@Override
	public int ClearAllClientsMessageBoxes() {
		for (int i=0; i< this.clientPointer;i++) {
			this.clients.get(i).clearClientBox();
		}
		return 0;
	}
    

}