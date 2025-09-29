package BlockDynasty.adapters.commands;

import lib.commands.abstractions.Command;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRegister {
    private static void registrarComando(RegisterCommandEvent<org.spongepowered.api.command.Command.Parameterized> event, PluginContainer container, Command command){
        org.spongepowered.api.command.Command.Builder mainCommandBuilder = registrarComandoRecursivo(command);
        event.register(container, mainCommandBuilder.build(), command.getName());
    }

    private static org.spongepowered.api.command.Command.Builder registrarComandoRecursivo(Command command){
        org.spongepowered.api.command.Command.Builder commandBuilder = org.spongepowered.api.command.Command.builder()
                .executor(new CommandAdapter(command));

        if (command.getArgs() != null && !command.getArgs().isEmpty()) {
            for (String argName : command.getArgs()) {
                if ("player".equalsIgnoreCase(argName)) {
                    commandBuilder.addParameter(
                            Parameter.string()
                                    .key(argName)
                                    .completer((context, currentInput) -> Sponge.server().onlinePlayers().stream()
                                            .map(ServerPlayer::name)
                                            .filter(name -> name.toLowerCase().startsWith(currentInput.toLowerCase()))
                                            .map(CommandCompletion::of)
                                            .collect(Collectors.toList()))
                                    .build()
                    );
                } else {
                    commandBuilder.addParameter(Parameter.string().key(argName).build());
                }
            }
            commandBuilder.addParameter(CommandAdapter.ARGS);
        }


        if (command.getPermission() != null && !command.getPermission().isEmpty()) {
            commandBuilder.permission(command.getPermission());
        }
        List<Command> subCommands = command.getSubCommands();
        if (subCommands != null && !subCommands.isEmpty()){
            for (Command subCommand : subCommands) {
                org.spongepowered.api.command.Command.Builder subCommandBuilder = registrarComandoRecursivo(subCommand);
                commandBuilder.addChild(subCommandBuilder.build(), subCommand.getName());
            }
        }
        return commandBuilder;
    }
    public static void registerCommands(RegisterCommandEvent<org.spongepowered.api.command.Command.Parameterized> event,
                                        PluginContainer container,
                                        List<Command> commands) {
        for (Command command : commands) {
            registrarComando(event, container, command);
        }
    }
}