package lib.messages;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageService {
    private static MessageProvider provider = new DefaultProvider();

    public static void setProvider(MessageProvider customProvider) {
        provider = customProvider;
    }

    public static String get(String key) {
        return provider.getMessage(key);
    }

    public static String getMessage(String key, Map<String, String> variables) {
        String raw = get(key);
        return MessageParser.colorize(MessageParser.parse(raw, variables));
    }

    public static String getMessage(String key) {
        return MessageParser.colorize(get(key));
    }
}