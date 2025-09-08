package lib.commands.abstractions;

import lib.gui.abstractions.IPlayer;

public interface Source {
    String getName();
    boolean isOnline();
    java.util.UUID getUniqueId();
    void sendMessage(String message);
    boolean hasPermission(String permission);
    void kickPlayer(String message);
    Object getHandle();
    IPlayer asIPlayer();
}

