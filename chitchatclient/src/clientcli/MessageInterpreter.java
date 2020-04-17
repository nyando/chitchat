package clientcli;

import java.util.Arrays;

/**
 * Converts server messages to readable form for the command line interface.
 */
public class MessageInterpreter {

    /**
     * Convert "@" whisper message to server-side "#WHISPER" message.
     * @param input "@" whisper string.
     * @return Server "#WHISPER" command.
     */
    public static String convertWhisperOut(String input) {
        input = input.trim();
        int msgStartIndex = input.indexOf(" ");
        return "#WHISPER;" + input.substring(1, msgStartIndex) + ";" + input.substring(msgStartIndex + 1);
    }

    /**
     * Convert server "#WHISPER" to more readable output form.
     * @param input Server "#WHISPER" message
     * @return Readably formatted whisper message.
     */
    public static String convertWhisperIn(String input) {
        input = input.trim();
        String[] tokens = input.split(";");
        return "(whisper) " + tokens[1] + ": " + tokens[2];
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
     * @param input Server output user broadcast message.
     * @return Formatted user broadcast message.
     */
    public static String convertUserBroadcast(String input) {
        return input.replace(";", ": ");
    }

    /**
     * @param input Server broadcast message.
     * @return Formatted server broadcast message.
     */
    public static String convertServerBroadcast(String input) {
        return "* Server message: " + input + " *";
    }

}
