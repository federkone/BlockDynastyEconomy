package BlockDynasty.BukkitImplementation.utils;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import Main.IConsole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class Console {
    private static IConsole console;

    public static void setConsole(IConsole console){
        Console.console = console;
    }

    public static void debug(String message) {
        console.debug(message);
    }

    public static void log(String message){
        console.log(message);

    }

    public static void logError(String message){
        console.log(message);
    }


}
