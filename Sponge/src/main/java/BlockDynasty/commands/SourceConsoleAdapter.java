package BlockDynasty.commands;

import lib.commands.abstractions.Source;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class SourceConsoleAdapter implements Source {
    private final Audience console;

    public SourceConsoleAdapter(Audience console) {
        this.console = console;
    }

    @Override
    public void sendMessage(String message) {
        console.sendMessage(Component.text(message));
    }

    @Override
    public String getName() {
        return "BlockDynasty";
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public boolean hasPermission(String permission) {
        // Console typically has all permissions
        return true;
    }

    @Override
    public void kickPlayer(String message) {
    }

}
