package BlockDynasty.utils;

import BlockDynasty.SpongePlugin;
import org.apache.logging.log4j.Logger;

public class Console {
    private static final Logger console= SpongePlugin.getLogger();
    private static boolean debugEnabled = false;
    private static final String Debug_Prefix = "[BlockDynastyEconomy-Debug] ";
    private static final String Console_Prefix = "[BlockDynastyEconomy] ";
    private static final String Error_Prefix = "[BlockDynastyEconomy-Error] ";


    public static void debug(String message) {
        if(debugEnabled) console.info(Debug_Prefix + message);
    }

    public static void log(String message){
        console.info(Console_Prefix + message);
    }

    public static void logError(String message){
        console.info(Error_Prefix + message);
    }

}
