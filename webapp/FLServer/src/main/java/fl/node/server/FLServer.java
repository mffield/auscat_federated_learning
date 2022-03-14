package fl.node.server;

/*
 * This class defines the federated learning interface server (FLServer) as a set of web service methods that are exposed.
 * 
 */

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
//@SOAPBinding(style=Style.RPC)
public interface FLServer {

	@WebMethod public int initClientInfo();
	@WebMethod public int AddClient(String ClientName, String password1);
	@WebMethod public boolean ClientExists(String ClientName);
	@WebMethod public int getClientIndex(String ClientName);
	@WebMethod public void DeActivateClient(int index);
	@WebMethod public void ActivateClient(int index);
	@WebMethod public boolean isActivatedClient(int index);
	@WebMethod public void uploadProgramPackageToClient(int index, String config_name, String config_contents, String executable_name, byte[] executable_content);
	@WebMethod public void uploadProgramBinaryPackageToClient(int index, String executable_name, byte[] executable_content);
	@WebMethod public String downloadPackageConfigName(int index);
	@WebMethod public String downloadPackageConfigContent(int index);
	@WebMethod public String downloadPackageExecutableName(int index);
	@WebMethod public byte[] downloadPackageExecutableContent(int index);
	@WebMethod public byte[] downloadBinaryPackageExecutableContent(int index);
	@WebMethod public boolean isPackageComplete(int index);
	@WebMethod public void clearPackage(int index);
	@WebMethod public int getNumber_of_participating_clients();
	@WebMethod public void setNumber_of_participating_clients(int number_of_participating_clients);
	@WebMethod public int getNumberofCurrentAddedClients();
	@WebMethod public int getNumberOfActiveClients();
	@WebMethod public String[] getActiveClientsList();
	@WebMethod public String[] getClientsList();
	@WebMethod public int[] getActiveClientsListIndexes();
	@WebMethod public boolean isMasterOn();
	@WebMethod public void setMasterOn(boolean masterOn);
	@WebMethod public void publishEXEToClients(String file_name, byte[] file_content);
	@WebMethod public void publishEXEandConfigToClients(String exefile_name, byte[] exefile_content, String configFile_name, String configFile_content);
	@WebMethod public boolean allClientsAdded();
	@WebMethod public void ClearAllClientsPackages();
	@WebMethod public boolean AllPackagesReceived();
	@WebMethod public boolean getPackageReceived(int index);
	@WebMethod public void setPackageReceived(int index);
	@WebMethod public void setClientStatus(String clientName, String password, String status);
	@WebMethod public String getClientStatus(int clientIndex);
	@WebMethod public String getClientName(int clientIndex);
	@WebMethod public void setStartJob();
	@WebMethod public void setFinishJob();
	@WebMethod public boolean getStartJob(int clientIndex);
	@WebMethod public void setAlgorithmOutput(String clientName, String password, String status);
	@WebMethod public String getAlgorithmOutput(int clientIndex);
	
}
