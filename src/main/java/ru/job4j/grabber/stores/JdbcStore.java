package ru.job4j.grabber.stores;

import ru.job4j.grabber.Post;
import ru.job4j.grabber.configs.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {

    private Connection connection;

    public JdbcStore(Config config) throws ClassNotFoundException, SQLException {
        Class.forName(config.get("driver-class-name"));
        connection = DriverManager.getConnection(
                config.get("url"),
                config.get("username"),
                config.get("password")
        );
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO post(id, title, link, description, created) VALUES(?, ?, ?, ?, ?) ON CONFLICT (link) DO NOTHING;"
        )) {
            statement.setLong(1, post.getId());
            statement.setString(2, post.getTitle());
            statement.setString(3, post.getLink());
            statement.setString(4, post.getDescription());
            statement.setTimestamp(5, new Timestamp(post.getTime()));
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
    public Optional<Post> findById(long id) {
        Post post = null;
        try (PreparedStatement statement =
                     connection.prepareStatement("select id, title, link, description, created from post where id = ?;")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(post);
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}