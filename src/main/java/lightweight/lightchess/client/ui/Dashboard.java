package lightweight.lightchess.client.ui;

import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

public class Dashboard {
    Main m;
    ClientNet clientNet;

    public void setMain(Main m) {
        this.m = m;
    }
    public void setClientNet(ClientNet clientNet) {
        this.clientNet = clientNet;
    }
}
