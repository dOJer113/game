package ru.rsreu.javafxfirsttry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameOverDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private Label errorLabel;

    private Stage dialogStage;
    private boolean saveClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getName() {
        return nameField.getText();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (nameField.getText().isEmpty()) {
            blinkErrorLabel();
        } else {
            saveClicked = true;
            dialogStage.close();
        }
    }

    private void blinkErrorLabel() {
        errorLabel.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> errorLabel.setVisible(false)),
                new KeyFrame(Duration.seconds(0.2), e -> errorLabel.setVisible(true)),
                new KeyFrame(Duration.seconds(0.3), e -> errorLabel.setVisible(false)),
                new KeyFrame(Duration.seconds(0.4), e -> errorLabel.setVisible(true)),
                new KeyFrame(Duration.seconds(0.5), e -> errorLabel.setVisible(false)),
                new KeyFrame(Duration.seconds(0.6), e -> errorLabel.setVisible(true))
        );
        timeline.setCycleCount(2);
        timeline.play();
    }
}