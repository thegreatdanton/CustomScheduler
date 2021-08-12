package CustomScheduler;

import lombok.SneakyThrows;

import java.util.concurrent.*;

public class CustomTask implements Runnable{
    private final Runnable task;
    private final Long initialDelay;
    private final TimeUnit timeUnit;
    private final Long period;
    private final int type;

    public CustomTask(Runnable task, Long initialDelay, TimeUnit timeUnit, Long period, int type) {
        this.task = task;
        this.initialDelay = initialDelay;
        this.timeUnit = timeUnit;
        this.period = period;
        this.type = type;
    }

    @SneakyThrows
    @Override
    public void run() {
        switch (type){
            case 1:
                try {
                    Thread.sleep(timeUnit.toMillis(initialDelay));
                    task.run();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Thread.sleep(timeUnit.toMillis(initialDelay));
                    do{
                        task.run();
                        Thread.sleep(timeUnit.toMillis(period));
                    }while (true);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }


            case 3:
                try {
                    Thread.sleep(timeUnit.toMillis(initialDelay));
                    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
                    do{
                        Future result = executor.submit(task);
                        result.get();
                        Thread.sleep(timeUnit.toMillis(period));
                    }while (true);
                }catch (InterruptedException e){
                    e.printStackTrace();
                } catch (ExecutionException e){
                    e.printStackTrace();
                }


        }
    }
}
