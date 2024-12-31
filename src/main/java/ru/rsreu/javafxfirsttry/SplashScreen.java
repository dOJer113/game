package ru.rsreu.javafxfirsttry;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SplashScreen extends Application {

    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/rsreu/javafxfirsttry/splash_screen.fxml"));
        Scene scene = new Scene(loader.load(), 320, 120); // Размер окна 320x120 пикселей

        // Настраиваем Stage
        primaryStage.initStyle(StageStyle.UNDECORATED); // Убираем стандартные элементы управления окном
        primaryStage.setScene(scene);

        // Отображаем окно для вычисления размеров
        primaryStage.show();

        // Центрируем окно на экране
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double windowWidth = primaryStage.getWidth();
        double windowHeight = primaryStage.getHeight();

        primaryStage.setX((screenWidth - windowWidth) / 2);
        primaryStage.setY((screenHeight - windowHeight) / 2);
        // Запускаем задачу, которая имитирует загрузку
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(12); // Задержка для имитации реальной загрузки
                }
                return null;
            }
        };
        new Thread(task).start();

        // Таймер для закрытия окна загрузки и открытия главного окна
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.75), event -> {
                    primaryStage.close();
                    try {
                        new App().start(new Stage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );

        // Запуск таймера
        timeline.playFromStart();
    }


    public static void main(String[] args) {
        launch(args);
    }
}