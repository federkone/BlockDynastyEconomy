package minestom.commands;

import adapters.PlayerAdapter;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import java.util.List;

public class commandMinestom extends Command {

    public commandMinestom(lib.commands.abstractions.Command command) {
        super(command.getName());

        setDefaultExecutor((sender, context) -> {
            command.execute(new PlayerAdapter((Player) sender), context.getInput().split(" "));
        });

        List<lib.commands.abstractions.Command> subCommands = command.getSubCommands();
        for (lib.commands.abstractions.Command subCommand : subCommands) {
                addSubcommand(new commandMinestom(subCommand));
        }
    }
}
