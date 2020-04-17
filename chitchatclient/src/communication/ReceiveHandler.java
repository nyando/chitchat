package communication;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Handle incoming server messages and pass them on to the message output interface.
 */
public class ReceiveHandler implements Runnable {

    private final InputStream in;
    private final IMessageOutput messageOutput;

    /**
     * Separate thread to handle incoming messages from server.
     * @param in InputStream streaming incoming messages
     * @param messageOutput Interface to which to write received messages (stdout, graphical interface, file, etc.).
     */
    public ReceiveHandler(InputStream in, IMessageOutput messageOutput) {
        this.in = in;
        this.messageOutput = messageOutput;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(this.in);
        while (sc.hasNextLine()) { this.messageOutput.printMessage(sc.nextLine()); }
        sc.close();
    }

}
