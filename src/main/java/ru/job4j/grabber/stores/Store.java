package ru.job4j.grabber.stores;

import ru.job4j.grabber.Post;

import java.util.List;
import java.util.Optional;

public interface Store extends AutoCloseable {
    void save(Post post);

    List<Post> getAll();

    Optional<Post> findById(long id);
}