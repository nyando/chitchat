import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

    private final int port;
    private final List<Client> clients;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        new Server(12345).init();
    }

    public void init() throws IOException {
        ServerSocket server = new ServerSocket(port);

        while (true) {
            // accept incoming connection from a client
            Socket clientSocket = server.accept();

            // do not close the scanner, it closes the input stream and takes the whole socket with it
            Scanner inputScan = new Scanner(clientSocket.getInputStream());
            String clientName = inputScan.nextLine();

            // check if desired username is taken, if it is, close socket, else create a new client object
            if (this.clients.stream().anyMatch(c -> c.getName().equals(clientName)) || clientName.equals("nickay")) {
                new PrintStream(clientSocket.getOutputStream()).println("#IDTAKEN");
                clientSocket.close();
            } else {
                this.addClient(clientName, clientSocket);
            }
        }
    }

    public void addClient(String name, Socket socket) throws IOException {
        Client newClient = new Client(name, socket);
        this.clients.add(newClient);
        this.broadcast(this.getUserList());
        this.whisper("SERVER", newClient.getName(), "Hi, " + newClient.getName() + "! Type something to chat.");
        new Thread(new ClientHandler(this, newClient)).start();
    }

    public void removeClient(Client client) {
        this.clients.remove(client);
        this.broadcast(this.getUserList());
    }

    public void broadcast(String msg) {
        if (msg.equals("")) { return; }

        this.clients.forEach(c -> {
            c.getOutStream().println(msg);
            c.getOutStream().flush();
        });
    }

    public void broadcast(String user, String msg) {
        if (msg.equals("")) { return; }

        this.clients.forEach(c -> {
            c.getOutStream().println(user + ";" + msg);
            c.getOutStream().flush();
        });
    }

    public boolean whisper(String src, String dst, String msg) {
        if (msg.equals("")) {
            return true;
        }

        Client dstClient = this.clients.stream().filter(c -> c.getName().equals(dst)).findFirst().orElse(null);
        if (dstClient == null) { return false; }
        else {
            dstClient.getOutStream().println("#WHISPER;" + src + ";" + msg);
            dstClient.getOutStream().flush();
            return true;
        }
    }

    private String getUserList() {
        return "#USERLIST;" + this.clients.stream().map(Client::getName).collect(Collectors.joining(";"));
    }

}
