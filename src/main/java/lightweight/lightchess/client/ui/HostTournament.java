package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HostTournament {

    q

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
    void host(ActionEvent event) {
        String _name = name.getText();
        String _starts_in = starts_in.getText();
        String _time_format = time_format.getText();
        String _duration = duration.getText();
        System.out.println("Starting Tournament " + _name + " " + _starts_in + " " + _time_format + " " + _duration);

    }

}
