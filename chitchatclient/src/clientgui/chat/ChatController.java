package clientgui.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

/**
 * Controller for the Chat view.
 * Creates the TextFieldInputWrapper that allows for communication over the input field.
 */
public class ChatController {

    private TextFieldInputWrapper wrapper;

    @FXML
    private WebView chatView;
    @FXML
    private TextField messageField;

    /**
     * Initialize TextFieldInputWrapper on creating a new Chat scene.
     */
    public void initWrapper() {
        this.wrapper = new TextFieldInputWrapper(this.messageField);
    }

    public TextFieldInputWrapper getWrapper() {
        return this.wrapper;
    }

    public WebView getChatView() {
        return this.chatView;
    }

    /**
     * On "send" click, notify waiting TextFieldInputWrapper that a new message is available.
     * @param actionEvent "Send" button click event.
     */
    public void sendMessage(ActionEvent actionEvent) {
        this.wrapper.send = true;
        // notify waiting input wrapper
        synchronized (this.messageField) {
            this.messageField.requestFocus();
            this.messageField.notify();
        }
    }
}
