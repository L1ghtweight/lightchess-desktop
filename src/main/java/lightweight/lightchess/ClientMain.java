package lightweight.lightchess;

import com.github.bhlangonijr.chesslib.Board;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lightweight.lightchess.client.ui.UIBoard;
import lightweight.lightchess.logic.Logic;

import java.io.IOException;

public class ClientMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Board gameboard = new Board();
        Logic logic = new Logic();

        UIBoard UIboard = new UIBoard(500, Color.web("#f0d9b5"), Color.web("#b58863"), gameboard);
        UIboard.setAlignment(Pos.CENTER);
        Scene scene = new Scene(UIboard, 500, 500);
        stage.setTitle("LightChess");
        logic.makeMove(gameboard, "e2e6");
        UIboard.updateBoard(gameboard);
        stage.setScene(scene);
        stage.show();
    }
}