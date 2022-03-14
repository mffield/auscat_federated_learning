package fl.node.data;

/*
 * Scheduled data node class initializes periodic processes for algorithm transfer and execution
 */

import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class ScheduledDataNode implements ServletContextListener  {

		private ExtendScheduledExecutor scheduler;
		
		@Override
		public void contextInitialized(ServletContextEvent event) {
			
		    new ExtendScheduledExecutor(2).scheduleWithFixedDelay(new FLData(), 0, 2, TimeUnit.SECONDS); 
		    new ExtendScheduledExecutor(4).scheduleWithFixedDelay(new AlgorithmExecutor(), 0, 2, TimeUnit.SECONDS); 
		    
		}

		@Override
		public void contextDestroyed(ServletContextEvent event) {
		    scheduler.shutdownNow();
		 }

	
}

