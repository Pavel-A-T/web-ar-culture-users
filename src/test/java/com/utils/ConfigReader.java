package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();
    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find application.properties");
            }
            try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                properties.load(reader);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
