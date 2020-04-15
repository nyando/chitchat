package clientgui.chat;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MessageInterpreter {

    static Map<String, String> users = new HashMap<>();

    public static String convertWhisperOut(String input) {
        input = input.trim();
        int msgStartIndex = input.indexOf(" ");
        return "#WHISPER;" + input.substring(1, msgStartIndex) + ";" + input.substring(msgStartIndex + 1);
    }

    public static String convertWhisperIn(String input) {
        input = input.trim();
        String[] tokens = input.split(";");
        return "<i>(whisper)</i> " + "<b><div style=\"color:" + users.get(tokens[1]) + "\">" + tokens[1] + "</div></b>: " + tokens[2];
    }

    public static String[] convertUserlist(String input) {
        input = input.trim();
        String[] users = input.split(";");
        return Arrays.copyOfRange(users, 1, users.length);
    }

    public static String getUserlistString(String input) {
        String[] userList = convertUserlist(input);
        updateUsers(userList);
        return "<b>User list</b>: " + String.join(", ", userList);
    }

    public static String convertUserBroadcast(String input) {
        input = input.trim();
        String[] broadcast = input.split(";");
        String name = broadcast[0];
        String msg = broadcast[1];
        return "<b><div style=\"color:" + users.get(name) + "\">" + name + "</div></b>: " + msg;
    }

    public static String convertServerBroadcast(String input) {
        return "<b>Server message</b>: " + input;
    }

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

    private static String getRandomColor() {
        return String.format("#%06x", ThreadLocalRandom.current().nextInt(0xffffff));
    }
}
