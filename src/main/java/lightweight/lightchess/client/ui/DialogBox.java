package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DialogBox {
    @FXML
    private Button noBtn;

    @FXML
    private Label prompt;
    private String username;
    @FXML
    private Button yesBtn;

    @FXML
    public void yesBtnClicked(ActionEvent e) {
        prompt.setText(username + " have invited you to play.");
    }

    @FXML
    public void noBtnClicked(ActionEvent e) {
        prompt.setText("haha "+" have invited you to play.");
    }

}
