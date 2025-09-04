package lib.gui.abstractions;

import java.util.UUID;

public interface IPlayer {
    UUID getUniqueId();
    String getName();
    void sendMessage(String message);
    void closeInventory();
    void openInventory(IInventory inventory);
    void playSuccessSound();
    void playFailureSound();
    Object getHandle();
}