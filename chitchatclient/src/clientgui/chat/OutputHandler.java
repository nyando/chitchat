package clientgui.chat;

import communication.IMessageOutput;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class OutputHandler implements IMessageOutput {

    private final Document doc;
    private String content;
    private final WebEngine engine;

    public OutputHandler(WebView view) {
        this.content = "<html></html>";
        this.doc = Jsoup.parse(this.content);
        this.engine = view.getEngine();
        this.engine.loadContent(this.content, "text/html");
    }

    @Override
    public void printMessage(String msg) {
        if (msg.startsWith("#WHISPER")) {
            this.doc.body().append(MessageInterpreter.convertWhisperIn(msg));
        } else if (msg.contains("#USERLIST")) {
            this.doc.body().append("<p>" + MessageInterpreter.getUserlistString(msg) + "</p>");
        } else if (msg.contains(";")) {
            this.doc.body().append("<p>" + MessageInterpreter.convertUserBroadcast(msg) + "</p>");
        } else {
            this.doc.body().append("<p>" + MessageInterpreter.convertServerBroadcast(msg) + "</p>");
        }

        this.content = this.doc.outerHtml();
        Platform.runLater(() -> {
            this.engine.loadContent(this.content, "text/html");
            this.engine.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        });

        System.out.println(msg);
    }
}
