package clientgui.chat;

import communication.IMessageInput;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TextFieldInputWrapper implements IMessageInput {

    volatile boolean send = false;

    private final TextField field;
    private final Queue<String> messageQueue;

    public TextFieldInputWrapper(TextField field) {
        this.field = field;
        // send contents of textfield when ENTER is pressed
        this.field.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.send = true;
                synchronized (this.field) {
                    this.field.notify();
                }
            }
        });
        this.messageQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean isActive() {
        if (this.messageQueue.isEmpty()) {
            // wait until notify from "send" button or ENTER
            synchronized (this.field) {
                while (!(this.field.isFocused() && this.send)) {
                    try {
                        this.field.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                String msg = this.field.getText();
                this.field.setText("");
                this.messageQueue.add(msg);
                this.send = false;
            }
        }
        return true;
    }

    @Override
    public String readLine() {
        String msg = this.messageQueue.remove();

        if (msg.startsWith("@")) {
            return MessageInterpreter.convertWhisperOut(msg);
        } else return msg;
    }
}
