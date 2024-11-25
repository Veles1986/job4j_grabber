package ru.job4j.grabber;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection connection;

    public PsqlStore(Properties config) throws ClassNotFoundException, SQLException {
        Class.forName(config.getProperty("driver-class-name"));
        connection = DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("username"),
                config.getProperty("password")
        );
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO post(id, title, link, description, created) VALUES(?, ?, ?, ?, ?) ON CONFLICT (link) DO NOTHING;"
        )) {
            statement.setInt(1, post.getId());
            statement.setString(2, post.getTitle());
            statement.setString(3, post.getLink());
            statement.setString(4, post.getDescription());
            statement.setTimestamp(5, Timestamp.valueOf(post.getCreated()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM POST;"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new Post(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement =
                     connection.prepareStatement("select id, title, link, description, created from post where id = ?;")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Properties config = new Properties();
        config.load(new FileReader("src/main/resources/db/rabbit.properties"));

        try (Connection connection = DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("username"),
                config.getProperty("password"));
             Statement statement = connection.createStatement()
        ) {
            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS post (
                        id SERIAL PRIMARY KEY,
                        title TEXT,
                        link TEXT UNIQUE,
                        description TEXT,
                        created TIMESTAMP
                    );
                    """;
            statement.execute(createTableSQL);
            System.out.println("Таблица 'post' создана или уже существует.");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try (PsqlStore store = new PsqlStore(config)) {
            Post post1 = new Post(1, "Java Developer", "https://example.com/java-dev", "Java job description", LocalDateTime.now());
            Post post2 = new Post(2, "Python Developer", "https://example.com/python-dev", "Python job description", LocalDateTime.now());
            store.save(post1);
            store.save(post2);
            System.out.println("Посты сохранены.");

            List<Post> allPosts = store.getAll();
            System.out.println("Все посты из базы данных:");
            allPosts.forEach(System.out::println);

            Post foundPost = store.findById(1);
            System.out.println("Найденный пост по ID 1:");
            System.out.println(foundPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}