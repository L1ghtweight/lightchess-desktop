package lightweight.lightchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;

public class Login {
    public Login() {}

    Main m;
    @FXML
    private Button button;
    @FXML
    private Label message;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    ClientNet clientNet;

    public void setMain(Main m) {
        this.m = m;
    }

    public void goToSignUp(ActionEvent event) throws IOException {
        m.showSignUp();
    }

    public void setClientNet(ClientNet clientNet) {
        this.clientNet = clientNet;
    }

    public void login(ActionEvent event) throws IOException {
        String usernameStr = username.getText();
        String passwordStr = password.getText();
        clientNet.sendLoginRequest(usernameStr, passwordStr);
    }

    public void setMessage(String s) {
        message.setText(s);
    }
}
