package ru.rsreu.javafxfirsttry;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class SettingsController {
    @FXML
    private ToggleButton muteButton; // Кнопка выключения звука

    @FXML
    private Slider volumeSlider; // Ползунок громкости
    private boolean isMuted = false; // Состояние звука (включен/выключен)
    private double previousVolume = App.mediaPlayer.getVolume(); // Предыдущее значение громкости

    @FXML
    public void initialize() {
        MediaPlayer mediaPlayer = App.mediaPlayer;

        // Устанавливаем начальное значение ползунка громкости
        double initialVolume = mediaPlayer.getVolume(); // Получаем текущую громкость (0.0–1.0)
        volumeSlider.setValue(initialVolume * 100); // Преобразуем в диапазон 0–100 и устанавливаем

        // Обработка изменения значения ползунка
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!isMuted) {
                double volume = newValue.doubleValue() / 100.0; // Преобразуем в диапазон 0.0–1.0
                mediaPlayer.setVolume(volume); // Устанавливаем громкость
            }
        });
    }

    // Обработка нажатия на кнопку "Выключить звук"
    @FXML
    protected void toggleMute() {
        MediaPlayer mediaPlayer = App.mediaPlayer;
        isMuted = !isMuted;
        if (isMuted) {
            previousVolume = volumeSlider.getValue(); // Сохраняем текущую громкость
            mediaPlayer.setVolume(0); // Устанавливаем громкость на 0
            muteButton.setText("Включить звук");
        } else {
            mediaPlayer.setVolume(previousVolume / 100.0); // Восстанавливаем предыдущую громкость
            muteButton.setText("Выключить звук");
        }
    }

    // Обработка нажатия на кнопку "Применить"
    @FXML
    protected void applySettings() {
        Stage stage = (Stage) volumeSlider.getScene().getWindow();
        stage.close();
    }
}