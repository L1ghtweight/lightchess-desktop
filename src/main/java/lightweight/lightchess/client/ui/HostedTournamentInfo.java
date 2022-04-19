package lightweight.lightchess.client.ui;

import lightweight.lightchess.ServerUI;
import lightweight.lightchess.server.net.ServerNet;

import java.util.Map;

public class HostedTournamentInfo {
    public ServerNet serverNet;
    public ServerUI serverUI;

    public void print()
    {
        for(Map.Entry<String, String>M : serverNet.tournament.matchPairs.entrySet()){
            System.out.println(M.getKey() + " is playing with " + M.getValue());
        }

        //for(serverNet.tournament.loggedInList)
    }
}
