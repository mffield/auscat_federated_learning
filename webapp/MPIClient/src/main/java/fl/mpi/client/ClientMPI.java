package fl.mpi.client;

/*
 * This defines the interface for the methods used in the message passing interface (MPI) on the client side.
 */
 

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ClientMPI {
	
	@WebMethod public boolean initialiseClientMPI();
	@WebMethod public boolean initialiseCentralMPI();
	@WebMethod public int initClientInfo();
	@WebMethod public int addClient();
	@WebMethod public int getClientIndex();
	@WebMethod public String readMasterMessage();
	@WebMethod public int readMasterItr() ;
	@WebMethod public int sendReplyToMaster(int itr, String msg);
	@WebMethod public int setAllowedClientsList(String clients_string);
	@WebMethod public int publishMessageToClients(String input, int itr);
	@WebMethod public boolean didAllClientsReply(int itr);
	@WebMethod public String getClientReply(int ClientIndex);
	@WebMethod public boolean isMasterOn();
	@WebMethod public int setMasterOn();
	@WebMethod public int setMasterOff();
	@WebMethod public int allClientsAdded();
	@WebMethod public int getNumberofCurrentClients();
	
}
