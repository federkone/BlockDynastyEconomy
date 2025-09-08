package lib.messages;


import java.util.Map;

public class MessageParser {
    public static String parse(String message, Map<String, String> variables) {
        if (message == null) return "";
        String parsed = message;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            parsed = parsed.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return parsed;
    }

    public static String colorize(String message) {
        // Implement color code translation if needed, or leave as-is for platform-specific
        return message;
    }
}