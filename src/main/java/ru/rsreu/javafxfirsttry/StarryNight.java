package ru.rsreu.javafxfirsttry;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class StarryNight extends Application {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 590;
    private long lastGiftTime = 0; // Время последнего создания подарка
    private static final long GIFT_COOLDOWN = 1_000_000_000L; // 2 секунды в наносекундах
    private List<Gift> gifts = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Рисуем фон, звёзды, сугробы и деревья
        drawDarkBackground(gc);
        drawDimStars(gc);
        drawDarkSnowdrifts(gc);
        drawDarkTrees(gc);

        // Преобразуем Canvas в Image для прокрутки
        Image image = canvas.snapshot(null, null);

        // Создаём два ImageView для бесконечной прокрутки
        ImageView background1 = new ImageView(image);
        ImageView background2 = new ImageView(image);
        Image santa = new Image("C:\\Users\\aleks\\Downloads\\IdeaPR\\JavaFXFirstTry\\src\\main\\resources\\ru\\rsreu\\javafxfirsttry\\santa2.0.gif");
        ImageView santaView = new ImageView(santa);

        background1.setFitWidth(WIDTH);
        background1.setFitHeight(HEIGHT);
        background2.setFitWidth(WIDTH);
        background2.setFitHeight(HEIGHT);

        // Размещаем второй ImageView справа от первого
        background2.setX(WIDTH);

        Pane pane = new Pane(background1, background2);
        Scene scene = new Scene(pane, WIDTH, HEIGHT);

        primaryStage.setTitle("Starry Night");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
        santaView.setFitWidth(216);
        santaView.setFitHeight(110);
        santaView.setX(200);
        santaView.setY((double) HEIGHT / 2 - santaView.getFitHeight() / 2);
        pane.getChildren().add(santaView);

        // Анимация прокрутки фона
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double speed = 1; // Скорость прокрутки
                background1.setX(background1.getX() - speed);
                background2.setX(background2.getX() - speed);

                // Перемещаем фон, если он уходит за пределы видимости
                if (background1.getX() + WIDTH <= 0) {
                    background1.setX(background2.getX() + WIDTH);
                }
                if (background2.getX() + WIDTH <= 0) {
                    background2.setX(background1.getX() + WIDTH);
                }

                List<Gift> giftsCopy = new ArrayList<>(gifts);

                // Итерируемся по копии списка
                for (Gift gift : giftsCopy) {
                    gift.update();

                    // Удаляем подарок, если он уходит за пределы экрана
                    if (gift.isOutOfScreen()) {
                        gifts.remove(gift); // Удаляем подарок из оригинального списка
                        gift.removeFromPane(); // Удаляем подарок с панели
                    }
                }
            }
        };

        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            double step = 5;

            // Текущие координаты Санты
            double currentY = santaView.getY();

            if (key == KeyCode.W) { // Вверх
                double newY = currentY - step;
                if (newY >= 0) { // Проверяем, чтобы Санта не выходил за верхнюю границу
                    santaView.setY(newY);
                }
            } else if (key == KeyCode.S) { // Вниз
                double newY = currentY + step;
                if (newY + santaView.getFitHeight() <= HEIGHT - 150) { // Проверяем, чтобы Санта не выходил за нижнюю границу
                    santaView.setY(newY);
                }
            } else if (key == KeyCode.SPACE) {
                long currentTime = System.nanoTime(); // Текущее время в наносекундах
                if (currentTime - lastGiftTime >= GIFT_COOLDOWN) { // Проверяем, прошло ли 2 секунды
                    double giftX = santaView.getX() + 20; // Центрируем подарок по горизонтали
                    double giftY = santaView.getY() + 80; // Подарок появляется под Сантой
                    Gift gift = new Gift(pane, giftX, giftY);
                    gifts.add(gift);
                    lastGiftTime = currentTime; // Обновляем время последнего создания подарка
                }
            }
        });

        timer.start();
    }


    // Рисуем фон
    private void drawDarkBackground(GraphicsContext gc) {
        gc.setFill(Color.color(0.05, 0.05, 0.1));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    // Рисуем звёзды
    private void drawDimStars(GraphicsContext gc) {
        gc.setFill(Color.color(0.8, 0.8, 0.8, 0.7));
        for (int i = 0; i < 200; i++) {
            double x = Math.random() * WIDTH;
            double y = Math.random() * HEIGHT * 0.7; // Звёзды только в верхней части
            double size = Math.random() * 2 + 1;
            gc.fillOval(x, y, size, size);
        }
    }

    // Рисуем сугробы с волнами
    private void drawDarkSnowdrifts(GraphicsContext gc) {
        gc.setFill(Color.color(0.2, 0.2, 0.3));
        gc.fillRect(0, HEIGHT * 0.7, WIDTH, HEIGHT * 0.3);

        gc.setStroke(Color.color(0.3, 0.3, 0.4));
        gc.setLineWidth(2);

        double startY = HEIGHT * 0.7; // Верхняя граница снега
        double amplitude = 10; // Амплитуда волн
        double frequency = 0.02; // Частота волн

        gc.beginPath();
        gc.moveTo(0, startY + Math.sin(0) * amplitude);

        for (int x = 0; x <= WIDTH; x++) {
            double y = startY + Math.sin(x * frequency) * amplitude;
            gc.lineTo(x, y);
        }

        gc.lineTo(WIDTH, HEIGHT);
        gc.lineTo(0, HEIGHT);
        gc.closePath();
        gc.fill();
    }

    // Рисуем деревья
    private void drawDarkTrees(GraphicsContext gc) {
        gc.setFill(Color.color(0.1, 0.1, 0.1));
        double snowTopY = HEIGHT * 0.7; // Верхняя граница снега

        for (int i = 0; i < 40; i++) {
            double x = Math.random() * WIDTH;
            double scale = Math.random() * 0.5 + 0.3; // Масштаб для глубины
            double treeBottomY = snowTopY + Math.random() * 10; // Случайное смещение вниз
            drawTree(gc, x, treeBottomY, scale);
        }
    }

    // Рисуем одно дерево
    private void drawTree(GraphicsContext gc, double x, double treeBottomY, double scale) {
        gc.fillPolygon(
                new double[]{x - 15 * scale, x, x + 15 * scale},
                new double[]{treeBottomY, treeBottomY - 40 * scale, treeBottomY},
                3
        );
        gc.fillPolygon(
                new double[]{x - 10 * scale, x, x + 10 * scale},
                new double[]{treeBottomY - 20 * scale, treeBottomY - 60 * scale, treeBottomY - 20 * scale},
                3
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}