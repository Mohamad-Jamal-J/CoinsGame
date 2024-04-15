package com.jam.mo.coinsgame;

import java.util.ArrayList;
import java.util.Objects;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CoinsGameDriver extends Application {
    static Label alertLabel = new Label("");
    static OptimalScore[][] mainTable;
    static Integer[] coinsArray;
    static Scene mainScene;
    int[] pickedCoinsIndices;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        alertLabel.setStyle("-fx-text-fill: firebrick;  -fx-font-size: 16; -fx-font-family: 'Lucia Console';");
        String cssFile = Objects.requireNonNull(CoinsGameDriver.class.getResource("/styles.css")).toExternalForm();
        Label welcome = new Label("Coins Game.");
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Lucida Console';");
        welcome.setPadding(new Insets(30));

        VBox mainContent = new VBox(5.0);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getStylesheets().add(cssFile);


        Label instructionLabel = new Label("Insert a set of even number of coins\n(comma or space seperated)");
        TextField inputCoins = getTextField();

        TextArea table = getDPTableView();
        table.setVisible(false);

        Button animationButton = getAnimationButton();
        Button startButton = getProcessCoinsButton(table, inputCoins, animationButton, mainContent);

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(startButton, animationButton);
        buttonsBox.setAlignment(Pos.CENTER);

        mainContent.getChildren().addAll(welcome, instructionLabel, inputCoins, alertLabel, buttonsBox);
        mainContent.setAlignment(Pos.CENTER);

        mainScene = new Scene(mainContent, 1200.0, 700.0);
        stage.setScene(mainScene);
        stage.setFullScreen(true);
        stage.setTitle("Coins Game");
        stage.setFullScreenExitHint("null");
        stage.show();
    }
    private Integer[] validateInput(String input) {
        alertLabel.setText("");
        if (input != null && !input.isBlank() && !input.isEmpty()) {
            String[] inputToStringArray = input.trim().split("[, ]");
            ArrayList<Integer> inputList = new ArrayList<>();

            for (String token : inputToStringArray) {
                try {
                    if (!token.isEmpty() && !token.isBlank()) {
                        int integer = Integer.parseInt(token.trim());
                        if (integer <= 0) {
                            alertLabel.setText("Non-positive value " + integer + " is not allowed!");
                            return null;
                        }

                        inputList.add(integer);
                    }
                } catch (NumberFormatException var9) {
                    alertLabel.setText("Non-numeric value " + token.trim() + " is not allowed!");
                    return null;
                }
            }

            if (inputList.size() % 2 == 0 && !inputList.isEmpty()) {
                return inputList.toArray(new Integer[0]);
            } else {
                alertLabel.setText("Number of coins has to be even!");
                return null;
            }
        } else {
            alertLabel.setText("No values were provided");
            return null;
        }
    }
    private OptimalScore[][] makeDPTable(Integer[] coinsArray) {
        OptimalScore[][] dpTable = new OptimalScore[coinsArray.length][coinsArray.length];

        int i;
        for(i = 0; i < dpTable.length; ++i) {
            dpTable[i][i] = new OptimalScore(coinsArray[i], 0);
        }

        for(i = 1; i < dpTable.length; ++i) {
            int a = i;

            for(int b = 0; a < dpTable.length; ++b) {
                this.setValuesForDpTable(coinsArray, i, a, b, dpTable);
                ++a;
            }
        }

        return dpTable;
    }
    private void setValuesForDpTable(Integer[] coinsArray, int i, int a, int b, OptimalScore[][] dpTable) {
        int resultFromAbove = coinsArray[a] + dpTable[a - 1][b].remainder;
        int resultFromRight = coinsArray[a - i] + dpTable[a][b + 1].remainder;
        int remainder = resultFromRight >= resultFromAbove ? dpTable[a][b + 1].playerOneScore : dpTable[a - 1][b].playerOneScore;
        dpTable[a][b] = new OptimalScore(Math.max(resultFromAbove, resultFromRight), remainder);
        if (resultFromRight > resultFromAbove) {
            dpTable[a][b].setDirectionArrow((byte)1);
        } else if (resultFromRight == resultFromAbove) {
            dpTable[a][b].setDirectionArrow((byte)(dpTable[a][b + 1].remainder >= dpTable[a - 1][b].remainder ? 0 : 1));
        } else {
            dpTable[a][b].setDirectionArrow((byte)0);
        }

    }
    private void setUpPickedCoins(OptimalScore[][] dpTable) {
        pickedCoinsIndices = new int[dpTable.length];
        int i = dpTable.length - 1;
        int k = 0;

        for(int j = 0; i >= 0 && k < dpTable.length; ++j) {
            if (dpTable[i][k].directionArrow == -1) {
                pickedCoinsIndices[j] = i;
                break;
            }

            if (dpTable[i][k].directionArrow == 0)
                pickedCoinsIndices[j] = i--;
            else
                pickedCoinsIndices[j] = k++;
        }

    }
    private static String dpTableToString(StringBuilder dpBuilder, int n) {
        int i;
        for(i = 0; i < n; ++i)
            dpBuilder.append(String.format("%18d\t", i + 1));
        dpBuilder.append("\t\t");

        for(i = 0; i < n; ++i) {
            dpBuilder.append(String.format("\n\n%12d\t", i + 1));

            for(int j = 0; j <= i; ++j)
                dpBuilder.append(String.format("%12s\t", mainTable[i][j].toString()));
        }
        dpBuilder.append("\n\n");
        return dpBuilder.toString();
    }
    private TextField getTextField() {
        TextField textField = new TextField("90 80 70 60 50 40 20 30 40 100 60 40 20 90 70 50 30 10 15 23 42 81 73 94");
        textField.setPromptText("Enter values here");
        textField.setAlignment(Pos.CENTER);
        return textField;
    }
    private static TextArea getDPTableView() {
        TextArea table = new TextArea("");
        table.setEditable(false);
        return table;
    }
    private Button getAnimationButton() {
        Button animationButton = new Button("View Animation");
        animationButton.setOnAction((action) -> {

            Scene animationScene = CoinsAnimation.getAnimationScene(pickedCoinsIndices, coinsArray);
            Stage animationStage = new Stage();
            animationStage.setScene(animationScene);
            animationStage.setFullScreen(true);
            animationStage.setTitle("Animation Scene");
            animationStage.show();
        });
        animationButton.setDisable(true);
        return animationButton;
    }
    private Button getProcessCoinsButton(TextArea table, TextField inputCoins, Button animationButton, VBox mainContents) {
        Button startButton = new Button("Start Game");
        startButton.setOnAction(action -> {
            table.setText("");
            coinsArray = validateInput(inputCoins.getText());
            pickedCoinsIndices = null;
            animationButton.setDisable(true);
            StringBuilder dpBuilder = new StringBuilder("\t");
            if (coinsArray != null) {
                int n = coinsArray.length;
                mainTable = this.makeDPTable(coinsArray);
                table.setText(dpTableToString(dpBuilder, n));
                animationButton.setDisable(false);
                setUpPickedCoins(mainTable);
                animationButton.fire();

                if (!mainContents.getChildren().contains(table))
                    mainContents.getChildren().add(table);
                table.setVisible(true);
            }else {
                if (mainContents.getChildren().contains(table))
                    mainContents.getChildren().remove(table);
                table.setVisible(false);
            }


        });
        return startButton;
    }

}




