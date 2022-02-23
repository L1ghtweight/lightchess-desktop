package lightweight.lightchess;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lightweight.lightchess.client.ui.UIBoard;
import lightweight.lightchess.logic.Logic;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;

public class ClientMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Board gameboard = new Board();
        Logic logic = new Logic();

        UIBoard UIboard = new UIBoard(1000, Color.web("#f0d9b5"), Color.web("#b58863"), gameboard);
        UIboard.setAlignment(Pos.CENTER);
        Scene scene = new Scene(UIboard, 1000, 1000);
        stage.setTitle("LightChess");
        stage.setScene(scene);
        stage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientNet clientNet = new ClientNet(UIboard);
                clientNet.start();
            }
        }).start();


    }
}