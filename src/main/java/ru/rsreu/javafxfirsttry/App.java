package ru.rsreu.javafxfirsttry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static final Media sound = new Media(App.class.getResource("/sounds/elki.mp3").toExternalForm());
    public static final MediaPlayer mediaPlayer = new MediaPlayer(sound);
    @Override
    public void start(Stage stage) throws IOException {

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Бесконечное воспроизведение
        mediaPlayer.setVolume(50);
        mediaPlayer.play(); // Начинаем воспроизведение

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/ru/rsreu/javafxfirsttry/main.fxml"));
        String userName = System.getProperty("user.name");
        stage.setTitle(userName+"`s app");
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}