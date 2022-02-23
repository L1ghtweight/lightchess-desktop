module org.lightweight.lightchess {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    requires chesslib;
    requires javafxsvg;

//    opens lightweight.lightchess to javafx.fxml;
//    exports lightweight.lightchess;
    exports lightweight.lightchess.client.ui;
    opens lightweight.lightchess.client.ui to javafx.fxml;
    exports lightweight.lightchess;
    opens lightweight.lightchess to javafx.fxml;
//    exports lightweight.lightchess.client.net;
//    opens lightweight.lightchess.client.net to javafx.fxml;
//    exports lightweight.lightchess.server.net;
//    opens lightweight.lightchess.server.net to javafx.fxml;
//    exports lightweight.lightchess.net;
//    opens lightweight.lightchess.net to javafx.fxml;
}