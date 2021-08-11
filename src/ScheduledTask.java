import java.util.concurrent.TimeUnit;

public class ScheduledTask {

    private final Runnable runnable;

    public Long ScheduledTime;

    private final int taskType;

    private final Long period;

    private final Long delay;

    private final TimeUnit timeUnit;


    public ScheduledTask(Runnable runnable, Long scheduledTime, int taskType, Long period, Long delay,
                         TimeUnit timeUnit) {
        ScheduledTime = scheduledTime;
        this.runnable = runnable;
        this.taskType = taskType;
        this.period = period;
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    public Long getScheduledTime(){
        return ScheduledTime;
    }

    public int getTaskType(){
        return taskType;
    }
    public Runnable getRunnable(){
        return runnable;
    }

    public TimeUnit getUnit() {
        return timeUnit;
    }

    public long getPeriod() {
        return period;
    }

    public long getDelay() {
        return delay;
    }

    public void setScheduledTime(long newScheduledTime) {
        ScheduledTime = newScheduledTime;
    }
}
