package BlockDynasty.BukkitImplementation.adapters;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import Main.IConsole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleAdapter implements IConsole {
    private final static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    private final static boolean debugEnabled = BlockDynastyEconomy.getInstance().getConfig().getBoolean("debug");
    private static final String Debug_Prefix = "§3[BlockDynastyEconomy-Debug] §f";
    private static final String Console_Prefix = "§2[BlockDynastyEconomy] §f";
    private static final String Error_Prefix = "§c[BlockDynastyEconomy-Error] §f";

    public void debug(String message) {
        if(debugEnabled) console.sendMessage(Debug_Prefix + colorize(message));
    }

    public void log(String message){
        console.sendMessage(Console_Prefix + colorize(message));
    }

    public void logError(String message){
        console.sendMessage(Error_Prefix + message);
    }

    private static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

