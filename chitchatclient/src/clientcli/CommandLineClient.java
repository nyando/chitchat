package clientcli;

import communication.IMessageOutput;
import communication.ChatHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class CommandLineClient implements IMessageOutput {

    public static void main(String[] args) throws IOException {
        new CommandLineClient().init();
    }

    public void init() throws IOException {
        Socket server;
        Scanner usernameReader, serverOutput;
        PrintStream serverInput;

        boolean loginAttempted = false;

        do {
            if (loginAttempted) { System.out.println("Username taken, please choose another."); }
            loginAttempted = true;

            server = new Socket("localhost", 12345);

            serverOutput = new Scanner(server.getInputStream());
            serverInput = new PrintStream(server.getOutputStream());
            usernameReader = new Scanner(System.in);

            System.out.print("Enter username: ");
            serverInput.println(usernameReader.nextLine());
        } while (serverOutput.nextLine().equals("#IDTAKEN"));

        System.out.println("Sign in successful.");

        try {
            new Thread(new ChatHandler(server, System.in, this)).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void printMessage(String msg) {
        System.out.println(msg);
    }

}
