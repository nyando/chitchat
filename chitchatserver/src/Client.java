import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    private final String name;
    private final InputStream inputStream;
    private final PrintStream outputStream;

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
