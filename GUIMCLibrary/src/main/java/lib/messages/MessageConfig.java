package lib.messages;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConfig implements MessageProvider {
    private final Map<String, String> messages = new HashMap<>();

    public void addDefault(String key, String message) {
        this.messages.put(key, message);
    }

    @Override
    public String getMessage(String key) {
        return messages.getOrDefault(key, "Message not found: " + key);
    }

}
