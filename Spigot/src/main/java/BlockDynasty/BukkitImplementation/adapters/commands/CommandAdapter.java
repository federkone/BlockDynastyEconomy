package BlockDynasty.BukkitImplementation.adapters.commands;

import lib.commands.abstractions.Source;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandAdapter implements CommandExecutor {
    private final lib.commands.abstractions.Command commandLib;

    public CommandAdapter(lib.commands.abstractions.Command command) {
        this.commandLib = command;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Source source= null;
        if (sender instanceof Player){
            source= new SourceAdapter((Player) sender);
        }else if (sender instanceof ConsoleCommandSender){
            source= new SourceConsoleAdapter((ConsoleCommandSender)sender);
        }
        if (source != null){
            return commandLib.execute(source, args);
        }
        return true;
    }
}
