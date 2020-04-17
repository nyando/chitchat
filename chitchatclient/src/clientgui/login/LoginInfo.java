package clientgui.login;

import java.net.Socket;

/**
 * Server socket and username information.
 */
public class LoginInfo {

    private Socket server;
    private String username;

    /**
     * Object containing client-side socket with the chat server connection and the client's chosen username.
     * @param server Client-side socket with server connection.
     * @param username Client's username on the server.
     */
    public LoginInfo(Socket server, String username) {
        this.server = server;
        this.username = username;
    }

    public Socket getServer() {
        return this.server;
    }

    public String getUsername() {
        return this.username;
    }

}
