package clientgui.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

public class ChatController {

    private TextFieldInputWrapper wrapper;

    @FXML
    private WebView chatView;
    @FXML
    private TextField messageField;

    public void initWrapper() {
        this.wrapper = new TextFieldInputWrapper(this.messageField);
    }

    public TextFieldInputWrapper getWrapper() {
        return this.wrapper;
    }

    public WebView getChatView() {
        return this.chatView;
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
