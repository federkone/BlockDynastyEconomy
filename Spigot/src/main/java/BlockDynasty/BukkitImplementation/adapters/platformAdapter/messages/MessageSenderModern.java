package BlockDynasty.BukkitImplementation.adapters.platformAdapter.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessageSenderModern implements IMessageSender {

    @Override
    public void sendMessage(Player player, String message) {
        Component textonuevo = MiniMessage.miniMessage().deserialize(message);
        player.sendMessage(textonuevo);
    }

    @Override
    public void sendMessage(ConsoleCommandSender commandSender, String message) {
        Component textonuevo = MiniMessage.miniMessage().deserialize(message);
        commandSender.sendMessage(textonuevo);
    }

}
