package BlockDynasty.BukkitImplementation.utils;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class Console {
    private final static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    private final static boolean debugEnabled = BlockDynastyEconomy.getInstance().getConfig().getBoolean("debug");
    private static final String Debug_Prefix = "§3[BlockDynastyEconomy-Debug] §f";
    private static final String Console_Prefix = "§2[BlockDynastyEconomy] §f";
    private static final String Error_Prefix = "§c[BlockDynastyEconomy-Error] §f";

    public static void debug(String message) {
        if(debugEnabled) console.sendMessage(Debug_Prefix + colorize(message));
    }

    public static void log(String message){
        console.sendMessage(Console_Prefix + colorize(message));
    }

    public static void logError(String message){
        console.sendMessage(Error_Prefix + message);
    }

    private static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
