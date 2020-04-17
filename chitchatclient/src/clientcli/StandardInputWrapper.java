package clientcli;

import communication.IMessageInput;

import java.util.Scanner;

/**
 * Implements message passing to ChatHandler for standard input.
 */
public class StandardInputWrapper implements IMessageInput {

    private Scanner sc;

    /**
     * The CLI input wrapper simply wraps a scanner around standard input.
     * Messages are sent by lines.
     */
    public StandardInputWrapper() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Blocks until a new message line is input and confirmed with newline.
     * @return True when a new message is available.
     */
    @Override
    public boolean isActive() {
        return sc.hasNextLine();
    }

    /**
     * If message starts with "@", it is a whisper message, so convert it before sending.
     * Otherwise simply hand off the message to the ChatHandler.
     * @return Whisper message or regular message, depending on input.
     */
    @Override
    public String readLine() {
        String msg = sc.nextLine();
        return msg.startsWith("@") ? MessageInterpreter.convertWhisperOut(msg) : msg;
    }
}
