package clientgui;

import clientgui.chat.ChatController;
import clientgui.chat.OutputHandler;
import clientgui.login.LoginController;
import clientgui.login.LoginInfo;
import communication.ChatHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Graphical interface of the chat. Allows user to login and chat once connected to a server.
 */
public class Main extends Application {

    private LoginController loginController;
    private ChatController chatController;
    private Stage window;
    private Scene login, chat;
    private LoginInfo info;

    /**
     * Load both login and chat scenes from their respective FXML files.
     * Begin with the login window.
     * @param primaryStage Main (and only) GUI frame.
     * @throws Exception On problems handling FXML, loaders, or controllers.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login/logingui.fxml"));
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("chat/chatgui.fxml"));
        this.login = new Scene(loginLoader.load(), 600, 400);
        this.chat = new Scene(chatLoader.load(), 600, 400);
        this.loginController = loginLoader.getController();
        this.loginController.setMain(this);
        this.chatController = chatLoader.getController();

        this.window.setTitle("ChitChat");
        this.window.setScene(this.login);
        this.window.show();
    }

    public void setInfo(LoginInfo info) {
        this.info = info;
    }

    /**
     * Switch scenes from login to chat on successful login to server.
     * @throws IOException On errors in handling socket IO.
     */
    public void switchToChat() throws IOException {
        this.chatController.initWrapper();
        new Thread(new ChatHandler(this.info.getServer(), this.chatController.getWrapper(), new OutputHandler(this.chatController.getChatView()))).start();
        this.window.setScene(this.chat);
        this.window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
