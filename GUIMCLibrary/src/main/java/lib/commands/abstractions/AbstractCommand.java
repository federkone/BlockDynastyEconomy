package lib.commands.abstractions;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements Command {
    private final List<Command> subCommands = new ArrayList<>();
    private final List<String> args = new ArrayList<>();
    private final String name;
    private String description="";
    private String permission="";

    public AbstractCommand( String name,String permission,List<String> args) {
        this.name = name;
        this.permission = permission;
        this.args.addAll(args);
    }

    public AbstractCommand( String name,String permission) {
        this.name = name;
        this.permission = permission;
    }

    public void registerSubCommand(Command executor) {
        subCommands.add(executor);
    }

    @Override
    public List<Command> getSubCommands() {
        return this.subCommands;
    }

    @Override
    public List<String> getArgs() {
        return this.args;
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public String getPermission() {
        return permission;
    }

}
