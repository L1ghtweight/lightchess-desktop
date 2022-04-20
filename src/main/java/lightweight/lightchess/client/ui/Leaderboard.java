package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

import java.util.Comparator;

public class Leaderboard {

    Main m;
    ClientNet clientNet;


    @FXML
    private GridPane leaderboard;

    public void setMain(Main m) {
        this.m = m;
    }

    public void setClientNet(ClientNet clientNet) {this.clientNet = clientNet;}

    public void init() {
        clientNet.fetchScoreBoard();

        if(this.clientNet.score_board == null)
            return;

        leaderboard.getChildren().clear();

        clientNet.score_board.sort((t1, t2) -> {
            int ret = -1;
            if((Integer.parseInt(t2.getValue()) > Integer.parseInt(t1.getValue())))
                ret = +1;
            return ret;
        });

        for(int i=0;i < clientNet.score_board.size(); i++) {

            Pair<String, String> userInfo = clientNet.score_board.get(i);
            String username = userInfo.getKey();
            String points = userInfo.getValue();

            Label ranking = new Label(Integer.toString(i+1));
            Label userLabel = new Label(username);
            Label pointsLabel = new Label(points);

            ranking.setAlignment(Pos.CENTER);
            userLabel.setAlignment(Pos.CENTER);
            pointsLabel.setAlignment(Pos.CENTER);

            ranking.setPrefWidth(510/3);
            userLabel.setPrefWidth(510/3);
            pointsLabel.setPrefWidth(510/3);

            if(userInfo.getKey().equals(clientNet.username)) {
                userLabel.setStyle("-fx-background-color: #90ee90;");
                pointsLabel.setStyle("-fx-background-color: #90ee90;");
                ranking.setStyle("-fx-background-color: #90ee90;");
            }

            leaderboard.add(ranking, 0, i, 1, 1);
            leaderboard.add(userLabel, 1, i, 1, 1);
            leaderboard.add(pointsLabel, 2, i, 1, 1);
        }
    }
}
