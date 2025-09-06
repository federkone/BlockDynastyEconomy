package BlockDynasty.commands;

import lib.commands.abstractions.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import java.util.List;

public class CommandRegister {
    private static void registrarComando(RegisterCommandEvent<org.spongepowered.api.command.Command.Parameterized> event,
                                                 PluginContainer container,
                                                 Command command){
        org.spongepowered.api.command.Command.Builder mainCommandBuilder = registrarComandoRecursivo(event, container, command);
        event.register(container, mainCommandBuilder.build(), command.getName());
    }

    private static org.spongepowered.api.command.Command.Builder registrarComandoRecursivo(RegisterCommandEvent<org.spongepowered.api.command.Command.Parameterized> event,
                                                                                    PluginContainer container,
                                                                                    Command command){
        org.spongepowered.api.command.Command.Builder commandBuilder = org.spongepowered.api.command.Command.builder()
                .executor(new CommandAdapter(command));

        if (command.getArgs() != null && !command.getArgs().isEmpty()) {
            for (String argName : command.getArgs()) {
                if ("player".equalsIgnoreCase(argName)) {
                    commandBuilder.addParameter(Parameter.player().key(argName).build());
                } else {
                    commandBuilder.addParameter(Parameter.string().key(argName).build());
                }
            }
        } else {
            commandBuilder.addParameter(CommandAdapter.ARGS);
        }
        if (command.getPermission() != null && !command.getPermission().isEmpty()) {
            commandBuilder.permission(command.getPermission());
        }
        List<Command> subCommands = command.getSubCommands();
        if (subCommands != null && !subCommands.isEmpty()){
            for (Command subCommand : subCommands) {
                org.spongepowered.api.command.Command.Builder subCommandBuilder = registrarComandoRecursivo(event, container, subCommand);
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