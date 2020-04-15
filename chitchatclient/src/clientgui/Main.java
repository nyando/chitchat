package clientgui;

import communication.ChatHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    LoginController loginController;
    ChatController chatController;
    Stage window;
    Scene login, chat;
    LoginInfo info;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("logingui.fxml"));
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("chatgui.fxml"));
        Parent login = loginLoader.load();
        Parent chat = chatLoader.load();
        this.login = new Scene(login, 600, 400);
        this.chat = new Scene(chat, 600, 400);
        this.loginController = loginLoader.getController();
        this.loginController.setMain(this);
        this.chatController = chatLoader.getController();

        this.window.setTitle("ChitChat");
        this.window.setScene(this.login);
        this.window.show();
    }

    public void switchToChat() throws IOException {
        this.window.setScene(this.chat);
        this.chatController.initWrapper();
        new Thread(new ChatHandler(this.info.getServer(), this.chatController.getWrapper(), new OutputHandler())).start();
        this.window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
