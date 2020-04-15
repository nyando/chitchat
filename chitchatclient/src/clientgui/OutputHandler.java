package clientgui;

import communication.IMessageOutput;

public class OutputHandler implements IMessageOutput {

    @Override
    public void printMessage(String msg) {
        // for testing purposes
        System.out.println(msg);
    }
}
