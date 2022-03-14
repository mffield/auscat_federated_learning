package fl.mpi.server;

/*
 * This class defines the storage of messages and iterations.
 */

public class Message {
	
	private String sender;
	private String text;
	private int iteration;
	
	
	public Message() {
		super();
		this.sender = "";
		this.text = "";
		this.iteration = -1;
	}
	
	public Message(String sender, String text, int iteration) {
		super();
		this.sender = sender;
		this.text = text;
		this.iteration = iteration;
	}
	
	
	public int getIteration() {
		return iteration;
	}
	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getMessageText() {
		return text;
	}
	public void setMessageText(String text) {
		this.text = text;
	}
	
	public boolean isEmptyMessage()
	{
		if (this.sender== "" && this.text=="" && this.iteration==-1 )
			return true;
		else
			return false;
	}
	
	public void clearMessage()
	{
		this.sender="";
		this.text="";
		this.iteration=-1;
	}
	
	
}
