import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    private final String name;
    private final InputStream inputStream;
    private final PrintStream outputStream;

    /**
     * Client object that encapsulates the socket and simplifies I/O.
     * @param name Username of the client.
     * @param conn Client's socket connection.
     * @throws IOException If there is a problem handling client's socket's I/O-streams.
     */
    public Client(String name, Socket conn) throws IOException {
        this.name = name;
        this.inputStream = conn.getInputStream();
        this.outputStream = new PrintStream(conn.getOutputStream());
    }

    public String getName() {
        return this.name;
    }

    public InputStream getInStream() {
        return this.inputStream;
    }

    public PrintStream getOutStream() {
        return this.outputStream;
    }

}
