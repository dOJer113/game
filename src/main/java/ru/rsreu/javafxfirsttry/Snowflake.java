package ru.rsreu.javafxfirsttry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Snowflake {
    private double x;
    private double y;
    private double realX;
    private Image image;
    private static final double RADIUS = 50; // Радиус круга

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

    public double getRealX() {
        return realX;
    }

    public void setRealX(double realX) {
        this.realX = realX;
    }

    public boolean isPointInside(double targetX, double targetY) {
        double centerX = realX + RADIUS;
        double centerY = y + RADIUS;

        double distance = Math.sqrt(Math.pow(targetX - centerX, 2) + Math.pow(targetY - centerY, 2));
        return distance <= 45;
    }
}