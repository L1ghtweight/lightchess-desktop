package lightweight.lightchess;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lightweight.lightchess.client.ui.UIBoard;

import java.io.IOException;

public class ClientMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UIBoard board = new UIBoard(500, Color.web("#f0d9b5"), Color.web("#b58863"));
        board.setAlignment(Pos.CENTER);
        Scene scene = new Scene(board, 500, 500);
        stage.setTitle("LightChess");
        stage.setScene(scene);
        stage.show();
    }
}