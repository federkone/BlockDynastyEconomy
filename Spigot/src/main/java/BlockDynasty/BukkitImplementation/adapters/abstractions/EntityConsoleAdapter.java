package BlockDynasty.BukkitImplementation.adapters.abstractions;

import lib.commands.abstractions.IEntityCommands;
import lib.gui.abstractions.IEntityGUI;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

//Adapter para CommandSender que no son jugadores (Consola, Bloque de comandos, etc)
public class EntityConsoleAdapter implements IEntityCommands {
    private final ConsoleCommandSender commandSender;
    private EntityConsoleAdapter(ConsoleCommandSender commandSender) {
        this.commandSender = commandSender;
    }

    public static EntityConsoleAdapter of(ConsoleCommandSender commandSender) {
        return new EntityConsoleAdapter(commandSender);
    }
    @Override
    public String getName() {
        return "BlockDynasty";
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void sendMessage(String message) {
        commandSender.sendMessage(translateColorCodes(message));
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "");
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void kickPlayer(String message) {

    }

    @Override
    public IEntityGUI asEntityGUI() {
        return null;
    }

    @Override
    public Object getRoot() {
        return commandSender;
    }

}
