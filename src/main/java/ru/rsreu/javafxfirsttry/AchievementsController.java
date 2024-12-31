package ru.rsreu.javafxfirsttry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.rsreu.javafxfirsttry.Achievement;

import java.io.IOException;

public class AchievementsController {

    @FXML
    private TableView<Achievement> achievementsTable;

    @FXML
    private TableColumn<Achievement, String> nameColumn;

    @FXML
    private TableColumn<Achievement, Integer> scoreColumn;

    @FXML
    public Button backButton;


    @FXML
    public void initialize() throws IOException {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        ObservableList<Achievement> achievements = FXCollections.observableArrayList(ScoreFileHandler.readAllRecords());

        achievementsTable.setItems(achievements);
    }

    @FXML
    public void goBackToMainMenu() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}