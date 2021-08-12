package CustomScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomScheduler {
    private final ThreadPoolExecutor workerPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    /**
     * Creates and executes a one-shot action that becomes enabled after the given delay.
     */
    public void schedule(Runnable command, long delay, TimeUnit unit) {
        //one time execution
        CustomTask task = new CustomTask(command, delay, unit, null, 1);
        workerPool.submit(task);
    }

    /**
     * Creates and executes a periodic action that becomes enabled first after the given initial delay, and
     * subsequently with the given period; that is executions will commence after initialDelay then
     * initialDelay+period, then initialDelay + 2 * period, and so on.
     */
    public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        CustomTask task = new CustomTask(command, initialDelay, unit, period, 2);
        workerPool.submit(task);
    }

    /*
     * Creates and executes a periodic action that becomes enabled first after the given initial delay, and
     * subsequently with the given delay between the termination of one execution and the commencement of the next.
     */
    public void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        CustomTask task = new CustomTask(command, initialDelay, unit, delay, 3);
        workerPool.submit(task);
    }

    public static void main(String[] args) {
        System.out.println("starting at "+System.currentTimeMillis()/1000);
        CustomScheduler service = new CustomScheduler();

        Runnable task1 = getRunnableTask("Task1");
        service.schedule(task1, 1, TimeUnit.SECONDS);
        Runnable task2 = getRunnableTask("Task2");
        service.scheduleAtFixedRate(task2,1, 2, TimeUnit.SECONDS);
        Runnable task3 = getRunnableTask("Task3");
        service.scheduleWithFixedDelay(task3,1,2,TimeUnit.SECONDS);
        Runnable task4 = getRunnableTask("Task4");
        service.scheduleAtFixedRate(task4,1, 2, TimeUnit.SECONDS);
    }

    private static Runnable getRunnableTask(String s) {
        return () -> {
            System.out.println(s +" started at " + System.currentTimeMillis() / 1000);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(s +" ended at " + System.currentTimeMillis() / 1000);
        };
    }

}
