package clientgui;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.InputStream;

public class MessageHandler extends InputStream {

    byte[] contents;
    int pointer;

    public MessageHandler(TextArea messageArea) {
        this.contents = messageArea.getText().getBytes();
        this.pointer = 0;
        messageArea.setText("");
    }

    @Override
    public int read() throws IOException {
        if (this.pointer >= contents.length) { return -1; }
        else return this.contents[pointer];
    }


}
