package me.BlockDynasty.Economy.aplication.commands;

import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OfferCommand implements CommandExecutor {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public OfferCommand(){

    }

    public void registerSubCommand(String name, CommandExecutor commandExecutor) {
        subCommands.put(name, commandExecutor);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.offer")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        if (args.length == 0) {
            //F.sendCurrencyUsage(sender); //offer accetpt <player> //offer deny <player> //offer cancel <player>
            sender.sendMessage(F.getOfferUsage());
            return true;
        }

        CommandExecutor subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(F.getUnknownSubCommand());
            return true;
        }

        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, args.length - 1);

        return subCommand.onCommand(sender, command, label, subCommandArgs);

    }
}
