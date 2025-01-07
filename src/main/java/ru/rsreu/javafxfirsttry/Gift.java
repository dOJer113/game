package ru.rsreu.javafxfirsttry;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Random;

public class Gift {
    private Pane root;
    private Rectangle base;
    private Rectangle lid;
    private Line ribbonHorizontal;
    private Line ribbonVertical;
    private Path bow;
    private double speed = 2; // Скорость падения подарка

    public Gift(Pane root, double x, double y) {
        this.root = root;
        Random random = new Random();

        Color giftColor = Color.rgb(
                150 + random.nextInt(106),  // Красный (150-255)
                150 + random.nextInt(106),  // Зелёный (150-255)
                100 + random.nextInt(106)   // Синий (150-255)
        );

        Color ribbonColor = Color.rgb(
                150 + random.nextInt(106),  // Красный (150-255)
                150 + random.nextInt(106),  // Зелёный (150-255)
                100 + random.nextInt(106)   // Синий (150-255)
        );

        // Основание подарка
        base = new Rectangle(x, y + 50, 25, 25); // Размеры 25x25
        base.setFill(giftColor);
        base.setStroke(Color.BLACK);

        // Крышка подарка
        lid = new Rectangle(x - 2.5, y + 47.5, 30, 5); // Размеры 30x5
        lid.setFill(giftColor.brighter());
        lid.setStroke(Color.BLACK);

        // Лента по горизонтали
        ribbonHorizontal = new Line(x, y + 62.5, x + 25, y + 62.5); // Центр основания
        ribbonHorizontal.setStroke(ribbonColor);
        ribbonHorizontal.setStrokeWidth(1.5);

        // Лента по вертикали
        ribbonVertical = new Line(x + 12.5, y + 50, x + 12.5, y + 75); // Центр крышки и основания
        ribbonVertical.setStroke(ribbonColor);
        ribbonVertical.setStrokeWidth(1.5);

        // Бантик
        bow = new Path();
        // Левая часть бантика
        MoveTo moveToLeft = new MoveTo(x + 12.5, y + 45);
        CubicCurveTo curveLeft1 = new CubicCurveTo(x + 7.5, y + 42.5, x + 5, y + 45, x + 7.5, y + 47.5);
        CubicCurveTo curveLeft2 = new CubicCurveTo(x + 5, y + 50, x + 7.5, y + 52.5, x + 12.5, y + 50);
        // Правая часть бантика
        MoveTo moveToRight = new MoveTo(x + 12.5, y + 45);
        CubicCurveTo curveRight1 = new CubicCurveTo(x + 17.5, y + 42.5, x + 20, y + 45, x + 17.5, y + 47.5);
        CubicCurveTo curveRight2 = new CubicCurveTo(x + 20, y + 50, x + 17.5, y + 52.5, x + 12.5, y + 50);

        bow.getElements().addAll(moveToLeft, curveLeft1, curveLeft2, moveToRight, curveRight1, curveRight2);
        bow.setFill(ribbonColor);
        bow.setStroke(Color.BLACK);

        // Добавляем все элементы
        root.getChildren().addAll(base, lid, ribbonHorizontal, ribbonVertical, bow);
    }

    public boolean isOutOfScreen() {
        return base.getY() > StarryNight.HEIGHT; // Подарок вышел за пределы экрана
    }

    public void removeFromPane() {
        root.getChildren().removeAll(base, lid, ribbonHorizontal, ribbonVertical, bow);
    }

    public void update() {
        // Двигаем подарок вниз и немного назад (влево)
        base.setY(base.getY() + speed);
        base.setX(base.getX() - speed * 0.5); // Двигаем влево с половиной скорости

        lid.setY(lid.getY() + speed);
        lid.setX(lid.getX() - speed * 0.5); // Двигаем влево с половиной скорости

        ribbonHorizontal.setStartY(ribbonHorizontal.getStartY() + speed);
        ribbonHorizontal.setStartX(ribbonHorizontal.getStartX() - speed * 0.5); // Двигаем влево с половиной скорости
        ribbonHorizontal.setEndY(ribbonHorizontal.getEndY() + speed);
        ribbonHorizontal.setEndX(ribbonHorizontal.getEndX() - speed * 0.5); // Двигаем влево с половиной скорости

        ribbonVertical.setStartY(ribbonVertical.getStartY() + speed);
        ribbonVertical.setStartX(ribbonVertical.getStartX() - speed * 0.5); // Двигаем влево с половиной скорости
        ribbonVertical.setEndY(ribbonVertical.getEndY() + speed);
        ribbonVertical.setEndX(ribbonVertical.getEndX() - speed * 0.5); // Двигаем влево с половиной скорости

        bow.setTranslateY(bow.getTranslateY() + speed);
        bow.setTranslateX(bow.getTranslateX() - speed * 0.5); // Двигаем влево с половиной скорости

        // Удаляем подарок, если он уходит за пределы экрана
        if (base.getY() > StarryNight.HEIGHT || base.getX() + base.getWidth() < 0) {
            root.getChildren().removeAll(base, lid, ribbonHorizontal, ribbonVertical, bow);
        }
    }
}