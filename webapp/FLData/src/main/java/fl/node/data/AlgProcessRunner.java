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
 * This class implements a process runner for the algorithms in the federated learning system.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import fl.node.server.jaxws.FLServer;

public class AlgProcessRunner implements Runnable {	
	
	private FLDataUtil util = new FLDataUtil();
	private FLServer serverNode = null;
	private Process pr = null;
	private Runtime rt = null;
	
	public AlgProcessRunner(FLDataUtil util, FLServer serverNode) {
		this.util = util;
		this.serverNode = serverNode;
	}
	
	public void run() {
		
		try {
			
			String line="";
			String doc="";
			String[] commands = {this.util.binaryFilePath+this.util.exeFileName,this.util.algorithmConfig,this.util.MPIClient};
			this.util.logger.info(this.util.binaryFilePath + this.util.exeFileName + " '" + this.util.algorithmConfig + "' " + this.util.MPIClient);
									
			this.util.stopProcessRunning(this.util.exeFileName);
			this.util.logger.info("Stopped process from running.");
						        
			this.rt = Runtime.getRuntime();
			this.pr = this.rt.exec(commands);
						        
			this.util.logger.info("Started process.");
								        
			BufferedReader bri = new BufferedReader(new InputStreamReader(this.pr.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(this.pr.getErrorStream()));
								        	
			
			while ((line = bri.readLine()) != null) {			
				doc = doc + " <br> " + line; 
				this.util.logger.info("info line is: " + line);
				this.serverNode.setAlgorithmOutput(this.util.clientName, this.util.password, doc);				
			}			
			
			bri.close();					    
			while ((line = bre.readLine()) != null) {
								    	
				doc = doc + " <br> " + line; 
				this.util.logger.info("error line is: " + line);
				this.serverNode.setAlgorithmOutput(this.util.clientName, this.util.password, doc);
			}
			
			bre.close(); 
			this.util.logger.info("Algorithm done.");									
			this.serverNode.setFinishJob();
			
		} catch (IOException e) {
								
			e.printStackTrace();
			return;
		}
		
		catch (InterruptedException e) {
			e.printStackTrace();
			this.util.logger.error("Interrupted the process thread.");
			
			this.pr.destroy();
			
			return;
		}
	}
	
}