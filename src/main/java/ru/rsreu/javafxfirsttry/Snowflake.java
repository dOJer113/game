package ru.rsreu.javafxfirsttry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Snowflake {
    private double x;
    private double y;
    private Image image;

    public Snowflake(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public double getX() {
        return x;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }
}