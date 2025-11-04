package EngineTest.mocks.utils;

public class VanillaColors implements Colors{
    @Override
    public String parse(String input) {
        if (input == null) return "";
        return input.replaceAll("§[0-9a-f]", "");
    }
}
