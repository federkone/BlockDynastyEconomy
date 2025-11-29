package minestom.commands;

import lib.commands.CommandsFactory;
import lib.commands.abstractions.Command;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

import java.util.List;

public class Commands {

    public static void register(){
        CommandManager manager = MinecraftServer.getCommandManager();
        manager.setUnknownCommandCallback((sender, c) -> sender.sendMessage("Command not found"));
        //por cada comando principal contenido en la api de economia vamos a hacer un new CommandMinestrom(command)
        List<Command> commands = CommandsFactory.Commands.getMainCommands();
        for (Command command : commands) {
            manager.register(new commandMinestom(command));
        }
    }
}
