package lightweight.lightchess;

import com.github.bhlangonijr.chesslib.Board;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lightweight.lightchess.client.ui.ChessBoard;
import lightweight.lightchess.logic.Logic;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;

public class ClientMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Board gameBoard = new Board();
        Logic logic = new Logic();

        ChessBoard chessBoard = new ChessBoard(500, Color.web("#f0d9b5"), Color.web("#b58863"), gameBoard);
        Scene scene = new Scene(chessBoard, 500, 500);
        stage.setTitle("LightChess");
        stage.setScene(scene);
        stage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientNet clientNet = new ClientNet(chessBoard);
                clientNet.start();
            }
        }).start();


    }
}