package clientcli;

import communication.IMessageOutput;
import communication.ChatHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Handles initialization and message output for the CLI chat.
 */
public class CommandLineClient implements IMessageOutput {

    public static void main(String[] args) throws IOException {
        new CommandLineClient().init(args[0], Integer.parseInt(args[1]));
    }

    /**
     * Start a client and attempt to sign in to chat server at (hostname:port).
     * User chooses a username, server will reply with #IDTAKEN if username is taken.
     * In this case, let the user try again until he picks a unique username.
     * @param hostname Hostname of the target chat server.
     * @param port Port number of the target chat server.
     * @throws IOException If problems occur while trying to open a socket connection to server.
     */
    public void init(String hostname, int port) throws IOException {
        Socket server;
        Scanner usernameReader, serverOutput;
        PrintStream serverInput;

        boolean loginAttempted = false;

        do {
            if (loginAttempted) { System.out.println("Username taken, please choose another."); }
            loginAttempted = true;

            server = new Socket(hostname, port);

            serverOutput = new Scanner(server.getInputStream());
            serverInput = new PrintStream(server.getOutputStream());
            usernameReader = new Scanner(System.in);

            System.out.print("Enter username: ");
            serverInput.println(usernameReader.nextLine());
            serverInput.flush();
        } while (serverOutput.nextLine().equals("#IDTAKEN"));

        System.out.println("Sign in successful.");

        try {
            new Thread(new ChatHandler(server, new StandardInputWrapper(), this)).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Output incoming messages to standard output after formatting them with MessageInterpreter.
     * @param msg Incoming message from server.
     */
    @Override
    public void printMessage(String msg) {
        if (msg.startsWith("#WHISPER")) {
            System.out.println(MessageInterpreter.convertWhisperIn(msg));
        } else if (msg.contains("#USERLIST")) {
            String[] userList = MessageInterpreter.convertUserlist(msg);
            System.out.println("User list update: " + String.join(", ", userList));
        } else if (msg.contains(";")) {
            System.out.println(MessageInterpreter.convertUserBroadcast(msg));
        } else {
            System.out.println(MessageInterpreter.convertServerBroadcast(msg));
        }
    }

}
