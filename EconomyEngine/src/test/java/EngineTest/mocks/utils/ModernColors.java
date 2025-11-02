package EngineTest.mocks.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModernColors implements Colors{

    @Override
    public String parse(String text) {
        Pattern hexPattern = Pattern.compile("<#([0-9A-Fa-f]{6})>");
        Matcher matcher = hexPattern.matcher(text);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);

            String ansiCode = String.format("\u001B[38;2;%d;%d;%dm", r, g, b);
            matcher.appendReplacement(result, ansiCode);
        }
        matcher.appendTail(result);

        return result.toString() + "\u001B[0m";
    }
}
