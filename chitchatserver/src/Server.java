import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

    private static final int DEFAULT_PORT = 6667;

    private final int port;
    private final List<Client> clients;

    /**
     * Create a new Server instance to handle all incoming client connections.
     * @param port Port on which to listen for incoming connections.
     */
    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    /**
     * Starts the server, default port (if not specified) is DEFAULT_PORT.
     *
     * @param args - Service port, if empty, use DEFAULT_PORT.
     * @throws IOException Passed up from init().
     */
    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;

        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                System.out.println("no valid number supplied as argument, using default port " + DEFAULT_PORT);
                port = DEFAULT_PORT;
            }

            if (port < 0 || port > 65535) {
                System.out.println("invalid port number, using default port " + DEFAULT_PORT);
                port = DEFAULT_PORT;
            }
        }

        new Server(port).init();
    }

    /**
     * Accepts incoming connections from clients indefinitely, expects message from client containing username.
     * If username is taken, replies with "#IDTAKEN" and closes the connection, else opens a new ClientHandler thread.
     * @throws IOException In case of problems opening the client socket's I/O-streams.
     */
    public void init() throws IOException {
        ServerSocket server = new ServerSocket(port);

        while (true) {
            // accept incoming connection from a client
            Socket clientSocket = server.accept();

            // do not close the scanner, it closes the input stream and takes the whole socket with it
            Scanner inputScan = new Scanner(clientSocket.getInputStream());
            String clientName = inputScan.nextLine();

            // check if desired username is taken, if it is, close socket, else create a new client object
            if (this.clients.stream().anyMatch(c -> c.getName().equals(clientName))) {
                new PrintStream(clientSocket.getOutputStream()).println("#IDTAKEN");
                clientSocket.close();
            } else {
                this.addClient(clientName, clientSocket);
            }
        }
    }

    /**
     * Create a new ClientHandler thread on successful client login.
     * Add new Client object to list of clients.
     * @param name Client's username
     * @param socket Client's socket object accepted by the server.
     * @throws IOException If there are problems accessing socket's I/O-streams.
     */
    public void addClient(String name, Socket socket) throws IOException {
        Client newClient = new Client(name, socket);
        this.clients.add(newClient);
        new Thread(new ClientHandler(this, newClient)).start();
    }

    /**
     * Remove client from clients list and broadcast updated user list.
     * @param client Client that disconnected from server.
     */
    public void removeClient(Client client) {
        this.clients.remove(client);
        this.broadcast(this.getUserList());
    }

    /**
     * Broadcast server message to all clients.
     * @param msg Message to broadcast.
     */
    public void broadcast(String msg) {
        if (msg.equals("")) { return; }

        this.clients.forEach(c -> {
            c.getOutStream().println(msg);
            c.getOutStream().flush();
        });
    }

    /**
     * Broadcast message from user to all clients (also reflected back to user).
     * Default mode of communication on the server. No broadcast happens if the message is empty.
     * @param user Username of client broadcasting message.
     * @param msg Message broadcast to all clients.
     */
    public void broadcast(String user, String msg) {
        if (msg.equals("")) { return; }

        this.clients.forEach(c -> {
            c.getOutStream().println(user + ";" + msg);
            c.getOutStream().flush();
        });
    }

    /**
     * Send private message msg from client src to client dst. No message sent if msg is empty.
     * @param src Username of sending client.
     * @param dst Username of receiving client.
     * @param msg Message to send.
     * @return True if user dst exists, false if not.
     */
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

    /**
     * Create string listing all usernames currently logged into server (Format: "#USERLIST;user1;...;userN").
     * @return User list in client-digestible form.
     */
    public String getUserList() {
        return "#USERLIST;" + this.clients.stream().map(Client::getName).collect(Collectors.joining(";"));
    }

}
