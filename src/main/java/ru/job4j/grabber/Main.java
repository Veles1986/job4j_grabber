package ru.job4j.grabber;

import ru.job4j.grabber.configs.Config;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.stores.MemStore;
import ru.job4j.grabber.stores.Store;

public class Main {
    public static void main(String[] args) throws Exception {
        SchedulerManager schedulerManager = new SchedulerManager();
        Config config = new Config();
        config.load("src/main/resources/db/app.properties");
        Store store = new JdbcStore(config);
        schedulerManager.init();
        schedulerManager.load(Integer.parseInt(config.get("time")), SuperJobGrab.class, store);
    }
}
