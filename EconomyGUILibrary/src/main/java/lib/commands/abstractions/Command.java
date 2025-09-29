package lib.commands.abstractions;


import java.util.List;

public interface Command {
    boolean execute(IEntityCommands sender, String[] args);
    List<Command> getSubCommands();
    List<String> getArgs();
    String getName();
    String getDescription();
    String getPermission();
}