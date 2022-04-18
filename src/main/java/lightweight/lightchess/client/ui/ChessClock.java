package lightweight.lightchess.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

public class ChessClock {

    @FXML
    private GridPane moves;

    @FXML
    private ScrollPane movesList;

    @FXML
    private Label opponentName;

    @FXML
    private Label opponentTime;

    @FXML
    private Label playerTime;

    @FXML
    private Label playerUsername;

    void init(String playerUsername, String playerTime, String opponentName, String opponentTime) {
        this.opponentName.setText(opponentName);
        this.opponentTime.setText(opponentTime);
        this.playerUsername.setText(playerUsername);
        this.playerTime.setText(playerTime);
    }

    void updateTime(String playerTime, String opponentTime) {
        this.playerTime.setText(playerTime);
        this.opponentTime.setText(opponentTime);
    }

    int col = 0, row = 1;


    public void showPGN(ArrayList<String> movesList) {
        /*for(int i = 0;i < movesList.size(); i++) {
            FXMLLoader fxmlLoader= new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("move.fxml"));
            try{
                AnchorPane ss = fxmlLoader.load();
                MoveController controller = fxmlLoader.getController();
                controller.setData(movesList.get(i));
                if(col == 2) {
                    col = 0;
                    row++;
                }
                moves.add(ss, col++, row);

            } catch (IOException e){
                e.printStackTrace();
            }
        }*/
    }
}
