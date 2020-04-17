import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Server server;
    private final Client client;

    /**
     * Thread that handles client-server connection for a single client. Relays messages from client to server.
     * @param server Server object that handles communication with the client.
     * @param client Client object communicating with the server.
     */
    public ClientHandler(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

    /**
     * Handle client messages as long as client's inputstream remains open.
     * On "#whisper", attempt to find client specified in the message, on success relay message to client, on failure
     * whisper back that user was not found.
     * If message has no additional commands, broadcast to all users.
     */
    @Override
    public void run() {
        String input;

        this.server.broadcast(client.getName() + " has entered the chat.");
        this.server.broadcast(this.server.getUserList());
        this.server.whisper("SERVER", this.client.getName(), "Hi, " + this.client.getName() + "! Type something to chat.");

        Scanner scInput = new Scanner(this.client.getInStream());
        while (scInput.hasNextLine()) {
            input = scInput.nextLine();

            if (input.toLowerCase().startsWith("#whisper")) {
                if (!this.server.whisper(this.client.getName(), this.getWhisperDestination(input), this.getWhisperMessage(input))) {
                    this.server.whisper("SERVER", this.client.getName(), "user " + this.getWhisperDestination(input) + " not found :(");
                } else {
                    this.server.whisper(this.client.getName(), this.client.getName(), this.getWhisperMessage(input));
                }
            } else {
                this.server.broadcast(client.getName(), input);
            }
        }

        this.server.removeClient(this.client);
        scInput.close();
    }

    /**
     * Get destination of a whisper message from a correctly formatted whisper command ("#WHISPER;destination;msg")
     * @param cmd Full whisper command sent from client.
     * @return Username of whisper's destination client.
     */
    private String getWhisperDestination(String cmd) {
        return cmd.split(";")[1];
    }

    /**
     * Get message contained in a whisper command.
     * @param cmd Full whisper command sent from client.
     * @return Whisper message
     */
    private String getWhisperMessage(String cmd) {
        return cmd.split(";")[2];
    }

}
