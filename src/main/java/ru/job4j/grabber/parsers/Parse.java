package ru.job4j.grabber.parsers;

import ru.job4j.grabber.Post;

import java.io.IOException;
import java.util.List;

public interface Parse {
    List<Post> fetch() throws IOException;
}