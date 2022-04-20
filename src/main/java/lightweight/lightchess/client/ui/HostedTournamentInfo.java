package lightweight.lightchess.client.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lightweight.lightchess.ServerUI;
import lightweight.lightchess.net.Information;
import lightweight.lightchess.server.net.ServerNet;

import java.util.Map;

public class HostedTournamentInfo {
    public ServerNet serverNet;
    public ServerUI serverUI;


    @FXML
    private GridPane ongoingMatches;

    @FXML
    private GridPane participants;


    public void update()
    {
        ongoingMatches.getChildren().clear();
        participants.getChildren().clear();

        int i = 0;
        for(Map.Entry<String, String>M : serverNet.tournament.matchPairs.entrySet()){

            Label player1 = new Label(M.getKey());
            Label player2 = new Label(M.getValue());
            Label vs = new Label("vs");
            player1.setPrefWidth(372/3);
            player2.setPrefWidth(372/3);
            vs.setPrefWidth(372/3);

            player1.setAlignment(Pos.CENTER);
            player2.setAlignment(Pos.CENTER);
            vs.setAlignment(Pos.CENTER);

            ongoingMatches.add(player1, 0, i, 1, 1);
            ongoingMatches.add(vs, 1, i, 1, 1);
            ongoingMatches.add(player2, 2, i, 1, 1);
            i++;
        }

        i = 0;

        for(Map.Entry<String, Information>M : serverNet.tournament.registeredList.entrySet()) {

            Label player = new Label(M.getKey());
            player.setAlignment(Pos.CENTER);
            player.setPrefWidth(372);
            participants.add(player, 0, i, 1, 1);
            i++;
        }
        //for(serverNet.tournament.loggedInList)
    }
}
