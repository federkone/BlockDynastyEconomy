package BlockDynasty.BukkitImplementation.commands;

import lib.commands.abstractions.Source;
import lib.gui.abstractions.IPlayer;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

//Adapter para CommandSender que no son jugadores (Consola, Bloque de comandos, etc)
public class SourceConsoleAdapter implements Source {
    private final ConsoleCommandSender commandSender;
    public SourceConsoleAdapter(ConsoleCommandSender commandSender) {
        this.commandSender = commandSender;
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
        commandSender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void kickPlayer(String message) {

    }
    @Override
    public Object getHandle() {
        return commandSender;
    }

    @Override
    public IPlayer asIPlayer() {
        return null;
    }
}
