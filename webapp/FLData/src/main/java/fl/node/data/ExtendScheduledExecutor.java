package fl.node.data;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* 
 * 
 */
public class ExtendScheduledExecutor extends ScheduledThreadPoolExecutor {

        public ExtendScheduledExecutor(int corePoolSize) {
                super(corePoolSize);
        }
        
		@Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
                return super.scheduleAtFixedRate(wrapRunnable(command), initialDelay, period, unit);
        }

        @Override
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
                return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, delay, unit);
        }

        private Runnable wrapRunnable(Runnable command) {
                return new LogOnExceptionRunnable(command);
        }

        private class LogOnExceptionRunnable implements Runnable {
                private Runnable theRunnable;

                public LogOnExceptionRunnable(Runnable theRunnable) {
                        super();
                        this.theRunnable = theRunnable;
                }

                @Override
                public void run() {
                        try {
                                theRunnable.run();
                        } catch (Exception e) {
                                System.err.println("error in executing: " + theRunnable + ". It will no longer be run!");
                                e.printStackTrace();

                                
                                throw new RuntimeException(e);
                        }
                }
        }

        public static void main(String[] args) {
                
        }
}