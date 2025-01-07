package ru.rsreu.javafxfirsttry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class House {

    private static final Color[] WALL_COLORS = {
            Color.DARKSLATEBLUE,
            Color.DARKSLATEGRAY,
            Color.DARKGREEN
    };

    private static final Color[] ROOF_COLORS = {
            Color.SIENNA,
            Color.MAROON,
            Color.DARKOLIVEGREEN
    };

    private static final Color[] GARLAND_COLORS = {
            Color.RED,
            Color.GREEN,
            Color.BLUE
    };

    private double x;
    private double y;
    private boolean isTwoStory;
    private static int garlandColorIndex = 0; // Индекс текущего цвета гирлянды

    public House(double x, double y, boolean isTwoStory) {
        this.x = x;
        this.y = y;
        this.isTwoStory = isTwoStory;
    }

    public void draw(GraphicsContext gc) {
        double scale = 0.4; // Масштабирующий коэффициент

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