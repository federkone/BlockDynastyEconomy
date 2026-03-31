package net.blockdynasty.economy.hytale.adapters.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import net.blockdynasty.economy.gui.commands.abstractions.Command;

public class MainCommand extends AbstractCommandCollection {

    public MainCommand(Command command) {
        super(command.getName(), command.getDescription());
        requirePermission(command.getPermission());
        command.getSubCommands().forEach(subCommand -> {
            this.addSubCommand(new CommandAdapter(subCommand));
        });
    }
}
