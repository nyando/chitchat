package clientgui.chat;

import communication.IMessageInput;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Provides message input interface for the TextField in the GUI chat.
 */
public class TextFieldInputWrapper implements IMessageInput {

    volatile boolean send = false;

    private final TextField field;
    private final Queue<String> messageQueue;

    /**
     * Create wrapper for an input message stream from a JavaFX text field.
     * Input is sent whenever ENTER is pressed.
     * Messages are stored in a queue.
     * @param field JavaFX text field functioning as input stream.
     */
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

    /**
     * Blocks while waiting for a message, returns true when ENTER is pressed (or the send button is clicked).
     * Then empties TextField and dispatches message to the message queue.
     * @return True on new available message.
     */
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

    /**
     * Return the next message waiting in the message queue.
     * @return Head of the message queue.
     */
    @Override
    public String readLine() {
        String msg = this.messageQueue.remove();

        if (msg.toLowerCase().equals("#quit")) {
            Platform.exit();
        }

        if (msg.startsWith("@")) {
            return MessageInterpreter.convertWhisperOut(msg);
        } else return msg;
    }
}
