package net.blockdynasty.economy.proxy.bungeecord;

import net.blockdynasty.economy.proxy.common.MessageProcessor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.logging.Logger;

public class CommandReload extends Command {
    private MessageProcessor messageProcessor;
    private Logger logger;

    public CommandReload(MessageProcessor messageProcessor, Logger logger) {
        super("bdreload", "blockdynastyeconomy.reload");
        this.messageProcessor = messageProcessor;
        this.logger = logger;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.getClass().getSimpleName().equals("ConsoleCommandSender")){
            messageProcessor.updateConfig();
            this.logger.info("Configuration reloaded.");
        }
    }
}
