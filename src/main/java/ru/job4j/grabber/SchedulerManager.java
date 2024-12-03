package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.stores.Store;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class SchedulerManager  {

    private Scheduler scheduler;

    public void init() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    public void load(int period, Class<SuperJobGrab> task, Store store) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        JobDetail job = newJob(task)
                .withIdentity("jobGrab", "grab")
                .usingJobData(data)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("triggerGrab", "grab")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(period)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }

    public void close() throws SchedulerException {
        scheduler.shutdown();
    }
}
