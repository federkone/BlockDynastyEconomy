package lib.abstractions;

import java.util.Map;

public interface IMessages {
    String format(Map<String, String> placeholders, String messageKey);
    String format(String messageKey);
    String[] formatLines(Map<String, String> placeholders, String messageKey);
    String[] formatLines(String messageKey);
}
