package lightweight.lightchess.client.ui;

import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;

public class Dashboard {
    Main m;
    ClientNet clientNet;

    public void setMain(Main m) {
        this.m = m;
    }
    public void setClientNet(ClientNet clientNet) {
        this.clientNet = clientNet;
    }
    public void goToCasualPlayerList() throws IOException {
        m.showCasualPlayerList();
    }
    public void goToTournamentsList() throws IOException {
        m.showTournamentsList();
    }
    public void showUserStatistics() throws IOException {
        m.showUserStatistics();
    }
}
