package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lightweight.lightchess.ServerUI;
import lightweight.lightchess.server.net.ServerNet;

import java.io.IOException;
import java.util.Map;

public class HostTournament {

    public ServerNet serverNet;
    public ServerUI serverUI;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField duration;

    @FXML
    private TextField name;

    @FXML
    private TextField starts_in;

    @FXML
    private TextField time_format;

    @FXML
    private Label status;

    @FXML
    public void host(ActionEvent event) {
        String _name = name.getText();
        String _starts_in = starts_in.getText();
        String _time_format = time_format.getText();
        String _duration = duration.getText();
        String[] args = _time_format.split("\\+");

        if(isDouble(_starts_in) && isDouble(_duration) && args.length == 2 && isDouble(args[0]) && isDouble(args[1])) {
            if(!serverNet.tournament.setValues(_name, seconds(_starts_in), seconds(_duration), _time_format)) {
                status.setText("Tournament already running");
            } else {
                try {
                    serverUI.showHostedTournamentInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverNet.tournament.printTournamentDetails();
        }
        else {
            status.setText("Invalid Format");
        }

    }

    public String seconds(String minutes) {
        return String.valueOf(Math.round(Double.parseDouble(minutes) * 60));
    }

    public boolean isDouble( String input ) {
        try {
            Double.parseDouble( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

}
