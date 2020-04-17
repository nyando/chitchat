package clientgui.chat;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Converts server messages to HTML-formatted output for the graphical user interface.
 */
public class MessageInterpreter {

    static Map<String, String> users = new HashMap<>();

    /**
     * Convert an outgoing whisper message into the proper format for server usage (i.e. "#WHISPER;destination;msg").
     * @param input User whisper message (i.e. "@destination msg").
     * @return Whisper message in server format.
     */
    public static String convertWhisperOut(String input) {
        input = input.trim();
        int msgStartIndex = input.indexOf(" ");
        return "#WHISPER;" + input.substring(1, msgStartIndex) + ";" + input.substring(msgStartIndex + 1);
    }

    /**
     * Convert incoming whisper to HTML format.
     * @param input Server whisper message.
     * @return HTML formatted whisper message for output.
     */
    public static String convertWhisperIn(String input) {
        input = input.trim();
        String[] tokens = input.split(";");
        return "<i>(whisper)</i> " + "<b><div style=\"color:" + users.get(tokens[1]) + "\">" + tokens[1] + "</div></b>: " + tokens[2];
    }

    /**
     * Convert server user list to username array.
     * @param input Server user list string.
     * @return List of all usernames as strings.
     */
    public static String[] convertUserlist(String input) {
        input = input.trim();
        String[] users = input.split(";");
        return Arrays.copyOfRange(users, 1, users.length);
    }

    /**
     * Convert server user list to HTML format.
     * @param input Server user list string.
     * @return HTML-formatted user list output.
     */
    public static String getUserlistString(String input) {
        String[] userList = convertUserlist(input);
        updateUsers(userList);
        return "<b>User list</b>: " + String.join(", ", userList);
    }

    /**
     * Convert user message to color-coded HTML format.
     * @param input Server-formatted user broadcast message.
     * @return HTML string with user-specific color-coding.
     */
    public static String convertUserBroadcast(String input) {
        input = input.trim();
        String[] broadcast = input.split(";");
        String name = broadcast[0];
        String msg = broadcast[1];
        return "<b><div style=\"color:" + users.get(name) + "\">" + name + "</div></b>: " + msg;
    }

    /**
     * Convert server message to HTML format.
     * @param input Server broadcast message.
     * @return HTML string containing server message.
     */
    public static String convertServerBroadcast(String input) {
        return "<b>Server message</b>: " + input;
    }

    /**
     * Update local user list after receiving a user list update from the server.
     * Assign new users a randomly generated color-coding.
     * @param userList (String-split) Server user list.
     */
    private static void updateUsers(String[] userList) {
        for (String user : userList) {
            if (!users.containsKey(user)) {
                users.put(user, getRandomColor());
            }
        }
        List<String> toRemove = new ArrayList<>();
        for (String user : users.keySet()) {
            if (!Arrays.asList(userList).contains(user)) {
                toRemove.add(user);
            }
        }
        toRemove.forEach(user -> users.remove(user));
    }

    /**
     * Generate random color for user color-coding.
     * @return Random RGB hexstring.
     */
    private static String getRandomColor() {
        return String.format("#%06x", ThreadLocalRandom.current().nextInt(0xffffff));
    }
}
