package lightweight.lightchess.client.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;

public class userStatistics {
    ClientNet clientNet;
    Main m;

    @FXML
    private Label username;

    @FXML
    private Label gamesDrawn;

    @FXML
    private Label gamesLost;

    @FXML
    private Label elo;

    @FXML
    private Label gamesWon;

    @FXML
    private Label tournamentsWon;

    public void setMain(Main m) {
        this.m = m;
    }
    public void setClientNet(ClientNet clientNet) {
        this.clientNet = clientNet;
    }
    public void goBack() throws IOException, InterruptedException {
        m.showDashboard();
    }

    public void init(String _username, String _gamesLost, String _elo, String _gamesDrawn, String _gamesWon, String _tournamentsWon) {
        username.setText(_username);
        gamesWon.setText(_gamesWon);
        gamesDrawn.setText(_gamesDrawn);
        gamesLost.setText(_gamesLost);
        elo.setText(_elo);
        tournamentsWon.setText(_tournamentsWon);
    }
}
