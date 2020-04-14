package communication;

import clientcli.MessageInterpreter;

import java.io.InputStream;
import java.util.Scanner;

public class ReceiveHandler implements Runnable {

    private final InputStream in;
    private final IMessageOutput messageOutput;

    public ReceiveHandler(InputStream in, IMessageOutput messageOutput) {
        this.in = in;
        this.messageOutput = messageOutput;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(this.in);

        while (sc.hasNextLine()) {
            String msg = sc.nextLine();
            if (msg.startsWith("#WHISPER")) {
                messageOutput.printMessage(MessageInterpreter.convertWhisperIn(msg));
            } else if (msg.contains("#USERLIST")) {
                String[] userList = MessageInterpreter.convertUserlist(msg);
                messageOutput.printMessage("User list update: " + String.join(", ", userList));
            } else if (msg.contains(";")) {
                messageOutput.printMessage(MessageInterpreter.convertUserBroadcast(msg));
            } else {
                messageOutput.printMessage(MessageInterpreter.convertServerBroadcast(msg));
            }
        }

        sc.close();
    }

}
