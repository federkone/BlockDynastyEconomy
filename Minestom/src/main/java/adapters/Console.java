package adapters;

import lib.abstractions.IConsole;
import minestom.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console implements IConsole {
    //private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Override
    public void debug(String message) {
        System.out.println(message);

    }

    @Override
    public void log(String message) {
        System.out.println(message);

    }

    @Override
    public void logError(String message) {
        System.out.println(message);

    }
}
