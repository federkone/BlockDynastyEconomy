package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.messages;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessageSenderVanilla implements IMessageSender{
    @Override
    public void sendMessage(Player player, String message) {
        message = translateColorCodes(message);
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(ConsoleCommandSender commandSender, String message) {
        message = translateColorCodes(message);
        commandSender.sendMessage(message);
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "§$1");
    }
}
