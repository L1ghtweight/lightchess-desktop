package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;
import org.mariadb.jdbc.client.Client;

import java.io.IOException;

public class DialogBox {

    Stage newStage;
    Main m;
    ClientNet clientNet;

    @FXML
    private Button noBtn;

    @FXML
    private Label prompt;
    private String username;
    @FXML
    private Button yesBtn;

    public void setMain(Main m) {
        this.m = m;
    }

    public void setClientNet(ClientNet clientNet) {
        this.clientNet = clientNet;
    }

    @FXML
    public void yesBtnClicked(ActionEvent e) throws IOException {
        clientNet.sendPlayRequestAccepted();
        newStage.close();
        m.clientNet.chessBoard.setClocks(clientNet.userInfo.get("time_format"));
        m.startGame();
    }

    @FXML
    public void noBtnClicked(ActionEvent e) {
        newStage.close();
    }

    public void setStage(Stage newStage) {
        this.newStage = newStage;
    }

    public void init(String opponent, String gameFormat) {
        prompt.setText(opponent + " has challenged you to a " + gameFormat + " game");
    }

}
