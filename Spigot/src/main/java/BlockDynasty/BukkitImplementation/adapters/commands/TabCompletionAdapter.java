package BlockDynasty.BukkitImplementation.adapters.commands;

import lib.commands.abstractions.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompletionAdapter implements TabCompleter {
    private final Command rootCommand;


    public TabCompletionAdapter(Command command) {
        this.rootCommand = command;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        // Start with the root command
        Command currentCommand = rootCommand;
        int subcommandEndPosition = 0;

        // Navigate through the command tree based on arguments
        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i];
            Command nextCommand = findSubCommand(currentCommand, arg);
            if (nextCommand == null) {
                // We've reached the end of subcommands
                break;
            }
            currentCommand = nextCommand;
            subcommandEndPosition = i + 1;
        }

        // Check if we should suggest subcommands
        if (args.length - 1 == subcommandEndPosition) {
            // We're at the position to suggest subcommands
            if (!currentCommand.getSubCommands().isEmpty()) {
                String partialArg = args[args.length - 1].toLowerCase();
                return currentCommand.getSubCommands().stream()
                        .map(Command::getName)
                        .filter(name -> name.toLowerCase().startsWith(partialArg))
                        .collect(Collectors.toList());
            }
        }

        // Get the list of arguments for the current command
        List<String> commandArgs = currentCommand.getArgs();
        if (commandArgs == null || commandArgs.isEmpty()) {
            return new ArrayList<>();
        }

        // Calculate which argument we're currently processing
        int argIndex = args.length - 1 - subcommandEndPosition;

        // If we've run out of defined arguments, return empty list
        if (argIndex >= commandArgs.size()) {
            return new ArrayList<>();
        }

        // Get the current argument type
        String currentArgType = commandArgs.get(argIndex);

        // Process based on argument type
        if (currentArgType.equals("player")) {
            String partialArg = args[args.length - 1].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(partialArg))
                    .collect(Collectors.toList());
        } else {
            // For other argument types, suggest the argument name itself
            String partialArg = args[args.length - 1].toLowerCase();
            if (currentArgType.toLowerCase().startsWith(partialArg)) {
                List<String> result = new ArrayList<>();
                result.add(currentArgType);
                return result;
            }
        }

        // Default: no suggestions
        return new ArrayList<>();
    }

    private Command findSubCommand(Command parent, String commandName) {
        if (parent == null || parent.getSubCommands() == null || parent.getSubCommands().isEmpty()) {
            return null;
        }

        return parent.getSubCommands().stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(commandName))
                .findFirst()
                .orElse(null);
    }
}