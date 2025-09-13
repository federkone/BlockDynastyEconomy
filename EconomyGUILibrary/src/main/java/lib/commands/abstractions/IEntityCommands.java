package lib.commands.abstractions;

import lib.gui.abstractions.IEntityGUI;

import java.util.UUID;

public interface IEntityCommands {
    UUID getUniqueId();
    String getName();
    void sendMessage(String message);
    Object getHandle();

    void playNotificationSound();

    boolean isOnline();
    boolean hasPermission(String permission);
    void kickPlayer(String message);

    IEntityGUI asEntityGUI();
}

