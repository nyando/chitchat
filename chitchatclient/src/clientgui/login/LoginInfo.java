package clientgui.login;

import java.net.Socket;

public class LoginInfo {

    private Socket server;
    private String username;

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
