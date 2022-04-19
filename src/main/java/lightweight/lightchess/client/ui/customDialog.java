package lightweight.lightchess.client.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class customDialog {
    @FXML
    private Label prompt;

    public void setPrompt(String msg) {
        prompt.setText(msg);
    }
}
