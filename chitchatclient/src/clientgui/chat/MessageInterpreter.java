package clientgui.chat;

import java.util.Arrays;

public class MessageInterpreter {

    public static String convertWhisperOut(String input) {
        input = input.trim();
        int msgStartIndex = input.indexOf(" ");
        return "#WHISPER;" + input.substring(1, msgStartIndex) + ";" + input.substring(msgStartIndex + 1);
    }

    public static String convertWhisperIn(String input) {
        input = input.trim();
        String[] tokens = input.split(";");
        return "<i>(whisper)</i> " + "<b>" + tokens[1] + "</b>: " + tokens[2];
    }

    public static String[] convertUserlist(String input) {
        input = input.trim();
        String[] users = input.split(";");
        return Arrays.copyOfRange(users, 1, users.length);
    }

    public static String getUserlistString(String input) {
        String[] userList = convertUserlist(input);
        return "<b>User list</b>: " + String.join(", ", userList);
    }

    public static String convertUserBroadcast(String input) {
        input = input.trim();
        String[] broadcast = input.split(";");
        String name = broadcast[0];
        String msg = broadcast[1];
        return "<b>" + name + "</b>: " + msg;
    }

    public static String convertServerBroadcast(String input) {
        return "<b>Server message</b>: " + input;
    }

}
