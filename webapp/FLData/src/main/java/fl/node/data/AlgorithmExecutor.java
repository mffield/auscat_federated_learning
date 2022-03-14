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
 * This class implements a scheduled process for running an algorithm and starts a new thread with AlgProcessRunner to run the algorithm once conditions are met.
 */

import fl.node.server.jaxws.FLServer;

public class AlgorithmExecutor implements Runnable {	
	

	public void run() {
		
		try {
			
		
		FLServer serverNode=null;
		int clientIndex = 0;
		
		FLDataUtil util = new FLDataUtil();
		
		if (util != null)
		{
				
			serverNode = util.serverConnect();
			if (serverNode != null) {
				
				if (serverNode.isMasterOn()) {		
								
					if (serverNode.clientExists(util.clientName)) {
						
						clientIndex = serverNode.getClientIndex(util.clientName);
						
						if (serverNode.isActivatedClient(clientIndex)) {
							
							if (serverNode.getStartJob(clientIndex))
							{
								util.exeFileName = serverNode.downloadPackageExecutableName(clientIndex);
								util.logger.info("AlgExec: Starting the process:"+util.exeFileName);
								serverNode.setClientStatus(util.clientName, util.password, "Job Starting");
			
								try {
										
									Thread t1 = new Thread(new AlgProcessRunner(util, serverNode));
									t1.start();
									
									while (serverNode.getStartJob(clientIndex))
									{
										Thread.sleep(200);
										
									}
									if (!serverNode.getStartJob(clientIndex))
									{
										t1.interrupt();
										util.logger.info("AlgExec: Algorithm process interrupted.");
									}
									else
									{
										util.logger.info("Algorithm done.");
									}
																		
									
								    serverNode.setFinishJob();
								}
								catch (InterruptedException e) {
									e.printStackTrace();
									return;
								}
						}					
					}
					
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