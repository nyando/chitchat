package communication;

import clientcli.MessageInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatHandler implements Runnable {

    private final Socket server;
    private final PrintStream out;
    private final InputStream messageInput;
    private final IMessageOutput messageOutput;
    private final Scanner inputScan;

    public ChatHandler(Socket server, InputStream messageInput, IMessageOutput messageOutput) throws IOException {
        this.server = server;
        this.out = new PrintStream(this.server.getOutputStream());
        this.messageInput = messageInput;
        this.messageOutput = messageOutput;
        this.inputScan = new Scanner(this.messageInput);
    }

    @Override
    public void run() {
        try {
            new Thread(new ReceiveHandler(server.getInputStream(), this.messageOutput)).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (this.inputScan.hasNextLine()) {
            String msg = this.inputScan.nextLine();

            if (msg.startsWith("@")) {
                this.out.println(MessageInterpreter.convertWhisperOut(msg));
            } else {
                this.out.println(msg);
            }
            this.out.flush();
        }

        this.out.close();
        this.inputScan.close();
    }

}
