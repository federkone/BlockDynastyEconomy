package lib.commands.abstractions;

import lib.gui.abstractions.IEntityGUI;
import lib.scheduler.IEntity;

import java.util.UUID;

public interface IEntityCommands extends IEntity {
    UUID getUniqueId();
    String getName();
    void sendMessage(String message);
    Object getRoot();

    void playNotificationSound();

    boolean isOnline();
    boolean hasPermission(String permission);
    void kickPlayer(String message);

    IEntityGUI asEntityGUI();
}

