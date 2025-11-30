package adapters;

import lib.abstractions.IConsole;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;

public class Console implements IConsole {
    private static final Logger LOGGER = MinecraftServer.LOGGER;

    @Override
    public void debug(String message) {
        LOGGER.info(message);
    }

    @Override
    public void log(String message) {
        LOGGER.info(message);
    }

    @Override
    public void logError(String message) {
        LOGGER.info(message);
    }
}
