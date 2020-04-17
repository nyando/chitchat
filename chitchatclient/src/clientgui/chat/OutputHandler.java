package clientgui.chat;

import communication.IMessageOutput;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Displays HTML-formatted output of the chat message history.
 */
public class OutputHandler implements IMessageOutput {

    private final Document doc;
    private String content;
    private final WebEngine engine;

    /**
     * OutputHandler outputs incoming chat messages to HTML shown in a WebView-Element in the chat GUI.
     * JSoup is used for parsing and editing the HTML document.
     * @param view JavaFX WebView-Element responsible for displaying the chat message document.
     */
    public OutputHandler(WebView view) {
        this.content = "<html></html>";
        this.doc = Jsoup.parse(this.content);
        this.engine = view.getEngine();
        this.engine.loadContent(this.content, "text/html");
        // set chat style
        this.doc.head().append("<style>body { background-color: #f4f4f4;font-family: Helvetica, sans-serif; }</style>");
        // set window to scroll to bottom of the page (last chat message)
        this.doc.head().append("<script>function scroll() { window.scrollTo(0, document.body.scrollHeight); }</script>");
        this.doc.body().attr("onload", "scroll()");
    }

    /**
     * Interface output method for chat messages. Messages are passed through the MessageInterpreter for proper
     * HTML formatting, each message gets its own paragraph element.
     * @param msg Incoming chat message to be displayed.
     */
    @Override
    public void printMessage(String msg) {
        if (msg.startsWith("#WHISPER")) {
            this.doc.body().append("<p>" + MessageInterpreter.convertWhisperIn(msg) + "</p>");
        } else if (msg.contains("#USERLIST")) {
            this.doc.body().append("<p>" + MessageInterpreter.getUserlistString(msg) + "</p>");
        } else if (msg.contains(";")) {
            this.doc.body().append("<p>" + MessageInterpreter.convertUserBroadcast(msg) + "</p>");
        } else {
            this.doc.body().append("<p>" + MessageInterpreter.convertServerBroadcast(msg) + "</p>");
        }

        // load HTML document content to string
        this.content = this.doc.outerHtml();

        // dispatch load command to JavaFX thread
        Platform.runLater(() -> {
            this.engine.loadContent(this.content, "text/html");
        });
    }
}
