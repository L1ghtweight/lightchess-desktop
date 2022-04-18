package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

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
}
