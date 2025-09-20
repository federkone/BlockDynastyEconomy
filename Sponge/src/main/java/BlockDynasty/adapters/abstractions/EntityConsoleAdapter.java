package BlockDynasty.adapters.abstractions;

import lib.commands.abstractions.IEntityCommands;
import lib.gui.abstractions.IEntityGUI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class EntityConsoleAdapter implements IEntityCommands {
    private final Audience console;

    private EntityConsoleAdapter(Audience console) {
        this.console = console;
    }

    public static EntityConsoleAdapter of(Audience console) {
        return new EntityConsoleAdapter(console);
    }

    @Override
    public void sendMessage(String message) {
        console.sendMessage(Component.text(translateColorCodes(message)));
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "");
    }

    @Override
    public void playNotificationSound() {

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

    @Override
    public IEntityGUI asEntityGUI() {
        return null;
    }

    @Override
    public Object getRoot() {
        return console;
    }

}
