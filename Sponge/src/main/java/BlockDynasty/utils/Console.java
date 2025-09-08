package BlockDynasty.utils;

import BlockDynasty.SpongePlugin;
import Main.IConsole;
import org.apache.logging.log4j.Logger;

public class Console {
    private static IConsole console;

    public static void setConsole(IConsole consoleInstance) {
        console = consoleInstance;
    }

    public static void debug(String message) {
        console.debug(message);
    }

    public static void log(String message){
        console.log(message);
    }

    public static void logError(String message){
        console.logError(message);
    }

}
