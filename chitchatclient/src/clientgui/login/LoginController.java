package clientgui.login;

import clientgui.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class LoginController {

    private Main main;

    @FXML
    private TextField serverAddressField;
    @FXML
    private TextField serverPortField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label userTakenLabel;

    public void setMain(Main main) {
        this.main = main;
    }

    public void login(ActionEvent actionEvent) throws IOException {
        Socket server;
        PrintStream serverInput;
        Scanner serverOutput;

        String serverAddress = this.serverAddressField.getText();
        String serverPort = this.serverPortField.getText();
        String username = this.usernameField.getText();

        server = new Socket(serverAddress, Integer.parseInt(serverPort));
        serverInput = new PrintStream(server.getOutputStream());
        serverOutput = new Scanner(server.getInputStream());

        serverInput.println(username);
        serverInput.flush();

        if (serverOutput.nextLine().equals("#IDTAKEN")) {
            this.userTakenLabel.setVisible(true);
        } else {
            this.main.setInfo(new LoginInfo(server, username));
            this.main.switchToChat();
        }
    }

}
