package lightweight.lightchess.client.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;

import java.io.IOException;

public class SignUp {
    public SignUp() {}

    private Main m;
    @FXML
    private Button button;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;
    ClientNet clientNet;


    public void goToLogin() throws IOException {
        m.showLogin();
    }

    public void setClientNet(ClientNet clientNet) {
        this.clientNet = clientNet;
    }

    public void signUp() throws IOException {
        String usernameStr = username.getText();
        String passwordStr = password.getText();
        clientNet.sendSignupRequest(usernameStr, passwordStr);
    }

    public void setMain(Main m) {
        this.m = m;
    }

    public void setMessage(String s) {
        message.setText(s);
    }
}
