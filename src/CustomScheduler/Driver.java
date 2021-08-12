package CustomScheduler;

import java.util.concurrent.TimeUnit;

public class Driver {
    public static void main(String[] args){
        System.out.println("starting at "+System.currentTimeMillis()/1000);
        ScheduleExecutorService scheduleExecutorService = new ScheduleExecutorService(10);
        Runnable task1 = getRunnableTask("Task1");
        //scheduleExecutorService.schedule(task1, 1, TimeUnit.SECONDS);
        Runnable task2 = getRunnableTask("Task2");
       // scheduleExecutorService.scheduleAtFixedRate(task2, 1,2, TimeUnit.SECONDS);
        Runnable task3 = getRunnableTask("Task3");
        //scheduleExecutorService.scheduleAtFixedDelay(task3, 1, 2, TimeUnit.SECONDS);
        Runnable task4 = getRunnableTask("Task4");
        scheduleExecutorService.scheduleAtFixedRate(task4, 1, 4, TimeUnit.SECONDS);
        scheduleExecutorService.start();
    }

    private static Runnable getRunnableTask(String name) {
        return () -> {
          System.out.println(name + " started at "+ System.currentTimeMillis()/1000);
          try{
              Thread.sleep(1000);

          }catch (InterruptedException e){
              e.printStackTrace();
          }
            System.out.println(name + " ended at "+ System.currentTimeMillis()/1000);
        };
    }
}
