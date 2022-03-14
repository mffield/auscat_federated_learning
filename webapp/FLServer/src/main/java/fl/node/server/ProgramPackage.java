package fl.node.server;

/*
 * This class implements package handling function for the federated learning interface.
 * 
 */
public class ProgramPackage {
	private String client_name;
	private String executable_file_name;
	private String executable_file_content;
	private String config_file_name;
	private String config_file_content;
	private String client_feedback;
	private String client_status;
	private String algorithm_output;
	private boolean start_job;
	private boolean package_complete;
	private boolean inActive;
	private boolean package_received;
	private byte[] binary_content;
	
	public ProgramPackage(String client_name, String executable_file_name,
			String executable_file_content, String config_file_name,
			String config_file_content) {
		super();
		this.client_name = client_name;
		this.executable_file_name = executable_file_name;
		this.executable_file_content = executable_file_content;
		this.config_file_name = config_file_name;
		this.config_file_content = config_file_content;
		this.client_feedback="";
		this.algorithm_output="";
		this.client_status="";
		this.inActive=true;
		this.start_job=false;
		this.package_complete=false;
		this.package_received=false;
	}
	
	public ProgramPackage(String client_name, String executable_file_name,
			String executable_file_content) {
		super();
		this.client_name = client_name;
		this.executable_file_name = executable_file_name;
		this.executable_file_content = executable_file_content;
		this.config_file_content="";
		this.config_file_name="";
		this.client_feedback="";
		this.algorithm_output="";
		this.inActive=true;
		this.start_job=false;
		this.package_complete=false;
		this.package_received=false;
	}
	
	

	public ProgramPackage() {
		super();
		this.client_name = "";
		this.executable_file_name = "";
		this.executable_file_content = "";
		this.config_file_name = "";
		this.config_file_content = "";
		this.client_feedback="";
		this.algorithm_output="";
		this.inActive=true;
		this.start_job=false;
		this.package_complete=false;
		this.package_received=false;
	}

	public boolean isInActive() {
		return inActive;
	}
	public void setInActive(boolean inActive) {
		this.inActive = inActive;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getExecutable_file_name() {
		return executable_file_name;
	}
	public void setExecutable_file_name(String executable_file_name) {
		this.executable_file_name = executable_file_name;
	}
	public String getExecutable_file_content() {
		return executable_file_content;
	}
	public void setExecutable_file_content(String executable_file_content) {
		this.executable_file_content = executable_file_content;
	}
	public String getConfig_file_name() {
		return config_file_name;
	}
	public void setConfig_file_name(String config_file_name) {
		this.config_file_name = config_file_name;
	}
	public String getConfig_file_content() {
		return config_file_content;
	}
	public void setConfig_file_content(String config_file_content) {
		this.config_file_content = config_file_content;
	}
	
	public void clearPackage()
	{
		this.config_file_content="";
		this.config_file_name="";
		this.executable_file_content="";
		this.executable_file_name="";
		this.package_complete=false;
		this.package_received=false;
		this.start_job=false;
	}
	
	public void startJob()
	{
		if (!this.inActive)
		{
			this.start_job=true;
		}
	}
	public void finishJob()
	{
		this.start_job=false;
	}
	
	public boolean isStartingJob()
	{
		return this.start_job;
	}
	
	
	public void Activate()
	{
		this.inActive=false;
	}
	public void DeActivate()
	{
		this.inActive=true;
	}
	public boolean isEmpty()
	{
		if(this.executable_file_content.trim().compareTo("")==0 && this.executable_file_name.trim().compareTo("")==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getClient_feedback() {
		return client_feedback;
	}

	public void setClient_feedback(String client_feedback) {
		this.client_feedback = client_feedback;
	}
	
	public String ping()
	{
		return "Running";
	}

	public boolean isPackage_complete() {
		return package_complete;
	}

	public void setPackage_complete(boolean package_complete) {
		this.package_complete = package_complete;
	}

	public boolean isPackage_received() {
		return package_received;
	}

	public void setPackage_received(boolean package_received) {
		this.package_received = package_received;
	}

	public byte[] getBinary_content() {
		return this.binary_content;
	}

	public void setBinary_content(byte[] binary_content1) {
		this.binary_content = binary_content1;
	}
	
	public void setClientStatus(String ClientStatus) {
		this.client_status = ClientStatus;
	}
	
	public String getClientStatus() {
		return this.client_status;
	}
	
	public void setAlgorithmOutput(String AlgorithmOutput) {
		this.algorithm_output = AlgorithmOutput;
	}
	
	public String getAlgorithmOutput() {
		return this.algorithm_output;
	}
	
}

