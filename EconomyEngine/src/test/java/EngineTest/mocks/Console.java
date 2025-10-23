package EngineTest.mocks;

import lib.abstractions.IConsole;

public class Console implements IConsole {


    @Override
    public void debug(String message) {
        System.out.println("[DEBUG] " + removeFormatCodes(message));
    }

    @Override
    public void log(String message) {
        System.out.println("[LOG] " + removeFormatCodes(message));
    }

    @Override
    public void logError(String message) {
        System.out.println("[ERROR] " + removeFormatCodes(message));
    }

    public static String removeFormatCodes(String input) {
        if (input == null) return "";
        return input.replaceAll("§[0-9a-f]", "");
    }
}
