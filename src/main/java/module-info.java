module org.lightweight.lightchess {
    requires javafx.controls;
    requires javafx.fxml;
    requires chesslib;

    opens org.lightweight.lightchess to javafx.fxml;
    exports org.lightweight.lightchess;
}