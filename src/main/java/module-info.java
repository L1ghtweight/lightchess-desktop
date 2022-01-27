module org.lightweight.lightchess {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.lightweight.lightchess to javafx.fxml;
    exports org.lightweight.lightchess;
}