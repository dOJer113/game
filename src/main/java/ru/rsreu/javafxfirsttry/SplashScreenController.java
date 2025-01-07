package ru.rsreu.javafxfirsttry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import javafx.application.Platform;

public class SplashScreenController {

    @FXML
    private ProgressBar progressBar;

    private Stage stage;
    private Stage mainMenuStage;

    public void showModal(Stage ownerStage, Stage mainMenuStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/rsreu/javafxfirsttry/splash_screen.fxml"));
        loader.setController(this);
        Scene scene = new Scene(loader.load(), 400, 150);

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerStage);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        this.mainMenuStage = mainMenuStage;

        startLoading();
    }

    private void startLoading() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(12);
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        new Thread(task).start();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.75), event -> {
                    stage.close(); // Close the splash screen

                    Platform.runLater(() -> {
                        StarryNight starryNight = new StarryNight();
                        starryNight.start(mainMenuStage);
                        //mainMenuStage.close();
                    });
                })
        );

        timeline.playFromStart();
    }
}