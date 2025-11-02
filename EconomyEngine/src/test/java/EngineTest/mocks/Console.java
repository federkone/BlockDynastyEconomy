package EngineTest.mocks;

import EngineTest.mocks.utils.Color;
import lib.abstractions.IConsole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console implements IConsole {


    @Override
    public void debug(String message) {
        System.out.println("[DEBUG] " + Color.parse(message));
    }

    @Override
    public void log(String message) {
        System.out.println("[LOG] " + Color.parse(message));
    }

    @Override
    public void logError(String message) {
        System.out.println("[ERROR] " + Color.parse(message));
    }

}
