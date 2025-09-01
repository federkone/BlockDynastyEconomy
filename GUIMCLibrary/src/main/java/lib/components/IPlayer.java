package lib.components;

import lib.templates.abstractions.AbstractGUI;

import java.util.UUID;

public interface IPlayer {
    UUID getUniqueId();
    String getName();
    void sendMessage(String message);
    void closeInventory();
    void openInventory(IInventory inventory);
    Object getHandle();
}