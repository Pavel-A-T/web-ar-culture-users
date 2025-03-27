package com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class ConfigReader {
  static String fileName = "config.json";

  public static String getProperty(String property) {
    // Создаем ObjectMapper для парсинга JSON
    ObjectMapper objectMapper = new ObjectMapper();
    // Получаем InputStream для файла из resources
    InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(fileName);
    try {
      // Преобразуем JSON в Map<String, Object>
      Map<String, Object> configMap = objectMapper.readValue(inputStream, Map.class);
      return configMap.get(property).toString();
    }
    catch (Exception e) {
      return null;
    }
  }
}
