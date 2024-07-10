package com.jam.mo.coinsgame;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Coin extends StackPane {
    public Coin(int number, double radius) {
        Circle circle = new Circle(radius);
        circle.setStyle(" -fx-fill: black");
        Text text = new Text(String.valueOf(number));
        text.setFont(Font.font("Arial", radius/1.5));
        text.setFill(Color.valueOf("#ddd"));
        text.setTranslateX(circle.getTranslateX() / 2);
        text.setTranslateY(circle.getTranslateX() / 2);

        getChildren().addAll(circle, text);
    }
}
