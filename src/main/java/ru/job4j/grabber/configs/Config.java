package ru.job4j.grabber.configs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private Properties properties = new Properties();

    public void load(String file) throws Exception {
        try (FileReader input = new FileReader(file)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

}
