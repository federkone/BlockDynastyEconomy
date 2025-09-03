package lib.commands.abstractions;

public interface PlatformAdapter {
    Source getPlayer(String name);
    void dispatchCommand(String command);
}
