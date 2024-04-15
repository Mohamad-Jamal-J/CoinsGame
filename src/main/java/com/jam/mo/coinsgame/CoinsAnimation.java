package com.jam.mo.coinsgame;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.util.LinkedList;
import java.util.List;

public class CoinsAnimation {

    final static int UP = -75, DOWN = 75;
    static int[] pickedCoinsIndices;
    static Integer[] coinsArray;
    static  ImageView rightArrowView, leftArrowView, playerOneView, playerTwoView;
    static StackPane root;
    static HBox coinsBox;
    static int player1Result =0, player2Result =0, turn = 0;
    static Label player1ResultLabel, player2ResultLabel, playerOneCoins, playerTwoCoins, pageLabel;
    static ScrollPane scrollPane;
    static VBox mainBox, movesBox;
    static Scene scene ;
    static Button autoButton , manualButton,reset ;
    static Label resultAnnouncement;
    private static Scene bulldozer() {
        // Create VBox as the mainBox container

        initializeImages();
        resultAnnouncement = new Label("");
        resultAnnouncement.setStyle("-fx-font-size: 30px; -fx-font-family: 'Lucida Console';" +
                " -fx-font-weight: bold; -fx-padding: 20px;" +
                " -fx-background-color: rgba(0,0,0,0.78); -fx-text-fill: white;  -fx-background-radius: 10px");
        resultAnnouncement.setVisible(false);


        mainBox = new VBox(40);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPrefSize(1500, 650);
        String cssFile = CoinsAnimation.class.getResource("/styles.css").toExternalForm();

        // Label for instructions
        Label welcome = new Label("Coins Game.");
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Lucida Console';");
        welcome.setPadding(new Insets(30));

        VBox container = coinsAnimationBox();

        HBox feedbackContainer = new HBox(20);
        GridPane resultsGrid = getGridPane();
        VBox logsContainer = getLogsContainer();

        feedbackContainer.getChildren().addAll(resultsGrid, logsContainer);
        feedbackContainer.setAlignment(Pos.BOTTOM_CENTER);

        VBox controlsContainer = new VBox(20);
        controlsContainer.setAlignment(Pos.CENTER);
        pageLabel = new Label("Use the right (respectively left) arrow key to see the next (previous) move.");
        pageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Lucida Console';");
        pageLabel.setVisible(false);
        HBox buttonsBox = getButtonsBox();
        buttonsBox.setAlignment(Pos.CENTER);

        controlsContainer.getChildren().addAll(pageLabel, buttonsBox);

        mainBox.getChildren().addAll(container, controlsContainer, feedbackContainer);

        root = new StackPane(welcome, leftArrowView, mainBox, resultAnnouncement, rightArrowView);
//        root.setStyle("-fx-background-color: linear-gradient(to right, #143e98, #2658d3);");
//        root.setStyle("-fx-background-color: #1d1716;");
//        root.setStyle("-fx-background-color: #14110F;");
        root.setStyle("-fx-background-color: #e8e8e8;");
        StackPane.setAlignment(welcome, Pos.TOP_CENTER);
        StackPane.setAlignment(mainBox, Pos.CENTER);
        StackPane.setAlignment(resultAnnouncement, Pos.CENTER);
        StackPane.setAlignment(rightArrowView, Pos.CENTER_RIGHT);
        StackPane.setAlignment(leftArrowView, Pos.CENTER_LEFT);
        root.getStylesheets().add(cssFile);

        leftArrowView.setVisible(false);
        rightArrowView.setVisible(false);
        return getScene(root, movesBox);
    }

    private static void initializeImages() {
        Image rightArrowImage = new Image(CoinsAnimation.class.getResourceAsStream("/images/right_arrow.png"));
        Image leftArrowImage = new Image(CoinsAnimation.class.getResourceAsStream("/images/left_arrow.png"));
        Image playerImage = new Image(CoinsAnimation.class.getResourceAsStream("/images/player.png"));
        rightArrowView = new ImageView(rightArrowImage);
        leftArrowView = new ImageView(leftArrowImage);
        playerOneView = new ImageView(playerImage);
//        playerOneView.setRotate(180);
        playerTwoView = new ImageView(playerImage);
    }

    private static VBox coinsAnimationBox() {
        VBox mainBox = new VBox(70);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setMaxWidth(pickedCoinsIndices.length*40 + (pickedCoinsIndices.length-1)*5);

        playerOneCoins = new Label("Player 1");
        playerOneCoins.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Lucida Console';");
        VBox playerOneProfile = new VBox(5);
        playerOneProfile.setAlignment(Pos.CENTER);
        playerOneProfile.getChildren().addAll(playerOneView, playerOneCoins);

        Line divider1 = new Line(0, 0, mainBox.getMaxWidth(), 0);

        VBox conty = new VBox(25);
//        divider1.setStyle("-fx-stroke: #a55233;  -fx-stroke-width: 2px");
//        divider1.setStyle("-fx-stroke: #34312D;  -fx-stroke-width: 2px");
        divider1.setStyle("-fx-stroke: #525252;  -fx-stroke-width: 2px");
        coinsBox = new HBox(5);
        coinsBox.setAlignment(Pos.CENTER);
        initializeHBox(coinsBox);
        Line divider2 = new Line(0, 0, mainBox.getMaxWidth(), 0);
//        divider2.setStyle("-fx-stroke: #a55233; -fx-stroke-width: 2px");
        divider2.setStyle("-fx-stroke: #525252; -fx-stroke-width: 2px");
        conty.getChildren().addAll(divider1, coinsBox, divider2);
        playerTwoCoins = new Label("Player 2");
        playerTwoCoins.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Lucida Console';");

        VBox playerTwoProfile = new VBox(5);
        playerTwoProfile.getChildren().addAll(playerTwoCoins, playerTwoView);
        playerTwoProfile.setAlignment(Pos.CENTER);

        mainBox.getChildren().addAll(playerOneProfile, conty, playerTwoProfile);
        return mainBox;
    }
    private static HBox getButtonsBox() {
        HBox buttonsBox = new HBox(5);
        autoButton = new Button("Start Animation");
        autoButton.setOnAction(event -> {
            pageLabel.setVisible(false);
            autoForwardAnimation();
        });

        reset = new Button("Reset");
//        reset.setStyle("-fx-background-color: #38302e");
//        reset.setStyle("-fx-background-color: #7E7F83; -fx-text-fill: #f3bc77");
        reset.setStyle("-fx-background-color: #b7b7b7; -fx-text-fill: #e8e8e8");

        reset.setOnAction(event -> {
            pageLabel.setVisible(false);
            autoBackwardAnimation();
        });

        reset.setOnMouseEntered(e -> {
            reset.setStyle("-fx-background-color: #312f2f; -fx-text-fill:  #e8e8e8;");
        });

        reset.setOnMouseExited(e -> {
            reset.setStyle("-fx-background-color: #b7b7b7; -fx-text-fill: #e8e8e8;");
        });

        manualButton = new Button("Play Manually");
//        manualButton.setStyle("-fx-background-color: #402a23");
        manualButton.setStyle("-fx-background-color: #8c8c8c; -fx-text-fill: #e8e8e8");

        manualButton.setOnMouseEntered(e -> {
            manualButton.setStyle("-fx-background-color: #312f2f; -fx-text-fill: #e8e8e8;");
        });

        manualButton.setOnMouseExited(e -> {
            manualButton.setStyle("-fx-background-color: #8c8c8c; -fx-text-fill: #e8e8e8; ");
        });

        manualButton.setOnAction(event -> {
            pageLabel.setVisible(true);
            mainBox.requestFocus();
        });

        buttonsBox.getChildren().addAll(autoButton, manualButton,reset);
        return buttonsBox;
    }
    private static VBox getLogsContainer() {
        VBox logsContainer = new VBox(4);
//        logsContainer.setStyle("-fx-border-radius: 5px; -fx-border-width: 1px; -fx-border-color: #402a23");
//        logsContainer.setStyle("-fx-border-radius: 5px; -fx-border-width: 1px; -fx-border-color: #7E7F83");
        logsContainer.setStyle("-fx-border-radius: 5px; -fx-border-width: 1px; -fx-border-color: #000000");

        logsContainer.setPrefSize(400, 134);
        logsContainer.setAlignment(Pos.TOP_CENTER);

        Label movesLogLabel = new Label("Moves Log:");
        movesLogLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-font-family: 'Lucida Console';");


        Line divider = new Line(0, 0, 390, 0);
//        divider.setStyle("-fx-stroke: #7c3d25;");
//        divider.setStyle("-fx-stroke: #34312D;");
        divider.setStyle("-fx-stroke: #525252;");

        logsContainer.getChildren().addAll(movesLogLabel, divider);

        movesBox = new VBox(10);
        movesBox.setAlignment(Pos.TOP_LEFT);
        movesBox.setStyle("-fx-border-color: rgba(255,255,255,0); -fx-border-width: 0px; -fx-background-color: rgba(255,255,255,0);");

        scrollPane = new ScrollPane();
        scrollPane.setContent(movesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(134);
        logsContainer.getChildren().add(scrollPane);
        return logsContainer;
    }
    private static GridPane getGridPane() {
        // GridPane for results
        GridPane resultsGrid = new GridPane();
        resultsGrid.setAlignment(Pos.CENTER);
        resultsGrid.setPrefSize(269, 134);
//        resultsGrid.setStyle("-fx-border-radius: 5px; -fx-border-width: 1px; -fx-border-color: #402a23");
//        resultsGrid.setStyle("-fx-border-radius: 5px; -fx-border-width: 1px; -fx-border-color: #7E7F83");
        resultsGrid.setStyle("-fx-border-radius: 5px; -fx-border-width: 1px; -fx-border-color: #000000");

        // Column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setMinWidth(10);
        col1.setMaxWidth(117.2);
        col1.setHalignment(HPos.CENTER);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        col2.setMinWidth(10);
        col2.setMaxWidth(165.6);

        resultsGrid.getColumnConstraints().addAll(col1, col2);

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(10);
        row1.setPrefHeight(30);
        row1.setVgrow(Priority.SOMETIMES);


        resultsGrid.getRowConstraints().addAll(row1, row1, row1);

        Label resultLabel = new Label("Result");

        resultLabel.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(resultLabel, 2);
        resultsGrid.add(resultLabel, 0, 0);

        Label player1Label = new Label("Player 1:");

        resultsGrid.add(player1Label, 0, 1);

        Label player2Label = new Label("Player 2:");
        resultsGrid.add(player2Label, 0, 2);

        player1ResultLabel = new Label("0");
        resultsGrid.add(player1ResultLabel, 1, 1);

        player2ResultLabel = new Label("0");
        resultsGrid.add(player2ResultLabel, 1, 2);
        return resultsGrid;
    }
    private static void autoForwardAnimation() {
        disableAllButtons(true);
        if (turn < pickedCoinsIndices.length) {
            int interval = 500;
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(interval)
                            , event -> animateNextPick(movesBox)));

            timeline.setCycleCount(pickedCoinsIndices.length);
            timeline.play();
            timeline.setOnFinished(actionEvent -> {
                resultAnnouncement.setText(player1Result>player2Result?
                        "Player 1 Won!":"It's a Draw!");
                flashView(resultAnnouncement,5.0);
                disableAllButtons(false);
                autoButton.setText("Restart");
            });
        }else {
            autoBackwardAnimation();
            autoForwardAnimation();
        }
    }

    private static void disableAllButtons(boolean b) {
        autoButton.setDisable(b);
        reset.setDisable(b);
        manualButton.setDisable(b);
    }

    private static void autoBackwardAnimation() {
        disableAllButtons(true);
        while (turn>0){
            animateUndoLastPick(movesBox);
        }
        disableAllButtons(false);
    }
    private static Scene getScene(StackPane root, VBox movesBox) {
        scene = new Scene(root);

        scene.setOnKeyPressed(action->{
            if (action.getCode() == KeyCode.RIGHT && turn < pickedCoinsIndices.length) {
                flashView(rightArrowView, 0.65);
                animateNextPick(movesBox);
            }else if (action.getCode() == KeyCode.LEFT && turn > 0 && !movesBox.getChildren().isEmpty()) {
                flashView(leftArrowView, 0.65);
                animateUndoLastPick(movesBox);
            }
            scrollPane.setContent(movesBox);

        });
        return scene;
    }
    private static void animateUndoLastPick(VBox movesBox) {
        movesBox.getChildren().remove(movesBox.getChildren().size()-2, movesBox.getChildren().size());

        int index = pickedCoinsIndices[--turn];
        coinsBox.getChildren().get(index).setTranslateY(coinsBox.getTranslateY());
        if (turn % 2 == 0) {
            player1Result -= coinsArray[index];
            player1ResultLabel.setText(player1Result +"");
        } else {
            player2Result -= coinsArray[index];
            player2ResultLabel.setText(player2Result +"");
        }
    }
    private static void animateNextPick(VBox movesBox) {
        if (turn <pickedCoinsIndices.length) {

            int index = pickedCoinsIndices[turn];
            String side= ".", player;
            if (turn+1 <pickedCoinsIndices.length) {
                int nextIndex = pickedCoinsIndices[turn+1];
                side = " from the " + ((index>nextIndex)? "right.":"left.");
            }

            if (turn % 2 == 0) {
                player1Result += coinsArray[index];
                player1ResultLabel.setText(player1Result +"");
                moveCoin(coinsBox, index, UP);
                player = "- Player 1 picked ";

            } else {
                player2Result += coinsArray[index];
                player2ResultLabel.setText(player2Result +"");
                moveCoin(coinsBox, index, DOWN);
                player = "- Player 2 picked ";
            }
            Label label = new Label(turn+1 + player +coinsArray[index] + side);
            label.setStyle("-fx-font-size: 15px");
            movesBox.getChildren().add(label);
            addDividerLine(movesBox);
            refreshScrollView();
            turn++;

        }
    }
    private static void addDividerLine(VBox movesBox) {
        Line divider = new Line(0, 0, movesBox.getWidth()-5, 0);
        divider.setStyle("-fx-stroke: #402a23;");
        movesBox.getChildren().add(divider);
    }
    private static void refreshScrollView() {
        scrollPane.applyCss();
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }
    private static List<Coin> getCoinsShapeList(){
        List<Coin> list = new LinkedList<>();
        for(Integer i: coinsArray){
            list.add(new Coin(i, 20));
        }
        return list;
    }
    private static void initializeHBox(HBox coinsBox){
        List<Coin> list = getCoinsShapeList();
        coinsBox.getChildren().clear();
        coinsBox.getChildren().addAll(list);
    }
    private static void moveCoin(HBox coinsBox, int coinIndex, double direction){
        TranslateTransition transition = new TranslateTransition(Duration.millis(500),
                coinsBox.getChildren().get(coinIndex));
        transition.setByY(direction);
        transition.play();
    }
    public static Scene getAnimationScene(int[] pickedCoinsIndices, Integer[] coinsArray){
        reset();
        CoinsAnimation.pickedCoinsIndices = pickedCoinsIndices;
        CoinsAnimation.coinsArray = coinsArray;
        return bulldozer();
    }
    private static void reset(){
        turn = 0;
        player1Result = player2Result = 0;
    }

    private static void flashView(Node node, double duration) {
        node.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> node.setVisible(false));
    }

}
