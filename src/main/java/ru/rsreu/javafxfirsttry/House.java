package ru.rsreu.javafxfirsttry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

import static ru.rsreu.javafxfirsttry.StarryNight.WIDTH;

public class House {

    private static final Color[] WALL_COLORS = {
            Color.rgb(72, 61, 139),
            Color.rgb(47, 79, 79),
            Color.rgb(139, 0, 0),
            Color.rgb(255, 140, 0),
            Color.rgb(224, 191, 162)
    };

    private static final Color[] ROOF_COLORS = {
            Color.rgb(160, 82, 45),
            Color.rgb(128, 0, 0),
            Color.rgb(0, 0, 139),
            Color.rgb(184, 134, 11),
            Color.rgb(0, 139, 139),
            Color.rgb(189, 183, 107),
            Color.rgb(40, 20, 20)
    };
    public static final Color[] GARLAND_COLORS = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.ORANGE,
            Color.PURPLE,
            Color.CYAN,
            Color.MAGENTA,
            Color.rgb(255, 105, 180), // Розовый
            Color.rgb(255, 215, 0)    // Золотой
    };

    private double x;
    private double y;
    private boolean isTwoStory;
    public static int garlandColorIndex = 0; // Индекс текущего цвета гирлянды
    public static final double scale = 0.4; // Масштабирующий коэффициент

    public House(double x, double y, boolean isTwoStory) {
        this.x = x;
        this.y = y;
        this.isTwoStory = isTwoStory;
    }

    public double getX() {
        return x;
    }

    public double getWidth() {
        return 300 * scale;
    }

    public void updateGarland(GraphicsContext gc) {
        // Рисуем гирлянду на верхней части дома
        double roofY = isTwoStory ? y - 200 * scale : y;

        drawGarland(gc, x, roofY, 300 * scale, 10, scale);

        // Рисуем гирлянду на левой стене первого этажа
        drawGarland(gc, x, y, 200 * scale, 10, scale, true);

        // Рисуем гирлянду на правой стене первого этажа
        drawGarland(gc, x + 300 * scale, y, 200 * scale, 10, scale, true);

        // Проверяем, является ли дом двухэтажным
        if (isTwoStory) {
            // Рисуем гирлянду на левой стене второго этажа
            drawGarland(gc, x, y - 200 * scale, 200 * scale, 10, scale, true);

            // Рисуем гирлянду на правой стене второго этажа
            drawGarland(gc, x + 300 * scale, y - 200 * scale, 200 * scale, 10, scale, true);
        }
    }


    public void draw(GraphicsContext gc) {

        // Выбор цвета стен и крыши
        Random random = new Random();
        Color wallColor = WALL_COLORS[random.nextInt(WALL_COLORS.length)];
        Color roofColor = ROOF_COLORS[random.nextInt(ROOF_COLORS.length)];

        // Нижняя стена (основание дома)
        gc.setFill(wallColor);
        gc.fillRect(x, y, 300 * scale, 200 * scale);

        // Окно на первом этаже
        gc.setFill(Color.DARKCYAN);
        gc.fillRect(x + 60 * scale, y + 50 * scale, 60 * scale, 60 * scale);

        // Вертикальная и горизонтальная перегородки окна
        gc.setFill(Color.BLACK);
        gc.fillRect(x + 90 * scale, y + 50 * scale, 5 * scale, 60 * scale); // Вертикальная
        gc.fillRect(x + 60 * scale, y + 80 * scale, 60 * scale, 5 * scale); // Горизонтальная

        // Дверь
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(x + 140 * scale, y + 80 * scale, 60 * scale, 120 * scale);

        // Вертикальная и горизонтальная перегородки двери
        gc.setFill(Color.BLACK);
        gc.fillRect(x + 170 * scale, y + 80 * scale, 5 * scale, 120 * scale); // Вертикальная
        gc.fillRect(x + 140 * scale, y + 140 * scale, 60 * scale, 5 * scale); // Горизонтальная

        // Верхняя стена (для двухэтажного дома)
        if (isTwoStory) {
            gc.setFill(wallColor);
            gc.fillRect(x, y - 200 * scale, 300 * scale, 200 * scale);

            // Окно на втором этаже
            gc.setFill(Color.DARKCYAN);
            gc.fillRect(x + 140 * scale, y - 150 * scale, 60 * scale, 60 * scale);

            // Вертикальная и горизонтальная перегородки окна
            gc.setFill(Color.BLACK);
            gc.fillRect(x + 170 * scale, y - 150 * scale, 5 * scale, 60 * scale); // Вертикальная
            gc.fillRect(x + 140 * scale, y - 120 * scale, 60 * scale, 5 * scale); // Горизонтальная

            // Гирлянда на левой стене второго этажа
            drawGarland(gc, x, y - 200 * scale, 200 * scale, 10, scale, true);

            // Гирлянда на правой стене второго этажа
            drawGarland(gc, x + 300 * scale, y - 200 * scale, 200 * scale, 10, scale, true);
        }

        // Крыша
        gc.setFill(roofColor);
        double roofY = isTwoStory ? y - 200 * scale : y;
        double[] roofXPoints = {x, x + 300 * scale, x + 150 * scale};
        double[] roofYPoints = {roofY, roofY, roofY - 100 * scale};
        gc.fillPolygon(roofXPoints, roofYPoints, 3);

        // Дымоход
        gc.setFill(Color.DARKRED);
        gc.fillRect(x + 180 * scale, roofY - 140 * scale, 60 * scale, 120 * scale);


        // Гирлянда на верхней части дома
        drawGarland(gc, x, roofY, 300 * scale, 10, scale);

        // Гирлянда на левой стене первого этажа
        drawGarland(gc, x, y, 200 * scale, 10, scale, true);

        // Гирлянда на правой стене первого этажа
        drawGarland(gc, x + 300 * scale, y, 200 * scale, 10, scale, true);
    }

    // Метод для отрисовки гирлянды
    private void drawGarland(GraphicsContext gc, double startX, double startY, double length, int lightCount, double scale) {
        drawGarland(gc, startX, startY, length, lightCount, scale, false);
    }

    private void drawGarland(GraphicsContext gc, double startX, double startY, double length, int lightCount, double scale, boolean isVertical) {
        double spacing = length / (lightCount - 1);
        Color garlandColor = GARLAND_COLORS[garlandColorIndex];

        for (int i = 0; i < lightCount; i++) {
            double lightX = isVertical ? startX : startX + i * spacing;
            double lightY = isVertical ? startY + i * spacing : startY;
            gc.setFill(garlandColor);
            gc.fillOval(lightX - 5 * scale, lightY - 5 * scale, 10 * scale, 10 * scale);
        }
    }


}