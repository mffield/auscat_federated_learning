package fl.mpi.server;

/*
 * This class defines the structure of storage of messages per client in the network.
 */

public class NodeMessages {
	
	private Message[] messagefield;
	
	// Messages From central algorithm will be in element number 0 
	//and from client will be in number 1.
	private String ClientName;
	
		
	public NodeMessages() {
		super();
		messagefield = new Message [2];
		messagefield[0]= new Message();
		messagefield[1]= new Message();
	}
	
	
	public String getClientName() {
		return ClientName;
	}
	public void setClientName(String clientName) {
		ClientName = clientName;
	}
	
	public int getMasterMessageIteratio()
	{
		return messagefield[0].getIteration();
	}
	public void setMasterMessageIteratio(int itr)
	{
		messagefield[0].setIteration(itr);
	}
	public String getMasterMessageText()
	{
		return messagefield[0].getMessageText();
	}
	
	public void putMasterMessageText(String MasterMessage)
	{
		messagefield[0].setMessageText(MasterMessage);
	}
	
	public void setMasterNameAsSender(String master_sender_name)
	{
		messagefield[0].setSender(master_sender_name);
	}
	
	public boolean isFromMasterBoxEmpty()
	{
		return messagefield[0].isEmptyMessage();
	}
	
	public int getClientMessageIteration()
	{
		return messagefield[1].getIteration();
	}
	
	public void setClientMessageIteration(int itr)
	{
		messagefield[1].setIteration(itr);
	}
	
	public String getClientMessageText()
	{
		return messagefield[1].getMessageText();
	}
	
	public void putClientMessageText(String text)
	{
		messagefield[1].setMessageText(text);
	}
	public void putClientNameAsSender()
	{
		messagefield[1].setSender(this.ClientName);
	}
	
	public boolean isFromClientBoxEmpty()
	{
		return messagefield[1].isEmptyMessage();
	}
	
	public void clearClientBox()
	{
		messagefield[1].clearMessage();
	}
	
	

}
