package com.jam.mo.coinsgame;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Coin extends StackPane {
    public Coin(int number, double radius) {
        Circle circle = new Circle(radius);
//        circle.setStyle(" -fx-fill: linear-gradient(#c4613d, #963e1e, #c4613d)");
        circle.setStyle(" -fx-fill: black");
//        circle.setStroke(Color.BROWN);
//        circle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        Text text = new Text(String.valueOf(number));
        text.setFont(Font.font("Arial", radius/1.5));
        text.setFill(Color.valueOf("#ddd"));
        text.setTranslateX(circle.getTranslateX() / 2);
        text.setTranslateY(circle.getTranslateX() / 2);

        getChildren().addAll(circle, text);
    }
}
