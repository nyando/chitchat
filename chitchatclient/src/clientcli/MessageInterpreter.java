package clientcli;

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
        return "(whisper) " + tokens[1] + ": " + tokens[2];
    }

    public static String[] convertUserlist(String input) {
        input = input.trim();
        String[] users = input.split(";");
        return Arrays.copyOfRange(users, 1, users.length);
    }

    public static String convertUserBroadcast(String input) {
        return input.replace(";", ": ");
    }

    public static String convertServerBroadcast(String input) {
        return "* Server message: " + input + " *";
    }

}
