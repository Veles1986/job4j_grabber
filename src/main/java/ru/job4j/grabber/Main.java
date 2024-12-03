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
   /* public static void main(String[] args) throws Exception {
        SchedulerManager schedulerManager = new SchedulerManager();
        Config config = new Config();
        config.load("src/main/resources/db/app.properties");
        Store store = new JdbcStore(config);
        schedulerManager.init();
        schedulerManager.load(Integer.parseInt(config.get("time")), SuperJobGrab.class, store);
    }*/

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        var config = new Config();
        config.load("src/main/resources/db/application.properties");
        try {
            Store store = new JdbcStore(config);

            // Добавляем тестовый пост
            var post = new Post();
            post.setTitle("Super Java Job");
            store.save(post);

            // Настраиваем и запускаем планировщик
            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store
            );

            // Запускаем веб-сервер
            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            log.error("When creating a connection", e);
        }
    }
}
