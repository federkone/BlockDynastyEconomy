package net.blockdynasty.economy.hytale.adapters.commands;

import net.blockdynasty.economy.gui.commands.CommandService;
import net.blockdynasty.economy.gui.commands.abstractions.Command;
import net.blockdynasty.economy.hytale.BlockDynastyEconomy;

import java.util.List;

public class CommandRegister {


    public static void registerCommands() {
       BlockDynastyEconomy plugin= BlockDynastyEconomy.getInstance();

       List<Command> commands = CommandService.getMainCommands();
         for (Command command : commands) {
                  plugin.getCommandRegistry().registerCommand(new CommandAdapter(command));
         }
    }
}
