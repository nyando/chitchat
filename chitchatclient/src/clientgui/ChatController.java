package clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    TextField messageField;
    @FXML
    Button sendButton;

    private TextFieldInputWrapper wrapper;

    public void initWrapper() {
        this.wrapper = new TextFieldInputWrapper(this.messageField);
    }

    public TextFieldInputWrapper getWrapper() {
        return this.wrapper;
    }

    public void sendMessage(ActionEvent actionEvent) {
        this.wrapper.send = true;
        // notify waiting input wrapper
        synchronized (this.messageField) {
            this.messageField.requestFocus();
            this.messageField.notify();
        }
    }
}
