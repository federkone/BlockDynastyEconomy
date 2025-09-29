package lib.gui.abstractions;

import lib.commands.abstractions.IEntityCommands;

import java.util.UUID;

public interface IEntityGUI {
    UUID getUniqueId();
    String getName();
    void sendMessage(String message);
    Object getRoot();

    void playSuccessSound();
    void playFailureSound();

    void closeInventory();
    void openInventory(IInventory inventory);

    IEntityCommands asEntityCommands();
}