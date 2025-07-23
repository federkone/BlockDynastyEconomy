
package BlockDynasty.BukkitImplementation.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

public class UtilServer {
    private static boolean debugEnabled = false;
    private static final String Console_Prefix = "§2[BlockDynastyEconomy] §f";
    private static final String Error_Prefix = "§c[B-Eco-Error] §f";

    public static void consoleLog(String message){
        if(debugEnabled) getServer().getConsoleSender().sendMessage(Console_Prefix + colorize(message));
    }

    public static void consoleLogError(String message){
        getServer().getConsoleSender().sendMessage(Error_Prefix + message);
    }

    private static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static Server getServer(){
        return Bukkit.getServer();
    }
}
