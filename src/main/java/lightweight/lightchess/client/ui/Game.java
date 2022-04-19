package lightweight.lightchess.client.ui;

import com.github.bhlangonijr.chesslib.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;
import lightweight.lightchess.logic.Logic;

import java.io.IOException;

public class Game {

    @FXML
    public AnchorPane anchorPane;

    ClientNet clientNet;
    Main m;

    public void setMain(Main m) {
        this.m = m;
    }
    public void setClientNet(ClientNet clientNet) {
        this.clientNet = clientNet;
    }
    public void goBack() throws IOException, InterruptedException {
        Board gameBoard = new Board();
        Logic logic = new Logic();
        ChessBoard chessBoard = new ChessBoard(500, Color.web("#f0d9b5"), Color.web("#b58863"), gameBoard, logic);
        clientNet.chessBoard = chessBoard;
        m.showDashboard();
    }
}
