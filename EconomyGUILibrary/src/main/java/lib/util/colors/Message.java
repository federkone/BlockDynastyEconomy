package lib.util.colors;

import lib.abstractions.IMessages;

public class Message {
    private static IMessages messages;

    public static void addLang(IMessages lang){
        messages=lang;
    }

    public static String process(java.util.Map<String, String> placeholders,String messageKey) {
        return messages.format(placeholders,messageKey);
    }

    public static String process(String messageKey) {
        return messages.format(messageKey);
    }

    public static String[] processLines(java.util.Map<String, String> placeholders,String messageKey) {
        return messages.formatLines(placeholders,messageKey);
    }

    public static String[] processLines(String messageKey) {
        return messages.formatLines(messageKey);
    }
}
