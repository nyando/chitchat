package communication;

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
        while (sc.hasNextLine()) { this.messageOutput.printMessage(sc.nextLine()); }
        sc.close();
    }

}
