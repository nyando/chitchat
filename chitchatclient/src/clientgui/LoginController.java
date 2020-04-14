package clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    public TextField serverAddressField;
    public TextField serverPortField;
    public TextField usernameField;
    public Button loginButton;

    public LoginController() {

    }

    public void login(ActionEvent actionEvent) {
        String serverAddress = this.serverAddressField.getText();
        String serverPort = this.serverPortField.getText();
    }
}
