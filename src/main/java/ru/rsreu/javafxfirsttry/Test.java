package ru.rsreu.javafxfirsttry;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Test extends Application {
    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        Image image = new Image("C:\\Users\\aleks\\Downloads\\IdeaPR\\JavaFXFirstTry\\src\\main\\resources\\ru\\rsreu\\javafxfirsttry\\christmas.png");
        Image santa = new Image("C:\\Users\\aleks\\Downloads\\IdeaPR\\JavaFXFirstTry\\src\\main\\resources\\ru\\rsreu\\javafxfirsttry\\santa2.0.gif");

        ImageView background1 = new ImageView(image);
        ImageView background2 = new ImageView(image);
        ImageView santaView = new ImageView(santa);

        double backgroundWidth = 1000;
        double backgroundHeight = 500;
        background1.setFitWidth(backgroundWidth);
        background1.setFitHeight(backgroundHeight);
        background2.setFitWidth(backgroundWidth);
        background2.setFitHeight(backgroundHeight);
        background2.setX(backgroundWidth);

        santaView.setFitWidth(295);
        santaView.setFitHeight(150);
        santaView.setX(50);
        santaView.setY(backgroundHeight / 2 - santaView.getFitHeight() / 2); // Start Santa in the middle

        pane.getChildren().addAll(background1, background2, santaView);

        // List to store trail segments
        List<Rectangle> trailSegments = new ArrayList<>();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double speed = 1;
                background1.setX(background1.getX() - speed);
                background2.setX(background2.getX() - speed);

                if (background1.getX() + backgroundWidth <= 0) {
                    background1.setX(background2.getX() + backgroundWidth);
                }
                if (background2.getX() + backgroundWidth <= 0) {
                    background2.setX(background1.getX() + backgroundWidth);
                }
            }
        };
        timer.start();

        Scene scene = new Scene(pane, backgroundWidth, backgroundHeight);
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            double step = 3;
            if (key == KeyCode.W) {
                santaView.setY(santaView.getY() - step);
            } else if (key == KeyCode.S) {
                santaView.setY(santaView.getY() + step);
            }
        });

        stage.setTitle("Controlled Object");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
