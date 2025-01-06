package ru.rsreu.javafxfirsttry;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StarryNight extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawDarkBackground(gc); // Тёмный фон
        drawDimStars(gc); // Тусклые звёзды
        drawDarkSnowdrifts(gc); // Тёмные сугробы
        drawDarkTrees(gc); // Почти чёрные деревья

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Dark Starry Night with Distant Trees and Snowdrifts");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawDarkBackground(GraphicsContext gc) {
        gc.setFill(Color.color(0.05, 0.05, 0.1)); // Очень тёмный синий
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawDimStars(GraphicsContext gc) {
        gc.setFill(Color.color(0.8, 0.8, 0.8, 0.7)); // Тусклые звёзды с прозрачностью
        for (int i = 0; i < 100; i++) {
            double x = Math.random() * WIDTH;
            double y = Math.random() * HEIGHT * 0.7; // Звёзды только в верхней части неба
            double size = Math.random() * 2 + 1;
            gc.fillOval(x, y, size, size);
        }
    }
    private void drawDarkSnowdrifts(GraphicsContext gc) {
        // Заливаем сугробы тёмным цветом
        gc.setFill(Color.color(0.2, 0.2, 0.3)); // Тёмные сугробы
        gc.fillRect(0, HEIGHT * 0.7, WIDTH, HEIGHT * 0.3);

        // Рисуем волны сугробов
        gc.setStroke(Color.color(0.3, 0.3, 0.4)); // Тёмные волны
        gc.setLineWidth(2);

        // Начинаем рисовать волны от левого края
        double startY = HEIGHT * 0.7; // Верхняя граница снега
        double amplitude = 10; // Амплитуда волн
        double frequency = 0.02; // Частота волн

        // Рисуем волну с помощью линии, следующей за синусоидой
        gc.beginPath();
        gc.moveTo(0, startY + Math.sin(0) * amplitude); // Начальная точка

        for (int x = 0; x <= WIDTH; x++) {
            double y = startY + Math.sin(x * frequency) * amplitude; // Вычисляем y для текущего x
            gc.lineTo(x, y); // Рисуем линию до следующей точки
        }

        // Завершаем волну и заливаем её
        gc.lineTo(WIDTH, HEIGHT); // Нижний правый угол
        gc.lineTo(0, HEIGHT); // Нижний левый угол
        gc.closePath();

        // Заливаем волну тёмным цветом
        gc.fill();
    }

    private void drawDarkTrees(GraphicsContext gc) {
        Color treeColor = Color.color(0.1, 0.1, 0.1); // Почти чёрный цвет для деревьев
        gc.setFill(treeColor);

        double snowTopY = HEIGHT * 0.7; // Верхняя граница снега

        for (int i = 0; i < 20; i++) { // Больше деревьев для эффекта глубины
            double x = Math.random() * WIDTH;
            double scale = Math.random() * 0.5 + 0.3; // Меньший размер для далёких деревьев

            // Нижняя точка дерева должна быть на уровне или ниже верхней границы снега
            double treeBottomY = snowTopY + Math.random() * 10; // Случайное смещение вниз
            double treeTopY = treeBottomY - 60 * scale; // Верхняя точка дерева

            drawTree(gc, x, treeBottomY, scale);
        }
    }

    private void drawTree(GraphicsContext gc, double x, double treeBottomY, double scale) {
        // Рисуем ствол (почти чёрный)
        gc.setFill(Color.color(0.15, 0.1, 0.1)); // Тёмно-коричневый
        gc.fillRect(x - 3 * scale, treeBottomY - 20 * scale, 6 * scale, 20 * scale);

        // Рисуем крону (почти чёрную)
        gc.setFill(Color.color(0.1, 0.1, 0.1)); // Почти чёрный
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