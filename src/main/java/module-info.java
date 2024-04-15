module com.jam.mo.coinsgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jam.mo.coinsgame to javafx.fxml;
    exports com.jam.mo.coinsgame;
}