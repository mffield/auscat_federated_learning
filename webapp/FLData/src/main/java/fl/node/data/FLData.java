/*Copyright 2018 <Matthew Field> <University of New South Wales> email: matthew.field@unsw.edu.au

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial 
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package fl.node.data;

/*
 * This class is client side of the federated learning system for algorithm deployment. It connects to the central server and listens for activation of node and package for transfer.
 */

import java.io.IOException;

import fl.node.server.jaxws.FLServer;

public class FLData implements Runnable {	
	
 
	public void run() 
	{
		try {
		// TODO Auto-generated method stub		
		FLServer serverNode=null;
		
		int clientIndex = -1;
		
		FLDataUtil util = new FLDataUtil();
		if (util != null)
		{
			serverNode = util.serverConnect();
			if (serverNode != null) {
				
				if (serverNode.isMasterOn()) {		
								
					if (!serverNode.clientExists(util.clientName)) {
						clientIndex = serverNode.addClient(util.clientName, util.password);
						if (clientIndex==-1) {
							util.logger.info("Password incorrect for adding client");
							return;
						}
						else {
							util.logger.info("Added client");
						}
					}
					else
					{
						clientIndex = serverNode.getClientIndex(util.clientName);
					}
		

					if (serverNode.isActivatedClient(clientIndex)) {
						
						if (serverNode.isPackageComplete(clientIndex) && !serverNode.getPackageReceived(clientIndex)){
							
							
							util.logger.info("Package completed with name: " + util.exeFileName);
							serverNode.setClientStatus(util.clientName, util.password, "Package ready to download");
							
							// download package
							byte[] contents_instring_format = serverNode.downloadBinaryPackageExecutableContent(clientIndex) ;
							util.exeFileName = serverNode.downloadPackageExecutableName(clientIndex);
							//System.out.println("Package completed with name: "+util.exeFileName);
							
							// write binary file to local system
							serverNode.setClientStatus(util.clientName, util.password, "Package downloaded");
							serverNode.setPackageReceived(clientIndex);
							try {
								util.stopProcessRunning(util.binaryFilePath+util.exeFileName);
							} catch (IOException e) {
								e.printStackTrace();
							}
							catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							util.writeBinaryFile(contents_instring_format, util.binaryFilePath+util.exeFileName);
							serverNode.setPackageReceived(clientIndex);
							
							//System.out.println("The file is written at client:"+util.clientName);
							util.logger.info("The file is written at client:"+util.clientName);
						}
						else if (serverNode.getPackageReceived(clientIndex))
						{
							//System.out.println("Downloaded waiting for start");
							util.logger.info("Downloaded waiting for start");
							serverNode.setClientStatus(util.clientName, util.password, "Ready to start");
						}	
						else
						{
							//System.out.println("Waiting for package to be complete"); 
							util.logger.info("Waiting for package to be complete");
							serverNode.setClientStatus(util.clientName, util.password, "Waiting for algorithm");
						}
						
			
					}
					else {
						util.logger.info("Client " + util.clientName + " with ID= "+ clientIndex + " is an inactivated client");
						serverNode.setClientStatus(util.clientName, util.password, "Inactive");
						//serverNode.setMasterOn(true);
					}
				}
			}
			
			else {
				util.logger.info("Server connection returned null");
				return;
			}
		}
	}
	catch (Exception e) {
        e.printStackTrace();
	}
	
	}
}

