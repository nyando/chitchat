import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Server server;
    private final Client client;

    public ClientHandler(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

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
            } else if (input.toLowerCase().equals("#quit")) {
                break;
            } else {
                this.server.broadcast(client.getName(), input);
            }
        }

        this.server.removeClient(this.client);
        scInput.close();
    }

    private String getWhisperDestination(String cmd) {
        return cmd.split(";")[1];
    }

    private String getWhisperMessage(String cmd) {
        return cmd.split(";")[2];
    }

}
