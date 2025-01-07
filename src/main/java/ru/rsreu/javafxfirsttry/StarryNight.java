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
import java.util.Random;

public class StarryNight extends Application {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 590;
    private long lastGiftTime = 0; // Время последнего создания подарка
    private static final long GIFT_COOLDOWN = 1_000_000_000L; // 2 секунды в наносекундах
    private List<Gift> gifts = new ArrayList<>();
    private List<House> houses = new ArrayList<>(); // Список домов
    private Image image1;
    private Image image2;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas1 = new Canvas(WIDTH, HEIGHT);
        Canvas canvas2 = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc1 = canvas1.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();

        // Рисуем фон, звёзды, сугробы и деревья
        drawBackground(gc1);
        drawBackground(gc2);

        // Преобразуем Canvas в Image для прокрутки
        image1 = canvas1.snapshot(null, null); // Инициализируем поле image
        image2 = canvas2.snapshot(null, null); // Инициализируем поле image

        // Создаём два ImageView для бесконечной прокрутки
        ImageView background1 = new ImageView(image1);
        ImageView background2 = new ImageView(image2);
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
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Добавляем Санту
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
                    drawBackground(gc1); // Перерисовываем фон с новыми домиками
                    image1 = canvas1.snapshot(null, null); // Обновляем изображение фона
                    background1.setImage(image1);
                }
                if (background2.getX() + WIDTH <= 0) {
                    background2.setX(background1.getX() + WIDTH);
                    drawBackground(gc2); // Перерисовываем фон с новыми домиками
                    image2 = canvas2.snapshot(null, null); // Обновляем изображение фона
                    background2.setImage(image2);
                }

                // Update gifts
                List<Gift> giftsCopy = new ArrayList<>(gifts);
                for (Gift gift : giftsCopy) {
                    gift.update();
                    if (gift.isOutOfScreen()) {
                        gifts.remove(gift);
                        gift.removeFromPane();
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

    // Метод для отрисовки фона
    private void drawBackground(GraphicsContext gc) {
        // Очищаем старый фон
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Рисуем фон, звёзды, сугробы, деревья и домики
        drawDarkBackground(gc);
        drawDimStars(gc);
        drawDarkSnowdrifts(gc);
        drawDarkTrees(gc);
        drawHouses(gc);
    }

    private void drawHouses(GraphicsContext gc) {
        Random random = new Random();

        double twoBlocksChance = 0.5;

        if (random.nextDouble() < twoBlocksChance) {

            double baseX1 = 100;
            createHouseBlock(gc, baseX1);

            // Второй блок домов (в конце)
            double baseX2 = WIDTH - 400; // Позиция в конце
            createHouseBlock(gc, baseX2);
        } else {
            // Создаём один блок домов (в начале)
            double baseX = 100; // Начальная позиция
            createHouseBlock(gc, baseX);
        }
    }

    // Метод для создания блока домов (один или два дома)
    private void createHouseBlock(GraphicsContext gc, double baseX) {
        Random random = new Random();
        boolean createTwoHouses = random.nextBoolean(); // Создавать ли два домика

        if (createTwoHouses) {
            // Проверяем, достаточно ли места для второго дома
            if (baseX + 300 * 0.4 + 50 + 300 * 0.4 <= WIDTH) {
                // Создаём два домика рядом (расстояние 50 блоков)
                boolean isFirstHouseTwoStory = random.nextBoolean(); // Случайно выбираем, какой дом будет двухэтажным

                House house1 = new House(baseX, HEIGHT - 200, isFirstHouseTwoStory); // Первый домик
                House house2 = new House(baseX + 300 * 0.4 + 50, HEIGHT - 200, !isFirstHouseTwoStory); // Второй домик
                house1.draw(gc);
                house2.draw(gc);
                houses.add(house1); // Добавляем первый дом в список
                houses.add(house2); // Добавляем второй дом в список
            } else {
                // Если места недостаточно, создаём один домик
                boolean isTwoStory = random.nextBoolean(); // Случайно выбираем, будет ли дом двухэтажным
                House house = new House(baseX, HEIGHT - 200, isTwoStory); // Одиночный домик
                house.draw(gc);
                houses.add(house); // Добавляем дом в список
            }
        } else {
            // Создаём один домик
            boolean isTwoStory = random.nextBoolean(); // Случайно выбираем, будет ли дом двухэтажным
            House house = new House(baseX, HEIGHT - 200, isTwoStory); // Одиночный домик
            house.draw(gc);
            houses.add(house); // Добавляем дом в список
        }
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