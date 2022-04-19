package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CasualPlayerList implements Initializable {
    Main m;
    ClientNet clientNet;

    @FXML
    private GridPane activePlayers;

    @FXML
    private TextField time_format_text;

    @FXML
    private Label message;

    ArrayList<CasualPlayer> Players;

    public void setMain(Main m) {
        this.m = m;
    }

    public void setClientNet(ClientNet clientNet) {this.clientNet = clientNet;}

    public void init() {
        if(this.clientNet.usersList == null)
            return;
        activePlayers.getChildren().clear();
        for(int i=0;i < clientNet.usersList.size(); i++) {
            Pair<String, String> userInfo = clientNet.usersList.get(i);
            if(userInfo.getKey().equals(clientNet.username)) {
                continue;
            }

            String opponentUsername = userInfo.getKey();
            String timeString = userInfo.getValue();
            Button playerButton = new Button(userInfo.getKey());

            playerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    System.out.println("Sending play request");
                    clientNet.sendPlayRequest(opponentUsername);
                    m.clientNet.chessBoard.setClocks(timeString);
                    m.clientNet.chessBoard.opponentUsername = opponentUsername;
                }
            });

            playerButton.setPrefWidth(510/2);
            activePlayers.add(playerButton, 0, i, 1, 1);
            Label timeFormat = new Label(timeString);
            timeFormat.setStyle("-fx-border-color: #0f0f0f; -fx-border-radius: 15px; -fx-background-color: #ffffff; -fx-background-radius: 15px");
            timeFormat.setPrefWidth(510/2);
            timeFormat.setAlignment(Pos.CENTER);
            activePlayers.add(timeFormat, 1, i, 1, 1);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void goBack() throws IOException, InterruptedException {
        m.showDashboard();
    }

    public void setTimeFormat() throws IOException {
        String newTimeFormat = time_format_text.getText();
        System.out.println(newTimeFormat);
        String[] slices =  newTimeFormat.split("\\+",2);
        if(slices.length == 2 && isInteger(slices[0]) && isInteger(slices[1])) {
            clientNet.updateTimeFormat(newTimeFormat);
            clientNet.fetchUsersList();
            message.setText("");
            m.showCasualPlayerList();
        } else {
            message.setText("Invalid Time Format");
        }
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}


