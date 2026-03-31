package net.blockdynasty.economy.engine.services;

import net.blockdynasty.economy.libs.services.messages.IMessages;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.engine.platform.files.Languages;

import java.util.Map;

public class Message implements IMessages {
    private final static String color= ChatColor.stringValueOf(Colors.GRAY);
    private static String prefix;
    private static Languages lang;

    public static void addLang(Languages lang){
        Message.lang=lang;
        String rawPrefix = lang.getMessage("prefix");
        prefix = rawPrefix.replace("{color}", ChatColor.stringValueOf(Colors.GREEN));
    }

    public static String process(Map<String, String> placeholders,String messageKey) {
        String processedMessage = lang.getMessage(messageKey);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            processedMessage = processedMessage.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        processedMessage = processedMessage.replace("{prefix}", prefix);
        processedMessage = processedMessage.replace("{color}", color);
        return processedMessage;
    }

    public String format(Map<String, String> placeholders,String messageKey) {
        return process(placeholders, messageKey);
    }

    public String format(String messageKey) {
        return lang.getMessage(messageKey);
    }

    public String[] formatLines(Map<String, String> placeholders,String messageKey) {
        String processedMessage = process(placeholders, messageKey);
        return processedMessage.split(",");
    }

    public String[] formatLines(String messageKey) {
        String processedMessage = lang.getMessage(messageKey);
        return processedMessage.split(",");
    }
}