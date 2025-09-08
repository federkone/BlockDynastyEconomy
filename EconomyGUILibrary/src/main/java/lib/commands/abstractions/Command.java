package lib.commands.abstractions;


import java.util.List;
import java.util.Map;

public interface Command {
    boolean execute(Source sender, String[] args);
    List<Command> getSubCommands();
    List<String> getArgs();
    String getName();
    String getDescription();
    String getPermission();
}