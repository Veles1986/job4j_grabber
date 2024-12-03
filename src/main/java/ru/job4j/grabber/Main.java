package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.configs.Config;
import ru.job4j.grabber.service.Web;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.stores.MemStore;
import ru.job4j.grabber.stores.Store;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        var config = new Config();
        config.load("src/main/resources/db/application.properties");
        try {
            Store store = new JdbcStore(config);
            var post = new Post();
            post.setTitle("Super Java Job");
            store.save(post);
            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store
            );
            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            LOG.error("When creating a connection", e);
        }
    }
}
