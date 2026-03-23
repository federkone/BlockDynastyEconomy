package BlockDynasty.BukkitImplementation.adapters.platformAdapter.messages;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public interface IMessageSender {
    void sendMessage(Player player, String message);
    void sendMessage(ConsoleCommandSender commandSender, String message);
}
