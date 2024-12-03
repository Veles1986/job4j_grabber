package ru.job4j.grabber;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import ru.job4j.grabber.parsers.HabrCareerParse;
import ru.job4j.grabber.parsers.Parse;
import ru.job4j.grabber.stores.Store;

import java.io.IOException;

public class SuperJobGrab implements Job {

    private final Parse parse = new HabrCareerParse();

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        Store store = (Store) map.get("store");
        try {
            parse.fetch().forEach(store::save);
        } catch (IOException e) {
            System.err.println("Error during parsing or saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
