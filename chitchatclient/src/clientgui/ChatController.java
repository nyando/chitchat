package clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ChatController {

    @FXML
    TextArea messageArea;

    public void send(ActionEvent actionEvent) {
        this.messageArea.getText();


    }
}
