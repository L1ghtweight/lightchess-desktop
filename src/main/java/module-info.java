module com.lightweight.lightchess {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.lightweight.lightchess to javafx.fxml;
    exports com.lightweight.lightchess;
}