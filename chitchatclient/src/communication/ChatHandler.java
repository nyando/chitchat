package communication;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Handles outgoing messages from the client, spawns the thread that handles incoming messages.
 */
public class ChatHandler implements Runnable {

    private final Socket server;
    private final PrintStream out;
    private final IMessageInput messageInput;
    private final IMessageOutput messageOutput;

    /**
     * Starts a ReceiveHandler to handle incoming messages, handles outgoing messages itself.
     * Terminates on "#quit" command.
     * @param server Server socket.
     * @param messageInput Stream interface for outgoing messages.
     * @param messageOutput Stream interface for incoming messages.
     * @throws IOException If problems arise while handling socket I/O.
     */
    public ChatHandler(Socket server, IMessageInput messageInput, IMessageOutput messageOutput) throws IOException {
        this.server = server;
        this.out = new PrintStream(this.server.getOutputStream());
        this.messageInput = messageInput;
        this.messageOutput = messageOutput;
    }

    @Override
    public void run() {
        try {
            new Thread(new ReceiveHandler(server.getInputStream(), this.messageOutput)).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (this.messageInput.isActive()) {
            String msg = this.messageInput.readLine();
            if (msg.toLowerCase().equals("#quit")) {
                break;
            }
            this.out.println(msg);
            this.out.flush();
        }

        this.out.close();
    }

}
