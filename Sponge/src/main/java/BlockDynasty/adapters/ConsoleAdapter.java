package BlockDynasty.adapters;

import BlockDynasty.SpongePlugin;
import Main.IConsole;
import org.apache.logging.log4j.Logger;

public class ConsoleAdapter implements IConsole {
    private static final Logger console= SpongePlugin.getLogger();
    private static boolean debugEnabled = true;
    private static final String Debug_Prefix = "[BlockDynastyEconomy-Debug] ";
    private static final String Console_Prefix = "[BlockDynastyEconomy] ";
    private static final String Error_Prefix = "[BlockDynastyEconomy-Error] ";


    public void debug(String message) {
        if(debugEnabled) console.info(Debug_Prefix + message);
    }

    public void log(String message){
        console.info(Console_Prefix + message);
    }

    public void logError(String message){
        console.info(Error_Prefix + message);
    }
}
