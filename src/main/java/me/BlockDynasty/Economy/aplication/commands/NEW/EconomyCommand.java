package me.BlockDynasty.Economy.aplication.commands.NEW;

import me.BlockDynasty.Economy.config.file.F;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EconomyCommand implements CommandExecutor {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public EconomyCommand() {

    }

    public void registerSubCommand(String name, CommandExecutor executor) {
        subCommands.put(name.toLowerCase(), executor);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.economy")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }
        if (args.length == 0) {
            F.getManageHelp(sender); // Mensaje de ayuda
            return true;
        }
        CommandExecutor subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(F.getUnknownSubCommand());
            return true;
        }

        // Extrae los argumentos restantes para pasarlos al subcomando
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, args.length - 1);

        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }
}