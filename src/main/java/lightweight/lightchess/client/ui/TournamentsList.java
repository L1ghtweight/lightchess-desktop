package lightweight.lightchess.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TournamentsList implements Initializable {
    Main m;
    ClientNet clientNet;

    @FXML
    private GridPane activeTournaments;

    ArrayList<CasualPlayer> Players;

    public void setMain(Main m) {
        this.m = m;
    }

    public void setClientNet(ClientNet clientNet) {this.clientNet = clientNet;}

    public void init() {
        //this.Players = Players;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void goBack() throws IOException, InterruptedException {
        m.showDashboard();
    }
}


