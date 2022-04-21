package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        if(clientNet.tournament_info == null)
            return;

        String tournamentName = clientNet.tournament_info.get("name");

        if(tournamentName.equals("NULL"))
            return;

        String startTime = clientNet.tournament_info.get("start_time");
        String endTime = clientNet.tournament_info.get("end_time");

        if(LocalDateTime.now().isAfter(LocalDateTime.parse(endTime))){
            return;
        }

        int starts_in = Math.max(0, -1 * (int) Duration.between(LocalDateTime.parse(startTime), LocalDateTime.now()).toMinutes());
        int ends_in = Math.max(0, -1 * (int) Duration.between(LocalDateTime.parse(endTime), LocalDateTime.now()).toMinutes());

        startTime = "in " + Integer.toString(starts_in) + " mins";
        endTime = "in " + Integer.toString(ends_in) + " mins";

        String timeFormat = clientNet.tournament_info.get("time_format");

        Button button = new Button(tournamentName);
        button.setPrefWidth(510/4);
        activeTournaments.add(button, 0, 0, 1, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                clientNet.sendRegisterRequest();
                try {
                    m.showChessBoard();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Label time = new Label(timeFormat);
        time.setStyle("-fx-border-color: #0f0f0f; -fx-border-radius: 15px; -fx-background-color: #ffffff; -fx-background-radius: 15px");
        time.setPrefWidth(510/4);
        time.setAlignment(Pos.CENTER);
        activeTournaments.add(time, 1, 0, 1, 1);

        Label start = new Label(startTime);
        start.setStyle("-fx-border-color: #0f0f0f; -fx-border-radius: 15px; -fx-background-color: #ffffff; -fx-background-radius: 15px");
        start.setPrefWidth(510/4);
        start.setAlignment(Pos.CENTER);
        activeTournaments.add(start, 2, 0, 1, 1);

        Label end = new Label(endTime);
        end.setStyle("-fx-border-color: #0f0f0f; -fx-border-radius: 15px; -fx-background-color: #ffffff; -fx-background-radius: 15px");
        end.setPrefWidth(510/4);
        end.setAlignment(Pos.CENTER);
        activeTournaments.add(end, 3, 0, 1, 1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void goBack() throws IOException, InterruptedException {
        m.showDashboard();
    }
}


