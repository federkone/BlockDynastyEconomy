package BlockDynasty.BukkitImplementation.adapters.commands;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import lib.commands.abstractions.Command;
import lib.commands.CommandsFactory;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CommandRegister {
    private static List<Command> commands = CommandsFactory.Commands.getMainCommands();
    private static final JavaPlugin plugin= BlockDynastyEconomy.getInstance();

    public static void registerAll(){
        for(Command command: commands){
            CommandAdapter commandAdapter = new CommandAdapter(command);
            TabCompletionAdapter tabCompleterAdapter = new TabCompletionAdapter(command);

            PluginCommand pluginCommand = plugin.getCommand(command.getName());

            if (pluginCommand != null) {
                pluginCommand.setExecutor(commandAdapter);
                pluginCommand.setTabCompleter(tabCompleterAdapter);
                pluginCommand.setPermission(command.getPermission());
            }
        }
    }
}
