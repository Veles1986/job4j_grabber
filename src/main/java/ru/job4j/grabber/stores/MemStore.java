package ru.job4j.grabber.stores;

import ru.job4j.grabber.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemStore implements Store {

    private Map<Long, Post> mem = new HashMap<>();

    @Override
    public void save(Post post) {
        mem.put(post.getId(), post);
    }

    @Override
    public List<Post> getAll() {
        return (List<Post>) mem.values();
    }

    @Override
    public Optional<Post> findById(long id) {
        return Optional.of(mem.get(id));
    }

    @Override
    public void close() throws Exception {
    }
}
