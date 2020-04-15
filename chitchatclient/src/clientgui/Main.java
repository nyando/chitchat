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

public class Main extends Application {

    LoginController loginController;
    ChatController chatController;
    Stage window;
    Scene login, chat;
    private LoginInfo info;

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

    public void switchToChat() throws IOException {
        this.window.setScene(this.chat);
        this.chatController.initWrapper();
        new Thread(new ChatHandler(this.info.getServer(), this.chatController.getWrapper(), new OutputHandler(this.chatController.getChatView()))).start();
        this.window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
