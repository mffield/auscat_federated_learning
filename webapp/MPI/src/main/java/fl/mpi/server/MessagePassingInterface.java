package fl.mpi.server;

/*
 * This defines the interface for the methods used in the message passing interface (MPI) on the server side.
 */

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
//@SOAPBinding(style=Style.RPC)
public interface MessagePassingInterface {

	@WebMethod public int initClientInfo();
	@WebMethod public int AddClient(String ClientName, String password1);
	@WebMethod public boolean isClientAllow(String clientName);
	@WebMethod public boolean ClientExists(String ClientName);
	@WebMethod public int getClientIndex(String ClientName);
	@WebMethod public String readMasterMessage(int ClientIndex);
	@WebMethod public int readMasterItr(int ClientIndex) ;
	@WebMethod public int readNextMasterItr(int ClientIndex, int Prev_itr);
	@WebMethod public int sendReplyToMaster(int ClientIndex, int itr, String msg);
	@WebMethod public int setAllowed_Clients_list(String clients_string);
	@WebMethod public int getNumber_of_participating_clients();
	@WebMethod public int setNumber_of_participating_clients(int number_of_participating_clients);
	@WebMethod public int publishMessageToClients(String input, int itr);
	@WebMethod public boolean didAllClientsReply(int itr);
	@WebMethod public int getClientMessageIteration(int ClientIndex);
	@WebMethod public String getClientReply(int ClientIndex);
	@WebMethod public boolean isMasterOn();
	@WebMethod public int setMasterOn(String pass);
	@WebMethod public int setMasterOff();
	@WebMethod public boolean allClientsAdded();
	@WebMethod public int ClearAllClientsMessageBoxes();
	@WebMethod public int getNumberofCurrentClients();
	
	
}

