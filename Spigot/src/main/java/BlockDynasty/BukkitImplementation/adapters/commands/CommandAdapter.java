package BlockDynasty.BukkitImplementation.adapters.commands;

import BlockDynasty.BukkitImplementation.adapters.abstractions.EntityPlayerAdapter;
import BlockDynasty.BukkitImplementation.adapters.abstractions.EntityConsoleAdapter;
import lib.commands.abstractions.IEntityCommands;
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
        IEntityCommands source= null;
        if (sender instanceof Player){
            source= EntityPlayerAdapter.of((Player) sender);
        }else if (sender instanceof ConsoleCommandSender){
            source= EntityConsoleAdapter.of((ConsoleCommandSender)sender);
        }
        if (source != null){
            return commandLib.execute(source, args);
        }
        return true;
    }
}
