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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarryNight extends Application {

    public double gameDuration;
    public boolean gameOver;
    private static final String SNOWFLAKE_IMAGE_PATH = "C:\\Users\\aleks\\Downloads\\IdeaPR\\JavaFXFirstTry\\src\\main\\resources\\ru\\rsreu\\javafxfirsttry\\snowflake.png"; // Укажите путь к изображению снежинки
    private Image snowflakeImage = new Image(SNOWFLAKE_IMAGE_PATH, 100, 100, true, true);
    private List<Snowflake> snowflakes = new ArrayList<>();

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 590;
    public static int SPEED = 3;
    private long lastGiftTime = 0;
    private static final long GIFT_COOLDOWN = 600_000_000L;
    private int tickCounter = 0;
    public static List<Gift> gifts = new ArrayList<>();
    private List<House> housesCanvas1 = new ArrayList<>(); // Список домов для canvas1
    private List<House> housesCanvas2 = new ArrayList<>(); // Список домов для canvas2
    private GraphicsContext gc1; // GraphicsContext для canvas1
    private GraphicsContext gc2;
    private Image image1;
    private Image image2;
    private int c = 0;
    public static int score = 0;
    public static boolean isFirst = true;

    private static final int FRAME_COUNT = 10; // Количество кадров
    private static final int FRAME_DURATION = 100_000_000; // Длительность одного кадра в наносекундах (0.1 секунды)
    private static final String SANTA_SPRITE_PATH = "C:\\Users\\aleks\\Downloads\\IdeaPR\\JavaFXFirstTry\\src\\main\\resources\\ru\\rsreu\\javafxfirsttry\\santa\\Run (%d).png"; // Путь к спрайтам
    private Image[] santaFrames;
    private ImageView santaView;
    private int currentFrame = 0;
    private long lastUpdate = 0;

    @Override
    public void start(Stage primaryStage) {
        // Загрузка спрайтов Санты
        santaFrames = new Image[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            santaFrames[i] = new Image(String.format(SANTA_SPRITE_PATH, i + 1));
        }

        santaView = new ImageView(santaFrames[0]);
        santaView.setFitWidth(145);
        santaView.setFitHeight(100);
        santaView.setX(200);
        santaView.setY(100);

        Canvas canvas1 = new Canvas(WIDTH, HEIGHT);
        Canvas canvas2 = new Canvas(WIDTH, HEIGHT);
        gc1 = canvas1.getGraphicsContext2D();
        gc2 = canvas2.getGraphicsContext2D();
        Text scoreText = new Text("Score: 0");
        scoreText.setFont(Font.font("Arial", 24));
        scoreText.setFill(Color.WHITE);
        scoreText.setX(20);
        scoreText.setY(30);

        // Рисуем фон, звёзды, сугробы и деревья
        drawBackground(gc1);
        drawBackground(gc2);

        // Преобразуем Canvas в Image для прокрутки
        image1 = canvas1.snapshot(null, null); // Инициализируем поле image
        image2 = canvas2.snapshot(null, null); // Инициализируем поле image

        // Создаём два ImageView для бесконечной прокрутки
        ImageView background1 = new ImageView(image1);
        ImageView background2 = new ImageView(image2);

        background1.setFitWidth(WIDTH);
        background1.setFitHeight(HEIGHT);
        background2.setFitWidth(WIDTH);
        background2.setFitHeight(HEIGHT);

        background2.setX(WIDTH);

        Pane pane = new Pane(background1, background2, scoreText, santaView);
        Scene scene = new Scene(pane, WIDTH, HEIGHT);

        primaryStage.setTitle("Starry Night");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Анимация прокрутки фона и анимация Санты
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Анимация Санты
                if (now - lastUpdate >= FRAME_DURATION) {
                    currentFrame = (currentFrame + 1) % FRAME_COUNT;
                    santaView.setImage(santaFrames[currentFrame]);
                    lastUpdate = now;
                }

                // Прокрутка фона и логика игры
                scoreText.setText("Score: " + score);
                background1.setX(background1.getX() - SPEED);
                background2.setX(background2.getX() - SPEED);

                if (background1.getX() + WIDTH <= 0) {
                    background1.setX(background2.getX() + WIDTH);
                    drawBackground(gc1);
                    image1 = canvas1.snapshot(null, null);
                    background1.setImage(image1);
                }
                if (background2.getX() + WIDTH <= 0) {
                    background2.setX(background1.getX() + WIDTH);
                    drawBackground(gc2);
                    image2 = canvas2.snapshot(null, null);
                    background2.setImage(image2);
                }

                tickCounter++;
                if (tickCounter % 30 == 0) {
                    House.garlandColorIndex = (House.garlandColorIndex + 1) % House.GARLAND_COLORS.length;
                    for (House house : housesCanvas1) {
                        house.updateGarland(canvas1.getGraphicsContext2D());
                        image1 = canvas1.snapshot(null, null);
                        background1.setImage(image1);
                    }
                    for (House house : housesCanvas2) {
                        house.updateGarland(canvas2.getGraphicsContext2D());
                        image2 = canvas2.snapshot(null, null);
                        background2.setImage(image2);
                    }
                }

                List<Gift> giftsCopy = new ArrayList<>(gifts);
                for (Gift gift : giftsCopy) {
                    double currentX = gift.getBase().getX();
                    isFirst = currentX > background1.getX() && currentX < background1.getX() + WIDTH;
                    List<House> currentHouses = isFirst ? housesCanvas1 : housesCanvas2;
                    for (House house : currentHouses) {
                        if (!house.isHasGift()) {
                            double start = isFirst ? background2.getX() : background1.getX();
                            if (start <= 0) {
                                house.setRealX(start + 1000 + house.getX());
                            } else {
                                house.setRealX(start - 1000 + house.getX());
                            }
                            if (house.isGiftOnRoof(gift)) {
                                gifts.remove(gift);
                                gift.removeFromPane();
                                score++;
                                house.giveGift();
                            }
                        }
                    }
                    gift.update();
                    if (gift.isOutOfScreen()) {
                        gifts.remove(gift);
                        gift.removeFromPane();
                    }
                }
            }
        };

        // Управление Сантой
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            double step = 5;

            // Текущие координаты Санты
            double currentY = santaView.getY();

            if (key == KeyCode.W) { // Вверх
                double newY = currentY - step;
                if (newY >= 0) {
                    santaView.setY(newY);
                }
            } else if (key == KeyCode.S) { // Вниз
                double newY = currentY + step;
                if (newY + santaView.getFitHeight() <= HEIGHT - 350) {
                    santaView.setY(newY);
                }
            } else if (key == KeyCode.SPACE) {
                long currentTime = System.nanoTime();
                if (currentTime - lastGiftTime >= GIFT_COOLDOWN) {
                    double giftX = santaView.getX() + 20;
                    double giftY = santaView.getY();
                    Gift gift = new Gift(pane, giftX, giftY);
                    gifts.add(gift);
                    lastGiftTime = currentTime;
                }
            }
        });

        timer.start();
    }

    private void drawSnowflakes(GraphicsContext gc) {
        snowflakes.clear();

        double areaHeight = 100;
        double area1Y = 0;

        createSnowflakeInArea(area1Y, areaHeight);

        // Отрисовываем снежинки
        for (Snowflake snowflake : snowflakes) {
            snowflake.draw(gc);
        }
    }

    private void createSnowflakeInArea(double areaY, double areaHeight) {
        Random random = new Random();
        double x = 100 + random.nextDouble() * (WIDTH - 200);

        double y1 = areaY;
        double y2 = areaY + areaHeight;

        double y = random.nextBoolean() ? y1 : y2;

        boolean isOverlapping = false;
        for (Snowflake snowflake : snowflakes) {
            if (Math.abs(snowflake.getX() - x) < 100 + random.nextDouble() * 50) {
                isOverlapping = true;
                break;
            }
        }

        if (!isOverlapping) {
            Snowflake snowflake = new Snowflake(x, y, snowflakeImage);
            snowflakes.add(snowflake);
        }
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
        if (c == 0) {
            c++;
            return;
        }
        drawSnowflakes(gc);
        if (gc == gc1) {
            housesCanvas1.clear();
            drawHouses(gc, housesCanvas1); // Рисуем дома для canvas1
        } else if (gc == gc2) {
            housesCanvas2.clear();
            drawHouses(gc, housesCanvas2); // Рисуем дома для canvas2
        }
    }

    // Метод для отрисовки домов
    private void drawHouses(GraphicsContext gc, List<House> houses) {
        Random random = new Random();
        double twoBlocksChance = 0.5;

        if (random.nextDouble() < twoBlocksChance) {
            // Создаём два блока домов
            double baseX1 = 100;
            createHouseBlock(gc, baseX1, houses);

            double baseX2 = WIDTH - 400; // Позиция в конце
            createHouseBlock(gc, baseX2, houses);
        } else {
            // Создаём один блок домов
            double baseX = 100; // Начальная позиция
            createHouseBlock(gc, baseX, houses);
        }
    }

    // Метод для создания блока домов
    private void createHouseBlock(GraphicsContext gc, double baseX, List<House> houses) {
        Random random = new Random();
        boolean createTwoHouses = random.nextBoolean(); // Создавать ли два домика

        if (createTwoHouses) {
            // Проверяем, достаточно ли места для второго дома
            if (baseX + 300 * 0.4 + 50 + 300 * 0.4 <= WIDTH) {
                // Создаём два домика рядом
                boolean isFirstHouseTwoStory = random.nextBoolean();
                House house1 = new House(baseX, HEIGHT - 200, isFirstHouseTwoStory);
                House house2 = new House(baseX + 300 * 0.4 + 50, HEIGHT - 200, !isFirstHouseTwoStory);
                house1.draw(gc);
                house2.draw(gc);
                houses.add(house1); // Добавляем первый дом в список
                houses.add(house2); // Добавляем второй дом в список
            } else {
                // Создаём один домик
                boolean isTwoStory = random.nextBoolean();
                House house = new House(baseX, HEIGHT - 200, isTwoStory);
                house.draw(gc);
                houses.add(house); // Добавляем дом в список
            }
        } else {
            // Создаём один домик
            boolean isTwoStory = random.nextBoolean();
            House house = new House(baseX, HEIGHT - 200, isTwoStory);
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