package ru.rsreu.javafxfirsttry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ScoreFileHandler {

    private static final String FILE_NAME = "scores.txt"; // Имя файла
    private static final String RESOURCES_DIR = "src/main/resources"; // Папка resources

    private static final String FILE_PATH = RESOURCES_DIR + "/" + FILE_NAME;

    private static void ensureFileExists() throws IOException {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent()); // Создаем папки, если их нет
            Files.createFile(path); // Создаем файл
            // Записываем начальные данные
            writeInitialData();
        }
    }

    private static void writeInitialData() throws IOException {
        String systemName = System.getProperty("user.name"); // Системное имя пользователя
        Achievement initialAchievement = new Achievement(systemName, 0);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(initialAchievement.getName() + ":" + initialAchievement.getScore());
        }
    }

    public static List<Achievement> readAllRecords() throws IOException {
        ensureFileExists();
        List<Achievement> achievements = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    achievements.add(new Achievement(name, score));
                }
            }
        }
        return achievements;
    }

    public static void updateOrAddRecord(String name, int newScore) throws IOException {
        ensureFileExists(); // Убедимся, что файл существует
        List<Achievement> achievements = readAllRecords();
        boolean recordFound = false;

        for (int i = 0; i < achievements.size(); i++) {
            Achievement achievement = achievements.get(i);
            if (achievement.getName().equals(name)) {
                if (newScore > achievement.getScore()) {
                    achievement.setScore(newScore); // Обновляем рекорд, если новый больше
                }
                recordFound = true;
                break;
            }
        }

        if (!recordFound) {
            achievements.add(new Achievement(name, newScore));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Achievement achievement : achievements) {
                writer.write(achievement.getName() + ":" + achievement.getScore());
                writer.newLine();
            }
        }
    }

}