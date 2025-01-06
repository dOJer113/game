package ru.rsreu.javafxfirsttry;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private Button settingsButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button tabButton;
    @FXML
    private Button playButton;
    @FXML
    protected void onTabButtonClick() throws IOException {
        URL url = MainController.class.getResource("/ru/rsreu/javafxfirsttry/achievements.fxml");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        Stage currentStage = (Stage) tabButton.getScene().getWindow();
        stage.initOwner(currentStage);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.centerOnScreen();

        stage.show();
        stage.toFront();

    }

    @FXML
    protected void onPlayButtonClick() {
        Stage mainMenuStage = (Stage) playButton.getScene().getWindow();
        try {
            SplashScreenController splashScreen = new SplashScreenController();
            splashScreen.showModal(mainMenuStage, mainMenuStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSettingsButtonClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ru/rsreu/javafxfirsttry/settings.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();

        Stage currentStage = (Stage) settingsButton.getScene().getWindow();

        stage.initOwner(currentStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.centerOnScreen();
        App.mediaPlayer.setVolume(50);
        stage.show();
        stage.toFront();
    }

    @FXML
    protected void onExitButtonClick() {
        System.exit(0);
    }
}