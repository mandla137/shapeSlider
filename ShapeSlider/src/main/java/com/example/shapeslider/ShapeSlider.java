package com.example.shapeslider;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;


import java.util.Random;

public class ShapeSlider extends Application {

    private Shape currentShape;
    private Pane pane;
    private final Shape[] shapes = {new Circle(150), new Triangle(250, 250, 100), new Rectangle(200, 200)};
    private int currentIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();
        pane.setPrefSize(600, 600); // Increased size of the pane

        currentShape = shapes[currentIndex];
        currentShape.setFill(Color.LIGHTBLUE);
        currentShape.setLayoutX((pane.getWidth() - currentShape.getBoundsInLocal().getWidth()) / 2); // Center horizontally
        currentShape.setLayoutY((pane.getHeight() - currentShape.getBoundsInLocal().getHeight()) / 2); // Center vertically
        pane.getChildren().add(currentShape);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shape Slider");
        primaryStage.show();

        // Previous button
        Button previousButton = new Button("Previous");
        previousButton.setOnAction(event -> {
            currentIndex = (currentIndex - 1 + shapes.length) % shapes.length;
            updateShape();
        });

        // Next button
        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> {
            currentIndex = (currentIndex + 1) % shapes.length;
            updateShape();
        });

        // Change background button
        Button changeBackgroundButton = new Button("Change Background");
        changeBackgroundButton.setOnAction(event -> {
            currentShape.setFill(randomColor());
            pane.setStyle("-fx-background-color: " + randomColorHex()); // Change background color
            changeBackgroundButton.setStyle("-fx-background-color: " + randomColorHex()); // Change button background color
        });

        // Layout buttons
        pane.getChildren().addAll(previousButton, nextButton, changeBackgroundButton);
        layoutButtons(previousButton, nextButton, changeBackgroundButton);

        // Handle mouse drag events for shape movement
        currentShape.setOnMousePressed(this::handleMousePressed);
        currentShape.setOnMouseDragged(this::handleMouseDragged);
    }

    private void updateShape() {
        pane.getChildren().remove(currentShape);
        currentShape = shapes[currentIndex];
        currentShape.setFill(Color.LIGHTBLUE);
        currentShape.setLayoutX((pane.getWidth() - currentShape.getBoundsInLocal().getWidth()) / 2); // Center horizontally
        currentShape.setLayoutY((pane.getHeight() - currentShape.getBoundsInLocal().getHeight()) / 2); // Center vertically
        pane.getChildren().add(currentShape);
        currentShape.setOnMousePressed(this::handleMousePressed);
        currentShape.setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event) {
        currentShape.toFront(); // Bring the current shape to the front
    }

    private void handleMouseDragged(MouseEvent event) {
        double offsetX = event.getX();
        double offsetY = event.getY();
        currentShape.setLayoutX(currentShape.getLayoutX() + offsetX);
        currentShape.setLayoutY(currentShape.getLayoutY() + offsetY);
    }

    private Color randomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private String randomColorHex() {
        Random random = new Random();
        return String.format("#%02x%02x%02x", random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // Layout buttons relative to pane size and shape size
    private void layoutButtons(Button previousButton, Button nextButton, Button changeBackgroundButton) {
        double buttonHeight = previousButton.getHeight();
        double paneWidth = pane.getWidth();
        double paneHeight = pane.getHeight();
        double margin = 10;

        // Position previous button on the left bottom
        previousButton.setLayoutX(margin);
        previousButton.setLayoutY(paneHeight - buttonHeight - margin);

        // Position next button on the bottom right
        nextButton.setLayoutX(paneWidth - nextButton.getWidth() - margin);
        nextButton.setLayoutY(paneHeight - buttonHeight - margin);

        // Position change background button on the bottom middle
        changeBackgroundButton.setLayoutX((paneWidth - changeBackgroundButton.getWidth()) / 2);
        changeBackgroundButton.setLayoutY(paneHeight - buttonHeight - margin);

        // Adjust button positions to be above the center of the shapes
        double shapeCenterX = currentShape.getLayoutX() + currentShape.getBoundsInLocal().getWidth() / 2;
        double shapeCenterY = currentShape.getLayoutY() + currentShape.getBoundsInLocal().getHeight() / 2;

        previousButton.setLayoutY(shapeCenterY - previousButton.getHeight() / 2);
        nextButton.setLayoutY(shapeCenterY - nextButton.getHeight() / 2);
        changeBackgroundButton.setLayoutY(shapeCenterY - changeBackgroundButton.getHeight() / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }

    class Triangle extends Polygon {

        public Triangle(double centerX, double centerY, double size) {
            double halfSize = size / 2.0;
            double height = Math.sqrt(3) * halfSize;

            double[] points = {
                    centerX, centerY - height / 2,
                    centerX - halfSize, centerY + height / 2,
                    centerX + halfSize, centerY + height / 2
            };

            for (double point : points) {
                this.getPoints().add(point);
            }

            this.setFill(Color.TRANSPARENT);
            this.setStroke(Color.BLACK);
        }
    }
}
