package communication;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ChatHandler implements Runnable {

    private final Socket server;
    private final PrintStream out;
    private final IMessageInput messageInput;
    private final IMessageOutput messageOutput;

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
            this.out.println(msg);
            this.out.flush();
        }

        this.out.close();
    }

}
