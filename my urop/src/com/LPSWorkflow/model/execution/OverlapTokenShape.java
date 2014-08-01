package com.LPSWorkflow.model.execution;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Visual representation of overlapping (multiple) tokens
 */
public class OverlapTokenShape extends StackPane {
    public OverlapTokenShape() {
        this.getStyleClass().add("overlapping-token");
        Circle circle1 = new Circle(8);
        Circle circle2 = new Circle(8);
        Circle circle3 = new Circle(8);
        circle1.setTranslateX(4);
        circle1.setTranslateY(-8);
        circle2.setTranslateX(8);
        circle1.getStyleClass().add("overlapping-token-circle");
        circle2.getStyleClass().add("overlapping-token-circle");
        circle3.getStyleClass().add("overlapping-token-circle");
        StackPane tokenStackPane = new StackPane(circle1, circle2, circle3);
        this.getChildren().addAll(tokenStackPane);
    }
}
