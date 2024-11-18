package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportGenerator {
    private BufferedWriter writer;
    private LocalDateTime startTime;
    private int totalTests;
    private int passedTests;
    private int failedTests;

    public ReportGenerator() throws IOException {
        this.startTime = LocalDateTime.now();
        this.totalTests = 0;
        this.passedTests = 0;
        this.failedTests = 0;
        String basePath = ConfigReader.get("report.directory.path");
        if (basePath == null) {
            basePath = "./reports";
        }
        Path reportPath = createReportDirectories(basePath);
        String logFileName = "test-log-" + startTime.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".log";
        File logFile = new File(reportPath.toFile(), logFileName);
        writer = Files.newBufferedWriter(logFile.toPath(), StandardCharsets.UTF_8);
        String dateFormat = ConfigReader.get("log.file.date_format");
        writer.write("Дата запуска: " + startTime.format(DateTimeFormatter.ofPattern(dateFormat)) + "\n");
        writer.write("Группа тестов: " + ConfigReader.get("log.file.group_name") + "\n");
    }

    private Path createReportDirectories(String basePath) throws IOException {
        LocalDate currentDate = LocalDate.now();
        String year = String.valueOf(currentDate.getYear());
        String month = "month-" + String.format("%02d", currentDate.getMonthValue());
        String day = String.format("%02d", currentDate.getDayOfMonth());

        // Создаем структуру директорий
        Path yearDir = Paths.get(basePath, year);
        Path monthDir = yearDir.resolve(month);
        Path dayDir = monthDir.resolve(day);

        // Создаем директории, если они еще не существуют
        if (!Files.exists(dayDir)) {
            Files.createDirectories(dayDir);
        }

        return dayDir;
    }

    public void logTestResult(String testName, boolean passed) throws IOException {
        totalTests++;
        if (passed) {
            passedTests++;
        } else {
            failedTests++;
        }
        String dateFormat = ConfigReader.get("log.file.date_format");
        writer.write("Закончил тест: " + testName + ", Результат: " + (passed ? "ОК" : "FAIL") + ", Время: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)) + "\n");
    }


    public void closeLog(Long time) {
        try {
            if (writer != null) {
                writer.write("Дата: " + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\n");
                writer.write("Всего тестов: " + totalTests + "\n");
                writer.write("Пройдено: " + passedTests + "\n");
                writer.write("Неудачно: " + failedTests + "\n");
                writer.write("Среднее время выполнения теста: " + calculateAverageTime(totalTests, time) + " секунд\n");
                writer.write("======================================\n");
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл лога: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double calculateAverageTime(int totalTests, long time) {
        return Math.round((time / (totalTests  * 1000)) * 100.00 / 100.00);
    }
}
