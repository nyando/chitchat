package clientcli;

import communication.IMessageInput;

import java.util.Scanner;

public class StandardInputWrapper implements IMessageInput {

    private Scanner sc;

    public StandardInputWrapper() {
        this.sc = new Scanner(System.in);
    }

    @Override
    public boolean isActive() {
        return sc.hasNextLine();
    }

    @Override
    public String readLine() {
        String msg = sc.nextLine();
        return msg.startsWith("@") ? MessageInterpreter.convertWhisperOut(msg) : msg;
    }
}
